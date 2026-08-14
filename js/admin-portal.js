/**
 * SOLO LEVELING EXAM SYSTEM - ADMIN GUILD MASTER DASHBOARD CONTROLLER
 */

class AdminGuildMasterPortal {
  constructor() {
    this.aspirants = [];
    this.stats = null;
    this.apiBase = window.location.origin;
  }

  async fetchAspirantsData() {
    try {
      const res = await fetch(`${this.apiBase}/api/admin/aspirants`);
      if (res.ok) {
        const data = await res.json();
        if (data.success) {
          this.aspirants = data.aspirants || [];
          this.stats = data.stats || null;
          return true;
        }
      }
    } catch (e) {
      console.warn('Admin API offline or unreachable. Using local mock data:', e);
    }
    // Fallback if testing offline
    if (window.Player) {
      this.aspirants = [{
        userId: 'usr_local_host',
        username: 'sung_jinwoo',
        hunterName: window.Player.data.name,
        level: window.Player.data.level,
        rank: window.Player.data.rank,
        targetPostId: window.Player.data.targetPostId || 'iti',
        stats: window.Player.data.stats,
        totalQuestionsSolved: window.Player.data.statsUnlocked.totalQuestionsSolved,
        mockTestsCleared: window.Player.data.statsUnlocked.mockTestsCleared,
        streakDays: (window.Quests && window.Quests.data.streak) || 1,
        focusMinutes: window.Player.data.statsUnlocked.focusMinutes || 0,
        createdAt: new Date().toISOString()
      }];
      this.stats = {
        totalAspirants: 1,
        totalSolved: window.Player.data.statsUnlocked.totalQuestionsSolved,
        totalMocks: window.Player.data.statsUnlocked.mockTestsCleared,
        avgLevel: window.Player.data.level
      };
      return true;
    }
    return false;
  }

  downloadCsv() {
    window.open(`${this.apiBase}/api/admin/export-csv`, '_blank');
  }
}

window.AdminPortal = new AdminGuildMasterPortal();
