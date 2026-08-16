package com.sololeveling.sscprep.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sololeveling.sscprep.audio.SystemSoundAndHaptics
import com.sololeveling.sscprep.data.PyqPapersData
import com.sololeveling.sscprep.data.QuestionVaultData
import com.sololeveling.sscprep.data.SystemRepository
import com.sololeveling.sscprep.domain.engine.DailyQuestEngine
import com.sololeveling.sscprep.domain.engine.DungeonEngine
import com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator
import com.sololeveling.sscprep.domain.engine.PlayerEngine
import com.sololeveling.sscprep.domain.engine.ShadowEngine
import com.sololeveling.sscprep.domain.engine.ShopEngine
import com.sololeveling.sscprep.domain.model.*
import com.sololeveling.sscprep.sync.SyncManager
import com.sololeveling.sscprep.sync.SyncStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SystemRepository(application.applicationContext)
    val soundAndHaptics = SystemSoundAndHaptics(application.applicationContext)

    val syncManager = SyncManager(application)
    val syncStatus: StateFlow<SyncStatus> = syncManager.syncStatus

    val playerState: StateFlow<PlayerState> = repository.playerState
    val questState: StateFlow<DailyQuestState> = repository.questState
    val shadowState: StateFlow<ShadowArmyState> = repository.shadowState
    val inventoryState: StateFlow<InventoryState> = repository.inventoryState
    val bookmarkedQuestions: StateFlow<Set<String>> = repository.bookmarkedQuestions

    // CBT Raid state
    private val _activeRaidSession = MutableStateFlow<RaidSession?>(null)
    val activeRaidSession: StateFlow<RaidSession?> = _activeRaidSession.asStateFlow()

    private val _latestRaidResult = MutableStateFlow<RaidResult?>(null)
    val latestRaidResult: StateFlow<RaidResult?> = _latestRaidResult.asStateFlow()

    // Level up notification event
    private val _levelUpEvent = MutableSharedFlow<PlayerEngine.LevelUpResult>()
    val levelUpEvent: SharedFlow<PlayerEngine.LevelUpResult> = _levelUpEvent.asSharedFlow()

    // Toast message banner
    private val _systemBannerMessage = MutableStateFlow<String?>(null)
    val systemBannerMessage: StateFlow<String?> = _systemBannerMessage.asStateFlow()

    // Focus session timer
    private val _focusRemainingSeconds = MutableStateFlow<Int>(0)
    val focusRemainingSeconds: StateFlow<Int> = _focusRemainingSeconds.asStateFlow()
    private val _isFocusActive = MutableStateFlow<Boolean>(false)
    val isFocusActive: StateFlow<Boolean> = _isFocusActive.asStateFlow()

    private var raidTimerJob: Job? = null
    private var focusTimerJob: Job? = null

    // Countdown string to daily reset
    private val _countdownToMidnight = MutableStateFlow(DailyQuestEngine.getTimeUntilMidnight())
    val countdownToMidnight: StateFlow<String> = _countdownToMidnight.asStateFlow()

    // App Update state (non-forced)
    private val currentVersionCode = com.sololeveling.sscprep.BuildConfig.VERSION_CODE
    private val _appUpdateInfo = MutableStateFlow<com.sololeveling.sscprep.network.AppVersionResponse?>(null)
    val appUpdateInfo: StateFlow<com.sololeveling.sscprep.network.AppVersionResponse?> = _appUpdateInfo.asStateFlow()

    private val _showUpdateDialog = MutableStateFlow(false)
    val showUpdateDialog: StateFlow<Boolean> = _showUpdateDialog.asStateFlow()

    init {
        // Daily reset timer loop
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _countdownToMidnight.value = DailyQuestEngine.getTimeUntilMidnight()
            }
        }

        // Silent background check for optional updates on startup
        checkForUpdates(manual = false)
    }

    fun dismissUpdateDialog() {
        _showUpdateDialog.value = false
    }

    fun checkForUpdates(manual: Boolean = false) {
        viewModelScope.launch {
            try {
                val versionInfo = com.sololeveling.sscprep.network.ApiClient.apiService.checkAppVersion()
                if (versionInfo.success && versionInfo.latestVersionCode > currentVersionCode) {
                    _appUpdateInfo.value = versionInfo
                    _showUpdateDialog.value = true
                } else if (manual) {
                    showBanner("System is up to date (v1.0.0)")
                }
            } catch (e: Exception) {
                if (manual) {
                    showBanner("Could not reach update server.")
                }
            }
        }
    }

    fun pullAndApplyCloudState() {
        viewModelScope.launch {
            val cloudState = syncManager.pullFromCloud()
            if (cloudState != null) {
                repository.updatePlayerState(cloudState.playerState)
                repository.updateQuestState(cloudState.questState)
                repository.updateShadowState(cloudState.shadowState)
            }
        }
    }

    private fun triggerSync() {
        viewModelScope.launch {
            syncManager.pushToCloud(
                playerState = repository.playerState.value,
                questState = repository.questState.value,
                shadowState = repository.shadowState.value
            )
        }
    }

    fun showBanner(msg: String) {
        viewModelScope.launch {
            _systemBannerMessage.value = msg
            delay(3000)
            if (_systemBannerMessage.value == msg) {
                _systemBannerMessage.value = null
            }
        }
    }

    // --- Player actions ---
    fun allocateStat(statType: String) {
        val updated = PlayerEngine.allocateStat(playerState.value, statType)
        if (updated != null) {
            repository.updatePlayerState(updated)
            soundAndHaptics.playClick()
        }
        triggerSync()
    }

    fun setTargetPost(postId: String) {
        val updated = playerState.value.copy(targetPostId = postId)
        repository.updatePlayerState(updated)
        soundAndHaptics.playClick()
    }

    fun activateGodMode() {
        val current = playerState.value
        val boosted = current.copy(
            level = 100,
            rank = "Monarch",
            gold = current.gold + 999999,
            unallocatedPoints = current.unallocatedPoints + 500,
            title = "The Architect of the System",
            stats = current.stats.copy(
                intelligence = current.stats.intelligence + 100,
                vitality = current.stats.vitality + 100,
                agility = current.stats.agility + 100,
                sense = current.stats.sense + 100,
                strength = current.stats.strength + 100
            ),
            statsUnlocked = current.statsUnlocked.copy(
                totalQuestionsSolved = maxOf(current.statsUnlocked.totalQuestionsSolved, 5000),
                mockTestsCleared = maxOf(current.statsUnlocked.mockTestsCleared, 100),
                shadowsExtracted = maxOf(current.statsUnlocked.shadowsExtracted, 50)
            )
        )
        repository.updatePlayerState(boosted)
        soundAndHaptics.playAriseSound()
        showBanner("👑 DEVELOPER GOD MODE: Level 100 Monarch Activated!")
        triggerSync()
    }

    fun setPlayerName(name: String) {
        if (name.isNotBlank()) {
            val updated = playerState.value.copy(name = name.trim())
            repository.updatePlayerState(updated)
            soundAndHaptics.playClick()
        }
    }

    // --- Quests actions ---
    fun toggleTask(taskId: String) {
        val updated = DailyQuestEngine.toggleTaskDirect(questState.value, taskId)
        repository.updateQuestState(updated)
        soundAndHaptics.playClick()
    }

    fun incrementTask(taskId: String, amount: Int = 1) {
        val updated = DailyQuestEngine.incrementTask(questState.value, taskId, amount)
        repository.updateQuestState(updated)
    }

    fun claimDailyReward() {
        val result = DailyQuestEngine.claimDailyReward(questState.value, playerState.value)
        if (result != null && result.success) {
            repository.updateQuestState(result.updatedQuestState)
            repository.updatePlayerState(result.updatedPlayerState)
            soundAndHaptics.playLevelUp()
            showBanner("DAILY QUEST CLEARED: +${result.expGained} EXP, +${result.goldGained} Gold, +${result.statPointsGained} Stat Points!")
        }
        triggerSync()
    }

    // --- Dungeons & CBT Raids ---
    fun startRaid(gateId: String) {
        val gate = SYSTEM_DUNGEON_GATES.find { it.id == gateId } ?: return
        val questions = if (gate.isInfiniteTower) {
            InfiniteQuestionGenerator.generateBatch(gate.questionCount)
        } else {
            QuestionVaultData.getRandomBatch(gate.questionCount, gate.subject)
        }

        val session = DungeonEngine.createSession(gate, questions, playerState.value)
        _activeRaidSession.value = session
        _latestRaidResult.value = null
        soundAndHaptics.playAlert()
        startRaidTimer()
    }

    fun startPyqRaid(paperId: String) {
        val paper = PyqPapersData.papers.find { it.id == paperId } ?: return
        val gate = DungeonGate(
            id = paper.id,
            rank = "S",
            name = paper.exam,
            subject = "Official PYQ Paper",
            bossName = paper.bossName,
            bossAvatar = paper.bossAvatar,
            questionCount = paper.questions.size,
            timeMinutes = paper.durationMinutes,
            expReward = 650,
            goldReward = 350,
            desc = paper.desc
        )
        val session = DungeonEngine.createSession(gate, paper.questions, playerState.value)
        _activeRaidSession.value = session
        _latestRaidResult.value = null
        soundAndHaptics.playAlert()
        startRaidTimer()
    }

    private fun startRaidTimer() {
        raidTimerJob?.cancel()
        raidTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentSession = _activeRaidSession.value ?: break
                if (currentSession.isCompleted) break

                val remaining = currentSession.timeRemainingSeconds - 1
                if (remaining <= 0) {
                    _activeRaidSession.value = currentSession.copy(timeRemainingSeconds = 0)
                    submitRaid()
                    break
                } else {
                    _activeRaidSession.value = currentSession.copy(timeRemainingSeconds = remaining)
                }
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        val session = _activeRaidSession.value ?: return
        if (session.isCompleted) return

        val newAnswers = session.answers.toMutableList()
        newAnswers[session.currentIndex] = optionIndex
        _activeRaidSession.value = session.copy(answers = newAnswers)
        soundAndHaptics.playClick()
    }

    fun clearOption() {
        val session = _activeRaidSession.value ?: return
        if (session.isCompleted) return

        val newAnswers = session.answers.toMutableList()
        newAnswers[session.currentIndex] = null
        _activeRaidSession.value = session.copy(answers = newAnswers)
        soundAndHaptics.playClick()
    }

    fun toggleFlag() {
        val session = _activeRaidSession.value ?: return
        if (session.isCompleted) return

        val newFlags = session.flags.toMutableList()
        newFlags[session.currentIndex] = !newFlags[session.currentIndex]
        _activeRaidSession.value = session.copy(flags = newFlags)
        soundAndHaptics.playClick()
    }

    fun goToQuestion(index: Int) {
        val session = _activeRaidSession.value ?: return
        if (index in 0 until session.questions.size) {
            _activeRaidSession.value = session.copy(currentIndex = index)
            soundAndHaptics.playClick()
        }
    }

    fun nextQuestion() {
        val session = _activeRaidSession.value ?: return
        if (session.currentIndex < session.questions.size - 1) {
            goToQuestion(session.currentIndex + 1)
        }
    }

    fun prevQuestion() {
        val session = _activeRaidSession.value ?: return
        if (session.currentIndex > 0) {
            goToQuestion(session.currentIndex - 1)
        }
    }

    fun submitRaid() {
        val session = _activeRaidSession.value ?: return
        if (session.isCompleted) return

        raidTimerJob?.cancel()
        val nowFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val submission = DungeonEngine.submitSession(session, playerState.value, nowFormatted)

        _activeRaidSession.value = submission.completedSession
        _latestRaidResult.value = submission.raidResult
        repository.updatePlayerState(submission.updatedPlayerState)

        // Save fallen monsters to Shadow state
        if (submission.fallenMonsters.isNotEmpty()) {
            val updatedShadowList = shadowState.value.fallenMonsters.toMutableList()
            updatedShadowList.addAll(0, submission.fallenMonsters)
            repository.updateShadowState(shadowState.value.copy(fallenMonsters = updatedShadowList))
        }

        // Increment daily quest tasks
        val totalSolved = submission.raidResult.correctCount + submission.raidResult.wrongCount
        when (session.gate.subject) {
            "Quantitative Aptitude" -> incrementTask("t_quant", totalSolved)
            "General Intelligence & Reasoning" -> incrementTask("t_reas", totalSolved)
            "English Language" -> incrementTask("t_eng", totalSolved)
            else -> {
                incrementTask("t_quant", totalSolved / 3 + 1)
                incrementTask("t_reas", totalSolved / 3 + 1)
                incrementTask("t_eng", totalSolved / 3 + 1)
            }
        }

        if (submission.raidResult.isBossDefeated) {
            soundAndHaptics.playLevelUp()
        } else {
            soundAndHaptics.playAlert()
        }
        triggerSync()
    }

    fun dismissRaid() {
        _activeRaidSession.value = null
        _latestRaidResult.value = null
    }

    // --- Shadow extraction ("ARISE") ---
    fun extractShadow(fallenMonsterId: String) {
        val result = ShadowEngine.extractShadow(shadowState.value, playerState.value, fallenMonsterId)
        if (result != null && result.success) {
            repository.updateShadowState(result.updatedShadowState)
            repository.updatePlayerState(result.updatedPlayerState)
            soundAndHaptics.playAriseSound()
            showBanner("ARISE! Extracted shadow power for ${result.commanderName} (+80 EXP, +30 Gold)")
        }
        triggerSync()
    }

    // --- Pomodoro Focus ---
    fun startFocusSession(minutes: Int = 25) {
        _focusRemainingSeconds.value = minutes * 60
        _isFocusActive.value = true
        soundAndHaptics.playAlert()

        focusTimerJob?.cancel()
        focusTimerJob = viewModelScope.launch {
            while (_focusRemainingSeconds.value > 0 && _isFocusActive.value) {
                delay(1000)
                _focusRemainingSeconds.value -= 1
            }
            if (_isFocusActive.value) {
                // Completed focus session!
                _isFocusActive.value = false
                incrementTask("t_focus", minutes)
                val lvlUp = PlayerEngine.addExp(playerState.value, 60)
                val updatedMilestones = lvlUp.newState.milestones.copy(
                    focusMinutes = lvlUp.newState.milestones.focusMinutes + minutes
                )
                repository.updatePlayerState(lvlUp.newState.copy(milestones = updatedMilestones))
                soundAndHaptics.playLevelUp()
                showBanner("FOCUS DUNGEON CLEARED! +$minutes Minutes Study logged, +60 EXP rewarded!")
            }
        }
    }

    fun stopFocusSession() {
        focusTimerJob?.cancel()
        _isFocusActive.value = false
        _focusRemainingSeconds.value = 0
        soundAndHaptics.playClick()
    }

    // --- Shop ---
    fun buyItem(itemId: String) {
        val result = ShopEngine.purchaseItem(itemId, inventoryState.value, playerState.value)
        if (result.success) {
            repository.updateInventoryState(result.updatedInventory)
            repository.updatePlayerState(result.updatedPlayerState)
            soundAndHaptics.playLevelUp()
            showBanner(result.message)
        } else {
            soundAndHaptics.playAlert()
            showBanner(result.message)
        }
        triggerSync()
    }

    // --- Bookmarks ---
    fun toggleBookmark(questionId: String) {
        repository.toggleBookmark(questionId)
        soundAndHaptics.playClick()
    }
}
