# Kaleo

A modern event management platform for creating, managing, and attending events. Built with React, TypeScript, Vite, and Three.js, featuring stunning 3D animations and a seamless user experience.

## Features

### For Attendees

- **Browse Published Programs** - Discover upcoming events with beautiful card layouts
- **Purchase Passes** - Secure pass purchase with Keycloak authentication
- **Digital Passes** - View your passes with QR codes for venue entry
- **Manual Validation Codes** - Alternative validation method using 6-character codes
- **Pass Management** - Track your purchased passes with detailed information

### For Organizers

- **Program Management** - Create, edit, and manage events (Draft, Published, Cancelled, Completed)
- **Pass Type Configuration** - Define multiple pass types with pricing and availability limits
- **Pass Validation** - QR code scanner for venue entry validation
- **Dashboard Analytics** - Monitor programs and pass sales

### Technical Highlights

- **3D Landing Pages** - Interactive Three.js animations with particle effects
- **Responsive Design** - Mobile-first approach with Tailwind CSS
- **Authentication** - Keycloak integration with OIDC
- **Type Safety** - Full TypeScript coverage
- **Modern Stack** - React 19, React Router 7, Vite 6
- **API Integration** - REST API with Spring Boot pagination support
- **Docker Ready** - Containerized deployment with dynamic configuration

## Tech Stack

### Frontend

- **React 19** - Latest React with hooks and suspense
- **TypeScript** - Full type safety
- **Vite 6** - Lightning-fast build tool
- **React Router 7** - Client-side routing
- **Tailwind CSS 4** - Utility-first styling
- **Three.js** - 3D graphics and animations
- **React Three Fiber** - React renderer for Three.js
- **Framer Motion** - Animation library
- **GSAP** - Advanced animations
- **Radix UI** - Accessible component primitives
- **Lucide React** - Icon library

### Authentication & Security

- **Keycloak** - Identity and access management
- **OIDC** - OpenID Connect protocol
- **JWT** - Token-based authentication

### Development Tools

- **ESLint** - Code linting
- **Prettier** - Code formatting
- **TypeScript ESLint** - TypeScript-specific linting

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- A running Keycloak instance (or use the backend's Docker setup)
- Backend API running (Spring Boot)

### Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd demo-ui
   ```

2. **Install dependencies**

   ```bash
   npm install
   ```

3. **Configure environment variables**

   Create a `.env` file in the root directory:

   ```env
   VITE_KEYCLOAK_URL=http://localhost:9095/realms/kaleo-events
   VITE_OIDC_CLIENT_ID=kaleo-event-app
   ```

4. **Start the development server**

   ```bash
   npm run dev
   ```

   The app will be available at `http://localhost:5173`

### Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run format` - Format code with Prettier
- `npm run mocks` - Start JSON server for API mocking

## Configuration

### API Proxy

The app is configured to proxy `/api` requests to the backend. Update `vite.config.ts` to change the backend URL:

```typescript
server: {
  proxy: {
    "/api": {
      target: "http://localhost:8080", // Your backend URL
      changeOrigin: true,
    },
  },
}
```

### OIDC Configuration

OIDC configuration dynamically uses `window.location.origin` for redirect URIs, making it work seamlessly in:

- Local development (`localhost:5173`)
- Docker containers
- Production deployments

This is configured in `src/main.tsx`:

```typescript
const oidcConfig = {
  authority: import.meta.env.VITE_KEYCLOAK_URL,
  client_id: import.meta.env.VITE_OIDC_CLIENT_ID,
  redirect_uri: `${window.location.origin}/callback`,
  post_logout_redirect_uri: window.location.origin,
};
```

## Docker Deployment

The application works in containerized environments. When deployed with Docker:

1. **Environment variables** are passed through Docker configuration
2. **Redirect URIs** automatically use the container's host
3. **API proxying** routes requests to the backend container
4. **Static assets** are served efficiently

Make sure to configure Keycloak with the correct redirect URIs for your Docker setup.

## Project Structure

```
src/
├── components/          # React components
│   ├── common/         # Shared components (Nav, Footer, Pagination)
│   ├── errors/         # Error boundaries and pages
│   ├── landing/        # Landing page sections
│   ├── programs/       # Program-related components
│   └── ui/             # Radix UI components
├── pages/              # Page components
│   ├── auth/           # Authentication pages
│   ├── dashboard/      # Dashboard pages (organizers)
│   └── public/         # Public pages (attendees)
├── domain/             # TypeScript types and interfaces
├── hooks/              # Custom React hooks
├── lib/                # Utility functions and API client
├── shaders/            # GLSL shaders for 3D effects
├── assets/             # Static assets
└── main.tsx            # App entry point
```

## API Integration

The app integrates with a Spring Boot backend. Key features:

### Pagination Support

The app handles Spring Boot pagination responses in both formats:

- Standard flat format (most endpoints)
- Nested `page` object format (passes endpoint)

### Authentication

All API requests include JWT bearer tokens from Keycloak.

### Endpoints

- `GET /api/v1/programs` - List programs (organizers)
- `GET /api/v1/published-programs` - List published programs (public)
- `GET /api/v1/programs/:id` - Get program details
- `POST /api/v1/programs` - Create program
- `PUT /api/v1/programs/:id` - Update program
- `DELETE /api/v1/programs/:id` - Delete program
- `GET /api/v1/passes` - List user passes
- `GET /api/v1/passes/:id` - Get pass details
- `GET /api/v1/passes/:id/qr` - Get pass QR code
- `POST /api/v1/passes/validate` - Validate pass

## Features in Detail

### Pass System

- **QR Code Generation** - Each pass gets a unique QR code for scanning
- **Manual Codes** - 6-character alphanumeric codes (e.g., "8C8ADA") for manual validation
- **Pass Status** - ACTIVE, PURCHASED, USED, CANCELLED
- **Pass Types** - Multiple ticket tiers per event with pricing and limits

### Program Management

- **Status Workflow** - Draft → Published → Completed/Cancelled
- **Registration Windows** - Optional start/end dates for registration
- **Venue Information** - Detailed location details
- **Pass Type Configuration** - Flexible pricing and availability

### 3D Graphics

- **Particle Systems** - Custom GLSL shaders for particle effects
- **Scroll Animations** - GSAP-powered smooth scrolling
- **Custom Cursor** - Interactive cursor with particle trail
- **Background Scenes** - Three.js backgrounds with post-processing

## Browser Support

- Chrome/Edge (latest)
- Firefox (latest)
- Safari (latest)

WebGL 2.0 required for 3D features.

## Contributing

1. Follow the existing code style
2. Run `npm run format` before committing
3. Ensure `npm run lint` passes
4. Test in multiple browsers

## License

See LICENSE file for details.
