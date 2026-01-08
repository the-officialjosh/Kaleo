# Kaleo

### Church & Ministry Event Management API

[![CI/CD](https://img.shields.io/github/actions/workflow/status/the-officialjosh/kaleo/ci.yml?style=flat-square&logo=github&label=CI%2FCD)](../../actions)
[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-7.0.2-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Keycloak](https://img.shields.io/badge/Keycloak-26.4.7-4D4D4D?style=flat-square&logo=keycloak&logoColor=white)](https://www.keycloak.org/)
[![Adminer](https://img.shields.io/badge/Adminer-4.8.1-34567C?style=flat-square&logo=adminer&logoColor=white)](https://www.adminer.org/)
[![Tests](https://img.shields.io/badge/tests-160-success?style=flat-square)](docs/TEST_COVERAGE.md)

<div align="center">

<img src="docs/images/kaleo-banner.png" alt="Kaleo Banner" width="100%" />

A secure REST API for church and ministry event management. Organizers create programs, participants register for limited-capacity events, and staff validate attendance using QR-coded passes.

[Features](#what-it-does) • [Quick Start](#quick-start) • [API Docs](docs/API.md) • [Architecture](#architecture)

---

</div>

## ✨ What It Does

- 📋 **Program Management**: Create and manage church programs/events with role-based access
- 🎟️ **Registration**: Capacity-controlled registration with automatic pass generation (Coming Soon)
- 📱 **QR Passes**: Generate unique QR codes for event check-in (Coming Soon)
- ✅ **Validation**: Staff-managed check-in with duplicate prevention (Coming Soon)
- 📊 **Analytics**: Track attendance and generate reports (Coming Soon)
- 🔐 **Security**: JWT-based authentication with Keycloak OAuth2/OIDC integration

> 🚧 **Status**: In Development — Core domain model, security infrastructure, program management API, and comprehensive test suite complete

---

## 🛠️ Tech Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Language** | Java 25 | Modern JVM features |
| **Framework** | Spring Boot 4.0.1 | Application framework with Data JPA, Validation |
| **Security** | Spring Security 7.0.2 | Authentication & authorization framework |
| **Database** | PostgreSQL 18 | Relational database |
| **DB Admin** | Adminer 4.8.1 | Database management UI |
| **Auth Provider** | Keycloak 26.4.7 | OAuth2/OIDC authentication & RBAC |
| **Mapping** | MapStruct | Type-safe bean mapping |
| **Testing** | JUnit 5 + Mockito | Unit & integration testing |

---

## 🏗️ Architecture

```
┌─────────────────┐
│   Keycloak      │  OAuth2/OIDC Provider
│   (Port 9090)   │
└────────┬────────┘
         │ JWT
         ▼
┌─────────────────┐
│  Spring Boot    │  REST API Layer
│  (Port 8080)    │  • Security Filter Chain
│                 │  • Controllers
│                 │  • Service Layer
│                 │  • Repository Layer
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   PostgreSQL    │  Persistent Storage
│   (Port 5432)   │
└─────────────────┘
```

**Key Components:**
- **Security**: JWT-based authentication with role-based access (`ORGANIZER`, `PARTICIPANT`, `STAFF`)
- **Domain Model**: Program → PassType → Pass → QrCode → PassValidation
- **Validation**: Custom validators for date ranges and conditional fields
- **Mapping**: MapStruct for DTO ↔ Entity conversions

---

## 🚀 Quick Start

**Prerequisites:** Java 25, Docker, Docker Compose

```bash
# Clone repository
git clone https://github.com/yourusername/kaleo.git
cd kaleo

# Start dependencies (PostgreSQL + Keycloak)
docker-compose up -d

# Run application
./mvnw spring-boot:run
```

API available at: `http://localhost:8080`

**⚙️ Configuration:**
- Keycloak: `http://localhost:9090` (create `kaleo-events` realm)
- Database: `localhost:5432` (credentials in `.env`)
- Adminer: `http://localhost:8888`

---

## 🧪 Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProgramServiceImplTest

# Run with coverage
./mvnw verify jacoco:report
```

**Test Coverage:** 14 test classes with 160+ tests
- **API Layer**: Controllers (ProgramController, PublishedProgramController), DTOs, Validators, Exception Handlers
- **Application Layer**: Services, Mappers, Security (CurrentUserService)
- **Infrastructure**: Security Filters (UserProvisioningFilter)
- **Configuration**: Security Config (HttpSecurityConfig, SecurityFilterChain), JPA Config

See [Test Coverage Documentation](docs/TEST_COVERAGE.md) for detailed breakdown.

---

## 🖥️ Demo UI (Development)

A minimal React-based demo UI is included for testing and development purposes. This is **not a production component** — Kaleo is primarily an API service.

**Tech Stack:**
- React 19 with Vite
- React Router for navigation
- Radix UI for components
- Lightning CSS for styling

**Running the Demo UI:**
```bash
cd demo-ui
npm install
npm run dev
```

The demo UI will be available at `http://localhost:5173` and connects to the API at `http://localhost:8080`.

> **Note:** Ensure the backend API and Keycloak are running before starting the demo UI.

---

## 📚 Documentation

- [📖 API Documentation](docs/API.md) — Endpoints, request/response examples
- [📋 Project Plan](docs/project-plan.md) — Detailed specifications
- [🗺️ Domain Model](docs/images/Kaleo-domain.png) — Entity relationships
- [✅ Test Coverage](docs/TEST_COVERAGE.md) — Test structure and statistics

---

## 📄 License

MIT License

---

<div align="center">

**[Report Bug](../../issues)** • **[Request Feature](../../issues)** • **[Discussions](../../discussions)**

Made with ❤️ for churches and ministries

</div>

