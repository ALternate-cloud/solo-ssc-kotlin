// Service Worker for Solo Leveling SSC CGL PWA / Web APK
const CACHE_NAME = 'solo-leveling-ssc-v6';
const OFFLINE_URL = '/offline.html';

const ASSETS_TO_CACHE = [
  '/',
  '/index.html',
  '/offline.html',
  '/manifest.json',
  '/icons/icon-72.png',
  '/icons/icon-96.png',
  '/icons/icon-128.png',
  '/icons/icon-144.png',
  '/icons/icon-152.png',
  '/icons/icon-192.png',
  '/icons/icon-256.png',
  '/icons/icon-384.png',
  '/icons/icon-512.png',
  '/icons/maskable-icon-192.png',
  '/icons/maskable-icon-512.png',
  '/css/system-theme.css',
  '/css/layout.css',
  '/css/components.css',
  '/css/responsive.css',
  '/js/audio-engine.js',
  '/js/player-system.js',
  '/js/infinite-generator.js',
  '/js/pyq-papers.js',
  '/js/question-bank.js',
  '/js/online-sync.js',
  '/js/quests-engine.js',
  '/js/shadows-engine.js',
  '/js/dungeons-engine.js',
  '/js/shop-engine.js',
  '/js/auth-client.js',
  '/js/admin-portal.js',
  '/js/app.js'
];

// Install: Cache critical assets
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(async (cache) => {
      for (const url of ASSETS_TO_CACHE) {
        try {
          await cache.add(url);
        } catch (e) {
          // ignore non-critical asset cache errors
        }
      }
    })
  );
  self.skipWaiting();
});

// Activate: Clean up any old caches
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            return caches.delete(key);
          }
        })
      );
    })
  );
  self.clients.claim();
});

// Fetch: Always return valid Response, never throw or resolve null
self.addEventListener('fetch', (event) => {
  // Pass API requests directly to network
  if (event.request.url.includes('/api/')) {
    return;
  }

  // Only handle GET requests
  if (event.request.method !== 'GET') {
    return;
  }

  // Navigation requests (HTML page load)
  if (event.request.mode === 'navigate') {
    event.respondWith(
      fetch(event.request)
        .then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            const clone = networkResponse.clone();
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, clone));
          }
          return networkResponse;
        })
        .catch(async () => {
          const cached = await caches.match(event.request);
          if (cached) return cached;
          const indexCached = await caches.match('/index.html');
          if (indexCached) return indexCached;
          const offlineCached = await caches.match(OFFLINE_URL);
          if (offlineCached) return offlineCached;
          return new Response('<!DOCTYPE html><html><body><h2>Solo SSC Offline</h2><p>Reconnecting to System...</p></body></html>', {
            headers: { 'Content-Type': 'text/html; charset=utf-8' }
          });
        })
    );
    return;
  }

  // Static Assets (CSS, JS, Images): Cache First with background refresh
  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      if (cachedResponse) {
        fetch(event.request)
          .then((networkResponse) => {
            if (networkResponse && networkResponse.status === 200) {
              caches.open(CACHE_NAME).then((cache) => cache.put(event.request, networkResponse));
            }
          })
          .catch(() => {});
        return cachedResponse;
      }

      return fetch(event.request).catch(async () => {
        const offline = await caches.match(OFFLINE_URL);
        if (offline) return offline;
        return new Response('', { status: 404, statusText: 'Not Found' });
      });
    })
  );
});
