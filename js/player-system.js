/**
 * SOLO LEVELING EXAM SYSTEM - PLAYER STATE & STAT ENGINE
 */

const HUNTER_RANKS = [
  { rank: 'E', title: 'Aspirant (Weakest of All Mankind)', minLevel: 1, color: '#94a3b8' },
  { rank: 'D', title: 'Apprentice Hunter', minLevel: 10, color: '#4ade80' },
  { rank: 'C', title: 'Skilled Aspirant', minLevel: 25, color: '#38bdf8' },
  { rank: 'B', title: 'Elite Tactician', minLevel: 45, color: '#c084fc' },
  { rank: 'A', title: 'Master Strategist', minLevel: 70, color: '#fb923c' },
  { rank: 'S', title: 'National Level Aspirant', minLevel: 100, color: '#fde047' },
  { rank: 'Monarch', title: 'Shadow Monarch (Supreme Rank)', minLevel: 150, color: '#c084fc' }
];

const SSC_CGL_TARGET_POSTS = [
  { id: 'iti', name: 'Income Tax Inspector (ITI)', ministry: 'CBDT, Dept of Revenue', rankBadge: 'S-Rank Financial Monarch', icon: '💰', cutoffTarget: '335 / 390' },
  { id: 'aso_mea', name: 'ASO in Ministry of External Affairs (MEA)', ministry: 'Ministry of External Affairs', rankBadge: 'Shadow Diplomat', icon: '🌐', cutoffTarget: '340 / 390' },
  { id: 'gst_inspector', name: 'GST & Central Excise Inspector', ministry: 'CBIC, Dept of Revenue', rankBadge: 'Shadow Enforcer', icon: '⚡', cutoffTarget: '325 / 390' },
  { id: 'cbi_si', name: 'Sub-Inspector in CBI', ministry: 'Central Bureau of Investigation', rankBadge: 'Shadow Investigator', icon: '🔍', cutoffTarget: '330 / 390' },
  { id: 'ed_aeo', name: 'Assistant Enforcement Officer (ED)', ministry: 'Directorate of Enforcement', rankBadge: 'Financial Inquisitor', icon: '⚖️', cutoffTarget: '338 / 390' },
  { id: 'cag_aao', name: 'Assistant Audit Officer (AAO)', ministry: 'CAG (Gazetted Group B)', rankBadge: 'Treasury Monarch', icon: '👑', cutoffTarget: '345 / 390' },
  { id: 'preventive_officer', name: 'Customs Preventive Officer (PO)', ministry: 'CBIC (Airports & Seaports)', rankBadge: 'Coastal Vanguard', icon: '⚓', cutoffTarget: '328 / 390' },
  { id: 'divisional_accountant', name: 'Divisional Accountant (DA)', ministry: 'CAG State Divisions', rankBadge: 'Division Commander', icon: '📊', cutoffTarget: '320 / 390' }
];

const DEFAULT_PLAYER_STATE = {
  name: 'Sung Jin-Aspirant',
  title: 'The One Who Overcomes Formulas',
  jobClass: 'Exam Slayer / Player',
  targetPostId: 'iti',
  level: 1,
  exp: 0,
  maxExp: 100,
  rank: 'E',
  hp: 100,
  maxHp: 100,
  mp: 50,
  maxMp: 50,
  gold: 150,
  unallocatedPoints: 5,
  stats: {
    int: 10, // Intelligence (Concept Mastery & Reasoning)
    vit: 10, // Vitality (Study Endurance & Focus minutes)
    agi: 10, // Agility (Solving Speed)
    sen: 10, // Sense (Precision & Negative Mark Avoidance)
    str: 10  // Discipline & Habit Consistency
  },
  statsUnlocked: {
    totalQuestionsSolved: 0,
    mockTestsCleared: 0,
    shadowsExtracted: 0,
    streakDays: 1,
    focusMinutes: 0
  }
};

class PlayerSystem {
  constructor() {
    this.data = this.loadState();
    this.recalculateDerived();
  }

  loadState() {
    try {
      const saved = localStorage.getItem('solo_system_player');
      if (saved) {
        return Object.assign({}, DEFAULT_PLAYER_STATE, JSON.parse(saved));
      }
    } catch (e) {
      console.error('Failed to load player state:', e);
    }
    return JSON.parse(JSON.stringify(DEFAULT_PLAYER_STATE));
  }

  saveState() {
    try {
      localStorage.setItem('solo_system_player', JSON.stringify(this.data));
    } catch (e) {
      console.error('Failed to save player state:', e);
    }
    this.notifyUpdate();
  }

  recalculateDerived() {
    // Max HP scales with Vitality & Level
    this.data.maxHp = 100 + (this.data.stats.vit * 10) + (this.data.level * 5);
    // Max MP scales with Intelligence & Level
    this.data.maxMp = 50 + (this.data.stats.int * 8) + (this.data.level * 4);
    // EXP required formula
    this.data.maxExp = Math.floor(100 * Math.pow(1.22, this.data.level - 1));

    // Clamp current HP/MP
    if (this.data.hp > this.data.maxHp) this.data.hp = this.data.maxHp;
    if (this.data.mp > this.data.maxMp) this.data.mp = this.data.maxMp;

    // Rank evaluation
    this.evaluateRank();
  }

  evaluateRank() {
    let currentRank = 'E';
    for (let r of HUNTER_RANKS) {
      if (this.data.level >= r.minLevel) {
        currentRank = r.rank;
      }
    }
    if (this.data.rank !== currentRank) {
      const oldRank = this.data.rank;
      this.data.rank = currentRank;
      if (window.SystemAudio) window.SystemAudio.playLevelUp();
      if (window.SystemNotifications) {
        window.SystemNotifications.showRankUp(oldRank, currentRank);
      }
    }
  }

  addExp(amount) {
    this.data.exp += amount;
    let leveledUp = false;

    while (this.data.exp >= this.data.maxExp) {
      this.data.exp -= this.data.maxExp;
      this.data.level += 1;
      this.data.unallocatedPoints += 3;
      this.data.hp = this.data.maxHp;
      this.data.mp = this.data.maxMp;
      leveledUp = true;
      this.recalculateDerived();
    }

    if (leveledUp) {
      if (window.SystemAudio) window.SystemAudio.playLevelUp();
      if (window.SystemNotifications) {
        window.SystemNotifications.showLevelUp(this.data.level);
      }
    }

    this.saveState();
  }

  addGold(amount) {
    this.data.gold += amount;
    this.saveState();
  }

  spendGold(amount) {
    if (this.data.gold >= amount) {
      this.data.gold -= amount;
      this.saveState();
      return true;
    }
    return false;
  }

  allocateStat(statKey) {
    if (this.data.unallocatedPoints > 0 && this.data.stats[statKey] !== undefined) {
      this.data.stats[statKey] += 1;
      this.data.unallocatedPoints -= 1;
      this.recalculateDerived();
      this.saveState();
      if (window.SystemAudio) window.SystemAudio.playClick();
      return true;
    }
    return false;
  }

  setName(newName) {
    if (newName && newName.trim()) {
      this.data.name = newName.trim();
      this.saveState();
    }
  }

  modifyHp(delta) {
    this.data.hp = Math.max(0, Math.min(this.data.maxHp, this.data.hp + delta));
    this.saveState();
  }

  modifyMp(delta) {
    this.data.mp = Math.max(0, Math.min(this.data.maxMp, this.data.mp + delta));
    this.saveState();
  }

  getTargetPost() {
    const post = SSC_CGL_TARGET_POSTS.find(p => p.id === this.data.targetPostId);
    return post || SSC_CGL_TARGET_POSTS[0];
  }

  setTargetPost(postId) {
    const post = SSC_CGL_TARGET_POSTS.find(p => p.id === postId);
    if (post) {
      this.data.targetPostId = postId;
      this.saveState();
      return post;
    }
    return null;
  }

  notifyUpdate() {
    window.dispatchEvent(new CustomEvent('player-updated', { detail: this.data }));
  }
}

window.Player = new PlayerSystem();
window.Player.cglTargetPosts = SSC_CGL_TARGET_POSTS;
