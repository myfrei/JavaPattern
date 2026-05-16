# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Interactive playground for Java design / microservice patterns. Each pattern ships a "good" and "bad" implementation; the backend actually *runs* them (e.g. spawns concurrent threads) and returns a structured execution report, which the React frontend renders as a pixel-art step animation.

The project is split into two independent apps:
- `backend/` — Spring Boot REST API (Java 17, Maven)
- `frontend/` — React + Vite SPA (pixel-art UI)

The codebase is partly written in Russian (comments, UI strings, descriptions); follow the existing language when editing those.

## Commands

### Backend (`cd backend`, needs JDK 17+ and Maven 3.6+)
- `mvn spring-boot:run` — start API on `http://localhost:8080`
- `mvn package` — build a runnable jar
- `mvn test` — run tests (no test sources exist yet)

### Frontend (`cd frontend`)
- `npm install` — install dependencies
- `npm run dev` — dev server on `http://localhost:5173` (proxies `/api` → `:8080`)
- `npm run build` — production build
- `npm run preview` — serve the production build

There is no linter configured. Both apps must run for the UI to work.

## Architecture

### Request flow
`SingletonPage.jsx` → `api/patterns.js` (`fetch /api/...`) → Vite proxy → Spring `*Controller` → `*DemoService` → returns a `PatternDemoResponse` → frontend renders `steps` / `instances` as animation.

### Backend (`com.patterns.demo`)
- `controller/` — thin REST controllers, one per pattern, mapped under `/api/patterns/<group>/<pattern>`. `IndexController` serves catalog metadata at `/api/patterns`.
- `patterns/<group>/<pattern>/` — the actual pattern code. Each pattern folder holds the Good/Bad implementations plus a `*DemoService` that exercises them and builds the response. The demo source shown to the user is a hardcoded `GOOD_CODE`/`BAD_CODE` string constant in the service — not the live class file.
- `dto/PatternDemoResponse` — the single response contract for *all* pattern demos: `title`, `description`, `code`, `steps[]`, `instances[]`, `verdict`, `explanation`. The frontend depends on this shape; keep it stable.
- CORS for the Vite dev server (`localhost:5173`) is configured in `PatternsDemoApplication`.

### Frontend (`src/`)
- `pages/` — one page per pattern (`SingletonPage`) plus `Home`, `GroupPage`, `MicroservicesPage`, `ComingSoonPage`.
- `components/Visualization.jsx` — shared renderers (`StepTimeline`, `InstanceGrid`, `Verdict`) driven by a `PatternDemoResponse`.
- Routing is in `App.jsx`; each ready pattern needs an explicit `<Route>`. Unmatched paths fall through to `ComingSoonPage`.
- `styles/pixel.css` — global pixel-art theme.

## Key convention: the pattern catalog is duplicated

The list of patterns / which are `ready` lives in **three** hardcoded places that must be kept in sync manually:
1. `backend/.../controller/IndexController.java`
2. `frontend/src/components/Sidebar.jsx` (`MENU`)
3. `frontend/src/pages/GroupPage.jsx` (`GROUPS`)

### Adding a new pattern
1. Backend: create `patterns/<group>/<pattern>/` with Good/Bad classes + a `*DemoService`, and a matching `*Controller` exposing `/good` and `/bad`.
2. Frontend: add a page under `pages/`, an API method in `api/patterns.js`, an explicit `<Route>` in `App.jsx`.
3. Flip `ready: true` for the pattern in `Sidebar.jsx` and `GroupPage.jsx`, and update `IndexController`.
