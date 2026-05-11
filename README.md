# Java Patterns Playground

Интерактивная демо-площадка паттернов с пиксельным фронтом (React) и Java-бэкендом (Spring Boot).

## Структура

```
.
├── backend/    # Spring Boot, REST API с good/bad реализациями паттернов
└── frontend/   # React + Vite, пиксельный UI
```

## Разделы

- **Паттерны проектирования**
  - Поведенческие
  - Структурные
  - Порождающие  ← *стартуем тут (Singleton)*
- **Паттерны микросервисной архитектуры**

## Запуск

### Backend

Требуется JDK 17+ и Maven 3.6+.

```bash
cd backend
mvn spring-boot:run
```

API поднимется на `http://localhost:8080`.

Быстрая проверка:
```bash
curl http://localhost:8080/api/patterns/creational/singleton/good
curl http://localhost:8080/api/patterns/creational/singleton/bad
```

Эндпоинты Singleton:
- `GET /api/patterns/creational/singleton/good`
- `GET /api/patterns/creational/singleton/bad`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

UI на `http://localhost:5173`. Vite проксирует `/api` на бэкенд.

## Формат ответа бэкенда

```json
{
  "pattern": "Singleton",
  "variant": "good" | "bad",
  "title": "...",
  "description": "...",
  "code": "...",          // исходник Java
  "steps": [              // визуализированный ход выполнения
    { "actor": "Thread-1", "action": "getInstance()", "result": "hash=...", "ok": true }
  ],
  "instances": [          // сколько объектов в итоге создано
    { "hash": "...", "createdBy": "Thread-1" }
  ],
  "verdict": "...",
  "explanation": "..."
}
```

Бэк возвращает результат, фронт рисует пиксельную анимацию шагов.
