# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Interactive playground for Java design **and** microservice patterns. Each pattern ships a "good" and a "bad" implementation; the backend actually *runs* them in-process (real concurrent threads for Singleton; deterministic in-process simulations for the distributed/microservice patterns — fake downstreams, token buckets, compensations, etc.) and returns a structured execution report, which the React frontend renders as a pixel-art step animation.

Scope: **37 patterns** = 23 GoF design patterns (creational 5, structural 7, behavioral 11) + 14 microservice patterns (decomposition 3, communication 3, data 4, resilience 4).

The project is split into two independent apps:
- `backend/` — Spring Boot REST API (Java 17 target, compiles/runs on JDK 17–21, Maven)
- `frontend/` — React + Vite SPA (pixel-art UI)

The codebase is partly written in Russian (comments, UI strings, descriptions); follow the existing language when editing those.

## Commands

### Backend (`cd backend`, JDK 17–21 + Maven 3.6+)
- `mvn spring-boot:run` — start API on `http://localhost:8080`
- `mvn package` — build a runnable jar
- `mvn test` — run the JUnit 5 suite (one test class per pattern; ~164 tests). Tests are plain POJO unit tests (instantiate `new XDemoService()` etc., **no `@SpringBootTest`**) — fast and independent of the runtime JDK.

### Frontend (`cd frontend`)
- `npm install` — install dependencies
- `npm run dev` — dev server on `http://localhost:5173` (proxies `/api` → `:8080`)
- `npm run build` — production build
- `npm run preview` — serve the production build

No linter is configured. The catalog/detail pages work without the backend; the **Sandbox** ("Run") needs the backend up.

> Tip: if `mvn`/`node` aren't on your PATH, IntelliJ bundles Maven at
> `…/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn`; set `JAVA_HOME` to a JDK 17–21.

## Architecture

### Request flow
`pages/Sandbox.jsx` → `api/patterns.js` `run(id, flavor)` (reads the backend path from `DETAILS[id].backend`, `fetch /api/...`) → Vite proxy → Spring `*Controller` (`/good` | `/bad`) → `*DemoService` → returns a `PatternDemoResponse` → `sandbox/runner.js` `buildFor(viz, resp, flavor)` → `components/Diagrams.jsx` `renderViz(vizKind, …)` renders the animation.

### Backend (`com.patterns.demo`)
- `controller/` — thin REST controllers, **one per pattern**, each exposing `/good` and `/bad` under `/api/patterns/<group>/<pattern>` (design) or `/api/patterns/microservices/<category>/<pattern>` (microservices).
- `patterns/<group>/<pattern>/` and `patterns/microservices/<category>/<pattern>/` — the actual pattern code. Each folder holds `Good*` / `Bad*` implementations plus a `*DemoService` that exercises them and builds the response. The demo source shown to the user is a hardcoded `GOOD_CODE` / `BAD_CODE` string constant in the service — **not** the live class file.
  - groups: `creational`, `structural`, `behavioral`; microservice categories: `decomposition`, `communication`, `data`, `resilience`.
- `dto/PatternDemoResponse` — the single response contract for *all* demos: `pattern`, `variant`, `title`, `description`, `code`, `steps[]` (`{t, actor, action, result, ok}`), `instances[]` (`{hash, createdBy}`), `verdict`, `explanation`. **The frontend depends on this shape; keep it stable.**
- `controller/IndexController` — catalog metadata at `/api/patterns` (full mirror of implemented endpoints; see note below).
- CORS for the Vite dev server is configured in `PatternsDemoApplication`.
- Tests live in `src/test/java/...` mirroring the pattern package; one `*Test` per pattern, deterministic POJO unit tests.

### Frontend (`src/`)
- `data/patterns.js` — **the single source of truth for the catalog.** Exports `SECTIONS`, `CATEGORIES` (each category tagged with a `section`), `RAW`→`PATTERNS` (per-category pattern lists with `status`), `DETAILS` (rich per-pattern content keyed by pattern id), and helpers `getCategory` / `getSectionCategories` / `getSection` / `getCategoryPatterns` / `findPattern` / `findPatternAnywhere` / `getDetail`.
- `components/Diagrams.jsx` — all per-pattern visualization components **plus the `VIZ` registry** (`vizKind → render fn`) and `renderViz(kind, props)`. New viz components receive uniform props `{ result, frame, step, cmd }`; shared building blocks: `VizBox`, `VizStatus`, `stepTone`.
- `components/Pixel.jsx` — shared UI atoms; `CAT_COLOR` / `CAT_KIND` map category id → accent color / stripe kind (4 microservice categories use `--accent-4`).
- `pages/` — section-aware screens: `SectionPick` (pick design vs microservices), `Categories`, `PatternList`, `PatternDetail`, `Sandbox`, `ComingSoon`.
- `App.jsx` — routes generalized to `/patterns/:section/:category/:patternId[/sandbox]` (one set of pages serves both sections).
- `api/patterns.js` — `run()` resolves the backend path from `getDetail(id).backend`.
- `sandbox/runner.js` — `buildFor(viz, …)` picks a builder: bespoke `buildLiveRun` (singleton) / `buildChainRun` (chain), else generic `buildRun` (frames carry the raw `Step` fields + `reveal`).
- `styles/pixel.css` — global pixel-art theme; accent vars `--accent`, `--accent-2`, `--accent-3`, `--accent-4`.

## Key convention: the catalog lives in two places

Keep these in sync manually:
1. **`frontend/src/data/patterns.js`** — what the SPA actually renders (`RAW` entry + `status`, and the `DETAILS` entry).
2. **`backend/.../controller/IndexController.java`** — catalog metadata served at `/api/patterns`. The SPA does **not** consume it today; it's maintained as an honest mirror of the implemented endpoints.

`PatternDemoResponse` (the backend response contract) must stay stable — the whole frontend animation depends on its shape.

### Adding a new pattern
**Backend**
1. Create `patterns/<group>/<pattern>/` (or `patterns/microservices/<category>/<pattern>/`) with `Good*` / `Bad*` classes and a `*DemoService` (`runGood`/`runBad` build a `PatternDemoResponse` with `steps`/`instances`/`verdict`/`explanation` + `GOOD_CODE`/`BAD_CODE` constants).
2. Add a `*Controller` exposing `/good` and `/bad` under the matching `/api/patterns/...` path.
3. Add a deterministic `*Test` under `src/test/...` (plain `new`, no Spring context).
4. Add the pattern to `IndexController`.

**Frontend**
5. Add the pattern to `RAW` in `data/patterns.js` under its category and set `status: 'done'`.
6. Add a `DETAILS['<id>']` entry: `intent`, `motivation`, `pros`, `cons`, `useCases`, `related`, `code: { good, bad }`, `viz`, `live`, `backend` (the `/good|/bad` base path), and a static `preview: { instances, frame }`.
7. Add a viz component in `Diagrams.jsx` and register it in the `VIZ` map under the same `viz` key.

(Adding a whole new **section** also needs a `SECTIONS` entry and category rows tagged with that `section`; routing already handles any `:section` value.)
