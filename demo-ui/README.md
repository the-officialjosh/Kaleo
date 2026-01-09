# Kaleo Demo UI

React frontend for the Kaleo church event management platform.

## Tech Stack

- React 19 + TypeScript
- Vite 6
- Tailwind CSS 4
- React Router 7
- Radix UI components
- react-oidc-context (Keycloak auth)

## Quick Start

```bash
npm install
npm run dev
```

Runs at http://localhost:5173

## Environment Variables

Optional overrides (create `.env.local`):

```env
VITE_KEYCLOAK_URL=http://localhost:9095/realms/kaleo-events
VITE_OIDC_CLIENT_ID=kaleo-event-app
```

## Build for Production

```bash
npm run build
```

Output in `dist/` folder, served by Nginx in Docker deployment.

## Project Structure

```
src/
├── components/     # Reusable UI components
├── pages/          # Route pages
├── lib/            # Utilities
└── main.tsx        # App entry with OIDC config
```
