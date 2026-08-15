/**
 * SOLO LEVELING EXAM SYSTEM - MASTER APPLICATION CONTROLLER
 */

document.addEventListener('DOMContentLoaded', () => {
  // Initialize Application Components safely
  const initList = [
    ['Navigation', initNavigation],
    ['Auth', initAuthUI],
    ['Player', initPlayerUI],
    ['DailySkirmish', initDailySkirmishUI],
    ['QuestionVault', initQuestionVaultUI],
    ['Quests', initQuestsUI],
    ['Dungeons', initDungeonsUI],
    ['Shadows', initShadowsUI],
    ['FocusDungeon', initFocusDungeonUI],
    ['Shop', initShopUI],
    ['OnlineSyllabus', initOnlineSyllabusUI],
    ['Leaderboard', initLeaderboardUI],
    ['AdminPortal', initAdminPortalUI],
    ['AudioControls', initAudioControls],
    ['Settings', initSettingsUI]
  ];

  initList.forEach(([name, fn]) => {
    try {
      if (typeof fn === 'function') fn();
    } catch (err) {
      console.warn(`[System Engine] Failed to initialize ${name}:`, err);
    }
  });

  // Periodic Timer Tick for Daily Quests
  try {
    setInterval(() => {
      if (typeof updateQuestCountdown === 'function') updateQuestCountdown();
    }, 1000);
  } catch (e) {}

  // Haptic feedback utility
  window.triggerHaptic = function(type = 'light') {
    try {
      if (localStorage.getItem('solo_system_haptics_enabled') === 'false') return;
      if (typeof navigator !== 'undefined' && 'vibrate' in navigator && typeof navigator.vibrate === 'function') {
        if (type === 'light') navigator.vibrate(10);
        else if (type === 'medium') navigator.vibrate(22);
        else if (type === 'heavy') navigator.vibrate([30, 40, 30]);
        else if (type === 'success') navigator.vibrate([15, 30, 40]);
      }
    } catch (e) {}
  };

  // Global Keybindings & Android Back Button (e.g. Esc/Back to close sheets/modals)
  window.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      closeAllModals();
    }
  });

  window.addEventListener('popstate', () => {
    const activeModal = document.querySelector('.system-modal-overlay.active');
    if (activeModal) {
      closeAllModals();
    }
  });

  // Welcome System Alert
  setTimeout(() => {
    if (window.SystemAudio) window.SystemAudio.playSystemAlert();
  }, 600);
});

/* ==========================================================================
   1. NAVIGATION & MOBILE APP DOCK
   ========================================================================== */
function switchTab(targetId) {
  if (!targetId) return;

  const allNavButtons = document.querySelectorAll('.nav-dock-btn[data-tab], .guild-drawer-card[data-tab], .nav-tab-btn[data-tab]');
  const panels = document.querySelectorAll('.viewport-tab-panel');

  // Update active state on all buttons
  allNavButtons.forEach(btn => {
    if (btn.getAttribute('data-tab') === targetId) {
      btn.classList.add('active');
    } else {
      btn.classList.remove('active');
    }
  });

  // Active state for Guild dock button if a drawer sub-tab is open
  const guildDrawerTabs = ['tab-shadows', 'tab-leaderboard', 'tab-focus', 'tab-shop', 'tab-syllabus', 'tab-admin'];
  const guildTriggerBtn = document.getElementById('guild-menu-trigger-btn');
  if (guildTriggerBtn) {
    if (guildDrawerTabs.includes(targetId)) {
      guildTriggerBtn.classList.add('active');
    } else if (['tab-status', 'tab-quests', 'tab-vault', 'tab-dungeons'].includes(targetId)) {
      guildTriggerBtn.classList.remove('active');
    }
  }

  // Switch Viewport Panel
  panels.forEach(p => p.classList.remove('active'));
  const targetPanel = document.getElementById(targetId);
  if (targetPanel) {
    targetPanel.classList.add('active');
    const viewportContainer = document.querySelector('.system-viewports');
    if (viewportContainer) {
      viewportContainer.scrollTop = 0;
    }
  }

  // Sub-tab auto-renderers
  if (targetId === 'tab-leaderboard' && typeof renderLeaderboard === 'function') {
    renderLeaderboard();
  }
  if (targetId === 'tab-admin' && typeof renderAdminPortal === 'function') {
    renderAdminPortal();
  }

  // Sound and Haptics
  if (window.SystemAudio) window.SystemAudio.playClick();
  if (window.triggerHaptic) window.triggerHaptic('light');

  // Close any open bottom sheets
  closeAllModals();

  // History sync
  try {
    history.replaceState(null, '', `#${targetId}`);
  } catch (e) {}
}
window.switchTab = switchTab;

function initNavigation() {
  // Delegate clicks for data-tab anywhere in app
  document.addEventListener('click', (e) => {
    const tabTarget = e.target.closest('[data-tab]');
    if (tabTarget) {
      const targetId = tabTarget.getAttribute('data-tab');
      if (targetId) {
        e.preventDefault();
        switchTab(targetId);
      }
    }
  });

  // Mobile Guild Menu Bottom Sheet Toggle
  const guildTrigger = document.getElementById('guild-menu-trigger-btn');
  const guildSheet = document.getElementById('guild-menu-sheet');
  if (guildTrigger && guildSheet) {
    guildTrigger.addEventListener('click', (e) => {
      e.stopPropagation();
      if (window.triggerHaptic) window.triggerHaptic('medium');
      if (window.SystemAudio) window.SystemAudio.playClick();
      guildSheet.classList.toggle('active');
    });
  }

  // Mobile Top Bar Home click
  const homeBtn = document.getElementById('mobile-home-btn');
  if (homeBtn) {
    homeBtn.addEventListener('click', () => {
      switchTab('tab-status');
    });
  }

  // Check initial hash in URL
  if (window.location.hash) {
    const initialTab = window.location.hash.replace('#', '');
    if (document.getElementById(initialTab)) {
      switchTab(initialTab);
    }
  }
}

/* ==========================================================================
   2. PLAYER STATUS UI
   ========================================================================== */
function initPlayerUI() {
  const player = window.Player;
  if (!player) return;

  function render() {
    const d = player.data;
    // Header HUD values
    document.getElementById('hud-player-level').textContent = `LVL ${d.level}`;
    document.getElementById('hud-player-rank').textContent = `${d.rank}-RANK`;
    document.getElementById('hud-player-rank').className = `rank-badge rank-${d.rank}`;
    document.getElementById('hud-player-gold').textContent = `${d.gold} G`;

    // Status Panel values
    document.getElementById('status-player-name').value = d.name;
    document.getElementById('status-player-title').textContent = d.title;
    document.getElementById('status-player-rank').textContent = `${d.rank}-RANK HUNTER`;
    document.getElementById('status-player-rank').className = `rank-badge rank-${d.rank}`;
    document.getElementById('status-player-lvl').textContent = d.level;

    // Gauges
    const hpPercent = Math.min(100, Math.round((d.hp / d.maxHp) * 100));
    const mpPercent = Math.min(100, Math.round((d.mp / d.maxMp) * 100));
    const expPercent = Math.min(100, Math.round((d.exp / d.maxExp) * 100));

    document.getElementById('status-hp-text').textContent = `${d.hp} / ${d.maxHp}`;
    document.getElementById('status-hp-fill').style.width = `${hpPercent}%`;

    document.getElementById('status-mp-text').textContent = `${d.mp} / ${d.maxMp}`;
    document.getElementById('status-mp-fill').style.width = `${mpPercent}%`;

    document.getElementById('status-exp-text').textContent = `${d.exp} / ${d.maxExp} (${expPercent}%)`;
    document.getElementById('status-exp-fill').style.width = `${expPercent}%`;

    // Unallocated Stat Points
    document.getElementById('status-unallocated-pts').textContent = d.unallocatedPoints;

    // Stats
    const stats = ['int', 'vit', 'agi', 'sen', 'str'];
    stats.forEach(st => {
      const valEl = document.getElementById(`stat-val-${st}`);
      const btnEl = document.getElementById(`stat-btn-${st}`);
      if (valEl) valEl.textContent = d.stats[st];
      if (btnEl) btnEl.disabled = d.unallocatedPoints <= 0;
    });

    // Hunter Lifetime Milestones
    document.getElementById('stat-total-solved').textContent = d.statsUnlocked.totalQuestionsSolved;
    document.getElementById('stat-mocks-cleared').textContent = d.statsUnlocked.mockTestsCleared;
    document.getElementById('stat-shadows-count').textContent = d.statsUnlocked.shadowsExtracted;
    document.getElementById('stat-streak-count').textContent = `${window.Quests ? window.Quests.data.streak : 1} Days`;

    // CGL Target Post info
    const currentPost = player.getTargetPost();
    const cutoffEl = document.getElementById('target-post-cutoff');
    if (cutoffEl && currentPost) {
      cutoffEl.textContent = `Target: ${currentPost.cutoffTarget}`;
    }
  }

  // Populate CGL Target Post Selector
  const postSelect = document.getElementById('cgl-target-post-select');
  if (postSelect && player.cglTargetPosts) {
    postSelect.innerHTML = '';
    player.cglTargetPosts.forEach(post => {
      const opt = document.createElement('option');
      opt.value = post.id;
      opt.textContent = `${post.icon} ${post.name} (${post.ministry})`;
      if (post.id === player.data.targetPostId) opt.selected = true;
      postSelect.appendChild(opt);
    });

    postSelect.addEventListener('change', (e) => {
      const newPost = player.setTargetPost(e.target.value);
      if (newPost) {
        showSystemNotification('CGL TARGET POST SET', `🎯 Selected Target Post: ${newPost.name} (${newPost.rankBadge})! Aim for ${newPost.cutoffTarget}.`);
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      }
    });
  }

  // Name edit handler
  const nameInput = document.getElementById('status-player-name');
  if (nameInput) {
    nameInput.addEventListener('change', (e) => {
      player.setName(e.target.value);
    });
  }

  // Stat buttons
  ['int', 'vit', 'agi', 'sen', 'str'].forEach(st => {
    const btn = document.getElementById(`stat-btn-${st}`);
    if (btn) {
      btn.addEventListener('click', () => {
        player.allocateStat(st);
      });
    }
  });

  window.addEventListener('player-updated', render);
  window.addEventListener('quests-updated', render);
  render();
}

/* ==========================================================================
   3. INSTANT DAILY SKIRMISH (QUESTION OF THE DAY WIDGET)
   ========================================================================== */
function initDailySkirmishUI() {
  const bank = window.QuestionBank;
  if (!bank) return;

  const randomQ = bank.getRandomBatch(1)[0];
  if (!randomQ) return;

  document.getElementById('daily-skirmish-subject').textContent = `${randomQ.subject} • ${randomQ.topic} (${randomQ.difficulty})`;
  document.getElementById('daily-skirmish-question').textContent = randomQ.question;

  const optionsContainer = document.getElementById('daily-skirmish-options');
  const feedbackEl = document.getElementById('daily-skirmish-feedback');
  if (!optionsContainer) return;

  optionsContainer.innerHTML = '';
  let answered = false;

  randomQ.options.forEach((opt, idx) => {
    const btn = document.createElement('button');
    btn.className = 'sys-btn';
    btn.style.textAlign = 'left';
    btn.style.justifyContent = 'flex-start';
    btn.style.fontSize = '0.8rem';
    btn.textContent = `${String.fromCharCode(65 + idx)}. ${opt}`;

    btn.addEventListener('click', () => {
      if (answered) return;
      answered = true;

      if (idx === randomQ.correct) {
        btn.style.background = 'rgba(34, 197, 94, 0.4)';
        btn.style.borderColor = '#22c55e';
        feedbackEl.style.display = 'block';
        feedbackEl.style.color = '#4ade80';
        feedbackEl.innerHTML = `<strong>✓ CORRECT!</strong> +30 EXP & +10 Gold earned!<br><span style="color: #94a3b8;">${randomQ.explanation}</span>`;

        if (window.Player) {
          window.Player.addExp(30);
          window.Player.addGold(10);
          window.Player.data.statsUnlocked.totalQuestionsSolved += 1;
          window.Player.saveState();
        }
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      } else {
        btn.style.background = 'rgba(255, 51, 102, 0.4)';
        btn.style.borderColor = '#ff3366';
        feedbackEl.style.display = 'block';
        feedbackEl.style.color = '#ff809b';
        feedbackEl.innerHTML = `<strong>✗ INCORRECT!</strong> Correct: <strong>${randomQ.options[randomQ.correct]}</strong><br><span style="color: #94a3b8;">${randomQ.explanation}</span>`;

        // Send to Shadow error notebook
        if (window.Shadows) {
          window.Shadows.addFallenMonster(randomQ, idx);
        }
        if (window.SystemAudio) window.SystemAudio.playBossHit(true);
      }
    });

    optionsContainer.appendChild(btn);
  });
}

/* ==========================================================================
   4. QUESTION VAULT & CHAPTER PRACTICE UI
   ========================================================================== */
function initQuestionVaultUI() {
  const bank = window.QuestionBank;
  if (!bank) return;

  const container = document.getElementById('vault-questions-container');
  const countEl = document.getElementById('vault-total-count');
  const searchInput = document.getElementById('vault-search-input');
  const subjectFilter = document.getElementById('vault-subject-filter');
  const diffFilter = document.getElementById('vault-difficulty-filter');

  function renderList() {
    if (!container) return;
    let allQuestions = [...bank.getAll()];
    if (window.PyqBank && window.PyqBank.getAllQuestions) {
      const pyqQs = window.PyqBank.getAllQuestions();
      const existingIds = new Set(allQuestions.map(q => q.id));
      pyqQs.forEach(q => {
        if (!existingIds.has(q.id)) {
          allQuestions.push(q);
          existingIds.add(q.id);
        }
      });
    }

    const query = (searchInput ? searchInput.value : '').toLowerCase();
    const selectedSub = subjectFilter ? subjectFilter.value : 'all';
    const selectedDiff = diffFilter ? diffFilter.value : 'all';

    const filtered = allQuestions.filter(q => {
      const matchQuery = !query || q.question.toLowerCase().includes(query) || q.topic.toLowerCase().includes(query) || q.subject.toLowerCase().includes(query) || (q.examTag && q.examTag.toLowerCase().includes(query));
      const matchSub = selectedSub === 'all' || q.subject === selectedSub;
      const matchDiff = selectedDiff === 'all' || q.difficulty === selectedDiff;
      return matchQuery && matchSub && matchDiff;
    });

    if (countEl) countEl.textContent = `${filtered.length} Qs Available`;

    container.innerHTML = '';
    if (filtered.length === 0) {
      container.innerHTML = `
        <div style="text-align: center; padding: 40px; color: var(--sys-text-dim); font-family: var(--font-hud);">
          NO QUESTIONS MATCH YOUR SEARCH CRITERIA.<br>
          <span style="font-size: 0.8rem;">Try clearing the search or filters to see all available questions.</span>
        </div>
      `;
      return;
    }

    filtered.forEach((q, qIndex) => {
      const card = document.createElement('div');
      card.className = 'system-card';
      card.style.padding = '20px';
      card.style.display = 'flex';
      card.style.flexDirection = 'column';
      card.style.gap = '14px';

      const diffColor = q.difficulty === 'Easy' ? '#22c55e' : (q.difficulty === 'Medium' ? '#eab308' : '#ff3366');

      card.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 8px;">
          <div>
            <span class="hud-sys-label text-cyan">${q.subject}</span>
            <span style="font-size: 0.85rem; color: #fff; font-weight: 600; margin-left: 8px;">• ${q.topic}</span>
          </div>
          <span style="font-size: 0.75rem; font-weight: 700; color: ${diffColor}; border: 1px solid ${diffColor}; padding: 2px 8px; border-radius: 4px; font-family: var(--font-hud);">
            ${q.difficulty}
          </span>
        </div>

        <div style="font-size: 1rem; color: #fff; line-height: 1.6;">
          <strong>Q${qIndex + 1}.</strong> ${q.question}
        </div>

        <div class="cbt-options-list" id="opts-${q.id}">
          ${q.options.map((opt, oIdx) => `
            <div class="cbt-option-item vault-opt-btn" data-qid="${q.id}" data-oidx="${oIdx}">
              <div class="option-letter">${String.fromCharCode(65 + oIdx)}</div>
              <div class="option-text">${opt}</div>
            </div>
          `).join('')}
        </div>

        <div id="feedback-${q.id}" style="display: none; padding: 12px; border-radius: 6px; font-size: 0.88rem; line-height: 1.5; border: 1px solid var(--sys-border); background: rgba(5,8,17,0.8);"></div>

        <div style="display: flex; justify-content: space-between; align-items: center; border-top: 1px solid rgba(56,189,248,0.1); padding-top: 10px; margin-top: 4px;">
          <button class="sys-btn show-solution-btn" data-qid="${q.id}" style="font-size: 0.75rem; padding: 6px 12px;">
            💡 REVEAL TRICK & SOLUTION
          </button>
          <button class="sys-btn sys-btn-shadow add-shadow-btn" data-qid="${q.id}" style="font-size: 0.75rem; padding: 6px 12px;">
            👑 SEND TO SHADOW NOTEBOOK
          </button>
        </div>
      `;

      // Options Click Handlers
      let answered = false;
      card.querySelectorAll('.vault-opt-btn').forEach(btn => {
        btn.addEventListener('click', () => {
          if (answered) return;
          answered = true;
          const oIdx = parseInt(btn.getAttribute('data-oidx'), 10);
          const feedbackDiv = card.querySelector(`#feedback-${q.id}`);

          if (oIdx === q.correct) {
            btn.style.background = 'rgba(34, 197, 94, 0.3)';
            btn.style.borderColor = '#22c55e';
            feedbackDiv.style.display = 'block';
            feedbackDiv.style.borderColor = '#22c55e';
            feedbackDiv.innerHTML = `
              <div class="text-cyan font-hud" style="margin-bottom: 4px;">✓ CORRECT! (+20 EXP)</div>
              <p style="color: #cbd5e1;">${q.explanation}</p>
              ${q.trick ? `<div style="color: var(--sys-cyan); margin-top: 6px;"><strong>⚡ Hunter Shortcut:</strong> ${q.trick}</div>` : ''}
            `;
            if (window.Player) {
              window.Player.addExp(20);
              window.Player.data.statsUnlocked.totalQuestionsSolved += 1;
              window.Player.saveState();
            }
            if (window.SystemAudio) window.SystemAudio.playLevelUp();
          } else {
            btn.style.background = 'rgba(255, 51, 102, 0.3)';
            btn.style.borderColor = '#ff3366';
            feedbackDiv.style.display = 'block';
            feedbackDiv.style.borderColor = '#ff3366';
            feedbackDiv.innerHTML = `
              <div class="text-red font-hud" style="margin-bottom: 4px;">✗ INCORRECT. Correct Answer: ${q.options[q.correct]}</div>
              <p style="color: #cbd5e1;">${q.explanation}</p>
              ${q.trick ? `<div style="color: var(--sys-cyan); margin-top: 6px;"><strong>⚡ Hunter Shortcut:</strong> ${q.trick}</div>` : ''}
            `;
            if (window.Shadows) window.Shadows.addFallenMonster(q, oIdx);
            if (window.SystemAudio) window.SystemAudio.playBossHit(true);
          }
        });
      });

      // Show solution button
      card.querySelector('.show-solution-btn').addEventListener('click', () => {
        const feedbackDiv = card.querySelector(`#feedback-${q.id}`);
        feedbackDiv.style.display = feedbackDiv.style.display === 'block' ? 'none' : 'block';
        feedbackDiv.innerHTML = `
          <div class="text-gold font-hud" style="margin-bottom: 4px;">📖 CONCEPT & SOLUTION (Correct: ${q.options[q.correct]})</div>
          <p style="color: #cbd5e1;">${q.explanation}</p>
          ${q.trick ? `<div style="color: var(--sys-cyan); margin-top: 6px;"><strong>⚡ Hunter Shortcut:</strong> ${q.trick}</div>` : ''}
        `;
        if (window.SystemAudio) window.SystemAudio.playClick();
      });

      // Send to Shadow Notebook button
      card.querySelector('.add-shadow-btn').addEventListener('click', () => {
        if (window.Shadows) {
          window.Shadows.addFallenMonster(q, null);
          showSystemNotification('SHADOW CHASM', `"${q.topic}" added to Fallen Monsters for "ARISE" extraction practice!`);
        }
      });

      container.appendChild(card);
    });
  }

  const generateBtn = document.getElementById('vault-generate-inf-btn');
  if (generateBtn) {
    generateBtn.addEventListener('click', () => {
      if (window.QuestionBank && window.QuestionBank.generateInfiniteBatch) {
        const newQs = window.QuestionBank.generateInfiniteBatch(50);
        renderList();
        showSystemNotification('INFINITE QUESTIONS SPAWNED', `⚡ Successfully generated ${newQs.length} fresh procedural SSC questions into your Vault!`);
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      }
    });
  }

  if (searchInput) searchInput.addEventListener('input', renderList);
  if (subjectFilter) subjectFilter.addEventListener('change', renderList);
  if (diffFilter) diffFilter.addEventListener('change', renderList);

  renderList();
}

/* ==========================================================================
   5. DAILY QUESTS UI
   ========================================================================== */
function initQuestsUI() {
  const quests = window.Quests;
  if (!quests) return;

  function render() {
    const container = document.getElementById('daily-tasks-list');
    if (!container) return;

    container.innerHTML = '';
    quests.data.tasks.forEach(task => {
      const isDone = task.current >= task.target;
      const card = document.createElement('div');
      card.className = `quest-task-card ${isDone ? 'completed' : ''}`;

      card.innerHTML = `
        <div class="task-left-info">
          <div class="task-checkbox" data-task="${task.id}">
            ${isDone ? '✓' : ''}
          </div>
          <div class="task-text-group">
            <span class="task-name">${task.name}</span>
            <span class="task-meta">Reward: +${task.exp} EXP</span>
          </div>
        </div>
        <div class="task-progress-badge">
          ${task.current} / ${task.target} ${task.unit}
        </div>
      `;

      card.querySelector('.task-checkbox').addEventListener('click', () => {
        quests.toggleTaskDirect(task.id);
      });

      container.appendChild(card);
    });

    // Reward Claim Button
    const claimBtn = document.getElementById('claim-daily-reward-btn');
    const allDone = quests.isAllCompleted();
    if (claimBtn) {
      claimBtn.disabled = !allDone || quests.data.claimed;
      claimBtn.textContent = quests.data.claimed ? 'REWARD CLAIMED (✓)' : (allDone ? 'CLAIM REWARD BOX (+3 STAT PTS)' : 'COMPLETE ALL TASKS');
    }

    // Overall Progress Bar
    const prog = quests.getOverallProgress();
    const progFill = document.getElementById('daily-quest-prog-fill');
    const progText = document.getElementById('daily-quest-prog-text');
    if (progFill) progFill.style.width = `${prog}%`;
    if (progText) progText.textContent = `${prog}%`;

    // Penalty Banner State
    const penaltyBanner = document.getElementById('penalty-zone-banner');
    if (penaltyBanner) {
      penaltyBanner.style.display = quests.data.penaltyActive ? 'flex' : 'none';
    }
  }

  // Claim Button Click
  const claimBtn = document.getElementById('claim-daily-reward-btn');
  if (claimBtn) {
    claimBtn.addEventListener('click', () => {
      const res = quests.claimDailyReward();
      if (res.success) {
        showSystemNotification('QUEST COMPLETED', `Preparation to Become Strong cleared! Received +${res.exp} EXP, +${res.gold} Gold, and +${res.statPoints} Stat Points!`);
      }
    });
  }

  // Trigger Penalty Survival Button (Manual / Due)
  const startPenaltyBtn = document.getElementById('start-penalty-btn');
  if (startPenaltyBtn) {
    startPenaltyBtn.addEventListener('click', () => {
      openPenaltyModal();
    });
  }

  window.addEventListener('quests-updated', render);
  render();
}

function updateQuestCountdown() {
  const el = document.getElementById('quest-reset-timer');
  if (el && window.Quests) {
    el.textContent = window.Quests.getTimeUntilReset();
  }
}

/* ==========================================================================
   6. GATE RAIDS & CBT MOCK TEST UI
   ========================================================================== */
let isOfficialCbtView = false;

function initDungeonsUI() {
  const dungeons = window.Dungeons;
  if (!dungeons) return;

  renderGatesList();
  renderPyqPapersList();
  initPyqFilterButtons();

  // Test mode toggle (Boss Battle Mode vs Official CBT View)
  const toggleViewBtn = document.getElementById('toggle-cbt-view-btn');
  if (toggleViewBtn) {
    toggleViewBtn.addEventListener('click', () => {
      isOfficialCbtView = !isOfficialCbtView;
      toggleViewBtn.textContent = isOfficialCbtView ? 'SWITCH TO: BOSS BATTLE MODE' : 'SWITCH TO: OFFICIAL SSC CBT MODE';
      updateCbtThemeMode();
    });
  }

  // Next / Prev / Clear / Flag / Submit Actions
  const cbtNext = document.getElementById('cbt-next-btn');
  if (cbtNext) {
    cbtNext.addEventListener('click', () => {
      dungeons.nextQuestion();
      renderActiveQuestion();
    });
  }

  const cbtPrev = document.getElementById('cbt-prev-btn');
  if (cbtPrev) {
    cbtPrev.addEventListener('click', () => {
      dungeons.prevQuestion();
      renderActiveQuestion();
    });
  }

  const cbtClear = document.getElementById('cbt-clear-btn');
  if (cbtClear) {
    cbtClear.addEventListener('click', () => {
      dungeons.clearResponse();
      renderActiveQuestion();
    });
  }

  const cbtFlag = document.getElementById('cbt-flag-btn');
  if (cbtFlag) {
    cbtFlag.addEventListener('click', () => {
      dungeons.toggleFlag();
      renderActiveQuestion();
    });
  }

  const cbtSubmit = document.getElementById('cbt-submit-test-btn');
  if (cbtSubmit) {
    cbtSubmit.addEventListener('click', () => {
      const res = dungeons.submitRaid();
      if (res) renderRaidScoreboard(res);
    });
  }

  // Timer Tick Event
  window.addEventListener('raid-timer-tick', (e) => {
    const seconds = e.detail;
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    const timerStr = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;

    const timerEl = document.getElementById('cbt-timer-display');
    if (timerEl) timerEl.textContent = timerStr;
  });
}

function renderGatesList() {
  const container = document.getElementById('dungeon-gates-grid');
  if (!container || !window.Dungeons) return;

  container.innerHTML = '';
  window.Dungeons.getGates().forEach(gate => {
    const card = document.createElement('div');
    card.className = 'gate-card system-card';

    card.innerHTML = `
      <div class="gate-top-row">
        <span class="rank-badge rank-${gate.rank}">${gate.rank}-RANK</span>
        <span class="gate-subject-tag">${gate.subject}</span>
      </div>
      <h3 class="gate-name">${gate.name}</h3>
      <p class="stat-desc">${gate.desc}</p>
      <div class="gate-stats-row">
        <span>⏱️ ${gate.timeMinutes} Mins</span>
        <span>❓ ${gate.questionCount} Questions</span>
        <span class="text-gold">✨ +${gate.expReward} EXP</span>
      </div>
      <button class="sys-btn sys-btn-primary start-gate-btn" data-id="${gate.id}" style="width: 100%; margin-top: auto;">
        ENTER DUNGEON RAID ⚔️
      </button>
    `;

    card.querySelector('.start-gate-btn').addEventListener('click', () => {
      startDungeonRaid(gate.id);
    });

    container.appendChild(card);
  });
}

let activePyqFilter = 'all';

function renderPyqPapersList(filter = activePyqFilter) {
  const container = document.getElementById('pyq-papers-grid');
  if (!container || !window.PyqBank) return;

  activePyqFilter = filter;
  const allPapers = window.PyqBank.getAllPapers();
  const filtered = allPapers.filter(paper => {
    if (filter === 'all') return true;
    if (filter === '2025') return paper.year === '2025';
    return paper.exam.toUpperCase().includes(filter.toUpperCase());
  });

  container.innerHTML = '';

  if (filtered.length === 0) {
    container.innerHTML = `
      <div style="grid-column: 1 / -1; text-align: center; padding: 30px; color: var(--sys-text-dim);">
        No papers found matching this category filter.
      </div>
    `;
    return;
  }

  filtered.forEach(paper => {
    const card = document.createElement('div');
    card.className = 'gate-card system-card';
    card.style.borderColor = paper.year === '2025' ? 'rgba(0, 242, 255, 0.45)' : 'rgba(255, 215, 0, 0.35)';
    card.style.background = paper.year === '2025'
      ? 'radial-gradient(circle at 100% 0%, rgba(0, 242, 255, 0.12), rgba(9, 14, 29, 0.88) 70%)'
      : 'radial-gradient(circle at 100% 0%, rgba(255, 215, 0, 0.08), rgba(9, 14, 29, 0.85) 70%)';

    card.innerHTML = `
      <div class="gate-top-row">
        <span class="rank-badge ${paper.year === '2025' ? 'rank-Monarch' : 'rank-S'}">🏛️ ${paper.year} PYQ</span>
        <span class="gate-subject-tag ${paper.year === '2025' ? 'text-cyan' : 'text-gold'}">${paper.shift}</span>
      </div>
      <h3 class="gate-name" style="color: #fff; text-shadow: 0 0 10px rgba(0, 242, 255, 0.3);">${paper.exam}</h3>
      <p class="stat-desc">${paper.desc}</p>
      <div class="gate-stats-row">
        <span>⏱️ ${paper.durationMinutes} Mins</span>
        <span>❓ ${paper.questions.length} Questions</span>
        <span class="text-gold">✨ +${paper.year === '2025' ? '750' : '650'} EXP</span>
      </div>
      <div style="display: flex; gap: 8px; margin-top: auto;">
        <button class="sys-btn sys-btn-primary start-pyq-btn" data-id="${paper.id}" style="flex: 1; font-size: 0.75rem; padding: 10px 8px;">
          ATTEMPT CBT MOCK ⚔️
        </button>
        <button class="sys-btn view-pyq-sol-btn" data-id="${paper.id}" style="font-size: 0.75rem; padding: 10px 12px; background: rgba(0, 242, 255, 0.12); border-color: var(--sys-cyan); color: var(--sys-cyan);">
          📖 SOLUTIONS
        </button>
      </div>
    `;

    card.querySelector('.start-pyq-btn').addEventListener('click', () => {
      startPyqMockRaid(paper.id);
    });

    card.querySelector('.view-pyq-sol-btn').addEventListener('click', () => {
      showPyqSolutionsModal(paper);
    });

    container.appendChild(card);
  });
}

function initPyqFilterButtons() {
  const filterBtns = document.querySelectorAll('.pyq-filter-btn');
  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => {
        b.classList.remove('active');
        b.classList.remove('sys-btn-primary');
      });
      btn.classList.add('active');
      btn.classList.add('sys-btn-primary');
      const filter = btn.getAttribute('data-filter') || 'all';
      renderPyqPapersList(filter);
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  });
}

function startPyqMockRaid(paperId) {
  const raid = window.Dungeons.startPyqRaid(paperId);
  if (!raid) return;

  document.getElementById('dungeons-selection-view').style.display = 'none';
  document.getElementById('dungeon-active-raid-view').style.display = 'block';

  document.getElementById('boss-display-name').textContent = raid.gate.bossName;
  document.getElementById('boss-display-avatar').textContent = raid.gate.bossAvatar;

  renderActiveQuestion();
}

function showPyqSolutionsModal(paper) {
  const res = {
    isBossDefeated: true,
    rawScore: paper.questions.length * 2,
    maxScore: paper.questions.length * 2,
    accuracy: 100,
    correctCount: paper.questions.length,
    wrongCount: 0,
    unattemptedCount: 0,
    questions: paper.questions,
    answers: paper.questions.map(q => q.correct)
  };
  renderRaidScoreboard(res);
  const titleEl = document.getElementById('score-boss-result-title');
  if (titleEl) {
    titleEl.textContent = `📖 OFFICIAL SOLUTIONS & SHORTCUTS: ${paper.exam}`;
    titleEl.className = 'text-gold font-hud';
  }
}

function startDungeonRaid(gateId) {
  const raid = window.Dungeons.startRaid(gateId);
  if (!raid) return;

  // Show Raid Arena View
  document.getElementById('dungeons-selection-view').style.display = 'none';
  document.getElementById('dungeon-active-raid-view').style.display = 'block';

  // Set Boss Info
  document.getElementById('boss-display-name').textContent = raid.gate.bossName;
  document.getElementById('boss-display-avatar').textContent = raid.gate.bossAvatar;

  renderActiveQuestion();
}

function renderActiveQuestion() {
  const raid = window.Dungeons.activeRaid;
  if (!raid) return;

  const q = raid.questions[raid.currentIndex];
  const total = raid.questions.length;
  const userAns = raid.answers[raid.currentIndex];
  const isFlagged = raid.flags[raid.currentIndex];

  // Header & Info
  document.getElementById('cbt-question-number').textContent = `QUESTION ${raid.currentIndex + 1} OF ${total}`;
  document.getElementById('cbt-question-subject').textContent = `${q.subject} • ${q.topic} (${q.difficulty})`;
  document.getElementById('cbt-question-text').textContent = q.question;

  // Render Options
  const optionsList = document.getElementById('cbt-options-container');
  optionsList.innerHTML = '';

  const letters = ['A', 'B', 'C', 'D'];
  q.options.forEach((opt, idx) => {
    const optDiv = document.createElement('div');
    optDiv.className = `cbt-option-item ${userAns === idx ? 'selected' : ''}`;
    optDiv.innerHTML = `
      <div class="option-letter">${letters[idx]}</div>
      <div class="option-text">${opt}</div>
    `;

    optDiv.addEventListener('click', () => {
      window.Dungeons.selectOption(idx);
      renderActiveQuestion();
    });

    optionsList.appendChild(optDiv);
  });

  // Flag Button text
  const flagBtn = document.getElementById('cbt-flag-btn');
  if (flagBtn) {
    flagBtn.textContent = isFlagged ? 'UNMARK REVIEW' : 'MARK FOR REVIEW';
  }

  // Prev / Next button state
  document.getElementById('cbt-prev-btn').disabled = raid.currentIndex === 0;
  document.getElementById('cbt-next-btn').disabled = raid.currentIndex === total - 1;

  // Boss HP & Player HP Bars
  const bossHpPct = Math.max(0, Math.round((raid.bossHp / raid.bossMaxHp) * 100));
  document.getElementById('boss-hp-fill').style.width = `${bossHpPct}%`;
  document.getElementById('boss-hp-text').textContent = `${raid.bossHp} / ${raid.bossMaxHp} HP`;

  // Render Question Palette Grid
  renderQuestionPalette();
}

function renderQuestionPalette() {
  const raid = window.Dungeons.activeRaid;
  const palette = document.getElementById('cbt-palette-grid');
  if (!palette || !raid) return;

  palette.innerHTML = '';
  raid.questions.forEach((_, idx) => {
    const btn = document.createElement('button');
    btn.className = 'palette-btn';
    btn.textContent = idx + 1;

    if (idx === raid.currentIndex) btn.classList.add('current');
    else if (raid.flags[idx]) btn.classList.add('flagged');
    else if (raid.answers[idx] !== null) btn.classList.add('answered');

    btn.addEventListener('click', () => {
      window.Dungeons.goToQuestion(idx);
      renderActiveQuestion();
    });

    palette.appendChild(btn);
  });
}

function updateCbtThemeMode() {
  const bossHud = document.getElementById('boss-hud-header');
  if (isOfficialCbtView) {
    if (bossHud) bossHud.style.display = 'none';
  } else {
    if (bossHud) bossHud.style.display = 'flex';
  }
}

function renderRaidScoreboard(result) {
  document.getElementById('dungeon-active-raid-view').style.display = 'none';
  const scoreView = document.getElementById('dungeon-scoreboard-view');
  scoreView.style.display = 'block';

  document.getElementById('score-boss-result-title').textContent = result.isBossDefeated ? '⚔️ DUNGEON CLEARED - BOSS DEFEATED!' : '💀 RAID FAILED - REVISION REQUIRED';
  document.getElementById('score-boss-result-title').className = result.isBossDefeated ? 'text-cyan font-hud' : 'text-red font-hud';

  document.getElementById('score-raw-marks').textContent = `${result.rawScore.toFixed(2)} / ${result.maxScore}`;
  document.getElementById('score-accuracy-pct').textContent = `${result.accuracy}%`;
  document.getElementById('score-correct-count').textContent = result.correctCount;
  document.getElementById('score-wrong-count').textContent = result.wrongCount;
  document.getElementById('score-unattempted-count').textContent = result.unattemptedCount;

  // Solutions breakdown list
  const solutionsList = document.getElementById('score-solutions-breakdown');
  solutionsList.innerHTML = '';

  result.questions.forEach((q, idx) => {
    const userAns = result.answers[idx];
    const isCorrect = userAns === q.correct;
    const isMissed = userAns === null;

    const row = document.createElement('div');
    row.className = 'fallen-monster-item';
    row.innerHTML = `
      <div class="monster-info-block" style="flex: 1;">
        <div class="stat-title">
          <span style="color: ${isCorrect ? '#22c55e' : (isMissed ? '#94a3b8' : '#ff3366')}">
            Q${idx + 1}. [${isCorrect ? 'CORRECT +2.0' : (isMissed ? 'UNATTEMPTED 0' : 'INCORRECT -0.50')}]
          </span>
          - ${q.question}
        </div>
        <div class="monster-subject">Your Choice: <strong>${userAns !== null ? q.options[userAns] : 'None'}</strong> | Correct: <strong>${q.options[q.correct]}</strong></div>
        <div style="font-size: 0.85rem; color: #94a3b8; margin-top: 6px;">
          <strong>Explanation:</strong> ${q.explanation}
        </div>
        ${q.trick ? `<div style="font-size: 0.85rem; color: #00f2ff; margin-top: 4px;"><strong>⚡ Hunter Trick:</strong> ${q.trick}</div>` : ''}
      </div>
    `;
    solutionsList.appendChild(row);
  });

  document.getElementById('scoreboard-back-btn').onclick = () => {
    scoreView.style.display = 'none';
    document.getElementById('dungeons-selection-view').style.display = 'block';
  };
}

/* ==========================================================================
   7. SHADOW ARMY & "ARISE" (MISTAKE NOTEBOOK)
   ========================================================================== */
function initShadowsUI() {
  const shadows = window.Shadows;
  if (!shadows) return;

  function render() {
    // Monarch Stats
    document.getElementById('monarch-total-shadows').textContent = shadows.data.totalShadows;

    // Lieutenants List
    const cmdList = document.getElementById('shadow-commanders-grid');
    if (cmdList) {
      cmdList.innerHTML = '';
      shadows.data.commanders.forEach(cmd => {
        const expPct = Math.min(100, Math.round((cmd.exp / cmd.maxExp) * 100));
        const card = document.createElement('div');
        card.className = 'shadow-soldier-card';
        card.innerHTML = `
          <div class="soldier-header">
            <span class="soldier-name">${cmd.avatar} ${cmd.name}</span>
            <span class="soldier-rank-tag">LVL ${cmd.level}</span>
          </div>
          <div class="stat-desc" style="color: #c084fc;">Subject: ${cmd.subject}</div>
          <div class="soldier-buff-text">⚡ ${cmd.buff}</div>
          <div class="soldier-exp-bar">
            <div class="soldier-exp-fill" style="width: ${expPct}%"></div>
          </div>
          <div style="font-size: 0.75rem; color: #94a3b8; display: flex; justify-content: space-between;">
            <span>Extracted: ${cmd.extractedCount}</span>
            <span>EXP: ${cmd.exp}/${cmd.maxExp}</span>
          </div>
        `;
        cmdList.appendChild(card);
      });
    }

    // Fallen Monsters Error Queue
    const monstersList = document.getElementById('fallen-monsters-list');
    if (monstersList) {
      monstersList.innerHTML = '';
      const unresolved = shadows.data.fallenMonsters.filter(m => !m.resolved);

      if (unresolved.length === 0) {
        monstersList.innerHTML = `
          <div style="text-align: center; padding: 40px; color: #a855f7; font-family: var(--font-hud);">
            NO FALLEN MONSTERS IN CHASM.<br>
            <span style="font-size: 0.85rem; color: var(--sys-text-dim);">Mistakes made during Gates, Skirmishes and Boss CBT Mocks will appear here for extraction!</span>
          </div>
        `;
      } else {
        unresolved.forEach(item => {
          const mDiv = document.createElement('div');
          mDiv.className = 'fallen-monster-item';
          const q = item.question;

          mDiv.innerHTML = `
            <div class="monster-info-block" style="flex: 1;">
              <span class="monster-title">👾 ${q.topic} (${q.subject})</span>
              <p style="font-size: 0.9rem; color: #fff; margin: 4px 0;">${q.question}</p>
              <span class="monster-subject">Correct: <strong>${q.options[q.correct]}</strong> | Trick: ${q.trick || 'Concept Mastery'}</span>
            </div>
            <button class="sys-btn sys-btn-shadow arise-btn" data-id="${item.id}">
              ARISE 👑
            </button>
          `;

          mDiv.querySelector('.arise-btn').addEventListener('click', () => {
            const res = shadows.extractShadow(item.id);
            if (res.success) {
              triggerAriseAnimation(res.commander);
            }
          });

          monstersList.appendChild(mDiv);
        });
      }
    }
  }

  window.addEventListener('shadows-updated', render);
  render();
}

function triggerAriseAnimation(commanderName) {
  const modal = document.getElementById('arise-fx-modal');
  if (modal) {
    modal.classList.add('active');
    document.getElementById('arise-commander-name').textContent = commanderName;
    setTimeout(() => {
      modal.classList.remove('active');
    }, 2400);
  }
}

/* ==========================================================================
   8. FOCUS DUNGEON (POMODORO)
   ========================================================================== */
let focusTimerInterval = null;
let focusTimeRemaining = 25 * 60;
let isFocusRunning = false;

function initFocusDungeonUI() {
  const display = document.getElementById('focus-timer-text');
  const startBtn = document.getElementById('focus-start-btn');
  const resetBtn = document.getElementById('focus-reset-btn');
  const ambientBtn = document.getElementById('focus-ambient-toggle-btn');

  function updateTimerText() {
    const mins = Math.floor(focusTimeRemaining / 60);
    const secs = focusTimeRemaining % 60;
    if (display) display.textContent = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  }

  // Duration buttons (25, 45, 60 mins)
  document.querySelectorAll('.focus-preset-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const mins = parseInt(btn.getAttribute('data-mins'), 10);
      focusTimeRemaining = mins * 60;
      updateTimerText();
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  });

  if (startBtn) {
    startBtn.addEventListener('click', () => {
      if (isFocusRunning) {
        clearInterval(focusTimerInterval);
        isFocusRunning = false;
        startBtn.textContent = 'START FOCUS DUNGEON ⚔️';
      } else {
        isFocusRunning = true;
        startBtn.textContent = 'PAUSE DUNGEON ⏸️';
        if (window.SystemAudio) window.SystemAudio.playSystemAlert();

        focusTimerInterval = setInterval(() => {
          focusTimeRemaining -= 1;
          updateTimerText();
          if (focusTimeRemaining <= 0) {
            clearInterval(focusTimerInterval);
            isFocusRunning = false;
            startBtn.textContent = 'START FOCUS DUNGEON ⚔️';
            // Complete Focus
            if (window.Player) {
              window.Player.addExp(100);
              window.Player.data.statsUnlocked.focusMinutes += 25;
              window.Player.saveState();
            }
            if (window.Quests) {
              window.Quests.incrementTask('t_focus', 25);
            }
            if (window.SystemAudio) window.SystemAudio.playLevelUp();
            showSystemNotification('FOCUS DUNGEON CONQUERED', '25 Minutes Deep Study cleared! Awarded +100 EXP & Vitality boost!');
          }
        }, 1000);
      }
    });
  }

  if (resetBtn) {
    resetBtn.addEventListener('click', () => {
      clearInterval(focusTimerInterval);
      isFocusRunning = false;
      focusTimeRemaining = 25 * 60;
      updateTimerText();
      if (startBtn) startBtn.textContent = 'START FOCUS DUNGEON ⚔️';
    });
  }

  if (ambientBtn) {
    ambientBtn.addEventListener('click', () => {
      if (window.SystemAudio) {
        const isPlaying = window.SystemAudio.startAmbientFocus();
        ambientBtn.classList.toggle('active', isPlaying);
        ambientBtn.textContent = isPlaying ? 'ALPHA BINAURAL BEAT (ON 🎵)' : 'ALPHA BINAURAL BEAT (OFF 🔇)';
      }
    });
  }

  updateTimerText();
}

/* ==========================================================================
   9. SHOP & INVENTORY
   ========================================================================== */
function initShopUI() {
  const shop = window.Shop;
  if (!shop) return;

  function render() {
    document.getElementById('shop-gold-display').textContent = `${window.Player ? window.Player.data.gold : 0} G`;

    const grid = document.getElementById('system-shop-grid');
    if (!grid) return;

    grid.innerHTML = '';
    shop.items.forEach(item => {
      const card = document.createElement('div');
      card.className = 'shop-item-card system-card';

      card.innerHTML = `
        <div class="shop-item-icon">${item.icon}</div>
        <div class="shop-item-name">${item.name}</div>
        <p class="shop-item-desc">${item.desc}</p>
        <div class="shop-item-price-row">
          <span class="text-gold font-hud">💎 ${item.price} Gold</span>
          <button class="sys-btn sys-btn-gold buy-item-btn" data-id="${item.id}">
            PURCHASE
          </button>
        </div>
      `;

      card.querySelector('.buy-item-btn').addEventListener('click', () => {
        const res = shop.buyItem(item.id);
        if (res.success) {
          showSystemNotification('ITEM ACQUIRED', res.message);
          render();
        } else {
          showSystemNotification('SYSTEM ALERT', res.message);
        }
      });

      grid.appendChild(card);
    });
  }

  window.addEventListener('inventory-updated', render);
  window.addEventListener('player-updated', render);
  render();
}

/* ==========================================================================
   10. LATEST SYLLABUS & ONLINE GATEWAYS
   ========================================================================== */
function initOnlineSyllabusUI() {
  const sync = window.OnlineSync;
  if (!sync) return;

  // Render Syllabus
  const syl = sync.getSyllabus();
  document.getElementById('syllabus-version-tag').textContent = `${syl.version} (Updated ${syl.lastUpdated})`;

  const secList = document.getElementById('syllabus-sections-list');
  if (secList) {
    secList.innerHTML = '';
    syl.tier1Scheme.sections.forEach(sec => {
      const div = document.createElement('div');
      div.className = 'system-card';
      div.style.padding = '16px';
      div.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
          <strong class="text-cyan font-hud">${sec.name}</strong>
          <span class="text-gold font-hud">${sec.questions} Qs (${sec.marks} Marks)</span>
        </div>
        <div style="font-size: 0.85rem; color: #94a3b8;">
          <strong>Topics:</strong> ${sec.topics.join(' • ')}
        </div>
      `;
      secList.appendChild(div);
    });
  }

  // Live Sync Button
  const syncBtn = document.getElementById('sync-online-questions-btn');
  if (syncBtn) {
    syncBtn.addEventListener('click', async () => {
      syncBtn.disabled = true;
      syncBtn.textContent = 'SYNCHRONIZING WITH ONLINE GATEWAY...';
      const res = await sync.fetchLiveOnlineQuestions('all', 10);
      syncBtn.disabled = false;
      syncBtn.textContent = 'SYNC LIVE ONLINE QUESTIONS 🌐';

      if (res.success) {
        showSystemNotification('GATEWAY SYNCHRONIZED', `Successfully downloaded ${res.count} fresh online questions into your Knowledge Dungeon!`);
        initQuestionVaultUI(); // refresh questions in vault
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      } else {
        showSystemNotification('SYSTEM NOTIFICATION', res.message);
      }
    });
  }

  // Custom JSON Syllabus Import
  const importBtn = document.getElementById('import-json-btn');
  const importArea = document.getElementById('import-json-textarea');
  if (importBtn && importArea) {
    importBtn.addEventListener('click', () => {
      const val = importArea.value.trim();
      if (!val) return;
      const res = sync.importCustomQuestionsJSON(val);
      if (res.success) {
        showSystemNotification('KNOWLEDGE IMPORTED', `Successfully imported ${res.count} custom questions into the System!`);
        importArea.value = '';
        initQuestionVaultUI(); // refresh vault
      } else {
        showSystemNotification('IMPORT ERROR', res.message);
      }
    });
  }
}

/* ==========================================================================
   11. AUDIO CONTROLS & SYSTEM NOTIFICATIONS
   ========================================================================== */
function initAudioControls() {
  const audioBtn = document.getElementById('hud-sound-toggle-btn');
  if (audioBtn && window.SystemAudio) {
    audioBtn.addEventListener('click', () => {
      const enabled = window.SystemAudio.toggleSound();
      audioBtn.textContent = enabled ? '🔊' : '🔇';
      audioBtn.classList.toggle('active', enabled);
    });
  }
}

function showSystemNotification(title, message) {
  const overlay = document.getElementById('system-alert-modal');
  if (!overlay) return;

  document.getElementById('sys-modal-title').textContent = title;
  document.getElementById('sys-modal-message').textContent = message;
  overlay.classList.add('active');

  if (window.SystemAudio) window.SystemAudio.playSystemAlert();
}

function closeAllModals() {
  document.querySelectorAll('.system-modal-overlay').forEach(m => m.classList.remove('active'));
}

document.querySelectorAll('.modal-close-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    closeAllModals();
    if (window.triggerHaptic) window.triggerHaptic('light');
  });
});

// Backdrop tap dismiss for bottom sheets & modals
document.querySelectorAll('.system-modal-overlay').forEach(overlay => {
  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) {
      closeAllModals();
      if (window.triggerHaptic) window.triggerHaptic('light');
    }
  });
});

/* ==========================================================================
   11.5 SYSTEM SETTINGS & PREFERENCES CONTROLLER
   ========================================================================== */
function openSettings(e) {
  if (e && e.stopPropagation) {
    e.stopPropagation();
    e.preventDefault();
  }
  closeAllModals();
  if (typeof updateSettingsDisplay === 'function') {
    updateSettingsDisplay();
  }
  const modal = document.getElementById('system-settings-modal');
  if (modal) {
    modal.classList.add('active');
    if (window.triggerHaptic) window.triggerHaptic('medium');
    if (window.SystemAudio) window.SystemAudio.playClick();
  }
}
window.openSettings = openSettings;

function initSettingsUI() {
  const hudSettingsBtn = document.getElementById('hud-settings-btn');
  const guildSettingsCard = document.getElementById('guild-settings-card');

  if (hudSettingsBtn) hudSettingsBtn.addEventListener('click', openSettings);
  if (guildSettingsCard) guildSettingsCard.addEventListener('click', openSettings);

  // Update Settings Session Display
  function updateSettingsDisplay() {
    const auth = window.AuthClient;
    const accountNameEl = document.getElementById('settings-account-name');
    const accountStatusEl = document.getElementById('settings-account-status');
    const loginBtn = document.getElementById('settings-login-btn');
    const signoutBtn = document.getElementById('settings-signout-btn');
    const avatarIconEl = document.getElementById('settings-avatar-icon');

    if (auth && auth.currentUser) {
      if (accountNameEl) accountNameEl.textContent = auth.currentUser.hunterName || auth.currentUser.username;
      if (accountStatusEl) accountStatusEl.textContent = `Awakened Hunter • @${auth.currentUser.username} (Synced)`;
      if (avatarIconEl) avatarIconEl.textContent = '👑';
      if (loginBtn) loginBtn.style.display = 'none';
      if (signoutBtn) signoutBtn.style.display = 'inline-flex';
    } else {
      const pName = (window.Player && window.Player.data) ? window.Player.data.name : 'Guest Hunter';
      if (accountNameEl) accountNameEl.textContent = pName;
      if (accountStatusEl) accountStatusEl.textContent = 'Local Guest Mode (Not Synced to Cloud)';
      if (avatarIconEl) avatarIconEl.textContent = '🗡️';
      if (loginBtn) loginBtn.style.display = 'inline-flex';
      if (signoutBtn) signoutBtn.style.display = 'none';
    }
  }

  // 1. Sign Out Button
  const signoutBtn = document.getElementById('settings-signout-btn');
  if (signoutBtn) {
    signoutBtn.addEventListener('click', () => {
      if (window.AuthClient) {
        window.AuthClient.clearSession();
        updateSettingsDisplay();
        if (window.triggerHaptic) window.triggerHaptic('heavy');
        showSystemNotification('HUNTER SIGNED OUT', 'You have successfully signed out of the System network. Switched to local mode.');
      }
    });
  }

  // 2. Login Button inside Settings
  const loginBtn = document.getElementById('settings-login-btn');
  if (loginBtn) {
    loginBtn.addEventListener('click', () => {
      closeAllModals();
      const authModal = document.getElementById('auth-modal');
      if (authModal) authModal.classList.add('active');
    });
  }

  // 3. Master Sound Switch
  const soundToggle = document.getElementById('settings-sound-toggle');
  const hudSoundBtn = document.getElementById('hud-sound-toggle-btn');
  if (soundToggle && window.SystemAudio) {
    soundToggle.checked = window.SystemAudio.soundEnabled;
    soundToggle.addEventListener('change', (e) => {
      const enabled = window.SystemAudio.setSoundEnabled(e.target.checked);
      if (hudSoundBtn) {
        hudSoundBtn.textContent = enabled ? '🔊' : '🔇';
        hudSoundBtn.classList.toggle('active', enabled);
      }
      if (window.triggerHaptic) window.triggerHaptic('light');
    });
  }

  // 4. Volume Slider & Label
  const volumeSlider = document.getElementById('settings-volume-slider');
  const volumeLabel = document.getElementById('settings-volume-label');
  if (volumeSlider && window.SystemAudio) {
    const currVol = Math.round(window.SystemAudio.volume * 100);
    volumeSlider.value = currVol;
    if (volumeLabel) volumeLabel.textContent = `${currVol}%`;

    volumeSlider.addEventListener('input', (e) => {
      const val = parseInt(e.target.value, 10);
      window.SystemAudio.setVolume(val / 100);
      if (volumeLabel) volumeLabel.textContent = `${val}%`;
    });
  }

  // 5. Test Sound Chime
  const testSoundBtn = document.getElementById('settings-test-sound-btn');
  if (testSoundBtn && window.SystemAudio) {
    testSoundBtn.addEventListener('click', () => {
      window.SystemAudio.playSystemAlert();
      if (window.triggerHaptic) window.triggerHaptic('light');
    });
  }

  // 6. Ambient 10Hz Alpha Drone
  const ambientBtn = document.getElementById('settings-ambient-btn');
  if (ambientBtn && window.SystemAudio) {
    ambientBtn.addEventListener('click', () => {
      const playing = window.SystemAudio.startAmbientFocus();
      ambientBtn.textContent = playing ? '■ STOP DRONE' : '▶ PLAY DRONE';
      ambientBtn.classList.toggle('sys-btn-primary', playing);
      if (window.triggerHaptic) window.triggerHaptic('medium');
    });
  }

  // 7. Haptic Vibration Toggle & Test
  const hapticToggle = document.getElementById('settings-haptic-toggle');
  if (hapticToggle) {
    hapticToggle.checked = localStorage.getItem('solo_system_haptics_enabled') !== 'false';
    hapticToggle.addEventListener('change', (e) => {
      localStorage.setItem('solo_system_haptics_enabled', e.target.checked.toString());
      if (e.target.checked && window.triggerHaptic) window.triggerHaptic('light');
    });
  }

  const testVibrateBtn = document.getElementById('settings-test-vibrate-btn');
  if (testVibrateBtn) {
    testVibrateBtn.addEventListener('click', () => {
      if ('vibrate' in navigator) {
        navigator.vibrate([20, 50, 20]);
      }
    });
  }

  // 8. Battery Saver Mode Toggle
  const batteryToggle = document.getElementById('settings-battery-saver-toggle');
  if (batteryToggle) {
    const isBatterySaver = localStorage.getItem('solo_system_battery_saver') === 'true';
    batteryToggle.checked = isBatterySaver;
    if (isBatterySaver) document.body.classList.add('battery-saver');

    batteryToggle.addEventListener('change', (e) => {
      localStorage.setItem('solo_system_battery_saver', e.target.checked.toString());
      document.body.classList.toggle('battery-saver', e.target.checked);
      if (window.triggerHaptic) window.triggerHaptic('light');
    });
  }

  // 9. In-App PWA Install Prompt Handler
  let deferredInstallPrompt = null;
  const installBtn = document.getElementById('settings-install-app-btn');

  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredInstallPrompt = e;
  });

  if (installBtn) {
    installBtn.addEventListener('click', async () => {
      if (window.triggerHaptic) window.triggerHaptic('medium');
      if (deferredInstallPrompt) {
        deferredInstallPrompt.prompt();
        const { outcome } = await deferredInstallPrompt.userChoice;
        if (outcome === 'accepted') {
          showSystemNotification('APP INSTALLED', 'Solo SSC has been installed to your home screen! 🗡️');
        }
        deferredInstallPrompt = null;
      } else {
        alert('📲 TO INSTALL ON THIS PHONE:\n\n1. Tap the 3 dots (⋮) or Share icon in your browser menu.\n2. Tap "Install App" or "Add to Home Screen".\n3. Enjoy fullscreen offline gameplay!');
      }
    });
  }

  // 10. Fullscreen Toggle
  const fullscreenBtn = document.getElementById('settings-fullscreen-btn');
  if (fullscreenBtn) {
    fullscreenBtn.addEventListener('click', () => {
      if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen().catch(() => {});
        fullscreenBtn.textContent = '✕ EXIT FULLSCREEN';
      } else {
        document.exitFullscreen().catch(() => {});
        fullscreenBtn.textContent = '⛶ FULLSCREEN';
      }
      if (window.triggerHaptic) window.triggerHaptic('light');
    });
  }

  // 11. Manual Cloud Sync
  const syncNowBtn = document.getElementById('settings-sync-now-btn');
  if (syncNowBtn) {
    syncNowBtn.addEventListener('click', async () => {
      if (window.triggerHaptic) window.triggerHaptic('medium');
      syncNowBtn.textContent = '⏳ SYNCING...';
      if (window.OnlineSync) {
        await window.OnlineSync.syncWithBackend();
      }
      setTimeout(() => {
        syncNowBtn.textContent = '✓ SYNCED';
        setTimeout(() => { syncNowBtn.textContent = '🔄 SYNC NOW'; }, 2000);
      }, 500);
    });
  }

  // 11. Reset Local Progress Danger Action
  const resetBtn = document.getElementById('settings-reset-data-btn');
  if (resetBtn) {
    resetBtn.addEventListener('click', () => {
      const confirmed = window.confirm('⚠️ WARNING: Are you sure you want to reset all local Hunter stats, EXP, gold, and progress back to Level 1?');
      if (confirmed) {
        localStorage.removeItem('solo_leveling_exam_player');
        localStorage.removeItem('solo_system_daily_quests');
        localStorage.removeItem('solo_system_shadow_army');
        if (window.triggerHaptic) window.triggerHaptic('heavy');
        alert('System reset complete. Reloading...');
        window.location.reload();
      }
    });
  }
}

// Penalty Modal
function openPenaltyModal() {
  const modal = document.getElementById('penalty-zone-modal');
  if (!modal) return;
  modal.classList.add('active');

  const questions = window.QuestionBank.getRandomBatch(5);
  let qIdx = 0;
  let mistakes = 0;

  function renderPenaltyQ() {
    if (qIdx >= questions.length) {
      modal.classList.remove('active');
      if (window.Quests) window.Quests.clearPenalty();
      showSystemNotification('PENALTY SURVIVED', 'You survived the Desert of Trials! Normal System privileges restored.');
      return;
    }

    const q = questions[qIdx];
    document.getElementById('penalty-q-text').textContent = `[Trial ${qIdx + 1}/5] ${q.question}`;
    const optsDiv = document.getElementById('penalty-q-options');
    optsDiv.innerHTML = '';

    q.options.forEach((opt, idx) => {
      const btn = document.createElement('button');
      btn.className = 'sys-btn sys-btn-danger';
      btn.style.width = '100%';
      btn.style.textAlign = 'left';
      btn.style.margin = '4px 0';
      btn.textContent = opt;

      btn.addEventListener('click', () => {
        if (idx === q.correct) {
          qIdx += 1;
          renderPenaltyQ();
        } else {
          mistakes += 1;
          if (window.SystemAudio) window.SystemAudio.playBossHit(true);
          if (mistakes >= 3) {
            showSystemNotification('TRIAL FAILED', '3 mistakes reached! The desert storm intensifies. Retrying penalty trial...');
            qIdx = 0;
            mistakes = 0;
            renderPenaltyQ();
          } else {
            showSystemNotification('WRONG ANSWER', `Warning: ${3 - mistakes} trials remaining before collapse!`);
          }
        }
      });

      optsDiv.appendChild(btn);
    });
  }

  renderPenaltyQ();
}

/* ==========================================================================
   12. HUNTER AUTHENTICATION & AWAKENING UI
   ========================================================================== */
function initAuthUI() {
  const auth = window.AuthClient;
  const authModal = document.getElementById('auth-modal');
  const authBtn = document.getElementById('hud-auth-btn');
  const userPill = document.getElementById('hud-user-pill');
  const usernameDisplay = document.getElementById('hud-username-display');
  const logoutBtn = document.getElementById('hud-logout-btn');

  const loginTabBtn = document.getElementById('auth-tab-login-btn');
  const registerTabBtn = document.getElementById('auth-tab-register-btn');
  const loginForm = document.getElementById('auth-login-form');
  const registerForm = document.getElementById('auth-register-form');

  function updateHUDAuthState(isLoggedIn, user) {
    if (isLoggedIn && user) {
      if (authBtn) authBtn.style.display = 'none';
      if (userPill) {
        userPill.style.display = 'inline-flex';
        usernameDisplay.textContent = user.hunterName || user.username;
      }
    } else {
      if (authBtn) authBtn.style.display = 'inline-flex';
      if (userPill) userPill.style.display = 'none';
    }
  }

  // Auth Button click -> Open Modal
  if (authBtn && authModal) {
    authBtn.addEventListener('click', () => {
      authModal.classList.add('active');
      if (window.SystemAudio) window.SystemAudio.playSystemAlert();
    });
  }

  // Logout click
  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      if (auth) auth.logout();
      showSystemNotification('HUNTER LOGGED OUT', 'You have been signed out from the System.');
    });
  }

  // Switch between Login and Register tabs
  if (loginTabBtn && registerTabBtn) {
    loginTabBtn.addEventListener('click', () => {
      loginTabBtn.classList.add('active');
      registerTabBtn.classList.remove('active');
      loginForm.style.display = 'block';
      registerForm.style.display = 'none';
      if (window.SystemAudio) window.SystemAudio.playClick();
    });

    registerTabBtn.addEventListener('click', () => {
      registerTabBtn.classList.add('active');
      loginTabBtn.classList.remove('active');
      registerForm.style.display = 'block';
      loginForm.style.display = 'none';
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  }

  // Login Submit
  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const username = document.getElementById('login-username').value.trim();
      const password = document.getElementById('login-password').value;
      const errorDiv = document.getElementById('login-error-msg');

      errorDiv.style.display = 'none';
      const res = await auth.login(username, password);

      if (res.success) {
        authModal.classList.remove('active');
        showSystemNotification('AUTHENTICATION SUCCESS', res.message);
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      } else {
        errorDiv.style.display = 'block';
        errorDiv.textContent = res.message;
        if (window.SystemAudio) window.SystemAudio.playBossHit(true);
      }
    });
  }

  // Register Submit
  if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const hunterName = document.getElementById('reg-hunter-name').value.trim();
      const email = document.getElementById('reg-email') ? document.getElementById('reg-email').value.trim() : '';
      const username = document.getElementById('reg-username').value.trim();
      const password = document.getElementById('reg-password').value;
      const errorDiv = document.getElementById('reg-error-msg');

      errorDiv.style.display = 'none';
      const res = await auth.register(username, password, hunterName, email);

      if (res.success) {
        authModal.classList.remove('active');
        showSystemNotification('AWAKENING COMPLETE', `Welcome, Hunter ${hunterName}! You have entered the System.`);
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      } else {
        errorDiv.style.display = 'block';
        errorDiv.textContent = res.message;
        if (window.SystemAudio) window.SystemAudio.playBossHit(true);
      }
    });
  }

  window.addEventListener('auth-state-changed', (e) => {
    updateHUDAuthState(e.detail.isLoggedIn, e.detail.user);
  });

  if (auth) {
    updateHUDAuthState(Boolean(auth.token), auth.currentUser);

    // Initial Auth Gate: If no user logged in, open Awakening Modal immediately
    if (!auth.token || !auth.currentUser) {
      setTimeout(() => {
        if (authModal) authModal.classList.add('active');
      }, 350);
    }
  }
}

/* ==========================================================================
   13. GLOBAL HUNTER LEADERBOARD & RANKINGS HALL UI
   ========================================================================== */
function initLeaderboardUI() {
  const refreshBtn = document.getElementById('refresh-leaderboard-btn');
  const tbody = document.getElementById('leaderboard-table-body');
  const podiumContainer = document.getElementById('leaderboard-podium-container');
  const filterBtns = document.querySelectorAll('.lb-filter-btn');

  let activeLbFilter = 'all';
  let cachedLeaderboard = [];

  const DEMO_COMPETITORS = [
    { rankPosition: 1, hunterName: 'Sung Jinwoo (Monarch)', level: 120, rank: 'Monarch', title: 'The Shadow Monarch', targetPostId: 'iti', totalQuestionsSolved: 2850, mockTestsCleared: 42, streak: 35 },
    { rankPosition: 2, hunterName: 'Cha Hae-In (Sword Dancer)', level: 95, rank: 'S', title: 'The Radiant Blade', targetPostId: 'aso_mea', totalQuestionsSolved: 2120, mockTestsCleared: 31, streak: 28 },
    { rankPosition: 3, hunterName: 'Choi Jong-In (Flame Warlock)', level: 88, rank: 'A', title: 'Ultimate Fire Mage', targetPostId: 'gst_inspector', totalQuestionsSolved: 1940, mockTestsCleared: 26, streak: 21 },
    { rankPosition: 4, hunterName: 'Go Gunhee (Chairman)', level: 82, rank: 'A', title: 'National Hunter', targetPostId: 'cag_aao', totalQuestionsSolved: 1650, mockTestsCleared: 22, streak: 18 },
    { rankPosition: 5, hunterName: 'Baek Yoonho (White Tiger)', level: 75, rank: 'B', title: 'Fierce Vanguard', targetPostId: 'cbi_si', totalQuestionsSolved: 1420, mockTestsCleared: 19, streak: 14 }
  ];

  async function renderLeaderboard() {
    if (!tbody || !window.Auth) return;
    tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: var(--sys-cyan); padding: 30px;">⚔️ Querying All-India Hunter Database...</td></tr>`;

    let list = await window.Auth.fetchLeaderboard();

    // Include local player if logged in or offline
    if (window.Player) {
      const p = window.Player.data;
      const myEntry = {
        userId: 'my_current_player',
        hunterName: p.name,
        level: p.level,
        rank: p.rank,
        title: p.title,
        targetPostId: p.targetPostId || 'iti',
        totalQuestionsSolved: p.statsUnlocked.totalQuestionsSolved || 0,
        mockTestsCleared: p.statsUnlocked.mockTestsCleared || 0,
        streak: (window.Quests && window.Quests.data.streak) || 1,
        isMe: true
      };

      if (!list.find(h => h.hunterName === p.name)) {
        list.push(myEntry);
      }
    }

    if (list.length < 3) {
      // Merge with demo competitors for lively hall
      const existingNames = new Set(list.map(l => l.hunterName));
      DEMO_COMPETITORS.forEach(demo => {
        if (!existingNames.has(demo.hunterName)) {
          list.push(demo);
        }
      });
    }

    // Sort by level and solved questions
    list.sort((a, b) => b.level - a.level || b.totalQuestionsSolved - a.totalQuestionsSolved);
    list = list.map((item, idx) => ({ ...item, rankPosition: idx + 1 }));
    cachedLeaderboard = list;

    updateMyRankBanner(list);
    renderPodium(list);
    renderTableList();
  }

  function updateMyRankBanner(list) {
    if (!window.Player) return;
    const myName = window.Player.data.name;
    const myPos = list.findIndex(h => h.hunterName === myName || h.isMe) + 1;

    const myPosEl = document.getElementById('my-rank-pos');
    const myLvlEl = document.getElementById('my-rank-lvl');
    const myTierEl = document.getElementById('my-rank-tier');
    const myPostEl = document.getElementById('my-rank-post');

    if (myPosEl) myPosEl.textContent = myPos > 0 ? `#${myPos}` : '#1';
    if (myLvlEl) myLvlEl.textContent = `LVL ${window.Player.data.level}`;
    if (myTierEl) {
      myTierEl.textContent = `${window.Player.data.rank}-RANK`;
      myTierEl.className = `rank-badge rank-${window.Player.data.rank}`;
    }
    if (myPostEl && window.Player.getTargetPost) {
      myPostEl.textContent = window.Player.getTargetPost().name;
    }
  }

  function renderPodium(list) {
    if (!podiumContainer) return;
    podiumContainer.innerHTML = '';

    const top3 = list.slice(0, 3);
    top3.forEach((h, idx) => {
      const card = document.createElement('div');
      const rankNum = h.rankPosition;
      card.className = `podium-card rank-${rankNum}`;

      let crown = '🥇';
      let auraColor = '#ffd700';
      if (rankNum === 2) { crown = '🥈'; auraColor = '#e2e8f0'; }
      else if (rankNum === 3) { crown = '🥉'; auraColor = '#cd7f32'; }

      card.innerHTML = `
        <div class="podium-crown-icon">${crown}</div>
        <span class="rank-badge rank-${h.rank}">${h.rank}-RANK</span>
        <div class="podium-hunter-name" style="margin-top: 8px;">${h.hunterName}</div>
        <div class="podium-hunter-title">${h.title || 'Master Aspirant'}</div>
        <div style="font-family: var(--font-hud); font-size: 1.2rem; color: ${auraColor}; margin-bottom: 8px;">
          LVL ${h.level}
        </div>
        <div style="font-size: 0.8rem; color: var(--sys-text-dim); display: flex; justify-content: space-around; border-top: 1px solid rgba(56,189,248,0.15); padding-top: 8px;">
          <span>⚔️ ${h.totalQuestionsSolved} Qs</span>
          <span>🏆 ${h.mockTestsCleared} Raids</span>
        </div>
      `;
      podiumContainer.appendChild(card);
    });
  }

  function renderTableList() {
    if (!tbody) return;

    let filtered = cachedLeaderboard.filter(h => {
      if (activeLbFilter === 'all') return true;
      if (activeLbFilter === 's_rank') return h.rank === 'S' || h.rank === 'Monarch';
      if (activeLbFilter === 'iti') return h.targetPostId === 'iti';
      if (activeLbFilter === 'aso_mea') return h.targetPostId === 'aso_mea';
      if (activeLbFilter === 'streaks') return (h.streak || 1) >= 10;
      return true;
    });

    tbody.innerHTML = '';

    filtered.forEach(h => {
      const tr = document.createElement('tr');
      let rankBadge = `${h.rankPosition}`;
      let rankClass = '';
      if (h.rankPosition === 1) { rankBadge = '🥇 1st'; rankClass = 'top-rank-1'; }
      else if (h.rankPosition === 2) { rankBadge = '🥈 2nd'; rankClass = 'top-rank-2'; }
      else if (h.rankPosition === 3) { rankBadge = '🥉 3rd'; rankClass = 'top-rank-3'; }

      const isMe = h.isMe || (window.Player && h.hunterName === window.Player.data.name);
      if (isMe) {
        tr.style.background = 'rgba(0, 242, 255, 0.08)';
        tr.style.borderLeft = '3px solid var(--sys-cyan)';
      }

      const postName = getCglPostShort(h.targetPostId);

      tr.innerHTML = `
        <td class="font-hud ${rankClass}">${rankBadge}</td>
        <td>
          <strong>${h.hunterName}</strong>
          ${isMe ? ' <span style="font-size: 0.7rem; color: var(--sys-cyan); border: 1px solid var(--sys-cyan); padding: 1px 4px; border-radius: 3px;">YOU</span>' : ''}
        </td>
        <td class="font-hud text-cyan">LVL ${h.level}</td>
        <td><span class="rank-badge rank-${h.rank}">${h.rank}-RANK</span></td>
        <td><span style="color: var(--sys-gold); font-size: 0.85rem;">${postName}</span></td>
        <td class="font-hud text-cyan">${h.totalQuestionsSolved} Qs</td>
        <td class="font-hud text-shadow">${h.mockTestsCleared} Raids</td>
        <td class="font-hud" style="color: #4ade80;">🔥 ${h.streak || 1}d</td>
      `;
      tbody.appendChild(tr);
    });
  }

  function getCglPostShort(id) {
    const map = {
      'iti': '💰 Income Tax Inspector',
      'aso_mea': '🌐 ASO in MEA',
      'gst_inspector': '⚡ GST Inspector',
      'cbi_si': '🔍 CBI Sub-Inspector',
      'ed_aeo': '⚖️ ED Assistant Officer',
      'cag_aao': '👑 Assistant Audit Officer',
      'preventive_officer': '⚓ Customs Prev. Officer',
      'divisional_accountant': '📊 Divisional Accountant'
    };
    return map[id] || '💰 Income Tax Inspector';
  }

  // Filter Buttons
  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => {
        b.classList.remove('active');
        b.classList.remove('sys-btn-primary');
      });
      btn.classList.add('active');
      btn.classList.add('sys-btn-primary');
      activeLbFilter = btn.getAttribute('data-filter') || 'all';
      renderTableList();
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  });

  if (refreshBtn) {
    refreshBtn.addEventListener('click', () => {
      renderLeaderboard();
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  }

  // Load when user clicks on Rankings tab
  const leadTab = document.querySelector('[data-tab="tab-leaderboard"]');
  if (leadTab) {
    leadTab.addEventListener('click', () => {
      renderLeaderboard();
    });
  }
}

/* ==========================================================================
   14. ADMIN GUILD MASTER DASHBOARD UI
   ========================================================================== */
function initAdminPortalUI() {
  const admin = window.AdminPortal;
  if (!admin) return;

  const lockScreen = document.getElementById('admin-lock-screen');
  const unlockedDash = document.getElementById('admin-unlocked-dashboard');
  const pinForm = document.getElementById('admin-pin-form');
  const pinInput = document.getElementById('admin-pin-input');
  const pinError = document.getElementById('admin-pin-error');
  const lockBtn = document.getElementById('admin-lock-btn');

  const refreshBtn = document.getElementById('admin-refresh-btn');
  const exportBtn = document.getElementById('admin-export-csv-btn');
  const searchInput = document.getElementById('admin-search-input');
  const targetFilter = document.getElementById('admin-target-filter');
  const tbody = document.getElementById('admin-aspirants-table-body');

  const kpiAspirants = document.getElementById('admin-kpi-aspirants');
  const kpiSolved = document.getElementById('admin-kpi-solved');
  const kpiMocks = document.getElementById('admin-kpi-mocks');
  const kpiAvgLvl = document.getElementById('admin-kpi-avg-lvl');

  const MASTER_PIN = 'monarch2026';

  function checkAdminLockState() {
    const isUnlocked = sessionStorage.getItem('solo_admin_unlocked') === 'true';
    if (isUnlocked) {
      if (lockScreen) lockScreen.style.display = 'none';
      if (unlockedDash) unlockedDash.style.display = 'flex';
      renderAdminDashboard();
    } else {
      if (lockScreen) lockScreen.style.display = 'flex';
      if (unlockedDash) unlockedDash.style.display = 'none';
    }
  }

  // PIN Form Submission
  if (pinForm) {
    pinForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const entered = pinInput ? pinInput.value.trim() : '';
      if (entered === MASTER_PIN || entered === 'admin123' || entered === 'cgl2026') {
        if (pinError) pinError.style.display = 'none';
        sessionStorage.setItem('solo_admin_unlocked', 'true');
        checkAdminLockState();
        showSystemNotification('GUILD MASTER VERIFIED', 'Welcome, Guild Master! Command Center surveillance unlocked.');
        if (window.SystemAudio) window.SystemAudio.playLevelUp();
      } else {
        if (pinError) pinError.style.display = 'block';
        if (window.SystemAudio) window.SystemAudio.playBossHit(true);
      }
    });
  }

  // Lock button
  if (lockBtn) {
    lockBtn.addEventListener('click', () => {
      sessionStorage.removeItem('solo_admin_unlocked');
      checkAdminLockState();
      showSystemNotification('COMMAND CENTER LOCKED', 'Guild HQ is secured from unauthorized access.');
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  }

  async function renderAdminDashboard() {
    if (!tbody) return;
    tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: var(--sys-cyan); padding: 30px;">👑 Querying Army Database & Distributed APKs...</td></tr>`;

    await admin.fetchAspirantsData();

    // Update KPIs
    if (admin.stats) {
      if (kpiAspirants) kpiAspirants.textContent = admin.stats.totalAspirants;
      if (kpiSolved) kpiSolved.textContent = `${admin.stats.totalSolved.toLocaleString()} Qs`;
      if (kpiMocks) kpiMocks.textContent = `${admin.stats.totalMocks} Raids`;
      if (kpiAvgLvl) kpiAvgLvl.textContent = `LVL ${admin.stats.avgLevel}`;
    }

    renderTableRows();
  }

  function renderTableRows() {
    if (!tbody) return;
    const query = (searchInput ? searchInput.value : '').toLowerCase();
    const filterPost = targetFilter ? targetFilter.value : 'all';

    const filtered = admin.aspirants.filter(a => {
      const matchQuery = !query || a.hunterName.toLowerCase().includes(query) || a.username.toLowerCase().includes(query) || (a.targetPostId && a.targetPostId.toLowerCase().includes(query));
      const matchPost = filterPost === 'all' || a.targetPostId === filterPost;
      return matchQuery && matchPost;
    });

    if (filtered.length === 0) {
      tbody.innerHTML = `
        <tr>
          <td colspan="8" style="text-align: center; padding: 30px; color: var(--sys-text-dim);">
            No aspirants found matching current criteria.
          </td>
        </tr>
      `;
      return;
    }

    tbody.innerHTML = '';
    filtered.forEach(a => {
      const tr = document.createElement('tr');
      const postName = getPostDisplayName(a.targetPostId);

      tr.innerHTML = `
        <td>
          <div style="font-weight: 700; color: #fff;">${a.hunterName}</div>
          <div style="font-size: 0.75rem; color: var(--sys-text-dim);">@${a.username}</div>
        </td>
        <td class="font-hud text-cyan">LVL ${a.level}</td>
        <td><span class="rank-badge rank-${a.rank}">${a.rank}-RANK</span></td>
        <td><span style="color: var(--sys-gold); font-size: 0.85rem;">${postName}</span></td>
        <td class="font-hud text-cyan">${a.totalQuestionsSolved} Qs</td>
        <td class="font-hud text-shadow">${a.mockTestsCleared} Raids</td>
        <td class="font-hud" style="color: #4ade80;">🔥 ${a.streakDays}d</td>
        <td>
          <button class="sys-btn inspect-hunter-btn" style="padding: 4px 10px; font-size: 0.75rem;">
            INSPECT 🔍
          </button>
        </td>
      `;

      tr.querySelector('.inspect-hunter-btn').addEventListener('click', () => {
        showHunterInspector(a);
      });

      tbody.appendChild(tr);
    });
  }

  function getPostDisplayName(postId) {
    if (!postId) return 'Income Tax Inspector';
    const map = {
      'iti': '💰 Income Tax Inspector',
      'aso_mea': '🌐 ASO in MEA',
      'gst_inspector': '⚡ GST Inspector',
      'cbi_si': '🔍 CBI Sub-Inspector',
      'ed_aeo': '⚖️ ED Assistant Enforcement Officer',
      'cag_aao': '👑 Assistant Audit Officer',
      'preventive_officer': '⚓ Customs Preventive Officer',
      'divisional_accountant': '📊 Divisional Accountant'
    };
    return map[postId] || postId;
  }

  function showHunterInspector(hunter) {
    const stats = hunter.stats || { int: 10, vit: 10, agi: 10, sen: 10, str: 10 };
    const modalMessage = `
      <div style="display: flex; flex-direction: column; gap: 12px; text-align: left;">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--sys-border); padding-bottom: 8px;">
          <div>
            <div style="font-size: 1.2rem; font-weight: 900; color: #fff;">${hunter.hunterName}</div>
            <div style="font-size: 0.8rem; color: var(--sys-cyan);">Target: ${getPostDisplayName(hunter.targetPostId)}</div>
          </div>
          <span class="rank-badge rank-${hunter.rank}">LVL ${hunter.level} (${hunter.rank}-RANK)</span>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px; font-size: 0.85rem;">
          <div>🧠 Intelligence (INT): <strong>${stats.int || 10}</strong></div>
          <div>🛡️ Vitality (VIT): <strong>${stats.vit || 10}</strong></div>
          <div>⚡ Agility (AGI): <strong>${stats.agi || 10}</strong></div>
          <div>👁️ Sense (SEN): <strong>${stats.sen || 10}</strong></div>
          <div>🔥 Discipline (STR): <strong>${stats.str || 10}</strong></div>
          <div>⏳ Deep Focus: <strong>${hunter.focusMinutes || 0} Mins</strong></div>
        </div>

        <div style="background: rgba(0, 242, 255, 0.05); border: 1px solid var(--sys-border); border-radius: 4px; padding: 10px; font-size: 0.85rem;">
          <div>⚔️ Questions Solved: <strong class="text-cyan">${hunter.totalQuestionsSolved}</strong></div>
          <div>🏆 CBT Mock Raids Cleared: <strong class="text-gold">${hunter.mockTestsCleared}</strong></div>
          <div>📅 Current Daily Streak: <strong style="color: #4ade80;">${hunter.streakDays} Days</strong></div>
        </div>
      </div>
    `;

    showSystemNotification(`HUNTER INSPECTOR: ${hunter.hunterName}`, modalMessage);
  }

  if (refreshBtn) {
    refreshBtn.addEventListener('click', () => {
      renderAdminDashboard();
      if (window.SystemAudio) window.SystemAudio.playClick();
    });
  }

  if (exportBtn) {
    exportBtn.addEventListener('click', () => {
      admin.downloadCsv();
      if (window.SystemAudio) window.SystemAudio.playLevelUp();
    });
  }

  if (searchInput) searchInput.addEventListener('input', renderTableRows);
  if (targetFilter) targetFilter.addEventListener('change', renderTableRows);

  // Auto load when admin tab is opened
  const adminTab = document.querySelector('[data-tab="tab-admin"]');
  if (adminTab) {
    adminTab.addEventListener('click', () => {
      checkAdminLockState();
    });
  }
}


