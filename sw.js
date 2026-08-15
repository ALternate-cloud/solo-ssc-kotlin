// Service Worker for Solo Leveling SSC CGL PWA / Web APK
const CACHE_NAME = 'solo-leveling-ssc-v5';
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

// Install: Cache core assets safely without breaking if one fails
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(async (cache) => {
      for (const url of ASSETS_TO_CACHE) {
        try {
          await cache.add(url);
        } catch (err) {
          console.warn(`[SW] Cache skipped for: ${url}`, err);
        }
      }
    })
  );
  self.skipWaiting();
});

// Activate: Purge old cache versions immediately
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

// Fetch: Safe Network-First for Navigation, Stale-While-Revalidate for Assets
self.addEventListener('fetch', (event) => {
  // Always let API calls go directly to the server
  if (event.request.url.includes('/api/')) {
    return;
  }

  // Navigation requests: Network -> Cache -> Offline Fallback
  if (event.request.mode === 'navigate') {
    event.respondWith(
      fetch(event.request)
        .then((response) => {
          if (response && response.status === 200) {
            const responseClone = response.clone();
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, responseClone));
          }
          return response;
        })
        .catch(async () => {
          const cached = await caches.match(event.request);
          if (cached) return cached;
          const indexCached = await caches.match('/index.html');
          if (indexCached) return indexCached;
          return caches.match(OFFLINE_URL);
        })
    );
    return;
  }

  // Static Assets: Stale-While-Revalidate with Safe Fallback
  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      const fetchPromise = fetch(event.request)
        .then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200 && event.request.method === 'GET') {
            const responseClone = networkResponse.clone();
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, responseClone));
          }
          return networkResponse;
        })
        .catch(() => null);

      return cachedResponse || fetchPromise || caches.match(OFFLINE_URL);
    })
  );
});
