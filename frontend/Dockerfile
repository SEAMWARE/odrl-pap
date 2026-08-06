# Stage 1: Build the React app
FROM node:18-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .
RUN npx vite build

# Stage 2: Serve the static build artifacts with runtime env injection
FROM nginx:1.21.6-alpine

# Copy the nginx config as a template — envsubst replaces $PAP_BACKEND_URL
# at container startup so the same image works in any environment.
COPY nginx.conf /etc/nginx/templates/default.conf.template

COPY --from=build /app/dist /usr/share/nginx/html

# The env-config.js template uses shell variable syntax ($VITE_API_BASE_URL)
# that envsubst replaces at container startup. The original template is kept
# so the image can be re-started with different values.
COPY --from=build /app/dist/env-config.js /usr/share/nginx/html/env-config.js.template

EXPOSE 80

# Health check using the /healthz endpoint defined in nginx.conf
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget -qO- http://localhost/healthz || exit 1

# At startup:
# 1. Substitute $PAP_BACKEND_URL into the nginx config template
#    (only this variable is listed so nginx's own $uri/$host/etc. are preserved)
# 2. Substitute $VITE_API_BASE_URL into env-config.js for the rare case where
#    the frontend needs to call a different origin than the proxy
# 3. Start nginx
CMD ["/bin/sh", "-c", "envsubst '$PAP_BACKEND_URL' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf && envsubst '$VITE_API_BASE_URL' < /usr/share/nginx/html/env-config.js.template > /usr/share/nginx/html/env-config.js && nginx -g 'daemon off;'"]
