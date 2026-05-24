/**
 * Тонкая обертка над REST API бэка. В dev-режиме Vite проксирует /api на :8080.
 */

const BASE = '/api';

async function get(url) {
  const res = await fetch(BASE + url);
  if (!res.ok) {
    throw new Error(`API ${url} вернул ${res.status}`);
  }
  return res.json();
}

export const api = {
  index: () => get('/patterns'),
  singletonGood: () => get('/patterns/creational/singleton/good'),
  singletonBad:  () => get('/patterns/creational/singleton/bad'),
  // Live demo run for a pattern variant. Maps a frontend pattern id to its
  // backend endpoint; patterns without a backend reject (Sandbox shows demo/empty).
  run: (patternId, flavor) => {
    const fl = flavor === 'bad' ? 'bad' : 'good';
    const ENDPOINTS = {
      singleton: '/patterns/creational/singleton',
      'chain-of-resp': '/patterns/behavioral/chain-of-responsibility',
    };
    const base = ENDPOINTS[patternId];
    if (base) return get(`${base}/${fl}`);
    return Promise.reject(new Error(`Бэкенд пока не умеет запускать "${patternId}"`));
  },
};
