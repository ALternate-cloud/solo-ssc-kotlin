/**
 * SOLO LEVELING EXAM SYSTEM - PLAYER CLOUD SYNC ROUTES
 */

const express = require('express');
const router = express.Router();
const db = require('../database');
const { authenticateToken } = require('./auth');

// 1. GET FULL SYNC DATA (Player stats, quests, shadow army)
router.get('/sync', authenticateToken, (req, res) => {
  try {
    const userId = req.user.id;

    // Get Player Progress
    const playerRow = db.prepare('SELECT * FROM player_progress WHERE user_id = ?').get(userId);
    // Get Quests
    const questsRow = db.prepare('SELECT * FROM daily_quests WHERE user_id = ?').get(userId);
    // Get Shadow Army
    const shadowRow = db.prepare('SELECT * FROM shadow_army WHERE user_id = ?').get(userId);

    const playerData = playerRow ? {
      level: playerRow.level,
      exp: playerRow.exp,
      maxExp: playerRow.max_exp,
      rank: playerRow.rank,
      hp: playerRow.hp,
      maxHp: playerRow.max_hp,
      mp: playerRow.mp,
      maxMp: playerRow.max_mp,
      gold: playerRow.gold,
      unallocatedPoints: playerRow.unallocated_points,
      title: playerRow.title,
      stats: JSON.parse(playerRow.stats_json || '{}'),
      statsUnlocked: JSON.parse(playerRow.milestones_json || '{}')
    } : null;

    const questsData = questsRow ? {
      lastDate: questsRow.last_date,
      claimed: Boolean(questsRow.claimed),
      streak: questsRow.streak,
      penaltyActive: Boolean(questsRow.penalty_active),
      tasks: JSON.parse(questsRow.tasks_json || '[]')
    } : null;

    const shadowData = shadowRow ? {
      totalShadows: shadowRow.total_shadows,
      commanders: JSON.parse(shadowRow.commanders_json || '[]'),
      fallenMonsters: JSON.parse(shadowRow.fallen_monsters_json || '[]')
    } : null;

    res.json({
      success: true,
      data: {
        player: playerData,
        quests: questsData,
        shadows: shadowData
      }
    });
  } catch (err) {
    console.error('Player sync fetch error:', err);
    res.status(500).json({ success: false, message: 'Failed to fetch player cloud state: ' + err.message });
  }
});

// 2. SAVE FULL SYNC DATA (Push client updates to cloud database)
router.post('/sync', authenticateToken, (req, res) => {
  try {
    const userId = req.user.id;
    const { player, quests, shadows } = req.body;

    if (player) {
      db.prepare(`
        INSERT INTO player_progress (user_id, level, exp, max_exp, rank, hp, max_hp, mp, max_mp, gold, unallocated_points, title, stats_json, milestones_json, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        ON CONFLICT(user_id) DO UPDATE SET
          level = excluded.level,
          exp = excluded.exp,
          max_exp = excluded.max_exp,
          rank = excluded.rank,
          hp = excluded.hp,
          max_hp = excluded.max_hp,
          mp = excluded.mp,
          max_mp = excluded.max_mp,
          gold = excluded.gold,
          unallocated_points = excluded.unallocated_points,
          title = excluded.title,
          stats_json = excluded.stats_json,
          milestones_json = excluded.milestones_json,
          updated_at = CURRENT_TIMESTAMP
      `).run(
        userId,
        player.level || 1,
        player.exp || 0,
        player.maxExp || 100,
        player.rank || 'E',
        player.hp || 100,
        player.maxHp || 100,
        player.mp || 50,
        player.maxMp || 50,
        player.gold || 150,
        player.unallocatedPoints !== undefined ? player.unallocatedPoints : 5,
        player.title || 'The One Who Overcomes Formulas',
        JSON.stringify(player.stats || {}),
        JSON.stringify(player.statsUnlocked || {})
      );

      // Also update hunter_name in users if changed
      if (player.name) {
        db.prepare('UPDATE users SET hunter_name = ? WHERE id = ?').run(player.name, userId);
      }
    }

    if (quests) {
      db.prepare(`
        INSERT INTO daily_quests (user_id, last_date, claimed, streak, penalty_active, tasks_json, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        ON CONFLICT(user_id) DO UPDATE SET
          last_date = excluded.last_date,
          claimed = excluded.claimed,
          streak = excluded.streak,
          penalty_active = excluded.penalty_active,
          tasks_json = excluded.tasks_json,
          updated_at = CURRENT_TIMESTAMP
      `).run(
        userId,
        quests.lastDate || new Date().toDateString(),
        quests.claimed ? 1 : 0,
        quests.streak || 1,
        quests.penaltyActive ? 1 : 0,
        JSON.stringify(quests.tasks || [])
      );
    }

    if (shadows) {
      db.prepare(`
        INSERT INTO shadow_army (user_id, total_shadows, commanders_json, fallen_monsters_json, updated_at)
        VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)
        ON CONFLICT(user_id) DO UPDATE SET
          total_shadows = excluded.total_shadows,
          commanders_json = excluded.commanders_json,
          fallen_monsters_json = excluded.fallen_monsters_json,
          updated_at = CURRENT_TIMESTAMP
      `).run(
        userId,
        shadows.totalShadows || 0,
        JSON.stringify(shadows.commanders || []),
        JSON.stringify(shadows.fallenMonsters || [])
      );
    }

    res.json({ success: true, message: 'Hunter progress successfully synchronized with server cloud!' });
  } catch (err) {
    console.error('Player sync save error:', err);
    res.status(500).json({ success: false, message: 'Failed to save player cloud state: ' + err.message });
  }
});

module.exports = router;
