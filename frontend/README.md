# ODRL PAP Frontend

> :warning: This is currently just an experimental frontend, without any proper tests provided. DO NOT use it in production.

This is a React-based frontend for the ODRL Policy Administration Point (PAP). It provides a visual policy editor for creating ODRL policies through a guided, form-based interface.

## Prerequisites

- **Node.js** >= 18 and **npm** >= 9
- A running instance of the ODRL PAP backend (default: `http://localhost:8080`)

## Quick Start (Development Mode)

The development server provides hot module replacement (HMR) so that changes to
source files are reflected in the browser immediately, and a built-in proxy that
forwards API requests to the backend.

```bash
# 1. Install dependencies
cd frontend
npm install

# 2. Copy the environment template and adjust if needed
cp .env.example .env
#    Edit .env to set VITE_API_PROXY_TARGET to your backend URL (default: http://localhost:8080)

# 3. Start the Vite dev server
npm run dev
```

The editor opens at **http://localhost:5173**. API calls to `/mappings`,
`/policy`, and `/validate` are proxied to the backend specified by
`VITE_API_PROXY_TARGET` in `.env`.

### Starting the Backend

If you do not already have a backend running, start one from the repository root:

```bash
# From the repository root
./mvnw quarkus:dev
```

This starts the PAP backend on `http://localhost:8080` with live-reload.

## Preview Mode (Production Build Preview)

Preview mode builds the production bundle and serves it locally through Vite's
built-in static file server. Use this to verify the production build before
deploying.

```bash
# 1. Create the production build
npm run build

# 2. Serve the build output locally
npm run preview
```

The preview server starts at **http://localhost:4173** by default. Note that
preview mode does **not** proxy API requests, so the application will use the
`VITE_API_BASE_URL` value (set in `.env` or at build time) to reach the backend.

## Running Tests

The project uses [Vitest](https://vitest.dev/) with React Testing Library and
[MSW](https://mswjs.io/) (Mock Service Worker) for API mocking.

```bash
# Run all tests once
npm test

# Run tests in watch mode (re-runs on file changes)
npm run test:watch

# Run tests with code coverage report
npm run test:coverage

# Run tests with verbose output (useful in CI)
npm run test:ci
```

## Regenerating the API Client

If you make changes to the `api/odrl.yaml` OpenAPI spec, regenerate the
TypeScript API client:

```bash
npm run generate-api
```

## Configuration

Environment variables are defined in a `.env` file in the `frontend` directory.
Copy `.env.example` as a starting point.

| Variable | Purpose | Default |
|---|---|---|
| `VITE_API_PROXY_TARGET` | Backend URL used by the Vite dev-server proxy | `http://localhost:8080` |
| `VITE_API_BASE_URL` | Base URL for API calls in the production build | `/api` |

- **Development mode** uses `VITE_API_PROXY_TARGET` to proxy `/mappings`,
  `/policy`, and `/validate` requests to the backend.
- **Production / preview mode** uses `VITE_API_BASE_URL` as the base URL for
  API requests.

## Building for Production

To create a production build of the application, run:

```bash
npm run build
```

This creates a `dist` directory with the optimized static assets.

## Docker

A Dockerfile is provided to containerize the frontend application. The Docker
image uses Nginx to serve the static build and supports runtime configuration
of the API base URL via environment variables.

1.  **Build the Docker Image**
    ```bash
    docker build -t odrl-pap-frontend .
    ```

2.  **Run the Docker Container**
    ```bash
    docker run -p 8080:80 -e VITE_API_BASE_URL=https://api.example.com odrl-pap-frontend
    ```
    The frontend will be available at `http://localhost:8080`.
    
    The `VITE_API_BASE_URL` environment variable is injected at container startup
    via `envsubst`, so a single image can be reused across environments without
    rebuilding.