<div align="center">

# Kaleo

### A Church and Program Events Management API

![Kaleo banner](docs/images/kaleo-banner.png)

[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Keycloak](https://img.shields.io/badge/Keycloak-OIDC-4D4D4D?style=for-the-badge&logo=keycloak&logoColor=white)](https://www.keycloak.org/)

*Kaleo is a secure REST API for church and ministry program management. Organizers publish programs, participants register for limited capacity gatherings, and staff validate attendance using QR coded passes.*

---

</div>

## ✨ Features

- 📋 **Program Management** — Create, publish, and manage church programs and events
- 🎟️ **Registration System** — Capacity-safe registration with automatic pass generation
- 📱 **QR Code Passes** — Unique QR coded passes for easy check-in
- ✅ **Check-in Validation** — Prevent duplicate check-ins with staff validation
- 📊 **Reporting** — Attendance tracking and comprehensive analytics

---

## 🚧 Status

> **In Development** — Core domain model, security configuration, and program management foundation are in place.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| ![Java](https://img.shields.io/badge/-Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white) | 25      | Core language |
| ![Spring Boot](https://img.shields.io/badge/-Spring_Boot-6DB33F?style=flat-square&logo=spring&logoColor=white) | 4.0.1   | Application framework |
| ![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white) | 18      | Database |
| ![Keycloak](https://img.shields.io/badge/-Keycloak-4D4D4D?style=flat-square&logo=keycloak&logoColor=white) | 26.4.7  | OIDC & RBAC |
| ![MapStruct](https://img.shields.io/badge/-MapStruct-FF6600?style=flat-square&logo=java&logoColor=white) | 1.6.3   | DTO mapping |
| ![JUnit](https://img.shields.io/badge/-JUnit_5-25A162?style=flat-square&logo=junit5&logoColor=white) | 5       | Testing |

---

## 🔌 API Endpoints

### Programs

All program endpoints require authentication via JWT token and `ORGANIZER` role.

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/api/v1/programs` | Create a new program | `CreateProgramRequestDto` | `201 Created` |
| `GET` | `/api/v1/programs` | List programs for current organizer | — | `200 OK` (Paginated) |
| `GET` | `/api/v1/programs/{programId}` | Get program details by ID | — | `200 OK` / `404 Not Found` |

#### Create Program Request

```json
{
  "name": "Sunday Service",
  "startTime": "2026-01-11T09:00:00",
  "endTime": "2026-01-11T12:00:00",
  "venue": "Main Auditorium",
  "registrationStart": "2026-01-07T00:00:00",
  "registrationEnd": "2026-01-10T23:59:59",
  "status": "DRAFT",
  "passTypes": [
    {
      "name": "General Admission",
      "price": 0.00,
      "description": "Free entry",
      "totalAvailable": 500
    }
  ]
}
```

#### Program Response

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunday Service",
  "startTime": "2026-01-11T09:00:00",
  "endTime": "2026-01-11T12:00:00",
  "venue": "Main Auditorium",
  "registrationStart": "2026-01-07T00:00:00",
  "registrationEnd": "2026-01-10T23:59:59",
  "status": "DRAFT",
  "passTypes": [...],
  "createdAt": "2026-01-07T10:30:00",
  "updatedAt": "2026-01-07T10:30:00"
}
```

### Authentication

All endpoints require a valid JWT token from Keycloak in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

---

## 📚 Documentation

| Document                                     | Description |
|----------------------------------------------|-------------|
| [Project Plan](docs/project-plan.md)         | Detailed project overview and specifications |
| [Domain Model](docs/images/Kaleo-domain.png) | Domain model diagram |

### 🏗️ Domain Model
![Kaleo Domain Model](docs/images/Kaleo-domain.png)

---

## 📋 Prerequisites

- **Java 25** or later
- **Docker** and **Docker Compose**
- **Maven** (or use the included Maven Wrapper)

---

## 🚀 Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/kaleo.git
cd kaleo
```

### 2. Configure environment variables

Create a `.env` file in the project root:

```env
POSTGRES_USER=your_db_user
POSTGRES_PASSWORD=your_db_password
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=your_keycloak_password
```

### 3. Start dependencies

```bash
docker-compose up -d
```

This starts:
- **PostgreSQL 18** on port `5432`
- **Keycloak 26.4.7** on port `9090`
- **Adminer** (DB admin UI) on port `8888`

### 4. Configure Keycloak

1. Open http://localhost:9090 and login with your admin credentials
2. Create a realm named `kaleo-events`
3. Configure clients and roles as needed

### 5. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at http://localhost:8080

---

## 📄 License

This project is licensed under the MIT License.

---

<div align="center">

**[Report Bug](../../issues) · [Request Feature](../../issues)**

</div>
