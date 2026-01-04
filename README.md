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

> **In Progress** — This repository is being built incrementally with small commits and milestone-based delivery.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| ![Java](https://img.shields.io/badge/-Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white) | 25      | Core language |
| ![Spring Boot](https://img.shields.io/badge/-Spring_Boot-6DB33F?style=flat-square&logo=spring&logoColor=white) | 4.0.1   | Application framework |
| ![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white) | 18      | Database |
| ![Keycloak](https://img.shields.io/badge/-Keycloak-4D4D4D?style=flat-square&logo=keycloak&logoColor=white) | 26.4.7  | OIDC & RBAC |
| ![Swagger](https://img.shields.io/badge/-OpenAPI-85EA2D?style=flat-square&logo=swagger&logoColor=black) | —       | API documentation |
| ![JUnit](https://img.shields.io/badge/-JUnit_5-25A162?style=flat-square&logo=junit5&logoColor=white) | 5       | Testing |

---

## 🗺️ Roadmap

### Milestone 1: Foundation
- [x] Project setup, Maven wrapper, code style
- [x] PostgreSQL integration
- [x] Keycloak integration, roles
- [x] MapStruct and Lombok Mapstruct Binding for annotation

### Milestone 2: Program Management
- [ ] Create program (draft)
- [ ] Add registration types and capacity
- [ ] Publish and close program
- [ ] Browse programs (public endpoints)

### Milestone 3: Registration & Pass Issuance
- [ ] Register for a program (capacity safe)
- [ ] Create registration record and pass
- [ ] Generate QR payload and unique codes
- [ ] List passes for a participant

### Milestone 4: Check-in
- [ ] Validate pass via QR payload or manual code
- [ ] Prevent duplicate check-in
- [ ] Record check-in events (timestamp, staff)

### Milestone 5: Reporting
- [ ] Totals by program and registration type
- [ ] Check-in totals and attendance rate
- [ ] Registrant lists with status

---

## 📚 Documentation
## 📚 Documentation

| Document | Description |
|----------|-------------|
| [Project Brief](docs/project-brief.md) | Detailed project overview and specifications |
| [Domain Model](docs/images/kaleo-domain.png) | Domain model diagram |

### 🏗️ Domain Model
![Kaleo Domain Model](docs/images/kaleo-domain.png)

---

## 🚀 Running Locally

> **Coming Soon** — Local setup will be provided via Docker Compose (PostgreSQL 18, Keycloak) and the Maven Wrapper.

```bash
# Clone the repository
git clone https://github.com/yourusername/kaleo.git

# Navigate to project directory
cd kaleo

# Start dependencies (coming soon)
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

---

<div align="center">

**[Report Bug](../../issues) · [Request Feature](../../issues)**

</div>
