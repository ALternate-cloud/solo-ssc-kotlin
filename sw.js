// Service Worker for Solo Leveling SSC CGL PWA / APK
const CACHE_NAME = 'solo-leveling-ssc-v2';
const ASSETS_TO_CACHE = [
  '/',
  '/index.html',
  '/manifest.json',
  '/icons/icon-192.png',
  '/icons/icon-512.png',
  '/icons/maskable-icon-512.png',
  '/icons/screenshot-desktop.png',
  '/icons/screenshot-mobile.png',
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

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(ASSETS_TO_CACHE).catch((err) => console.warn('Cache addAll warning:', err));
    })
  );
  self.skipWaiting();
});

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

self.addEventListener('fetch', (event) => {
  // Always fetch dynamic API endpoints from network
  if (event.request.url.includes('/api/')) {
    return;
  }

  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      if (cachedResponse) {
        // Fetch in background to update cache
        fetch(event.request).then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, networkResponse));
          }
        }).catch(() => {});
        return cachedResponse;
      }
      return fetch(event.request);
    }).catch(() => fetch(event.request))
  );
});
