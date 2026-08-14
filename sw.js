// Service Worker for Solo Leveling SSC CGL PWA / APK
const CACHE_NAME = 'solo-leveling-ssc-v1';
const ASSETS_TO_CACHE = [
  './',
  './index.html',
  './manifest.json',
  './css/system-theme.css',
  './css/layout.css',
  './css/components.css',
  './css/responsive.css',
  './js/audio-engine.js',
  './js/player-system.js',
  './js/infinite-generator.js',
  './js/pyq-papers.js',
  './js/question-bank.js',
  './js/online-sync.js',
  './js/quests-engine.js',
  './js/shadows-engine.js',
  './js/dungeons-engine.js',
  './js/shop-engine.js',
  './js/auth-client.js',
  './js/admin-portal.js',
  './js/app.js'
];

self.addEventListener('install', (e) => {
  e.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS_TO_CACHE))
  );
  self.skipWaiting();
});

self.addEventListener('activate', (e) => {
  e.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE_NAME).map((k) => caches.delete(k)))
    )
  );
  self.clients.claim();
});

self.addEventListener('fetch', (e) => {
  if (e.request.url.includes('/api/')) {
    return; // Pass dynamic API calls straight to network
  }
  e.respondWith(
    caches.match(e.request).then((res) => res || fetch(e.request))
  );
});
