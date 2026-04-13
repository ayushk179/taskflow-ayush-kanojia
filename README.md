# TaskFlow

TaskFlow is a backend task management system that allows users to create projects, manage tasks, assign users, and track progress. It is built as a RESTful API with authentication, role-based access control, and PostgreSQL persistence.

---

# 1. Overview

TaskFlow is a backend system designed to manage projects and tasks efficiently.

### Key Features
- User registration & login (JWT authentication)
- Project creation and management
- Task creation, assignment, and status tracking
- Filtering tasks by status and assignee
- Pagination support on list APIs
- Secure access control (owner + participant-based authorization)

### Tech Stack
- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Flyway (DB migrations)
- Docker & Docker Compose
- Maven

---

# 2. Architecture Decisions

### Layered Architecture
The project follows a standard layered structure:

- Controller → handles HTTP requests
- Service → contains business logic
- Repository → database access layer
- Entity → database models
- DTO → request/response contracts

### Why this design?
- Keeps business logic separate from API layer
- Improves testability
- Makes scaling easier (can later extract services)

---

### JWT-Based Authentication
- Stateless authentication using JWT
- No session storage required
- Works well for distributed systems

---

### Flyway for migrations
- Ensures database schema consistency across environments
- Version-controlled schema changes

---

### Tradeoffs
- No caching layer (Redis) to keep scope simple
- No event-driven architecture (Kafka/RabbitMQ) to reduce complexity
- Basic error handling instead of full RFC-compliant error model

---

### What was intentionally skipped
- Advanced observability (tracing/metrics)
- Rate limiting
- Multi-tenant support
- CI/CD pipeline

---

# 3. Running Locally

### Prerequisites
- Docker
- Docker Compose

### Steps

```bash
git clone https://github.com/your-username/taskflow.git
cd taskflow/backend
cp .env.example .env
docker compose up --build
