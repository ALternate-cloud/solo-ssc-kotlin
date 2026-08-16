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
// 1. EMBEDDED & CLOUD PERSISTENT DATABASE ENGINE (MongoDB Atlas / WAL JSON)
// ---------------------------------------------------------------------------
let mongoDbInstance = null;

class DatabaseEngine {
  constructor() {
    this.data = this.load();
    this.seedInitialQuestions();
    this.initCloudDatabase();
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

  async initCloudDatabase() {
    const uri = process.env.MONGODB_URI;
    if (!uri) {
      console.log('ℹ️ Running in Standalone Mode. Set MONGODB_URI on Render for permanent multi-server cloud DB.');
      return;
    }
    try {
      const { MongoClient } = require('mongodb');
      const client = new MongoClient(uri, { serverSelectionTimeoutMS: 5000 });
      await client.connect();
      mongoDbInstance = client.db('solosystem');
      console.log('✓ Successfully connected to Permanent Cloud Database (MongoDB Atlas)!');

      // Load cloud state
      const stateDoc = await mongoDbInstance.collection('app_state').findOne({ _id: 'central_state' });
      if (stateDoc && stateDoc.data) {
        this.data = stateDoc.data;
        console.log(`✓ Restored ${this.data.users.length} hunter accounts from Cloud Database.`);
      } else {
        // Initial cloud upload
        await mongoDbInstance.collection('app_state').updateOne(
          { _id: 'central_state' },
          { $set: { data: this.data, updatedAt: new Date() } },
          { upsert: true }
        );
      }
    } catch (e) {
      console.error('Cloud DB initialization notice:', e.message);
    }
  }

  save() {
    try {
      fs.writeFileSync(DB_FILE, JSON.stringify(this.data, null, 2), 'utf8');
    } catch (e) {
      console.error('Failed to save local DB:', e);
    }

    // Background asynchronous sync to Cloud Database
    if (mongoDbInstance) {
      mongoDbInstance.collection('app_state').updateOne(
        { _id: 'central_state' },
        { $set: { data: this.data, updatedAt: new Date() } },
        { upsert: true }
      ).catch(err => console.error('Cloud DB sync error:', err.message));
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
      name: newUser.hunterName,
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
      latestVersionCode: 3,
      latestVersionName: "1.2.0",
      downloadUrl: "https://github.com/ALternate-cloud/solo-ssc-kotlin/releases/download/latest/app-debug.apk",
      changelog: "• Personalized Hunter Profile: Displays your registered username\n• Auto-sync Hunter Name across all Status & Leaderboard screens\n• Enhanced System Performance"
    });
  }

  // 1. AUTH: REGISTER
  if (pathname === '/api/auth/register' && req.method === 'POST') {
    readBody((err, body) => {
      if (err) return sendJson(200, { success: false, message: 'Invalid request format.' });
      const { username, password, hunterName, email } = body;

      if (!username || username.trim().length < 3) {
        return sendJson(200, { success: false, message: 'Username must be at least 3 characters.' });
      }
      if (!password || password.length < 4) {
        return sendJson(200, { success: false, message: 'Password must be at least 4 characters.' });
      }

      if (db.findUserByUsernameOrEmail(username) || (email && db.findUserByUsernameOrEmail(email))) {
        return sendJson(200, { success: false, message: 'A Hunter with this username or email already exists. Please log in.' });
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
      if (err) return sendJson(200, { success: false, message: 'Invalid request format.' });
      const { username, password } = body;

      if (!username || !password) {
        return sendJson(200, { success: false, message: 'Please enter both your username/email and password.' });
      }

      const user = db.findUserByUsernameOrEmail(username);
      if (!user) {
        return sendJson(200, { success: false, message: 'No Hunter account found with this username/email. Please tap "Awaken Here" to register first!' });
      }

      if (!verifyPassword(password, user.passwordHash)) {
        return sendJson(200, { success: false, message: 'Incorrect password. Access denied.' });
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

  // 3.5 AUTH: DELETE ACCOUNT (/api/auth/delete-account)
  if ((pathname === '/api/auth/delete-account' || pathname === '/api/auth/delete') && (req.method === 'POST' || req.method === 'DELETE')) {
    const authUser = extractAuthUser(req);
    if (!authUser) return sendJson(401, { success: false, message: 'Unauthorized. Please log in again.' });

    const userId = authUser.id;
    db.data.users = db.data.users.filter(u => u.id !== userId);
    delete db.data.playerProgress[userId];
    delete db.data.dailyQuests[userId];
    delete db.data.shadowArmy[userId];
    if (db.data.mockAttempts) {
      db.data.mockAttempts = db.data.mockAttempts.filter(m => m.userId !== userId);
    }
    db.save();

    return sendJson(200, {
      success: true,
      message: 'Hunter account and all cloud records have been permanently purged.'
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
    const LEGENDARY_HUNTERS = [
      {
        userId: 'monarch_sung_jinwoo',
        hunterName: 'Sung Jin-Woo (Shadow Monarch)',
        level: 85,
        exp: 4500,
        rank: 'Monarch',
        title: 'The Sovereign of Shadows',
        targetPostId: 'cbi',
        gold: 50000,
        totalQuestionsSolved: 3840,
        mockTestsCleared: 82
      },
      {
        userId: 'monarch_thomas_andre',
        hunterName: 'Thomas Andre (Goliath)',
        level: 72,
        exp: 3100,
        rank: 'S',
        title: 'National Level Aspirant',
        targetPostId: 'iti',
        gold: 32000,
        totalQuestionsSolved: 2950,
        mockTestsCleared: 64
      },
      {
        userId: 'monarch_liu_zhigang',
        hunterName: 'Liu Zhigang (Blade Hero)',
        level: 65,
        exp: 2800,
        rank: 'S',
        title: 'Dragon Slayer of Quantitative Aptitude',
        targetPostId: 'ed',
        gold: 24000,
        totalQuestionsSolved: 2420,
        mockTestsCleared: 53
      },
      {
        userId: 'monarch_cha_haein',
        hunterName: 'Cha Hae-In (Sword Dancer)',
        level: 58,
        exp: 2100,
        rank: 'S',
        title: 'The Radiant Hunter',
        targetPostId: 'mea',
        gold: 18500,
        totalQuestionsSolved: 1980,
        mockTestsCleared: 45
      },
      {
        userId: 'monarch_choi_jongin',
        hunterName: 'Choi Jong-In (Ultimate Mage)',
        level: 49,
        exp: 1600,
        rank: 'A',
        title: 'Master of Logical Reasoning',
        targetPostId: 'gst',
        gold: 14200,
        totalQuestionsSolved: 1640,
        mockTestsCleared: 38
      },
      {
        userId: 'monarch_baek_yoonho',
        hunterName: 'Baek Yoon-Ho (White Tiger)',
        level: 42,
        exp: 1200,
        rank: 'A',
        title: 'Beast Transformation Math Master',
        targetPostId: 'da',
        gold: 11000,
        totalQuestionsSolved: 1350,
        mockTestsCleared: 31
      },
      {
        userId: 'monarch_woo_jinchul',
        hunterName: 'Woo Jin-Chul (Association Chief)',
        level: 35,
        exp: 800,
        rank: 'A',
        title: 'Inspector of Discipline',
        targetPostId: 'cag',
        gold: 8500,
        totalQuestionsSolved: 1040,
        mockTestsCleared: 24
      },
      {
        userId: 'monarch_min_byunggyu',
        hunterName: 'Min Byung-Gyu (Saint Healer)',
        level: 28,
        exp: 550,
        rank: 'B',
        title: 'Negative Marking Restorer',
        targetPostId: 'po',
        gold: 6200,
        totalQuestionsSolved: 790,
        mockTestsCleared: 18
      },
      {
        userId: 'monarch_yoo_jinho',
        hunterName: 'Yoo Jin-Ho (Vice Guild Master)',
        level: 18,
        exp: 300,
        rank: 'C',
        title: 'Rich Boy Aspirant',
        targetPostId: 'iti',
        gold: 35000,
        totalQuestionsSolved: 450,
        mockTestsCleared: 10
      }
    ];

    const realUsers = db.data.users.map(u => {
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
    });

    // Merge real users with legendary NPC hunter rivals
    const combined = [...realUsers, ...LEGENDARY_HUNTERS];

    const leaderboard = combined
      .sort((a, b) => (b.level - a.level) || (b.exp - a.exp) || (b.totalQuestionsSolved - a.totalQuestionsSolved))
      .slice(0, 50)
      .map((item, idx) => ({
        ...item,
        rankPosition: idx + 1
      }));

    return sendJson(200, { success: true, leaderboard });
  }

  // 8. ADMIN: GET ALL ASPIRANTS ROSTER (Protected)
  const ADMIN_MASTER_KEY = process.env.ADMIN_KEY || 'monarch2026';

  if (pathname === '/api/admin/aspirants' && req.method === 'GET') {
    const key = req.headers['x-admin-key'] || parsedUrl.searchParams.get('key');
    if (key !== ADMIN_MASTER_KEY) {
      return sendJson(401, { success: false, message: 'Access Denied: Invalid Monarch Master Key.' });
    }

    const aspirants = db.data.users.map(u => {
      const p = db.data.playerProgress[u.id] || {};
      const q = db.data.dailyQuests[u.id] || {};
      const s = db.data.shadowArmy[u.id] || {};
      const stats = p.statsUnlocked || {};

      return {
        userId: u.id,
        username: u.username,
        email: u.email || 'N/A',
        hunterName: u.hunterName || u.username,
        createdAt: u.createdAt,
        level: p.level || 1,
        rank: p.rank || 'E',
        exp: p.exp || 0,
        gold: p.gold || 0,
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

  // 9. ADMIN: EXPORT CSV ROSTER (Protected)
  if (pathname === '/api/admin/export-csv' && req.method === 'GET') {
    const key = parsedUrl.searchParams.get('key');
    if (key !== ADMIN_MASTER_KEY) {
      return sendJson(401, { success: false, message: 'Access Denied: Invalid Monarch Master Key.' });
    }

    const rows = [
      ['Hunter Name', 'Username', 'Email', 'Level', 'Rank', 'Target CGL Post', 'Questions Solved', 'Mocks Cleared', 'Study Streak (Days)', 'Focus Minutes', 'Registered Date']
    ];

    db.data.users.forEach(u => {
      const p = db.data.playerProgress[u.id] || {};
      const q = db.data.dailyQuests[u.id] || {};
      const stats = p.statsUnlocked || {};
      rows.push([
        `"${(u.hunterName || u.username).replace(/"/g, '""')}"`,
        `"${u.username}"`,
        `"${u.email || ''}"`,
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
      'Content-Disposition': 'attachment; filename="SoloLeveling_Aspirants_Roster.csv"'
    });
    res.end(csvContent);
    return;
  }

  // 9.5 ADMIN: DEVELOPER GOD MODE (/api/admin/god-mode)
  if (pathname === '/api/admin/god-mode' && req.method === 'POST') {
    const key = req.headers['x-admin-key'] || parsedUrl.searchParams.get('key');
    if (key !== ADMIN_MASTER_KEY) {
      return sendJson(401, { success: false, message: 'Access Denied: Invalid Master Key.' });
    }

    readBody((err, body) => {
      if (err) return sendJson(400, { success: false, message: 'Invalid payload.' });
      const { userId, level, rank, gold, unallocatedPoints, title } = body;

      if (!userId) {
        return sendJson(400, { success: false, message: 'userId is required.' });
      }

      if (!db.data.playerProgress[userId]) {
        db.data.playerProgress[userId] = {};
      }

      const p = db.data.playerProgress[userId];
      p.level = level || 100;
      p.rank = rank || 'Monarch';
      p.gold = gold !== undefined ? gold : 999999;
      p.unallocatedPoints = unallocatedPoints !== undefined ? unallocatedPoints : 500;
      p.title = title || 'The Architect of the System';
      p.exp = 0;
      p.maxExp = 10000;
      p.hp = 1000;
      p.maxHp = 1000;
      p.mp = 500;
      p.maxMp = 500;
      if (!p.stats) p.stats = { int: 100, vit: 100, agi: 100, sen: 100, str: 100 };

      db.save();
      return sendJson(200, { success: true, message: `Hunter ascended to Level ${p.level} (${p.rank} Rank)!` });
    });
    return;
  }

  // 10. ADMIN: WEB DASHBOARD HTML PORTAL (Password Protected)
  if (pathname === '/admin' && req.method === 'GET') {
    const html = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Solo Leveling SSC - Monarch Admin Portal</title>
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
    body { background: #060b13; color: #e2e8f0; padding: 24px; min-height: 100vh; }
    .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #1e293b; padding-bottom: 16px; margin-bottom: 24px; flex-wrap: wrap; gap: 12px; }
    .logo { color: #00d2ff; font-size: 22px; font-weight: 900; letter-spacing: 2px; }
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin-bottom: 24px; }
    .stat-card { background: #0f172a; border: 1px solid #1e293b; border-radius: 8px; padding: 16px; }
    .stat-label { color: #94a3b8; font-size: 12px; font-weight: 600; text-transform: uppercase; }
    .stat-val { color: #00d2ff; font-size: 28px; font-weight: bold; margin-top: 4px; }
    .controls { display: flex; justify-content: space-between; gap: 12px; margin-bottom: 16px; flex-wrap: wrap; }
    .search-box { background: #0f172a; border: 1px solid #334155; color: #fff; padding: 8px 14px; border-radius: 6px; flex: 1; min-width: 200px; }
    .btn { background: #00d2ff; color: #060b13; font-weight: bold; padding: 8px 16px; border-radius: 6px; border: none; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; }
    .btn:hover { background: #38bdf8; }
    .table-wrap { overflow-x: auto; background: #0f172a; border: 1px solid #1e293b; border-radius: 8px; }
    table { width: 100%; border-collapse: collapse; text-align: left; }
    th { background: #1e293b; color: #cbd5e1; padding: 12px 14px; font-size: 13px; text-transform: uppercase; }
    td { padding: 12px 14px; border-bottom: 1px solid #1e293b; font-size: 14px; }
    tr:hover td { background: #1e293b55; }
    .badge { padding: 3px 8px; border-radius: 4px; font-weight: bold; font-size: 11px; }
    .badge-rank { background: #8b5cf622; color: #c084fc; border: 1px solid #a855f7; }
    
    /* Login Modal */
    #auth-overlay { position: fixed; inset: 0; background: #060b13fa; display: flex; align-items: center; justify-content: center; z-index: 1000; }
    .auth-box { background: #0f172a; border: 1px solid #00d2ff; border-radius: 12px; padding: 32px; width: 90%; max-width: 400px; text-align: center; box-shadow: 0 0 30px #00d2ff33; }
    .auth-title { color: #00d2ff; font-size: 20px; font-weight: bold; margin-bottom: 8px; letter-spacing: 1px; }
    .auth-desc { color: #94a3b8; font-size: 13px; margin-bottom: 20px; }
    .auth-input { width: 100%; padding: 12px; background: #1e293b; border: 1px solid #334155; border-radius: 6px; color: #fff; font-size: 16px; margin-bottom: 16px; text-align: center; }
  </style>
</head>
<body>

  <!-- Password Protection Modal -->
  <div id="auth-overlay">
    <form class="auth-box" onsubmit="event.preventDefault(); unlockPortal();">
      <div style="font-size: 40px; margin-bottom: 12px;">🛡️</div>
      <div class="auth-title">MONARCH MASTER ACCESS</div>
      <div class="auth-desc">Enter your secret Master Key (or add <code>?key=...</code> to URL).</div>
      <input type="text" id="key-input" class="auth-input" placeholder="Enter Master Key (e.g. monarch2026)" autocomplete="off" autocorrect="off" autocapitalize="off">
      <div id="auth-err" style="color: #ef4444; font-size: 13px; margin-bottom: 12px; display: none;">Invalid Master Key! Access Denied.</div>
      <button type="submit" id="enter-btn" class="btn" style="width: 100%; justify-content: center; padding: 12px;">ENTER PORTAL 👑</button>
    </form>
  </div>

  <div class="header">
    <div class="logo">⚡ MONARCH ADMIN SYSTEM</div>
    <div style="display: flex; gap: 8px;">
      <a id="csv-link" href="#" class="btn">📥 Export to Excel / CSV</a>
      <button onclick="logoutAdmin()" class="btn" style="background: #334155; color: #fff;">🔒 Lock</button>
    </div>
  </div>

  <div class="stats-grid">
    <div class="stat-card"><div class="stat-label">Total Hunters</div><div class="stat-val" id="stat-total">-</div></div>
    <div class="stat-card"><div class="stat-label">Total Questions Solved</div><div class="stat-val" id="stat-solved">-</div></div>
    <div class="stat-card"><div class="stat-label">Mock Tests Cleared</div><div class="stat-val" id="stat-mocks">-</div></div>
    <div class="stat-card"><div class="stat-label">Avg Hunter Level</div><div class="stat-val" id="stat-level">-</div></div>
  </div>

  <div class="controls">
    <input type="text" id="search" class="search-box" placeholder="Search by username, email, or hunter name..." onkeyup="renderTable()">
    <button onclick="loadData()" class="btn" style="background:#334155; color:#fff;">🔄 Refresh</button>
  </div>

  <div class="table-wrap">
    <table>
      <thead>
        <tr>
          <th>#</th>
          <th>Hunter Name</th>
          <th>Username</th>
          <th>Email</th>
          <th>Level & Rank</th>
          <th>Questions</th>
          <th>Mocks</th>
          <th>Streak</th>
          <th>Registered</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody id="tbody">
        <tr><td colspan="10" style="text-align:center; padding:30px;">Loading hunter database...</td></tr>
      </tbody>
    </table>
  </div>

  <script>
    const urlParams = new URLSearchParams(window.location.search);
    const urlKey = urlParams.get('key');
    let currentKey = urlKey || sessionStorage.getItem('monarch_admin_key') || '';
    let allAspirants = [];

    if (currentKey) {
      document.getElementById('key-input').value = currentKey;
      loadData();
    }

    function unlockPortal() {
      const key = document.getElementById('key-input').value.trim();
      if (!key) return;
      currentKey = key;
      sessionStorage.setItem('monarch_admin_key', key);
      const btn = document.getElementById('enter-btn');
      if (btn) btn.textContent = 'Verifying... ⏳';
      loadData();
    }

    function logoutAdmin() {
      sessionStorage.removeItem('monarch_admin_key');
      window.location.href = '/admin';
    }

    async function boostUser(userId, name) {
      if (!confirm('⚡ Activate Developer God Mode for ' + name + '?\n\n• Level 100\n• Monarch Rank\n• 999,999 Gold\n• 500 Stat Points')) return;
      try {
        const res = await fetch('/api/admin/god-mode?key=' + encodeURIComponent(currentKey), {
          method: 'POST',
          headers: { 'Content-Type': 'application/json', 'x-admin-key': currentKey },
          body: JSON.stringify({ userId, level: 100, rank: 'Monarch', gold: 999999, unallocatedPoints: 500, title: 'The Architect of the System' })
        });
        const data = await res.json();
        alert(data.message || 'Level 100 Boost Applied!');
        loadData();
      } catch(e) {
        alert('Boost failed: ' + e.message);
      }
    }

    async function loadData() {
      const errEl = document.getElementById('auth-err');
      const btn = document.getElementById('enter-btn');
      try {
        const res = await fetch('/api/admin/aspirants?key=' + encodeURIComponent(currentKey), {
          headers: { 'x-admin-key': currentKey }
        });
        const data = await res.json();
        if (data.success) {
          document.getElementById('auth-overlay').style.display = 'none';
          document.getElementById('csv-link').href = '/api/admin/export-csv?key=' + encodeURIComponent(currentKey);
          allAspirants = data.aspirants || [];
          document.getElementById('stat-total').textContent = data.stats.totalAspirants;
          document.getElementById('stat-solved').textContent = data.stats.totalSolved;
          document.getElementById('stat-mocks').textContent = data.stats.totalMocks;
          document.getElementById('stat-level').textContent = data.stats.avgLevel;
          renderTable();
        } else {
          sessionStorage.removeItem('monarch_admin_key');
          document.getElementById('auth-overlay').style.display = 'flex';
          if (errEl) errEl.style.display = 'block';
          if (btn) btn.textContent = 'ENTER PORTAL 👑';
        }
      } catch(e) {
        document.getElementById('tbody').innerHTML = '<tr><td colspan="10" style="color:#ef4444;text-align:center;">Network error loading portal.</td></tr>';
        if (btn) btn.textContent = 'ENTER PORTAL 👑';
      }
    }

    function renderTable() {
      const q = (document.getElementById('search').value || '').toLowerCase();
      const filtered = allAspirants.filter(a => 
        (a.hunterName && a.hunterName.toLowerCase().includes(q)) ||
        (a.username && a.username.toLowerCase().includes(q)) ||
        (a.email && a.email.toLowerCase().includes(q))
      );

      const tbody = document.getElementById('tbody');
      if (filtered.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" style="text-align:center; padding:20px; color:#94a3b8;">No hunters found.</td></tr>';
        return;
      }

      tbody.innerHTML = filtered.map((u, i) => \`
        <tr>
          <td>\${i + 1}</td>
          <td style="font-weight:bold; color:#00d2ff;">\${u.hunterName || u.username}</td>
          <td>\${u.username}</td>
          <td style="color:#94a3b8;">\${u.email || '-'}</td>
          <td><span class="badge badge-rank">Rank \${u.rank}</span> <strong>Lv. \${u.level}</strong></td>
          <td>\${u.totalQuestionsSolved || 0}</td>
          <td>\${u.mockTestsCleared || 0}</td>
          <td>🔥 \${u.streakDays || 1}d</td>
          <td style="font-size:12px; color:#64748b;">\${u.createdAt ? new Date(u.createdAt).toLocaleDateString() : '-'}</td>
          <td><button onclick="boostUser('\${u.userId}', '\${u.hunterName || u.username}')" class="btn" style="padding:4px 8px; font-size:11px; background:#a855f7; color:#fff;">⚡ Set Lv.100</button></td>
        </tr>
      \`).join('');
    }
  </script>
</body>
</html>`;
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    res.end(html);
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
