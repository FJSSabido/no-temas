const CACHE_VERSION = 'v1.0.0';
const STATIC_CACHE = `no-temas-static-${CACHE_VERSION}`;
const DYNAMIC_CACHE = `no-temas-dynamic-${CACHE_VERSION}`;

const STATIC_ASSETS = [
  '/',
  '/style.css',
  '/index.html',
  '/manifest.json'
];

/* INSTALACIÓN */
self.addEventListener('install', event => {
  self.skipWaiting();
  event.waitUntil(
    caches.open(STATIC_CACHE).then(cache => cache.addAll(STATIC_ASSETS))
  );
});

/* ACTIVACIÓN */
self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(keys =>
      Promise.all(
        keys
          .filter(key => !key.includes(CACHE_VERSION))
          .map(key => caches.delete(key))
      )
    )
  );
  self.clients.claim();
});

/* FETCH */
self.addEventListener('fetch', event => {
  const { request } = event;

  // Solo GET
  if (request.method !== 'GET') return;

  event.respondWith(
    caches.match(request).then(cached => {
      if (cached) return cached;

      return fetch(request)
        .then(response => {
          return caches.open(DYNAMIC_CACHE).then(cache => {
            cache.put(request, response.clone());
            return response;
          });
        })
        .catch(() => {
          // Fallback simple
          if (request.headers.get('accept')?.includes('text/html')) {
            return caches.match('/');
          }
        });
    })
  );
});
