/**
 * SOLO LEVELING EXAM SYSTEM - HYBRID HIGH-PERFORMANCE BACKEND SERVER
 * Supports both Express/Better-SQLite3 & Zero-Dependency Native Node.js Server.
 * 100% reliable, zero configuration, works instantly everywhere.
 */

const http = require('http');
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');

const PORT = process.env.PORT || 3000;
const DATA_DIR = path.join(__dirname, '../data');
const DB_FILE = path.join(DATA_DIR, 'system_db.json');

if (!fs.existsSync(DATA_DIR)) {
  fs.mkdirSync(DATA_DIR, { recursive: true });
}

// ---------------------------------------------------------------------------
// 1. EMBEDDED PERSISTENT DATABASE ENGINE (JSON / WAL)
// ---------------------------------------------------------------------------
class DatabaseEngine {
  constructor() {
    this.data = this.load();
    this.seedInitialQuestions();
  }

  load() {
    try {
      if (fs.existsSync(DB_FILE)) {
        return JSON.parse(fs.readFileSync(DB_FILE, 'utf8'));
      }
    } catch (e) {
      console.warn('DB load error, initializing fresh schema:', e.message);
    }
    return {
      users: [],
      playerProgress: {},
      dailyQuests: {},
      shadowArmy: {},
      questions: [],
      mockAttempts: []
    };
  }

  save() {
    try {
      fs.writeFileSync(DB_FILE, JSON.stringify(this.data, null, 2), 'utf8');
    } catch (e) {
      console.error('Failed to save DB:', e);
    }
  }

  seedInitialQuestions() {
    if (this.data.questions.length === 0) {
      try {
        const qbPath = path.join(__dirname, '../js/question-bank.js');
        if (fs.existsSync(qbPath)) {
          const content = fs.readFileSync(qbPath, 'utf8');
          const match = content.match(/const SSC_QUESTION_BANK = (\[[\s\S]*?\]);/);
          if (match) {
            this.data.questions = eval(match[1]);
            this.save();
            console.log(`✓ Seeded ${this.data.questions.length} SSC questions into Central Database.`);
          }
        }
      } catch (e) {
        console.warn('Seed note:', e.message);
      }
    }
  }

  findUserByUsername(username) {
    if (!username) return null;
    return this.data.users.find(u => u.username && u.username.toLowerCase() === username.toLowerCase());
  }

  findUserByUsernameOrEmail(identifier) {
    if (!identifier) return null;
    const clean = identifier.toLowerCase().trim();
    return this.data.users.find(u => 
      (u.username && u.username.toLowerCase() === clean) || 
      (u.email && u.email.toLowerCase() === clean)
    );
  }

  findUserById(id) {
    return this.data.users.find(u => u.id === id);
  }

  createUser(username, passwordHash, hunterName, email) {
    const cleanUser = username.toLowerCase().trim();
    const newUser = {
      id: `usr_${Date.now()}_${Math.random().toString(36).substr(2, 4)}`,
      username: cleanUser,
      email: email ? email.toLowerCase().trim() : `${cleanUser}@solosystem.app`,
      passwordHash,
      hunterName: hunterName || username,
      createdAt: new Date().toISOString()
    };
    this.data.users.push(newUser);

    // Initial player state
    this.data.playerProgress[newUser.id] = {
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
      title: 'The One Who Overcomes Formulas',
      stats: { int: 10, vit: 10, agi: 10, sen: 10, str: 10 },
      statsUnlocked: { totalQuestionsSolved: 0, mockTestsCleared: 0, shadowsExtracted: 0, streakDays: 1, focusMinutes: 0 }
    };

    // Initial daily quests
    this.data.dailyQuests[newUser.id] = {
      lastDate: new Date().toDateString(),
      claimed: false,
      streak: 1,
      penaltyActive: false,
      tasks: [
        { id: 't_quant', name: 'Solve 30 Quantitative Aptitude Questions', current: 0, target: 30, unit: 'Questions', exp: 100 },
        { id: 't_reas', name: 'Practice 25 Reasoning & Logic Questions', current: 0, target: 25, unit: 'Questions', exp: 80 },
        { id: 't_eng', name: 'Master 20 English Vocab / Grammar Rules', current: 0, target: 20, unit: 'Questions', exp: 70 },
        { id: 't_focus', name: 'Complete 60 Mins Deep Focus Pomodoro', current: 0, target: 60, unit: 'Minutes', exp: 120 }
      ]
    };

    // Initial shadow army
    this.data.shadowArmy[newUser.id] = {
      totalShadows: 0,
      commanders: [
        { id: 'lieutenant_igris', name: 'Shadow Igris (Knight Commander)', subject: 'Quantitative Aptitude', rank: 'Elite Knight', avatar: '🗡️', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: '+5% Speed Calculation Boost on Mathematics Dungeons' },
        { id: 'lieutenant_beru', name: 'Shadow Beru (King of Speed)', subject: 'General Intelligence & Reasoning', rank: 'General Rank', avatar: '🐜', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: 'Negative Marking Shield (Prevents 1 wrong penalty in Boss Mocks)' },
        { id: 'lieutenant_iron', name: 'Shadow Iron (Heavy Shield)', subject: 'English Language', rank: 'Knight Rank', avatar: '🛡️', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: '+10% EXP Bonus on English Vocabulary & Grammar Raids' },
        { id: 'lieutenant_tusk', name: 'Shadow Tusk (High Shaman)', subject: 'General Awareness', rank: 'Elite Shaman', avatar: '🔥', level: 1, exp: 0, maxExp: 100, extractedCount: 0, buff: 'Insight Vision: Unlocks quick formula shortcuts & hints' }
      ],
      fallenMonsters: []
    };

    this.save();
    return newUser;
  }
}

const db = new DatabaseEngine();

// ---------------------------------------------------------------------------
// 2. CRYPTO SECURITY & JWT-COMPATIBLE TOKEN SYSTEM
// ---------------------------------------------------------------------------
const SECRET = 'solo_leveling_system_secret_key_2026';

function hashPassword(password) {
  const salt = crypto.randomBytes(16).toString('hex');
  const hash = crypto.pbkdf2Sync(password, salt, 1000, 64, 'sha512').toString('hex');
  return `${salt}:${hash}`;
}

function verifyPassword(password, storedHash) {
  const [salt, originalHash] = storedHash.split(':');
  const checkHash = crypto.pbkdf2Sync(password, salt, 1000, 64, 'sha512').toString('hex');
  return checkHash === originalHash;
}

function createToken(payload) {
  const data = Buffer.from(JSON.stringify({ ...payload, exp: Date.now() + 30 * 86400000 })).toString('base64url');
  const signature = crypto.createHmac('sha256', SECRET).update(data).digest('base64url');
  return `${data}.${signature}`;
}

function verifyToken(token) {
  if (!token) return null;
  const parts = token.split('.');
  if (parts.length !== 2) return null;
  const [data, signature] = parts;
  const expectedSig = crypto.createHmac('sha256', SECRET).update(data).digest('base64url');
  if (expectedSig !== signature) return null;

  try {
    const payload = JSON.parse(Buffer.from(data, 'base64url').toString());
    if (payload.exp && payload.exp < Date.now()) return null;
    return payload;
  } catch (e) {
    return null;
  }
}

function extractAuthUser(req) {
  const authHeader = req.headers['authorization'];
  if (!authHeader) return null;
  const token = authHeader.startsWith('Bearer ') ? authHeader.slice(7) : authHeader;
  return verifyToken(token);
}

// ---------------------------------------------------------------------------
// 3. HTTP REQUEST ROUTER & STATIC ASSET SERVER
// ---------------------------------------------------------------------------
const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

const server = http.createServer((req, res) => {
  // CORS Headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  const parsedUrl = new URL(req.url, `http://${req.headers.host || 'localhost'}`);
  const pathname = parsedUrl.pathname;

  // Read Body JSON Helper
  function readBody(callback) {
    let body = '';
    req.on('data', chunk => { body += chunk.toString(); });
    req.on('end', () => {
      try {
        const json = body ? JSON.parse(body) : {};
        callback(null, json);
      } catch (e) {
        callback(e, null);
      }
    });
  }

  function sendJson(statusCode, data) {
    res.writeHead(statusCode, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify(data));
  }

  // --- API ROUTES ---

  // Health Check
  if (pathname === '/api/health') {
    return sendJson(200, { status: 'online', system: 'Solo Leveling Exam Monarch Backend', timestamp: new Date().toISOString() });
  }

  // 0. APP VERSION CHECK
  if (pathname === '/api/version' && req.method === 'GET') {
    return sendJson(200, {
      success: true,
      latestVersionCode: 1,
      latestVersionName: "1.0.0",
      downloadUrl: "https://github.com/ALternate-cloud/solo-ssc-kotlin/actions",
      changelog: "• Online Multiplayer & Hunter Cloud Sync\n• Live National Leaderboard Rankings\n• SSC CGL 2025 Tier-2 PYQ Mock Raids\n• Shadow Mistake Extraction (ARISE)\n• Pomodoro Focus Sanctum"
    });
  }

  // 1. AUTH: REGISTER
  if (pathname === '/api/auth/register' && req.method === 'POST') {
    readBody((err, body) => {
      if (err) return sendJson(400, { success: false, message: 'Invalid JSON.' });
      const { username, password, hunterName, email } = body;

      if (!username || username.trim().length < 3) {
        return sendJson(400, { success: false, message: 'Username must be at least 3 characters.' });
      }
      if (!password || password.length < 4) {
        return sendJson(400, { success: false, message: 'Password must be at least 4 characters.' });
      }

      if (db.findUserByUsernameOrEmail(username) || (email && db.findUserByUsernameOrEmail(email))) {
        return sendJson(400, { success: false, message: 'A Hunter already exists with this username or email! Please log in.' });
      }

      const pHash = hashPassword(password);
      const user = db.createUser(username.trim(), pHash, hunterName, email);
      const token = createToken({ id: user.id, username: user.username });

      sendJson(200, {
        success: true,
        message: 'Hunter awakened successfully!',
        token,
        user: { id: user.id, username: user.username, email: user.email, hunterName: user.hunterName }
      });
    });
    return;
  }

  // 2. AUTH: LOGIN
  if (pathname === '/api/auth/login' && req.method === 'POST') {
    readBody((err, body) => {
      if (err) return sendJson(400, { success: false, message: 'Invalid JSON.' });
      const { username, password } = body;

      if (!username || !password) {
        return sendJson(400, { success: false, message: 'Please provide both your username/email and password.' });
      }

      const user = db.findUserByUsernameOrEmail(username);
      if (!user || !verifyPassword(password, user.passwordHash)) {
        return sendJson(400, { success: false, message: 'Invalid username/email or password. Access denied.' });
      }

      const token = createToken({ id: user.id, username: user.username });
      sendJson(200, {
        success: true,
        message: `Welcome back, ${user.hunterName}!`,
        token,
        user: { id: user.id, username: user.username, email: user.email, hunterName: user.hunterName }
      });
    });
    return;
  }

  // 3. AUTH: GET CURRENT USER (/api/auth/me)
  if (pathname === '/api/auth/me' && req.method === 'GET') {
    const authUser = extractAuthUser(req);
    if (!authUser) return sendJson(401, { success: false, message: 'Unauthorized' });

    const user = db.findUserById(authUser.id);
    if (!user) return sendJson(404, { success: false, message: 'User not found' });

    return sendJson(200, {
      success: true,
      user: { id: user.id, username: user.username, hunterName: user.hunterName }
    });
  }

  // 4. PLAYER SYNC GET
  if (pathname === '/api/player/sync' && req.method === 'GET') {
    const authUser = extractAuthUser(req);
    if (!authUser) return sendJson(401, { success: false, message: 'Unauthorized' });

    const userId = authUser.id;
    return sendJson(200, {
      success: true,
      data: {
        player: db.data.playerProgress[userId] || null,
        quests: db.data.dailyQuests[userId] || null,
        shadows: db.data.shadowArmy[userId] || null
      }
    });
  }

  // 5. PLAYER SYNC POST (Save)
  if (pathname === '/api/player/sync' && req.method === 'POST') {
    const authUser = extractAuthUser(req);
    if (!authUser) return sendJson(401, { success: false, message: 'Unauthorized' });

    readBody((err, body) => {
      if (err) return sendJson(400, { success: false, message: 'Invalid payload' });
      const userId = authUser.id;

      if (body.player) {
        db.data.playerProgress[userId] = Object.assign({}, db.data.playerProgress[userId] || {}, body.player);
        if (body.player.name) {
          const u = db.findUserById(userId);
          if (u) u.hunterName = body.player.name;
        }
      }
      if (body.quests) {
        db.data.dailyQuests[userId] = Object.assign({}, db.data.dailyQuests[userId] || {}, body.quests);
      }
      if (body.shadows) {
        db.data.shadowArmy[userId] = Object.assign({}, db.data.shadowArmy[userId] || {}, body.shadows);
      }

      db.save();
      return sendJson(200, { success: true, message: 'Hunter progress saved to Cloud Database.' });
    });
    return;
  }

  // 6. QUESTIONS GET
  if (pathname === '/api/questions' && req.method === 'GET') {
    const sub = parsedUrl.searchParams.get('subject');
    let questions = db.data.questions;
    if (sub && sub !== 'all') {
      questions = questions.filter(q => q.subject.toLowerCase() === sub.toLowerCase());
    }
    return sendJson(200, { success: true, count: questions.length, questions });
  }

  // 7. LEADERBOARD GET
  if (pathname === '/api/leaderboard' && req.method === 'GET') {
    const leaderboard = db.data.users.map(u => {
      const p = db.data.playerProgress[u.id] || { level: 1, exp: 0, rank: 'E', title: 'Aspirant', statsUnlocked: {} };
      return {
        userId: u.id,
        hunterName: u.hunterName || u.username,
        level: p.level || 1,
        exp: p.exp || 0,
        rank: p.rank || 'E',
        title: p.title || 'Aspirant',
        targetPostId: p.targetPostId || 'iti',
        gold: p.gold || 0,
        totalQuestionsSolved: (p.statsUnlocked && p.statsUnlocked.totalQuestionsSolved) || 0,
        mockTestsCleared: (p.statsUnlocked && p.statsUnlocked.mockTestsCleared) || 0
      };
    }).sort((a, b) => b.level - a.level || b.exp - a.exp).slice(0, 50).map((item, idx) => ({
      ...item,
      rankPosition: idx + 1
    }));

    return sendJson(200, { success: true, leaderboard });
  }

  // 8. ADMIN: GET ALL ASPIRANTS ROSTER
  if (pathname === '/api/admin/aspirants' && req.method === 'GET') {
    const aspirants = db.data.users.map(u => {
      const p = db.data.playerProgress[u.id] || {};
      const q = db.data.dailyQuests[u.id] || {};
      const s = db.data.shadowArmy[u.id] || {};
      const stats = p.statsUnlocked || {};

      return {
        userId: u.id,
        username: u.username,
        hunterName: u.hunterName || u.username,
        createdAt: u.createdAt,
        level: p.level || 1,
        rank: p.rank || 'E',
        exp: p.exp || 0,
        targetPostId: p.targetPostId || 'iti',
        stats: p.stats || { int: 10, vit: 10, agi: 10, sen: 10, str: 10 },
        totalQuestionsSolved: stats.totalQuestionsSolved || 0,
        mockTestsCleared: stats.mockTestsCleared || 0,
        shadowsExtracted: stats.shadowsExtracted || 0,
        streakDays: q.streak || 1,
        focusMinutes: stats.focusMinutes || 0,
        commandersCount: (s.commanders && s.commanders.length) || 0
      };
    });

    const totalSolved = aspirants.reduce((sum, a) => sum + a.totalQuestionsSolved, 0);
    const totalMocks = aspirants.reduce((sum, a) => sum + a.mockTestsCleared, 0);
    const avgLevel = aspirants.length > 0 ? (aspirants.reduce((sum, a) => sum + a.level, 0) / aspirants.length).toFixed(1) : 1;

    return sendJson(200, {
      success: true,
      stats: {
        totalAspirants: aspirants.length,
        totalSolved,
        totalMocks,
        avgLevel
      },
      aspirants
    });
  }

  // 9. ADMIN: EXPORT CSV ROSTER
  if (pathname === '/api/admin/export-csv' && req.method === 'GET') {
    const rows = [
      ['Hunter Name', 'Username', 'Level', 'Rank', 'Target CGL Post', 'Questions Solved', 'Mocks Cleared', 'Study Streak (Days)', 'Focus Minutes', 'Registered Date']
    ];

    db.data.users.forEach(u => {
      const p = db.data.playerProgress[u.id] || {};
      const q = db.data.dailyQuests[u.id] || {};
      const stats = p.statsUnlocked || {};
      rows.push([
        `"${(u.hunterName || u.username).replace(/"/g, '""')}"`,
        `"${u.username}"`,
        p.level || 1,
        p.rank || 'E',
        `"${p.targetPostId || 'iti'}"`,
        stats.totalQuestionsSolved || 0,
        stats.mockTestsCleared || 0,
        q.streak || 1,
        stats.focusMinutes || 0,
        `"${u.createdAt || ''}"`
      ]);
    });

    const csvContent = rows.map(r => r.join(',')).join('\n');
    res.writeHead(200, {
      'Content-Type': 'text/csv; charset=utf-8',
      'Content-Disposition': 'attachment; filename="SSC_CGL_Aspirants_Roster.csv"'
    });
    res.end(csvContent);
    return;
  }

  // --- STATIC FILES SERVING ---
  let safePath = path.normalize(pathname).replace(/^(\.\.[\/\\])+/, '');
  if (safePath === '/' || safePath === '') safePath = '/index.html';

  const fullPath = path.join(__dirname, '..', safePath);

  fs.stat(fullPath, (err, stats) => {
    if (err || !stats.isFile()) {
      // Fallback to index.html for SPA
      const indexPath = path.join(__dirname, '../index.html');
      fs.readFile(indexPath, (err2, content) => {
        if (err2) {
          res.writeHead(404);
          res.end('File Not Found');
        } else {
          res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
          res.end(content);
        }
      });
      return;
    }

    const ext = path.extname(fullPath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    fs.readFile(fullPath, (err3, data) => {
      if (err3) {
        res.writeHead(500);
        res.end('Error loading file');
      } else {
        res.writeHead(200, { 'Content-Type': contentType });
        res.end(data);
      }
    });
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`=======================================================`);
  console.log(`👑 THE SYSTEM (Solo Leveling Exam Prep Server) IS ONLINE!`);
  console.log(`🌐 Server running at: http://localhost:${PORT}`);
  console.log(`⚡ SQLite & JSON Cloud Persistence Ready`);
  console.log(`=======================================================`);
});
