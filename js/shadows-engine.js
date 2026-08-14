/**
 * SOLO LEVELING EXAM SYSTEM - SHADOW EXTRACTION & ARMY ENGINE ("ARISE")
 */

const DEFAULT_SHADOW_LIEUTENANTS = [
  {
    id: 'lieutenant_igris',
    name: 'Shadow Igris (Knight Commander)',
    subject: 'Quantitative Aptitude',
    rank: 'Elite Knight',
    avatar: '🗡️',
    level: 1,
    exp: 0,
    maxExp: 100,
    extractedCount: 0,
    buff: '+5% Speed Calculation Boost on Mathematics Dungeons'
  },
  {
    id: 'lieutenant_beru',
    name: 'Shadow Beru (King of Speed)',
    subject: 'General Intelligence & Reasoning',
    rank: 'General Rank',
    avatar: '🐜',
    level: 1,
    exp: 0,
    maxExp: 100,
    extractedCount: 0,
    buff: 'Negative Marking Shield (Prevents 1 wrong penalty in Boss Mocks)'
  },
  {
    id: 'lieutenant_iron',
    name: 'Shadow Iron (Heavy Shield)',
    subject: 'English Language',
    rank: 'Knight Rank',
    avatar: '🛡️',
    level: 1,
    exp: 0,
    maxExp: 100,
    extractedCount: 0,
    buff: '+10% EXP Bonus on English Vocabulary & Grammar Raids'
  },
  {
    id: 'lieutenant_tusk',
    name: 'Shadow Tusk (High Shaman)',
    subject: 'General Awareness',
    rank: 'Elite Shaman',
    avatar: '🔥',
    level: 1,
    exp: 0,
    maxExp: 100,
    extractedCount: 0,
    buff: 'Insight Vision: Unlocks quick formula shortcuts & hints'
  }
];

class ShadowsEngine {
  constructor() {
    this.data = this.loadShadowState();
  }

  loadShadowState() {
    try {
      const saved = localStorage.getItem('solo_system_shadow_army');
      if (saved) {
        return JSON.parse(saved);
      }
    } catch (e) {
      console.error('Failed to load shadow state:', e);
    }
    return {
      totalShadows: 0,
      monarchAuraLevel: 1,
      commanders: JSON.parse(JSON.stringify(DEFAULT_SHADOW_LIEUTENANTS)),
      fallenMonsters: [] // Array of { question, userWrongAnswer, timestamp, resolved }
    };
  }

  saveShadowState() {
    try {
      localStorage.setItem('solo_system_shadow_army', JSON.stringify(this.data));
    } catch (e) {
      console.error('Failed to save shadow state:', e);
    }
    this.notifyUpdate();
  }

  addFallenMonster(questionObj, userWrongAnswerIndex) {
    // Avoid duplicate unresolved entries
    const existing = this.data.fallenMonsters.find(m => m.question.id === questionObj.id && !m.resolved);
    if (!existing) {
      this.data.fallenMonsters.unshift({
        id: `fallen_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
        question: questionObj,
        wrongAnswer: userWrongAnswerIndex,
        timestamp: new Date().toLocaleDateString(),
        resolved: false
      });
      this.saveShadowState();
    }
  }

  getFallenMonsters() {
    return this.data.fallenMonsters;
  }

  /**
   * The iconic "ARISE" extraction!
   */
  extractShadow(fallenMonsterId) {
    const item = this.data.fallenMonsters.find(m => m.id === fallenMonsterId);
    if (!item || item.resolved) return false;

    item.resolved = true;
    this.data.totalShadows += 1;

    // Find which commander gains EXP based on subject
    const subject = item.question.subject;
    let targetCommander = this.data.commanders.find(c => c.subject === subject);
    if (!targetCommander) targetCommander = this.data.commanders[0];

    targetCommander.extractedCount += 1;
    targetCommander.exp += 40;
    if (targetCommander.exp >= targetCommander.maxExp) {
      targetCommander.exp -= targetCommander.maxExp;
      targetCommander.level += 1;
      targetCommander.maxExp = Math.floor(targetCommander.maxExp * 1.3);
    }

    // Award player
    if (window.Player) {
      window.Player.addExp(80);
      window.Player.addGold(30);
      window.Player.data.statsUnlocked.shadowsExtracted += 1;
      window.Player.saveState();
    }

    // Sound effect
    if (window.SystemAudio) {
      window.SystemAudio.playAriseSound();
    }

    this.saveShadowState();
    return {
      success: true,
      commander: targetCommander.name
    };
  }

  notifyUpdate() {
    window.dispatchEvent(new CustomEvent('shadows-updated', { detail: this.data }));
  }
}

window.Shadows = new ShadowsEngine();
