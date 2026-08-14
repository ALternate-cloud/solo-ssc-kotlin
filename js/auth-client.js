/**
 * SOLO LEVELING EXAM SYSTEM - FRONTEND AUTHENTICATION & CLOUD SYNC ENGINE
 */

class AuthClientEngine {
  constructor() {
    this.token = localStorage.getItem('solo_system_jwt_token') || null;
    this.currentUser = this.loadSavedUser();
    this.isSyncing = false;
    const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
    const isAppOrigin = window.location.origin.startsWith('file:') || window.location.origin.startsWith('capacitor:') || window.location.origin.startsWith('ionic:') || window.location.origin.startsWith('http://localhost');
    this.apiBase = (!isLocalhost && window.location.origin.includes('onrender.com')) ? window.location.origin : 'https://solo-leveling-ssc.onrender.com';

    // Check session on load
    if (this.token) {
      this.verifySession();
    }
  }

  loadSavedUser() {
    try {
      const saved = localStorage.getItem('solo_system_current_user');
      return saved ? JSON.parse(saved) : null;
    } catch (e) {
      return null;
    }
  }

  saveSession(token, user) {
    this.token = token;
    this.currentUser = user;
    localStorage.setItem('solo_system_jwt_token', token);
    localStorage.setItem('solo_system_current_user', JSON.stringify(user));
    this.notifyAuthChange();
    this.pullCloudSync(); // Immediately fetch cloud state
  }

  clearSession() {
    this.token = null;
    this.currentUser = null;
    localStorage.removeItem('solo_system_jwt_token');
    localStorage.removeItem('solo_system_current_user');
    this.notifyAuthChange();
  }

  async verifySession() {
    try {
      const res = await fetch(`${this.apiBase}/api/auth/me`, {
        headers: { 'Authorization': `Bearer ${this.token}` }
      });
      if (res.ok) {
        const data = await res.json();
        if (data.success && data.user) {
          this.currentUser = data.user;
          localStorage.setItem('solo_system_current_user', JSON.stringify(data.user));
          this.notifyAuthChange();
        }
      } else {
        // Token expired
        this.clearSession();
      }
    } catch (err) {
      console.warn('Backend server verification offline or unreachable. Using cached session.', err);
    }
  }

  // 1. REGISTER
  async register(username, password, hunterName) {
    try {
      const res = await fetch(`${this.apiBase}/api/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, hunterName })
      });

      const data = await res.json();
      if (data.success && data.token) {
        this.saveSession(data.token, data.user);
        return { success: true, message: data.message };
      } else {
        return { success: false, message: data.message || 'Registration failed.' };
      }
    } catch (err) {
      return { success: false, message: 'Server connection failed. Is the backend server running?' };
    }
  }

  // 2. LOGIN
  async login(username, password) {
    try {
      const res = await fetch(`${this.apiBase}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });

      const data = await res.json();
      if (data.success && data.token) {
        this.saveSession(data.token, data.user);
        return { success: true, message: data.message };
      } else {
        return { success: false, message: data.message || 'Login failed.' };
      }
    } catch (err) {
      return { success: false, message: 'Server connection failed. Is the backend server running?' };
    }
  }

  // 3. LOGOUT
  logout() {
    this.clearSession();
  }

  // 4. PULL FROM CLOUD (Load saved progress from backend DB)
  async pullCloudSync() {
    if (!this.token) return;
    try {
      const res = await fetch(`${this.apiBase}/api/player/sync`, {
        headers: { 'Authorization': `Bearer ${this.token}` }
      });
      if (res.ok) {
        const result = await res.json();
        if (result.success && result.data) {
          const { player, quests, shadows } = result.data;

          if (player && window.Player) {
            window.Player.data = Object.assign({}, window.Player.data, player);
            window.Player.saveState();
          }
          if (quests && window.Quests) {
            window.Quests.data = Object.assign({}, window.Quests.data, quests);
            window.Quests.saveQuests();
          }
          if (shadows && window.Shadows) {
            window.Shadows.data = Object.assign({}, window.Shadows.data, shadows);
            window.Shadows.saveShadowState();
          }
          console.log('✓ Cloud progress pulled from SQLite database.');
        }
      }
    } catch (err) {
      console.warn('Cloud sync pull failed (working offline).', err);
    }
  }

  // 5. PUSH TO CLOUD (Save current client progress to backend DB)
  async pushCloudSync() {
    if (!this.token || this.isSyncing) return;
    this.isSyncing = true;

    try {
      const payload = {
        player: window.Player ? window.Player.data : null,
        quests: window.Quests ? window.Quests.data : null,
        shadows: window.Shadows ? window.Shadows.data : null
      };

      const res = await fetch(`${this.apiBase}/api/player/sync`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${this.token}`
        },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        console.log('⚡ Progress synced to Cloud Database.');
      }
    } catch (err) {
      console.warn('Cloud sync push failed (cached locally).', err);
    } finally {
      this.isSyncing = false;
    }
  }

  // 6. FETCH LEADERBOARD
  async fetchLeaderboard() {
    try {
      const res = await fetch(`${this.apiBase}/api/leaderboard`);
      if (res.ok) {
        const data = await res.json();
        return data.success ? data.leaderboard : [];
      }
    } catch (err) {
      console.warn('Failed to fetch leaderboard from server:', err);
    }
    return [];
  }

  notifyAuthChange() {
    window.dispatchEvent(new CustomEvent('auth-state-changed', {
      detail: { isLoggedIn: Boolean(this.token), user: this.currentUser }
    }));
  }
}

window.Auth = new AuthClientEngine();

// Auto-sync hooks on player/quest/shadow events
window.addEventListener('player-updated', () => {
  if (window.Auth && window.Auth.token) {
    window.Auth.pushCloudSync();
  }
});
window.addEventListener('quests-updated', () => {
  if (window.Auth && window.Auth.token) {
    window.Auth.pushCloudSync();
  }
});
window.addEventListener('shadows-updated', () => {
  if (window.Auth && window.Auth.token) {
    window.Auth.pushCloudSync();
  }
});
