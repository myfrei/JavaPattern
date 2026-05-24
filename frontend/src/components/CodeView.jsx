import { PixelIcon } from './Pixel.jsx'

const KEYWORDS = new Set([
  'public', 'private', 'protected', 'final', 'static', 'class', 'void', 'if', 'else',
  'return', 'synchronized', 'volatile', 'new', 'this', 'null', 'while', 'for', 'try',
  'catch', 'throw', 'throws', 'enum', 'interface', 'extends', 'implements', 'package',
  'import', 'true', 'false', 'abstract', 'super', 'instanceof',
])
const TYPES = new Set([
  'String', 'int', 'long', 'boolean', 'Object', 'Thread', 'List', 'ArrayList', 'Map',
  'Integer', 'Subject', 'Observer', 'NewsFeed', 'EmailNotifier', 'PushNotifier',
  'GoodSingleton', 'BadSingleton', 'InterruptedException', 'EmailSvc', 'PushSvc', 'SmsSvc',
])

function escapeHtml(s) {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
}

// Returns highlighted HTML for a single line of Java.
export function highlightLine(raw) {
  const commentIdx = raw.indexOf('//')
  let codePart = raw
  let commentPart = ''
  if (commentIdx >= 0) {
    codePart = raw.slice(0, commentIdx)
    commentPart = raw.slice(commentIdx)
  }
  const tokens = codePart.split(/(\s+|[{}();.,<>])/)
  const rendered = tokens.map(tok => {
    if (KEYWORDS.has(tok)) return `<span class="kw">${escapeHtml(tok)}</span>`
    if (TYPES.has(tok)) return `<span class="ty">${escapeHtml(tok)}</span>`
    if (/^".*"$/.test(tok)) return `<span class="str">${escapeHtml(tok)}</span>`
    return escapeHtml(tok)
  }).join('')
  return commentPart
    ? rendered + `<span class="cm">${escapeHtml(commentPart)}</span>`
    : rendered
}

function highlight(code) {
  return (code || '').split('\n').map(highlightLine).join('\n')
}

// Code block with head bar (used on the detail screen).
export function CodeBlock({ code, fileName = 'Observer.java', lang = 'Java 17', height }) {
  return (
    <div className="code-frame" style={height ? { height } : undefined}>
      <div className="code-frame__head">
        <span className="row gap-6" style={{ alignItems: 'center' }}>
          <PixelIcon kind="code" size={12} /> {fileName}
        </span>
        <span>{lang}</span>
      </div>
      <div className="code-frame__body" dangerouslySetInnerHTML={{ __html: highlight(code) }} />
    </div>
  )
}

// Line-numbered code view with an active line (used in the sandbox).
export function CodeLines({ code, activeLine = -1 }) {
  const lines = (code || '').split('\n')
  return (
    <div className="code-frame__body f1 scroll-y" style={{ background: 'var(--paper)', border: 0, boxShadow: 'none' }}>
      {lines.map((ln, i) => (
        <div key={i} className={'code-line' + (i === activeLine ? ' is-active' : '')}>
          <span className="code-line__no">{i + 1}</span>
          <span dangerouslySetInnerHTML={{ __html: highlightLine(ln) || ' ' }} />
        </div>
      ))}
    </div>
  )
}
