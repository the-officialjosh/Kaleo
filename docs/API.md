# Kaleo API Documentation

## Authentication

All endpoints require authentication via JWT token from Keycloak.

**Header:**
```
Authorization: Bearer <your_jwt_token>
```

## Base URL

```
http://localhost:8080/api/v1
```

---

## Programs API

All program endpoints require the `ORGANIZER` role.

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/programs` | Create a new program |
| `GET` | `/programs` | List programs (paginated) |
| `GET` | `/programs/{id}` | Get program details |
| `PUT` | `/programs/{id}` | Update a program |
| `DELETE` | `/programs/{id}` | Delete a program |

---

### Create Program

**Endpoint:** `POST /programs`

**Request Body:**
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
    },
    {
      "name": "VIP",
      "price": 25.00,
      "description": "Premium seating",
      "totalAvailable": 50
    }
  ]
}
```

**Response:** `201 Created`
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
  "passTypes": [
    {
      "id": "650e8400-e29b-41d4-a716-446655440001",
      "name": "General Admission",
      "price": 0.00,
      "description": "Free entry",
      "totalAvailable": 500
    },
    {
      "id": "650e8400-e29b-41d4-a716-446655440002",
      "name": "VIP",
      "price": 25.00,
      "description": "Premium seating",
      "totalAvailable": 50
    }
  ],
  "createdAt": "2026-01-07T10:30:00",
  "updatedAt": "2026-01-07T10:30:00"
}
```

**Validation Rules:**
- `name`: Required, not blank
- `startTime`: Required, must be before `endTime`
- `endTime`: Required
- `venue`: Required, not blank
- `status`: Required (DRAFT, PUBLISHED, CANCELLED, COMPLETED)
- `registrationStart`: Optional, must be before `registrationEnd` if provided
- `registrationEnd`: Optional, must be after `registrationStart` if provided
- `passTypes`: Must have at least one pass type

---

### Update Program

**Endpoint:** `PUT /programs/{id}`

**Request Body:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunday Service - Updated",
  "startTime": "2026-01-11T10:00:00",
  "endTime": "2026-01-11T13:00:00",
  "venue": "Main Auditorium - Hall B",
  "registrationStart": "2026-01-07T00:00:00",
  "registrationEnd": "2026-01-10T23:59:59",
  "status": "PUBLISHED",
  "passTypes": [
    {
      "id": "650e8400-e29b-41d4-a716-446655440001",
      "name": "General Admission",
      "price": 0.00,
      "description": "Free entry - Updated",
      "totalAvailable": 600
    },
    {
      "name": "Student",
      "price": 10.00,
      "description": "Student discount",
      "totalAvailable": 100
    }
  ]
}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunday Service - Updated",
  "startTime": "2026-01-11T10:00:00",
  "endTime": "2026-01-11T13:00:00",
  "venue": "Main Auditorium - Hall B",
  "registrationStart": "2026-01-07T00:00:00",
  "registrationEnd": "2026-01-10T23:59:59",
  "status": "PUBLISHED",
  "passTypes": [
    {
      "id": "650e8400-e29b-41d4-a716-446655440001",
      "name": "General Admission",
      "price": 0.00,
      "description": "Free entry - Updated",
      "totalAvailable": 600
    },
    {
      "id": "750e8400-e29b-41d4-a716-446655440003",
      "name": "Student",
      "price": 10.00,
      "description": "Student discount",
      "totalAvailable": 100
    }
  ],
  "createdAt": "2026-01-07T10:30:00",
  "updatedAt": "2026-01-07T11:45:00"
}
```

**Notes:**
- Request body `id` must match path parameter `{id}`
- To **update** an existing PassType: include its `id`
- To **create** a new PassType: omit the `id` field
- To **delete** a PassType: exclude it from the `passTypes` array

---

### List Programs

**Endpoint:** `GET /programs`

**Query Parameters:**
- `page` (optional): Page number, default `0`
- `size` (optional): Page size, default `20`
- `sort` (optional): Sort field and direction, e.g., `startTime,desc`

**Example Request:**
```
GET /programs?page=0&size=10&sort=startTime,desc
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Sunday Service",
      "startTime": "2026-01-11T09:00:00",
      "endTime": "2026-01-11T12:00:00",
      "venue": "Main Auditorium",
      "status": "PUBLISHED",
      "passTypes": [
        {
          "id": "650e8400-e29b-41d4-a716-446655440001",
          "name": "General Admission",
          "price": 0.00,
          "totalAvailable": 500
        }
      ]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1
}
```

---

### Get Program Details

**Endpoint:** `GET /programs/{id}`

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunday Service",
  "startTime": "2026-01-11T09:00:00",
  "endTime": "2026-01-11T12:00:00",
  "venue": "Main Auditorium",
  "registrationStart": "2026-01-07T00:00:00",
  "registrationEnd": "2026-01-10T23:59:59",
  "status": "PUBLISHED",
  "passTypes": [
    {
      "id": "650e8400-e29b-41d4-a716-446655440001",
      "name": "General Admission",
      "price": 0.00,
      "description": "Free entry",
      "totalAvailable": 500
    }
  ],
  "createdAt": "2026-01-07T10:30:00",
  "updatedAt": "2026-01-07T10:30:00"
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "Program with ID '550e8400-e29b-41d4-a716-446655440000' not found or you don't have access",
  "path": "/api/v1/programs/550e8400-e29b-41d4-a716-446655440000"
}
```

---

### Delete Program

**Endpoint:** `DELETE /programs/{id}`

**Response:** `204 No Content`

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "Program with ID '550e8400-e29b-41d4-a716-446655440000' not found or you don't have access",
  "path": "/api/v1/programs/550e8400-e29b-41d4-a716-446655440000"
}
```

---

## Published Programs API (Public)

These endpoints are publicly accessible and do not require authentication. They return only programs with `PUBLISHED` status.

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/published-programs` | List published programs (paginated, with search) |
| `GET` | `/published-programs/{id}` | Get published program details |

---

### List Published Programs

**Endpoint:** `GET /published-programs`

**Query Parameters:**
- `q` (optional): Search query to filter programs by name
- `page` (optional): Page number, default `0`
- `size` (optional): Page size, default `20`
- `sort` (optional): Sort field and direction, e.g., `startTime,desc`

**Example Request:**
```
GET /published-programs?q=Sunday&page=0&size=10
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Sunday Service",
      "startTime": "2026-01-11T09:00:00",
      "endTime": "2026-01-11T12:00:00",
      "venue": "Main Auditorium",
      "status": "PUBLISHED"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

---

### Get Published Program Details

**Endpoint:** `GET /published-programs/{id}`

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunday Service",
  "startTime": "2026-01-11T09:00:00",
  "endTime": "2026-01-11T12:00:00",
  "venue": "Main Auditorium",
  "status": "PUBLISHED",
  "passTypes": [
    {
      "id": "650e8400-e29b-41d4-a716-446655440001",
      "name": "General Admission",
      "price": 0.00,
      "description": "Free entry",
      "totalAvailable": 500
    }
  ]
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "Program not found",
  "path": "/api/v1/published-programs/550e8400-e29b-41d4-a716-446655440000"
}
```

---

## Error Responses

### 400 Bad Request (Validation Error)

```json
{
  "timestamp": "2026-01-07T11:45:00",
  "errors": {
    "name": "must not be blank",
    "startTime": "must not be null",
    "endTime": "Start time must be before end time"
  },
  "path": "/api/v1/programs"
}
```

### 401 Unauthorized

```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "Not authenticated",
  "path": "/api/v1/programs"
}
```

### 403 Forbidden

```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "Access denied",
  "path": "/api/v1/programs"
}
```

### 404 Not Found

```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "Program with ID 'xxx' not found or you don't have access",
  "path": "/api/v1/programs/xxx"
}
```

### 500 Internal Server Error

```json
{
  "timestamp": "2026-01-07T11:45:00",
  "message": "An unexpected error occurred",
  "path": "/api/v1/programs"
}
```

---

## Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Validation error or invalid input |
| 401 | Unauthorized - Missing or invalid authentication token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

---

## Common Data Types

### ProgramStatus Enum
- `DRAFT` - Program is in draft state
- `PUBLISHED` - Program is published and visible
- `CANCELLED` - Program has been cancelled
- `COMPLETED` - Program has been completed

### PassType Object
```json
{
  "id": "uuid",
  "name": "string",
  "price": "decimal",
  "description": "string (optional)",
  "totalAvailable": "integer (optional)"
}
```

---

## Future Endpoints (Planned)

- `POST /programs/{id}/publish` - Publish a draft program
- `POST /programs/{id}/cancel` - Cancel a program
- `GET /programs/{id}/registrations` - List registrations for a program
- `POST /programs/{id}/register` - Register for a program (PARTICIPANT role)
- `GET /passes/{id}` - Get pass details
- `POST /passes/{id}/validate` - Validate a pass (STAFF role)

