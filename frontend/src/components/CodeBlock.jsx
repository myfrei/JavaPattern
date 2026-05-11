/**
 * Очень простая «подсветка» Java-кода через regex.
 * Подключать highlight.js ради демки — оверкилл.
 */
const KEYWORDS = new Set([
  'public','private','protected','final','static','class','void','if','else',
  'return','synchronized','volatile','new','this','null','while','for','try',
  'catch','throw','throws','enum','interface','extends','implements','package','import',
  'true','false'
])
const TYPES = new Set([
  'String','int','long','boolean','GoodSingleton','BadSingleton','Object','Thread',
  'InterruptedException'
])

function escape(s) {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
}

function highlight(code) {
  const out = []
  // комменты — построчно
  const lines = code.split('\n')
  for (const raw of lines) {
    const commentIdx = raw.indexOf('//')
    let codePart = raw, commentPart = ''
    if (commentIdx >= 0) {
      codePart = raw.slice(0, commentIdx)
      commentPart = raw.slice(commentIdx)
    }
    const tokens = codePart.split(/(\s+|[{}();.,])/)
    const rendered = tokens.map(tok => {
      if (KEYWORDS.has(tok)) return `<span class="kw">${escape(tok)}</span>`
      if (TYPES.has(tok))    return `<span class="typ">${escape(tok)}</span>`
      if (/^".*"$/.test(tok)) return `<span class="str">${escape(tok)}</span>`
      return escape(tok)
    }).join('')
    const finalLine = commentPart
      ? rendered + `<span class="cmt">${escape(commentPart)}</span>`
      : rendered
    out.push(finalLine)
  }
  return out.join('\n')
}

export default function CodeBlock({ code }) {
  return <pre className="code" dangerouslySetInnerHTML={{ __html: highlight(code || '') }} />
}
