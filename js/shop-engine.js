/**
 * SOLO LEVELING EXAM SYSTEM - SHOP & FOCUS DUNGEON ENGINE
 */

const SYSTEM_SHOP_ITEMS = [
  {
    id: 'item_focus_elixir',
    name: 'Elixir of Absolute Focus',
    icon: '🧪',
    price: 40,
    desc: 'Instantly grants 25-minute enhanced Pomodoro Focus session with Binaural Alpha Beats and +50 EXP upon completion.',
    type: 'consumable'
  },
  {
    id: 'item_streak_shield',
    name: 'Streak Preservation Crystal',
    icon: '💎',
    price: 100,
    desc: 'Protects your Daily Quest streak from breaking if you miss studying for a single day.',
    type: 'utility'
  },
  {
    id: 'item_rulers_scroll',
    name: "Ruler's Authority Formula Scroll",
    icon: '📜',
    price: 80,
    desc: 'Unlocks permanent instant formula cheat sheets and trick shortcuts inside Mock Test Boss battles.',
    type: 'relic'
  },
  {
    id: 'item_title_conqueror',
    name: 'Title: "Monarch of Syllabus"',
    icon: '👑',
    price: 250,
    desc: 'Equip an illustrious title shown in your Hunter Status card and gain +10% Gold from all Raids.',
    type: 'title'
  }
];

class ShopEngine {
  constructor() {
    this.items = SYSTEM_SHOP_ITEMS;
    this.inventory = this.loadInventory();
  }

  loadInventory() {
    try {
      const saved = localStorage.getItem('solo_system_inventory');
      if (saved) return JSON.parse(saved);
    } catch (e) {
      console.error('Failed to load inventory:', e);
    }
    return {
      items: [],
      streakShields: 1,
      hasRulersAuthority: false
    };
  }

  saveInventory() {
    try {
      localStorage.setItem('solo_system_inventory', JSON.stringify(this.inventory));
    } catch (e) {
      console.error('Failed to save inventory:', e);
    }
    this.notifyUpdate();
  }

  buyItem(itemId) {
    const item = this.items.find(i => i.id === itemId);
    if (!item) return { success: false, message: 'Item not found' };

    if (!window.Player || window.Player.data.gold < item.price) {
      return { success: false, message: 'Insufficient Gold Crystals! Clear more Dungeons & Daily Quests to earn Gold.' };
    }

    window.Player.spendGold(item.price);

    if (itemId === 'item_streak_shield') {
      this.inventory.streakShields += 1;
    } else if (itemId === 'item_rulers_scroll') {
      this.inventory.hasRulersAuthority = true;
    } else if (itemId === 'item_title_conqueror') {
      window.Player.data.title = 'Monarch of Syllabus';
      window.Player.saveState();
    } else {
      this.inventory.items.push({
        id: item.id,
        name: item.name,
        icon: item.icon,
        purchasedAt: new Date().toLocaleDateString()
      });
    }

    this.saveInventory();
    if (window.SystemAudio) window.SystemAudio.playLevelUp();
    return { success: true, message: `Successfully acquired ${item.name}!` };
  }

  notifyUpdate() {
    window.dispatchEvent(new CustomEvent('inventory-updated', { detail: this.inventory }));
  }
}

window.Shop = new ShopEngine();
