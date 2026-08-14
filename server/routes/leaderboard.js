/**
 * SOLO LEVELING EXAM SYSTEM - GLOBAL HUNTER LEADERBOARD API
 */

const express = require('express');
const router = express.Router();
const db = require('../database');

router.get('/', (req, res) => {
  try {
    const rows = db.prepare(`
      SELECT 
        u.id, 
        u.username, 
        u.hunter_name, 
        p.level, 
        p.exp, 
        p.rank, 
        p.title, 
        p.gold,
        p.milestones_json
      FROM users u
      JOIN player_progress p ON u.id = p.user_id
      ORDER BY p.level DESC, p.exp DESC
      LIMIT 50
    `).all();

    const leaderboard = rows.map((r, index) => {
      let solved = 0;
      let mocks = 0;
      try {
        const milestones = JSON.parse(r.milestones_json || '{}');
        solved = milestones.totalQuestionsSolved || 0;
        mocks = milestones.mockTestsCleared || 0;
      } catch (e) {}

      return {
        rankPosition: index + 1,
        userId: r.id,
        hunterName: r.hunter_name || r.username,
        level: r.level,
        rank: r.rank,
        title: r.title,
        gold: r.gold,
        totalQuestionsSolved: solved,
        mockTestsCleared: mocks
      };
    });

    res.json({ success: true, leaderboard });
  } catch (err) {
    res.status(500).json({ success: false, message: 'Failed to fetch leaderboard: ' + err.message });
  }
});

module.exports = router;
