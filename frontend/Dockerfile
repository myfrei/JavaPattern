# ── build stage ──────────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /app

# cache deps
COPY package.json package-lock.json ./
RUN npm ci

# build static assets
COPY . .
RUN npm run build

# ── runtime stage (nginx serves static + proxies /api) ───────
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
