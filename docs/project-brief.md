# Kaleo (The Invitation), Church and Program Events Management API

## Project Brief

## 1. Summary
Kaleo is a RESTful backend API for church and ministry program management. It enables authorized organizers to publish programs, participants to register for limited capacity programs, and staff to validate attendance using QR coded passes. The API enforces capacity rules, prevents duplicate check ins, and exposes reporting endpoints for planning and operations.

## 2. Primary API actors (roles)
1. Organizer (Admin), creates and manages programs, capacity, and reporting.
2. Participant (Attendee), browses programs and registers, receives passes.
3. Staff (Usher), validates passes at entry and records check ins.

## 3. Core domain concepts
### 3.1 Program
A scheduled gathering with a date, time, venue, and registration settings, including capacity and registration close time.

### 3.2 Registration Type
A category of registration for a program (example, General, Workers, VIP, Service Team), each with its own capacity limit and rules.

### 3.3 Registration Pass
A server issued digital pass representing a successful registration. Each pass has a unique identifier and a QR payload used for validation.

### 3.4 Check In
A recorded validation event that marks a pass as used for the program, with timestamp and staff identity.

## 4. API capabilities and acceptance criteria

### 4.1 Program management endpoints (Organizer)
Goal: Provide CRUD style operations and publishing controls for programs.

Acceptance criteria
1. Organizer can create a program with name, date, time, and venue.
2. Organizer can configure multiple registration types for a program.
3. Organizer can set capacity per registration type.
4. Organizer can publish or close registration for a program.
5. Published programs are retrievable through public browse endpoints.

### 4.2 Registration endpoints (Participant)
Goal: Enable participants to discover programs and register safely under capacity constraints.

Acceptance criteria
1. Participant can search and browse published programs.
2. Participant can view program details, registration types, and remaining capacity.
3. Participant can register for a program and receive a QR coded pass.
4. API prevents oversubscription under concurrent registrations.
5. Registration closes automatically at the configured close time or when capacity is exhausted.

### 4.3 Check in validation endpoints (Staff)
Goal: Validate passes quickly and prevent reuse.

Acceptance criteria
1. Staff can validate a pass using QR payload or manual code entry.
2. API responds with validity status and reason (valid, already used, invalid, wrong program, expired).
3. Successful validation records a check in and marks the pass as used.
4. API prevents duplicate check ins for the same pass.

### 4.4 Reporting endpoints (Organizer)
Goal: Provide operational visibility for planning and attendance tracking.

Acceptance criteria
1. Organizer can view registration totals by program and by registration type.
2. Organizer can view a list of registrants and pass statuses (active, used).
3. Organizer can view check in totals and attendance rate.

## 5. Non functional requirements (API focused)
1. Security: role based access control for Organizer and Staff endpoints.
2. Data integrity: transactional registration flow to prevent oversubscription.
3. Auditability: check in events must record timestamp and staff identity.
4. Documentation: OpenAPI/Swagger documentation for all endpoints.

## 6. Summary
1. Kaleo exposes a secure REST API for program creation, registration, pass issuance, and check in validation.
2. The API enforces capacity, registration closing rules, and one time pass usage.
3. The API includes reporting endpoints to support planning and attendance monitoring.