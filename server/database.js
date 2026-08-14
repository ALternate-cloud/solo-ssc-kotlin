/**
 * SOLO LEVELING EXAM SYSTEM - BACKEND DATABASE ENGINE (SQLITE)
 */

const fs = require('fs');
const path = require('path');
const Database = require('better-sqlite3');

const dataDir = path.join(__dirname, '../data');
if (!fs.existsSync(dataDir)) {
  fs.mkdirSync(dataDir, { recursive: true });
}

const dbPath = path.join(dataDir, 'system.db');
const db = new Database(dbPath);

// Enable WAL mode for high concurrency & speed
db.pragma('journal_mode = WAL');

// Initialize Tables
function initDatabase() {
  db.exec(`
    CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      username TEXT UNIQUE NOT NULL,
      password_hash TEXT NOT NULL,
      hunter_name TEXT DEFAULT 'Sung Jin-Aspirant',
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS player_progress (
      user_id INTEGER PRIMARY KEY,
      level INTEGER DEFAULT 1,
      exp INTEGER DEFAULT 0,
      max_exp INTEGER DEFAULT 100,
      rank TEXT DEFAULT 'E',
      hp INTEGER DEFAULT 100,
      max_hp INTEGER DEFAULT 100,
      mp INTEGER DEFAULT 50,
      max_mp INTEGER DEFAULT 50,
      gold INTEGER DEFAULT 150,
      unallocated_points INTEGER DEFAULT 5,
      title TEXT DEFAULT 'The One Who Overcomes Formulas',
      stats_json TEXT DEFAULT '{"int":10,"vit":10,"agi":10,"sen":10,"str":10}',
      milestones_json TEXT DEFAULT '{"totalQuestionsSolved":0,"mockTestsCleared":0,"shadowsExtracted":0,"streakDays":1,"focusMinutes":0}',
      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

    CREATE TABLE IF NOT EXISTS daily_quests (
      user_id INTEGER PRIMARY KEY,
      last_date TEXT,
      claimed INTEGER DEFAULT 0,
      streak INTEGER DEFAULT 1,
      penalty_active INTEGER DEFAULT 0,
      tasks_json TEXT,
      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

    CREATE TABLE IF NOT EXISTS shadow_army (
      user_id INTEGER PRIMARY KEY,
      total_shadows INTEGER DEFAULT 0,
      commanders_json TEXT,
      fallen_monsters_json TEXT,
      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

    CREATE TABLE IF NOT EXISTS questions (
      id TEXT PRIMARY KEY,
      subject TEXT NOT NULL,
      topic TEXT NOT NULL,
      difficulty TEXT DEFAULT 'Medium',
      question TEXT NOT NULL,
      options_json TEXT NOT NULL,
      correct INTEGER NOT NULL,
      explanation TEXT,
      trick TEXT,
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS mock_attempts (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      user_id INTEGER NOT NULL,
      gate_id TEXT,
      score REAL,
      accuracy INTEGER,
      correct_count INTEGER,
      wrong_count INTEGER,
      unattempted_count INTEGER,
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
  `);

  console.log('⚡ Solo Leveling SQLite Database Initialized.');
}

initDatabase();

module.exports = db;
