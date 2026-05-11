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
};
