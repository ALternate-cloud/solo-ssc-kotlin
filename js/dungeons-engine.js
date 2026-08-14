/**
 * SOLO LEVELING EXAM SYSTEM - GATE RAIDS & BOSS BATTLE CBT MOCK ENGINE
 */

const DUNGEON_GATES = [
  {
    id: 'gate_quant',
    rank: 'E',
    name: 'Goblin Mine: Quantitative Aptitude',
    subject: 'Quantitative Aptitude',
    bossName: 'Hobgoblin Leader (Algebra & Arithmetic)',
    bossAvatar: '👹',
    questionCount: 10,
    timeMinutes: 12,
    expReward: 150,
    goldReward: 60,
    desc: 'Clear 10 high-frequency Quant questions to slay the Hobgoblin and conquer foundational math formulas.'
  },
  {
    id: 'gate_reas',
    rank: 'D',
    name: 'Gargoyle Belfry: Logical Reasoning',
    subject: 'General Intelligence & Reasoning',
    bossName: 'Obsidian Gargoyle (Patterns & Syllogism)',
    bossAvatar: '🦇',
    questionCount: 10,
    timeMinutes: 10,
    expReward: 140,
    goldReward: 50,
    desc: 'Decipher optical riddles, coding-decoding, and syllogism matrices to shatter the Gargoyle.'
  },
  {
    id: 'gate_eng',
    rank: 'C',
    name: 'Siren Cavern: English Language',
    subject: 'English Language',
    bossName: 'Abyssal Siren (Grammar & Vocabulary)',
    bossAvatar: '🧜‍♀️',
    questionCount: 10,
    timeMinutes: 8,
    expReward: 130,
    goldReward: 50,
    desc: 'Survive tricky cloze tests, error spotting, and archaic idioms without falling for misleading grammatical traps.'
  },
  {
    id: 'gate_ga',
    rank: 'B',
    name: 'Citadel of Knowledge: General Awareness',
    subject: 'General Awareness',
    bossName: 'Ancient Archmage (History & Polity)',
    bossAvatar: '🧙‍♂️',
    questionCount: 10,
    timeMinutes: 7,
    expReward: 160,
    goldReward: 70,
    desc: 'Test your knowledge on Constitution, Indian History, Economy, and Current Affairs.'
  },
  {
    id: 'gate_cgl_boss',
    rank: 'S',
    name: 'RED GATE: SSC CGL Tier-1 Mega Raid',
    subject: 'Full Mock Exam',
    bossName: 'Kargalgan the Demon King (Grand Examiner)',
    bossAvatar: '👿',
    questionCount: 25,
    timeMinutes: 30,
    expReward: 600,
    goldReward: 300,
    desc: 'The ultimate boss battle! Full-length comprehensive simulation across all 4 subjects with live Boss HP vs Player HP.'
  },
  {
    id: 'gate_demon_castle',
    rank: 'Monarch',
    name: 'DEMON CASTLE: Infinite Tower of Trials',
    subject: 'Procedural Infinite Gauntlet',
    bossName: 'Baran the Demon Monarch (Infinite Ruler)',
    bossAvatar: '👑',
    questionCount: 30,
    timeMinutes: 35,
    expReward: 850,
    goldReward: 450,
    desc: 'Procedurally generated infinite questions scaled to your level! Conquer endless dynamic mathematics, reasoning, and conceptual trials.'
  }
];

class DungeonsEngine {
  constructor() {
    this.gates = DUNGEON_GATES;
    this.activeRaid = null;
    this.timerInterval = null;
  }

  getGates() {
    return this.gates;
  }

  startRaid(gateId) {
    const gate = this.gates.find(g => g.id === gateId);
    if (!gate) return null;

    // Pick questions
    let questions = [];
    if (gate.subject === 'Full Mock Exam') {
      questions = window.QuestionBank.getRandomBatch(gate.questionCount);
    } else {
      questions = window.QuestionBank.getRandomBatch(gate.questionCount, gate.subject);
    }

    if (questions.length === 0) {
      questions = window.QuestionBank.getRandomBatch(gate.questionCount);
    }

    this.activeRaid = {
      gate: gate,
      questions: questions,
      currentIndex: 0,
      answers: new Array(questions.length).fill(null),
      flags: new Array(questions.length).fill(false),
      timeRemaining: gate.timeMinutes * 60,
      totalTime: gate.timeMinutes * 60,
      bossMaxHp: questions.length * 100,
      bossHp: questions.length * 100,
      playerMaxHp: window.Player ? window.Player.data.maxHp : 100,
      playerHp: window.Player ? window.Player.data.hp : 100,
      score: 0,
      correctCount: 0,
      wrongCount: 0,
      completed: false,
      startTime: Date.now()
    };

    if (window.SystemAudio) window.SystemAudio.playSystemAlert();
    this.startTimer();
    return this.activeRaid;
  }

  startPyqRaid(paperId) {
    if (!window.PyqBank) return null;
    const paper = window.PyqBank.getPaperById(paperId);
    if (!paper) return null;

    const gate = {
      id: paper.id,
      rank: 'S',
      name: `OFFICIAL PYQ: ${paper.exam}`,
      subject: 'Official PYQ Paper',
      bossName: paper.bossName,
      bossAvatar: paper.bossAvatar,
      questionCount: paper.questions.length,
      timeMinutes: paper.durationMinutes,
      expReward: 650,
      goldReward: 350,
      desc: paper.desc
    };

    this.activeRaid = {
      gate: gate,
      questions: paper.questions,
      currentIndex: 0,
      answers: new Array(paper.questions.length).fill(null),
      flags: new Array(paper.questions.length).fill(false),
      timeRemaining: paper.durationMinutes * 60,
      totalTime: paper.durationMinutes * 60,
      bossMaxHp: paper.questions.length * 100,
      bossHp: paper.questions.length * 100,
      playerMaxHp: window.Player ? window.Player.data.maxHp : 100,
      playerHp: window.Player ? window.Player.data.hp : 100,
      score: 0,
      correctCount: 0,
      wrongCount: 0,
      completed: false,
      startTime: Date.now()
    };

    if (window.SystemAudio) window.SystemAudio.playSystemAlert();
    this.startTimer();
    return this.activeRaid;
  }

  startTimer() {
    if (this.timerInterval) clearInterval(this.timerInterval);
    this.timerInterval = setInterval(() => {
      if (!this.activeRaid || this.activeRaid.completed) {
        clearInterval(this.timerInterval);
        return;
      }

      this.activeRaid.timeRemaining -= 1;
      if (this.activeRaid.timeRemaining <= 0) {
        this.activeRaid.timeRemaining = 0;
        this.submitRaid();
      } else {
        window.dispatchEvent(new CustomEvent('raid-timer-tick', { detail: this.activeRaid.timeRemaining }));
      }
    }, 1000);
  }

  selectOption(optionIndex) {
    if (!this.activeRaid || this.activeRaid.completed) return;
    this.activeRaid.answers[this.activeRaid.currentIndex] = optionIndex;
    if (window.SystemAudio) window.SystemAudio.playClick();
  }

  toggleFlag() {
    if (!this.activeRaid || this.activeRaid.completed) return;
    const idx = this.activeRaid.currentIndex;
    this.activeRaid.flags[idx] = !this.activeRaid.flags[idx];
  }

  clearResponse() {
    if (!this.activeRaid || this.activeRaid.completed) return;
    this.activeRaid.answers[this.activeRaid.currentIndex] = null;
  }

  goToQuestion(index) {
    if (!this.activeRaid || index < 0 || index >= this.activeRaid.questions.length) return;
    this.activeRaid.currentIndex = index;
    if (window.SystemAudio) window.SystemAudio.playClick();
  }

  nextQuestion() {
    if (!this.activeRaid) return;
    if (this.activeRaid.currentIndex < this.activeRaid.questions.length - 1) {
      this.goToQuestion(this.activeRaid.currentIndex + 1);
    }
  }

  prevQuestion() {
    if (!this.activeRaid) return;
    if (this.activeRaid.currentIndex > 0) {
      this.goToQuestion(this.activeRaid.currentIndex - 1);
    }
  }

  submitRaid() {
    if (!this.activeRaid || this.activeRaid.completed) return null;
    if (this.timerInterval) clearInterval(this.timerInterval);

    this.activeRaid.completed = true;
    let correct = 0;
    let wrong = 0;
    let unattempted = 0;

    const damagePerCorrect = 100;
    const damageToPlayer = 20;

    this.activeRaid.questions.forEach((q, idx) => {
      const userAns = this.activeRaid.answers[idx];
      if (userAns === null) {
        unattempted += 1;
      } else if (userAns === q.correct) {
        correct += 1;
        this.activeRaid.bossHp = Math.max(0, this.activeRaid.bossHp - damagePerCorrect);
      } else {
        wrong += 1;
        this.activeRaid.playerHp = Math.max(0, this.activeRaid.playerHp - damageToPlayer);
        // Log to Shadow Mistake Notebook!
        if (window.Shadows) {
          window.Shadows.addFallenMonster(q, userAns);
        }
      }
    });

    // SSC CGL Marking Scheme: +2 for correct, -0.50 for incorrect
    const rawScore = (correct * 2.0) - (wrong * 0.50);
    const maxScore = this.activeRaid.questions.length * 2.0;
    const accuracy = (correct + wrong) > 0 ? Math.round((correct / (correct + wrong)) * 100) : 0;
    const isBossDefeated = this.activeRaid.bossHp <= 0 || (correct >= Math.ceil(this.activeRaid.questions.length * 0.6));

    this.activeRaid.correctCount = correct;
    this.activeRaid.wrongCount = wrong;
    this.activeRaid.unattemptedCount = unattempted;
    this.activeRaid.rawScore = Math.max(0, rawScore);
    this.activeRaid.maxScore = maxScore;
    this.activeRaid.accuracy = accuracy;
    this.activeRaid.isBossDefeated = isBossDefeated;

    // Apply Player Rewards if cleared
    if (window.Player) {
      if (isBossDefeated) {
        window.Player.addExp(this.activeRaid.gate.expReward);
        window.Player.addGold(this.activeRaid.gate.goldReward);
      } else {
        // Partial EXP
        window.Player.addExp(Math.floor(this.activeRaid.gate.expReward * 0.4));
      }
      window.Player.data.statsUnlocked.totalQuestionsSolved += (correct + wrong);
      window.Player.data.statsUnlocked.mockTestsCleared += (isBossDefeated ? 1 : 0);
      window.Player.saveState();
    }

    // Update Daily Quests progress
    if (window.Quests) {
      if (this.activeRaid.gate.subject === 'Quantitative Aptitude') window.Quests.incrementTask('t_quant', correct + wrong);
      else if (this.activeRaid.gate.subject === 'General Intelligence & Reasoning') window.Quests.incrementTask('t_reas', correct + wrong);
      else if (this.activeRaid.gate.subject === 'English Language') window.Quests.incrementTask('t_eng', correct + wrong);
      else {
        window.Quests.incrementTask('t_quant', Math.ceil((correct + wrong) / 3));
        window.Quests.incrementTask('t_reas', Math.ceil((correct + wrong) / 3));
        window.Quests.incrementTask('t_eng', Math.ceil((correct + wrong) / 3));
      }
    }

    if (window.SystemAudio) {
      if (isBossDefeated) window.SystemAudio.playLevelUp();
      else window.SystemAudio.playBossHit(true);
    }

    return this.activeRaid;
  }
}

window.Dungeons = new DungeonsEngine();
