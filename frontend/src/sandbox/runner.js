// Builds a unified "run result" the Sandbox can play back, from either the
// baked demo (Observer) or a live backend response (Singleton).
//
// RunResult = {
//   status: 'ok' | 'error',
//   durationMs, fileName, code,
//   vizKind: 'observer' | 'singleton',
//   instances: [{hash, createdBy}],   // singleton viz
//   broken: bool,
//   frames: [{ codeLine, viz, label }],  // viz = observer step | singleton reveal count
//   stdout: [{ line, c }],
// }

import { getDetail } from '../data/patterns.js'

const OBSERVER_BAD_CODE = `// NewsFeed создаёт и дёргает ВСЕ сервисы сам — жёсткая связь
public class NewsFeed {
  public void publish(String n) {
    new EmailSvc().send(n);
    new PushSvc().send(n);
    new SmsSvc().send(n);
  }
}`

const OBSERVER_BAD = {
  status: 'ok', durationMs: 86, fileName: 'NewsFeed.java', code: OBSERVER_BAD_CODE,
  vizKind: 'observer', instances: [], broken: true,
  frames: [
    { codeLine: 3, viz: 3, label: 'new EmailSvc().send(n)' },
    { codeLine: 4, viz: 4, label: 'new PushSvc().send(n)' },
    { codeLine: 5, viz: 5, label: 'new SmsSvc().send(n)' },
  ],
  stdout: [
    { line: 'POST /api/v1/run', c: 'var(--ink-3)' },
    { line: '  body: { pattern: "observer", flavor: "bad" }', c: 'var(--ink-3)' },
    { line: '→ 200 OK · 86ms', c: 'var(--good)' },
    { line: '', c: '' },
    { line: '[trace]', c: 'var(--ink-3)' },
    { line: '[Email] sent', c: '' },
    { line: '[Push]  sent', c: '' },
    { line: '[Sms]   sent', c: '' },
    { line: '  ❗ Subject ↔ конкретные сервисы (жёсткая связь)', c: 'var(--bad)' },
    { line: '', c: '' },
    { line: 'Process exited: 0', c: 'var(--ink-3)' },
  ],
}

export function buildObserverRun(flavor) {
  if (flavor === 'bad') return OBSERVER_BAD
  const demo = getDetail('observer').demo
  return {
    status: 'ok', durationMs: 42, fileName: 'NewsFeed.java', code: demo.runner,
    vizKind: 'observer', instances: [], broken: false,
    frames: demo.frames,
    stdout: demo.stdout,
  }
}

const clamp = (v, lo, hi) => Math.max(lo, Math.min(hi, v))

// Transform the live PatternDemoResponse (steps/instances/verdict) into a RunResult.
export function buildLiveRun(resp, flavor) {
  const steps = resp.steps || []
  const instances = resp.instances || []
  const n = Math.max(steps.length, 1)
  const broken = flavor === 'bad' && instances.length > 1
  const lastT = steps.length ? steps[steps.length - 1].t : 0

  const frames = steps.map((s, i) => ({
    codeLine: -1,
    viz: clamp(Math.ceil(((i + 1) / n) * instances.length), instances.length ? 1 : 0, instances.length),
    label: `${s.actor} · ${s.action} → ${s.result}`,
    ok: s.ok,
  }))

  const stdout = [
    { line: `POST /api/patterns/creational/singleton/${flavor}`, c: 'var(--ink-3)' },
    { line: `→ 200 OK · ${lastT}ms`, c: 'var(--good)' },
    { line: '', c: '' },
    { line: '[trace]', c: 'var(--ink-3)' },
    ...steps.map(s => ({
      line: `${s.actor}  ${s.action}  →  ${s.result}`,
      c: s.ok ? 'var(--good)' : 'var(--bad)',
    })),
    { line: '', c: '' },
    { line: resp.verdict || '', c: (resp.verdict || '').startsWith('PASS') ? 'var(--good)' : 'var(--bad)' },
    { line: resp.explanation || '', c: 'var(--ink-2)' },
  ]

  return {
    status: 'ok', durationMs: lastT, fileName: 'Config.java', code: resp.code || '',
    vizKind: 'singleton', instances, broken,
    frames: frames.length ? frames : [{ codeLine: -1, viz: instances.length, label: 'getInstance()' }],
    stdout,
  }
}

// Transform the live PatternDemoResponse into a chain RunResult.
export function buildChainRun(resp, flavor) {
  const handlers = (resp.instances || []).map(i => ({ name: i.hash, limit: i.createdBy }))
  const steps = resp.steps || []
  const lastT = steps.length ? steps[steps.length - 1].t : 0

  const frames = steps.map(s => {
    const idx = handlers.findIndex(h => h.name === s.actor)
    const res = s.result || ''
    const decision = /APPROVED|✓/.test(res) ? 'approve'
      : /REJECT|✗/.test(res) ? 'reject'
      : idx >= 0 ? 'pass' : 'enter'
    return { codeLine: -1, viz: idx, activeIndex: idx, decision, label: `${s.actor} · ${s.action} → ${s.result}` }
  })

  const stdout = [
    { line: `POST /api/patterns/behavioral/chain-of-responsibility/${flavor}`, c: 'var(--ink-3)' },
    { line: `→ 200 OK · ${lastT}ms`, c: 'var(--good)' },
    { line: '', c: '' },
    { line: '[trace]', c: 'var(--ink-3)' },
    ...steps.map(s => {
      const res = s.result || ''
      const c = /APPROVED|✓/.test(res) ? 'var(--good)' : /REJECT|✗/.test(res) ? 'var(--bad)' : ''
      return { line: `${s.actor}  ${s.action}  →  ${s.result}`, c }
    }),
    { line: '', c: '' },
    { line: resp.verdict || '', c: (resp.verdict || '').startsWith('PASS') ? 'var(--good)' : 'var(--bad)' },
    { line: resp.explanation || '', c: 'var(--ink-2)' },
  ]

  return {
    status: 'ok', durationMs: lastT, fileName: 'Approver.java', code: resp.code || '',
    vizKind: 'chain', handlers, broken: flavor === 'bad',
    frames: frames.length ? frames : [{ codeLine: -1, viz: -1, activeIndex: -1, decision: 'enter', label: '—' }],
    stdout,
  }
}

// Generic builder: patterns whose viz reads steps/instances uniformly.
// Each frame carries the raw Step fields so a bespoke viz can map step → highlight.
export function buildRun(resp, flavor, vizKind) {
  const steps = resp.steps || []
  const instances = resp.instances || []
  const lastT = steps.length ? steps[steps.length - 1].t : 0

  const frames = steps.map((s, i) => ({
    codeLine: -1, idx: i,
    actor: s.actor, action: s.action, result: s.result, ok: s.ok,
    reveal: i + 1,
    label: `${s.actor} · ${s.action} → ${s.result}`,
  }))

  const stdout = [
    { line: `GET /api${resp.__path || '/...'}/${flavor}`, c: 'var(--ink-3)' },
    { line: `→ 200 OK · ${lastT}ms`, c: 'var(--good)' },
    { line: '', c: '' },
    { line: '[trace]', c: 'var(--ink-3)' },
    ...steps.map(s => ({ line: `${s.actor}  ${s.action}  →  ${s.result}`, c: s.ok ? '' : 'var(--bad)' })),
    { line: '', c: '' },
    { line: resp.verdict || '', c: (resp.verdict || '').startsWith('PASS') ? 'var(--good)' : 'var(--bad)' },
    { line: resp.explanation || '', c: 'var(--ink-2)' },
  ]

  return {
    status: 'ok', durationMs: lastT,
    fileName: (resp.pattern || 'Pattern').replace(/[^A-Za-z]/g, '') + '.java',
    code: resp.code || '', vizKind, instances, broken: flavor === 'bad',
    frames: frames.length ? frames : [{ codeLine: -1, idx: 0, reveal: instances.length, label: 'run', ok: true }],
    stdout,
  }
}

// Picks the right builder for a viz kind (bespoke ones keep their own builder).
export function buildFor(vizKind, resp, flavor) {
  if (vizKind === 'chain') return buildChainRun(resp, flavor)
  if (vizKind === 'singleton') return buildLiveRun(resp, flavor)
  return buildRun(resp, flavor, vizKind)
}

export function errorRun(message, flavor, patternId) {
  return {
    status: 'error', durationMs: 0, fileName: `${patternId}.java`, code: '',
    vizKind: 'singleton', instances: [], broken: true, frames: [],
    stdout: [
      { line: `POST /api/.../${patternId}/${flavor}`, c: 'var(--ink-3)' },
      { line: '→ ошибка соединения', c: 'var(--bad)' },
      { line: '', c: '' },
      { line: message, c: 'var(--bad)' },
      { line: 'Проверь, что бэкенд поднят на http://localhost:8080', c: 'var(--ink-3)' },
    ],
  }
}
