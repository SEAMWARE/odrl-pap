# Stage 1: Build the React app
FROM node:18-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .
RUN npx vite build

# Stage 2: Serve the static build artifacts with runtime env injection
FROM nginx:1.21.6-alpine

COPY --from=build /app/dist /usr/share/nginx/html

# The env-config.js template uses shell variable syntax ($VITE_API_BASE_URL)
# that envsubst replaces at container startup. The original template is kept
# so the image can be re-started with different values.
COPY --from=build /app/dist/env-config.js /usr/share/nginx/html/env-config.js.template

EXPOSE 80

# At startup, substitute environment variables into env-config.js, then start nginx.
# Only the explicitly listed variables are substituted to avoid corrupting other
# dollar-sign references in static assets.
CMD ["/bin/sh", "-c", "envsubst '$VITE_API_BASE_URL' < /usr/share/nginx/html/env-config.js.template > /usr/share/nginx/html/env-config.js && nginx -g 'daemon off;'"]
