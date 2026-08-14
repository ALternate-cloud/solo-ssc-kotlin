/**
 * SOLO LEVELING EXAM SYSTEM - DAILY QUESTS & PENALTY ENGINE
 */

const DEFAULT_DAILY_QUESTS = {
  lastDate: new Date().toDateString(),
  claimed: false,
  penaltyActive: false,
  penaltyMistakes: 0,
  streak: 1,
  tasks: [
    { id: 't_quant', name: 'Solve 30 Quantitative Aptitude Questions', current: 0, target: 30, unit: 'Questions', exp: 100 },
    { id: 't_reas', name: 'Practice 25 Reasoning & Logic Questions', current: 0, target: 25, unit: 'Questions', exp: 80 },
    { id: 't_eng', name: 'Master 20 English Vocab / Grammar Rules', current: 0, target: 20, unit: 'Questions', exp: 70 },
    { id: 't_focus', name: 'Complete 60 Mins Deep Focus Pomodoro', current: 0, target: 60, unit: 'Minutes', exp: 120 }
  ]
};

class QuestsEngine {
  constructor() {
    this.data = this.loadQuests();
    this.checkDailyReset();
  }

  loadQuests() {
    try {
      const saved = localStorage.getItem('solo_system_daily_quests');
      if (saved) {
        return Object.assign({}, DEFAULT_DAILY_QUESTS, JSON.parse(saved));
      }
    } catch (e) {
      console.error('Failed to load daily quests:', e);
    }
    return JSON.parse(JSON.stringify(DEFAULT_DAILY_QUESTS));
  }

  saveQuests() {
    try {
      localStorage.setItem('solo_system_daily_quests', JSON.stringify(this.data));
    } catch (e) {
      console.error('Failed to save daily quests:', e);
    }
    this.notifyUpdate();
  }

  checkDailyReset() {
    const today = new Date().toDateString();
    if (this.data.lastDate !== today) {
      // Check if yesterday's quest was unclaimed or uncompleted
      const wasAllCompleted = this.isAllCompleted();
      if (!wasAllCompleted && !this.data.claimed) {
        // Trigger Penalty Quest!
        this.data.penaltyActive = true;
        this.data.penaltyMistakes = 0;
        this.data.streak = 1; // reset streak
      } else if (wasAllCompleted) {
        this.data.streak += 1;
      }

      // Reset daily tasks
      this.data.lastDate = today;
      this.data.claimed = false;
      this.data.tasks.forEach(t => t.current = 0);
      this.saveQuests();
    }
  }

  incrementTask(taskId, amount = 1) {
    const task = this.data.tasks.find(t => t.id === taskId);
    if (task) {
      task.current = Math.min(task.target, task.current + amount);
      this.saveQuests();
      if (window.SystemAudio) window.SystemAudio.playClick();
      return true;
    }
    return false;
  }

  toggleTaskDirect(taskId) {
    const task = this.data.tasks.find(t => t.id === taskId);
    if (task) {
      if (task.current >= task.target) {
        task.current = 0;
      } else {
        task.current = task.target;
      }
      this.saveQuests();
      if (window.SystemAudio) window.SystemAudio.playClick();
      return true;
    }
    return false;
  }

  isAllCompleted() {
    return this.data.tasks.every(t => t.current >= t.target);
  }

  getOverallProgress() {
    let totalTarget = 0;
    let totalDone = 0;
    this.data.tasks.forEach(t => {
      totalTarget += t.target;
      totalDone += Math.min(t.target, t.current);
    });
    return totalTarget > 0 ? Math.round((totalDone / totalTarget) * 100) : 0;
  }

  claimDailyReward() {
    if (this.isAllCompleted() && !this.data.claimed) {
      this.data.claimed = true;
      this.saveQuests();

      // Award Player
      if (window.Player) {
        window.Player.addExp(250);
        window.Player.addGold(120);
        window.Player.data.unallocatedPoints += 3;
        window.Player.saveState();
      }

      if (window.SystemAudio) window.SystemAudio.playLevelUp();
      return {
        success: true,
        exp: 250,
        gold: 120,
        statPoints: 3
      };
    }
    return { success: false };
  }

  // Trigger Penalty Survival Mode
  triggerPenaltyMode() {
    this.data.penaltyActive = true;
    this.data.penaltyMistakes = 0;
    this.saveQuests();
    if (window.SystemAudio) window.SystemAudio.playSystemAlert();
  }

  clearPenalty() {
    this.data.penaltyActive = false;
    this.data.penaltyMistakes = 0;
    this.saveQuests();
    if (window.SystemAudio) window.SystemAudio.playLevelUp();
  }

  getTimeUntilReset() {
    const now = new Date();
    const midnight = new Date();
    midnight.setHours(24, 0, 0, 0);
    const diff = midnight - now;

    const hours = Math.floor(diff / (1000 * 60 * 60));
    const mins = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    const secs = Math.floor((diff % (1000 * 60)) / 1000);

    return `${String(hours).padStart(2, '0')}:${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  }

  notifyUpdate() {
    window.dispatchEvent(new CustomEvent('quests-updated', { detail: this.data }));
  }
}

window.Quests = new QuestsEngine();
