# Kaleo

### Church & Ministry Event Management Platform

[![CI/CD](https://img.shields.io/github/actions/workflow/status/the-officialjosh/kaleo/ci.yml?style=flat-square&logo=github&label=CI%2FCD)](../../actions)
[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Keycloak](https://img.shields.io/badge/Keycloak-26.4.7-4D4D4D?style=flat-square&logo=keycloak&logoColor=white)](https://www.keycloak.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)

<div align="center">

<img src="docs/images/kaleo-banner.png" alt="Kaleo Banner" width="100%" />

A secure REST API for church and ministry event management. Organizers create programs, participants register for limited-capacity events, and staff validate attendance using QR-coded passes.

[Features](#-what-it-does) • [Quick Start](#-quick-start) • [Docker Deploy](#-docker-deployment) • [API Docs](#-api-documentation)

---

</div>

## ✨ What It Does

- 📋 **Program Management**: Create and manage church programs/events with role-based access
- 🎟️ **Pass System**: Capacity-controlled registration with automatic pass generation
- 📱 **QR Check-in**: Generate unique QR codes and manual codes for event check-in
- ✅ **Validation**: Staff-managed check-in with duplicate prevention
- 🔐 **Security**: JWT-based authentication with Keycloak OAuth2/OIDC integration
- 📖 **Swagger UI**: Interactive API documentation with OAuth2 authorization

---

## 🛠️ Tech Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Backend** | Spring Boot 4.0.1 + Java 25 | REST API with Data JPA, Validation |
| **Database** | PostgreSQL 16 | Relational database |
| **Auth** | Keycloak 26.4.7 | OAuth2/OIDC authentication & RBAC |
| **Frontend** | React 19 + Vite + TypeScript | Demo UI |
| **API Docs** | springdoc-openapi | Swagger UI with OAuth2 |
| **Container** | Docker + Nginx | Production deployment |

---

## 🚀 Quick Start

### Development Mode

**Prerequisites:** Java 25, Node.js 20+, Docker

```bash
# Clone repository
git clone https://github.com/the-officialjosh/kaleo.git
cd kaleo

# Start dependencies (PostgreSQL + Keycloak)
docker compose up db keycloak -d

# Run API
./mvnw spring-boot:run

# Run frontend (in another terminal)
cd demo-ui && npm install && npm run dev
```

| Service | URL |
|---------|-----|
| API | http://localhost:8080 |
| Frontend | http://localhost:5173 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Keycloak | http://localhost:9095 |

---

## 🐳 Docker Deployment

### Full Stack with One Command

```bash
# Build API container
./mvnw -DskipTests spring-boot:build-image \
  -Dspring-boot.build-image.imageName=the-officialjosh/kaleo-api:latest

# Build frontend
cd demo-ui && npm run build && cd ..

# Start all services
docker compose up -d
```

### Services

| Service | Port | Description |
|---------|------|-------------|
| `frontend` | 80 | React app via Nginx |
| `api` | 8081 | Spring Boot API |
| `keycloak` | 9095 | Auth server (auto-imports realm) |
| `db` | 5432 | PostgreSQL |
| `adminer` | 8888 | Database admin UI |

### URLs (Docker mode)

| URL | Description |
|-----|-------------|
| http://localhost | Landing page |
| http://localhost/swagger-ui/index.html | Swagger via Nginx |
| http://localhost:8081/swagger-ui/index.html | Swagger direct |
| http://localhost/api/v1/published-programs | Public API (no auth) |

---

## 📖 API Documentation

### Swagger UI with OAuth2

Access Swagger UI at `/swagger-ui/index.html` and click **Authorize** to login via Keycloak.

### Public Endpoints (No Auth)

```
GET /api/v1/published-programs         # List published programs
GET /api/v1/published-programs/{id}    # Get program details
```

### Protected Endpoints

| Endpoint | Role Required |
|----------|---------------|
| `/api/v1/programs` | `ROLE_ORGANIZER` |
| `/api/v1/pass-validations/**` | `ROLE_STAFF` |
| All other endpoints | Authenticated |

---

## 🔐 Keycloak Setup

The realm is auto-imported on startup. The following roles are available:

| Role | Permissions |
|------|-------------|
| `ROLE_ORGANIZER` | Create/manage programs and pass types |
| `ROLE_STAFF` | Validate passes at events |
| `ROLE_ATTENDEE` | Register for events, view passes |

### Required Client Redirect URIs

```
http://localhost/callback
http://localhost:5173/callback
http://localhost/swagger-ui/oauth2-redirect.html
http://localhost:8081/swagger-ui/oauth2-redirect.html
```

---

## 🧪 Running Tests

```bash
./mvnw test                           # Run all tests
./mvnw test -Dtest=PassMapperTest     # Run specific test
./mvnw verify jacoco:report           # Run with coverage
```

**Test Coverage:** 22 test classes with 225+ tests covering API, Services, Mappers, Security, and Configuration.

---

## 📁 Project Structure

```
kaleo/
├── src/main/java/              # Spring Boot API
├── demo-ui/                    # React frontend
├── docker-compose.yaml         # Full stack deployment
├── nginx.conf                  # Nginx reverse proxy
└── docs/                       # Documentation
```

---

## 📚 Documentation

- [📖 API Documentation](docs/API.md)
- [🗺️ Domain Model](docs/images/Kaleo-domain.png)
- [✅ Test Coverage](docs/TEST_COVERAGE.md)

---

## 📄 License

MIT License

---

<div align="center">

**[Report Bug](../../issues)** • **[Request Feature](../../issues)**

Made with ❤️ for churches and ministries

</div>
