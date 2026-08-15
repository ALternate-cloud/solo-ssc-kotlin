// Service Worker for Solo Leveling SSC CGL PWA / APK
const CACHE_NAME = 'solo-leveling-ssc-v4';
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
      return cache.addAll(ASSETS_TO_CACHE).catch((err) => console.warn('Cache warning:', err));
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
  // Always allow API calls to go directly to network
  if (event.request.url.includes('/api/')) {
    return;
  }

  // If navigating to a page, try network -> cache -> offline fallback
  if (event.request.mode === 'navigate') {
    event.respondWith(
      fetch(event.request).catch(() => {
        return caches.match(event.request).then((res) => res || caches.match(OFFLINE_URL));
      })
    );
    return;
  }

  event.respondWith(
    caches.match(event.request).then((cached) => {
      if (cached) {
        fetch(event.request).then((fresh) => {
          if (fresh && fresh.status === 200) {
            caches.open(CACHE_NAME).then((c) => c.put(event.request, fresh));
          }
        }).catch(() => {});
        return cached;
      }
      return fetch(event.request);
    }).catch(() => caches.match(OFFLINE_URL))
  );
});
