# Kaleo - Project Architecture & Implementation

## Overview

**Kaleo** (Greek for "The Invitation") is a secure REST API for church and ministry event management. Built with Spring Boot 4.0.1 and Java 25, it demonstrates modern enterprise architecture patterns.

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 4.0.1, Java 25 |
| **Security** | Keycloak OAuth2/OIDC, JWT, Role-Based Access Control |
| **Database** | PostgreSQL 16, Spring Data JPA, Hibernate 7 |
| **API Docs** | springdoc-openapi (Swagger UI) |
| **Container** | Docker, Docker Compose, Nginx reverse proxy |
| **Frontend** | React 19 + Vite + TypeScript |

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                        API Layer                             │
│  Controllers → DTOs → Validators                             │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                         │
│  Services → Commands → Security                              │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                            │
│  Entities → Enums → Business Rules                          │
├─────────────────────────────────────────────────────────────┤
│                   Infrastructure Layer                       │
│  Repositories → Mappers → Config → Filters                  │
└─────────────────────────────────────────────────────────────┘
```

---

## Package Structure

```
src/main/java/dev/joshuaonyema/kaleo/
├── KaleoApplication.java
├── api/                          # API Layer (32 files)
│   ├── controller/               # REST Controllers (5)
│   │   ├── PassController.java
│   │   ├── PassTypeController.java
│   │   ├── PassValidationController.java
│   │   ├── ProgramController.java
│   │   └── PublishedProgramController.java
│   ├── dto/
│   │   ├── request/              # Request DTOs (5)
│   │   └── response/             # Response DTOs (15)
│   └── validation/               # Custom Validators (6)
│       ├── BothOrNone.java       # Annotation
│       ├── BothOrNoneValidator.java
│       ├── StartBeforeEnd.java   # Annotation
│       ├── StartBeforeEndValidator.java
│       ├── ValidPassValidationRequest.java
│       └── PassValidationRequestValidator.java
│
├── application/                  # Application Layer (15 files)
│   ├── command/                  # CQRS Commands (6)
│   │   ├── CreateProgramCommand.java
│   │   ├── CreatePassTypeCommand.java
│   │   ├── UpdateProgramCommand.java
│   │   ├── UpdatePassTypeCommand.java
│   │   └── common/
│   ├── security/
│   │   └── CurrentUserService.java
│   └── service/
│       ├── PassService.java
│       ├── PassValidationService.java
│       ├── ProgramService.java
│       ├── QrCodeService.java
│       └── impl/                 # Service Implementations (4)
│
├── config/                       # Configuration Layer (7 files)
│   ├── OpenApiConfig.java        # Swagger OAuth2 setup
│   ├── jpa/
│   │   └── JpaConfig.java        # JPA Auditing
│   ├── qrcode/
│   │   └── QrCodeConfig.java     # ZXing QRCodeWriter bean
│   └── security/
│       ├── HttpSecurityConfig.java
│       ├── JwtAuthenticationConverter.java
│       └── JwtDecoderConfig.java
│
├── domain/                       # Domain Layer
│   └── entity/                   # JPA Entities (12 files)
│       ├── Program.java
│       ├── ProgramStatus.java    # DRAFT, PUBLISHED, CANCELLED, COMPLETED
│       ├── PassType.java
│       ├── Pass.java
│       ├── PassStatus.java       # PURCHASED, CANCELLED
│       ├── PassValidation.java
│       ├── PassValidationStatus.java
│       ├── PassValidationMethod.java  # QR_CODE, MANUAL_CODE
│       ├── QrCode.java
│       ├── QrCodeStatus.java
│       ├── User.java
│       └── TimestampedEntity.java    # createdAt, updatedAt
│
├── exception/                    # Exception Layer (10 files)
│   ├── GlobalExceptionHandler.java
│   ├── CodeNotFoundException.java
│   ├── PassSoldOutException.java
│   ├── PassTypeNotFoundException.java
│   ├── ProgramNotFoundException.java
│   ├── ProgramPassException.java
│   ├── ProgramUpdateException.java
│   ├── QrCodeGenerationException.java
│   ├── QrCodeNotFoundException.java
│   └── UserNotFoundException.java
│
├── mapper/                       # Object Mappers (3 files)
│   ├── PassMapper.java
│   ├── PassValidationMapper.java
│   └── ProgramMapper.java
│
├── repository/                   # Data Access Layer (6 files)
│   ├── PassRepository.java
│   ├── PassTypeRepository.java
│   ├── PassValidationRepository.java
│   ├── ProgramRepository.java
│   ├── QrCodeRepository.java
│   └── UserRepository.java
│
└── util/                         # Utilities (1 file)
    └── ManualCodeGenerator.java  # 6-digit alphanumeric codes
```

---

## Core Features Implemented

### 1. Program Management (Organizer Role)
- **CRUD Operations**: Create, read, update, delete programs
- **Status Workflow**: DRAFT → PUBLISHED → COMPLETED/CANCELLED
- **Pass Types**: Multiple ticket types per program with capacity limits
- **Registration Windows**: Configurable start/end times

### 2. Pass System (Attendee Role)
- **Registration**: Purchase passes for programs
- **Capacity Control**: Atomic capacity checking with pessimistic locking
- **QR Code Generation**: Unique QR codes using ZXing library
- **Manual Codes**: 6-digit alphanumeric fallback codes

### 3. Check-in Validation (Staff Role)
- **QR Scan**: Validate passes by scanning QR codes
- **Manual Entry**: Fallback validation using manual codes
- **Duplicate Prevention**: One-time use enforcement
- **Validation Recording**: Audit trail with timestamp and staff identity

### 4. Security
- **OAuth2/OIDC**: Keycloak integration for authentication
- **JWT Validation**: Custom decoder for Docker networking
- **Role-Based Access**: ORGANIZER, STAFF, ATTENDEE roles
- **User Provisioning**: Auto-creates users from JWT claims

---

## API Endpoints

### Public Endpoints (No Auth)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/published-programs` | List published programs |
| GET | `/api/v1/published-programs/{id}` | Get program details |

### Protected Endpoints
| Method | Endpoint | Role Required |
|--------|----------|---------------|
| POST/GET/PUT/DELETE | `/api/v1/programs/**` | ROLE_ORGANIZER |
| POST | `/api/v1/pass-validations` | ROLE_STAFF |
| GET | `/api/v1/passes` | Authenticated |

---

## Test Coverage

**24 test classes with 474 tests** covering:
- Controllers (unit + integration)
- Services (business logic)
- Mappers (DTO conversions)
- Validators (custom annotations)
- Security (JWT, roles)
- Configuration (Spring context)

---

## Deployment

### Docker Compose Stack
- **nginx** - Reverse proxy (port 80)
- **api** - Spring Boot application (port 8081)
- **keycloak** - OAuth2/OIDC provider (port 9095)
- **db** - PostgreSQL database (port 5432)
- **frontend** - React demo UI
- **adminer** - Database admin UI (port 8888)

### Run Locally
```bash
docker build -t the-officialjosh/kaleo-api:latest .
docker compose up -d
```

---

## Design Decisions

1. **CQRS-Lite Pattern**: Separate command objects for create/update operations
2. **DTO Segregation**: Request and response DTOs for clean API contracts
3. **Custom Validators**: Reusable validation annotations (@BothOrNone, @StartBeforeEnd)
4. **Pessimistic Locking**: Prevents race conditions in pass purchases
5. **JPA Auditing**: Automatic timestamps for createdAt/updatedAt
6. **Environment-Driven Config**: Easy switch between dev/prod with env vars