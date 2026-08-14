/**
 * SOLO LEVELING EXAM SYSTEM - AUTHENTICATION ROUTES (JWT & BCRYPT)
 */

const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const db = require('../database');

const JWT_SECRET = process.env.JWT_SECRET || 'solo_leveling_monarch_secret_key_2026';

// Middleware to authenticate JWT
function authenticateToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({ success: false, message: 'Authentication required. Please awaken your Hunter account.' });
  }

  jwt.verify(token, JWT_SECRET, (err, user) => {
    if (err) {
      return res.status(403).json({ success: false, message: 'Invalid or expired Hunter session token.' });
    }
    req.user = user;
    next();
  });
}

// 1. REGISTER NEW HUNTER
router.post('/register', async (req, res) => {
  try {
    const { username, password, hunterName } = req.body;

    if (!username || username.trim().length < 3) {
      return res.status(400).json({ success: false, message: 'Username must be at least 3 characters.' });
    }
    if (!password || password.length < 4) {
      return res.status(400).json({ success: false, message: 'Password must be at least 4 characters.' });
    }

    const cleanUsername = username.trim().toLowerCase();
    const cleanHunterName = (hunterName && hunterName.trim()) || 'Sung Jin-Aspirant';

    // Check if user exists
    const existing = db.prepare('SELECT id FROM users WHERE username = ?').get(cleanUsername);
    if (existing) {
      return res.status(400).json({ success: false, message: 'Hunter with this username already awakened! Please log in.' });
    }

    // Hash password
    const passwordHash = await bcrypt.hash(password, 10);

    // Insert user
    const insertUser = db.prepare('INSERT INTO users (username, password_hash, hunter_name) VALUES (?, ?, ?)');
    const info = insertUser.run(cleanUsername, passwordHash, cleanHunterName);
    const userId = info.lastInsertRowid;

    // Create default player progress
    db.prepare(`
      INSERT INTO player_progress (user_id, level, exp, max_exp, rank, hp, max_hp, mp, max_mp, gold, unallocated_points, title, stats_json, milestones_json)
      VALUES (?, 1, 0, 100, 'E', 100, 100, 50, 50, 150, 5, 'The One Who Overcomes Formulas', ?, ?)
    `).run(
      userId,
      JSON.stringify({ int: 10, vit: 10, agi: 10, sen: 10, str: 10 }),
      JSON.stringify({ totalQuestionsSolved: 0, mockTestsCleared: 0, shadowsExtracted: 0, streakDays: 1, focusMinutes: 0 })
    );

    // Create default daily quests
    db.prepare(`
      INSERT INTO daily_quests (user_id, last_date, claimed, streak, penalty_active, tasks_json)
      VALUES (?, ?, 0, 1, 0, ?)
    `).run(
      userId,
      new Date().toDateString(),
      JSON.stringify([
        { id: 't_quant', name: 'Solve 30 Quantitative Aptitude Questions', current: 0, target: 30, unit: 'Questions', exp: 100 },
        { id: 't_reas', name: 'Practice 25 Reasoning & Logic Questions', current: 0, target: 25, unit: 'Questions', exp: 80 },
        { id: 't_eng', name: 'Master 20 English Vocab / Grammar Rules', current: 0, target: 20, unit: 'Questions', exp: 70 },
        { id: 't_focus', name: 'Complete 60 Mins Deep Focus Pomodoro', current: 0, target: 60, unit: 'Minutes', exp: 120 }
      ])
    );

    // Create default shadow army
    db.prepare(`
      INSERT INTO shadow_army (user_id, total_shadows, commanders_json, fallen_monsters_json)
      VALUES (?, 0, ?, ?)
    `).run(
      userId,
      JSON.stringify([
        { id: 'lieutenant_igris', name: 'Shadow Igris (Knight Commander)', subject: 'Quantitative Aptitude', rank: 'Elite Knight', avatar: '🗡️', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: '+5% Speed Calculation Boost on Mathematics Dungeons' },
        { id: 'lieutenant_beru', name: 'Shadow Beru (King of Speed)', subject: 'General Intelligence & Reasoning', rank: 'General Rank', avatar: '🐜', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: 'Negative Marking Shield (Prevents 1 wrong penalty in Boss Mocks)' },
        { id: 'lieutenant_iron', name: 'Shadow Iron (Heavy Shield)', subject: 'English Language', rank: 'Knight Rank', avatar: '🛡️', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: '+10% EXP Bonus on English Vocabulary & Grammar Raids' },
        { id: 'lieutenant_tusk', name: 'Shadow Tusk (High Shaman)', subject: 'General Awareness', rank: 'Elite Shaman', avatar: '🔥', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: 'Insight Vision: Unlocks quick formula shortcuts & hints' }
      ]),
      JSON.stringify([])
    );

    // Generate JWT
    const token = jwt.sign({ id: userId, username: cleanUsername }, JWT_SECRET, { expiresIn: '30d' });

    res.json({
      success: true,
      message: 'Hunter awakened successfully!',
      token,
      user: { id: userId, username: cleanUsername, hunterName: cleanHunterName }
    });
  } catch (err) {
    console.error('Register error:', err);
    res.status(500).json({ success: false, message: 'Server error during Hunter awakening: ' + err.message });
  }
});

// 2. LOGIN EXISTING HUNTER
router.post('/login', async (req, res) => {
  try {
    const { username, password } = req.body;

    if (!username || !password) {
      return res.status(400).json({ success: false, message: 'Please provide both username and password.' });
    }

    const cleanUsername = username.trim().toLowerCase();
    const user = db.prepare('SELECT * FROM users WHERE username = ?').get(cleanUsername);

    if (!user) {
      return res.status(400).json({ success: false, message: 'Hunter not found. Please check username or register.' });
    }

    const isMatch = await bcrypt.compare(password, user.password_hash);
    if (!isMatch) {
      return res.status(400).json({ success: false, message: 'Invalid password. Access denied.' });
    }

    const token = jwt.sign({ id: user.id, username: user.username }, JWT_SECRET, { expiresIn: '30d' });

    res.json({
      success: true,
      message: `Welcome back, ${user.hunter_name}!`,
      token,
      user: { id: user.id, username: user.username, hunterName: user.hunter_name }
    });
  } catch (err) {
    console.error('Login error:', err);
    res.status(500).json({ success: false, message: 'Server error during login: ' + err.message });
  }
});

// 3. GET CURRENT LOGGED-IN HUNTER PROFILE
router.get('/me', authenticateToken, (req, res) => {
  try {
    const user = db.prepare('SELECT id, username, hunter_name, created_at FROM users WHERE id = ?').get(req.user.id);
    if (!user) {
      return res.status(404).json({ success: false, message: 'Hunter profile not found.' });
    }
    res.json({ success: true, user });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

module.exports = { router, authenticateToken };
