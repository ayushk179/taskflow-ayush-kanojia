📌 TaskFlow

TaskFlow is a backend task management system that allows users to create projects, manage tasks, assign users, and track progress. It is built as a RESTful API using Spring Boot with JWT authentication and PostgreSQL.

1. Overview
Key Features
User registration & login (JWT authentication)
Project creation and management
Task creation, assignment, and status tracking
Filtering tasks by status and assignee
Basic pagination support
Secure access control (ownership + participation-based authorization)
🔐 Role-Based Authorization

TaskFlow uses a resource-based authorization model instead of fixed roles.

Roles
1. Project Owner
User who creates the project
Full control over project

✔ Can update/delete project
✔ Can view all tasks

2. Participant

User involved in project via tasks:

Assignee OR
Task creator

✔ Can view project
✔ Can access related tasks

3. Task Creator
User who created a task

✔ Can view task
✔ Can delete task (if allowed by rules)

Authorization Rules

Projects

GET /projects → owner + task participants
PATCH/DELETE /projects/:id → owner only

Tasks

GET /projects/:id/tasks → owner or participants

POST /projects/:id/tasks → project access required

PATCH /tasks/:id → assignee / owner / creator

DELETE /tasks/:id → owner or task creator

Example Logic
boolean isParticipant =
    taskRepository.existsByProjectIdAndAssigneeId(projectId, userId) ||
    taskRepository.existsByProjectIdAndCreatedById(projectId, userId);
	
⚙️ Tech Stack

Java 21
Spring Boot 3
Spring Security + JWT
Spring Data JPA
PostgreSQL
Flyway
Docker + Docker Compose
Maven

🏗 Architecture

Layered Design
Controller → API layer
Service → business logic
Repository → database access
Entity → DB models
DTO → request/response models
Why this design?
Clean separation of concerns
Easy to test and extend
Standard production architecture
Authentication
Stateless JWT-based auth
No session storage
Token contains user_id and email
Database
PostgreSQL
Schema managed via Flyway migrations
No Hibernate auto-DDL

🐳 Running Locally
Prerequisites
Docker
Docker Compose
Steps
git clone https://github.com/your-username/taskflow.git
cd taskflow/backend
cp .env.example .env
docker compose up --build
🧪 Migrations
Managed using Flyway
Auto-runs on startup

Default test user:

Email: test@taskflow.com
Password: password123

📡 API Overview
```
{
	"info": {
		"_postman_id": "3dc666aa-8b71-4594-bf2e-9c9f69499810",
		"name": "TaskFlow",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "21824884"
	},
	"item": [
		{
			"name": "Auth",
			"item": [
				{
					"name": "Register",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Ayush\",\r\n  \"email\": \"ayush@test.com\",\r\n  \"password\": \"password123\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8080/auth/register",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"auth",
								"register"
							]
						}
					},
					"response": []
				},
				{
					"name": "login",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"email\": \"ayush@test.com\",\r\n  \"password\": \"password123\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8080/auth/login",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"auth",
								"login"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Project",
			"item": [
				{
					"name": "Get all Projects for currunt User",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJheXVzaEB0ZXN0LmNvbSIsInVzZXJfaWQiOiJkY2EzNGM2Mi0xODkyLTRhMTktYmIxOC04OTQ0ODNlYTgzZWEiLCJlbWFpbCI6ImF5dXNoQHRlc3QuY29tIiwiaWF0IjoxNzc2MDEwNjQ1LCJleHAiOjE3NzYwOTcwNDV9.WJc_drsRBY25Tt1Fk3RAEpomWGfgRZvy0mTqdOUZpXE",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [
							{
								"key": "Authorization",
								"value": "Beare \neyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJheXVzaEB0ZXN0LmNvbSIsInVzZXJfaWQiOiJkY2EzNGM2Mi0xODkyLTRhMTktYmIxOC04OTQ0ODNlYTgzZWEiLCJlbWFpbCI6ImF5dXNoQHRlc3QuY29tIiwiaWF0IjoxNzc2MDEwNjQ1LCJleHAiOjE3NzYwOTcwNDV9.WJc_drsRBY25Tt1Fk3RAEpomWGfgRZvy0mTqdOUZpXE",
								"type": "text",
								"disabled": true
							}
						],
						"url": {
							"raw": "http://localhost:8080/projects",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get project details + its tasks",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJheXVzaEB0ZXN0LmNvbSIsInVzZXJfaWQiOiJkY2EzNGM2Mi0xODkyLTRhMTktYmIxOC04OTQ0ODNlYTgzZWEiLCJlbWFpbCI6ImF5dXNoQHRlc3QuY29tIiwiaWF0IjoxNzc2MDEwNjQ1LCJleHAiOjE3NzYwOTcwNDV9.WJc_drsRBY25Tt1Fk3RAEpomWGfgRZvy0mTqdOUZpXE",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects/43245",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects",
								"43245"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get project stats",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJheXVzaEB0ZXN0LmNvbSIsInVzZXJfaWQiOiJkY2EzNGM2Mi0xODkyLTRhMTktYmIxOC04OTQ0ODNlYTgzZWEiLCJlbWFpbCI6ImF5dXNoQHRlc3QuY29tIiwiaWF0IjoxNzc2MDEwNjQ1LCJleHAiOjE3NzYwOTcwNDV9.WJc_drsRBY25Tt1Fk3RAEpomWGfgRZvy0mTqdOUZpXE",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects/43245",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects",
								"43245"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create Project",
					"request": {
						"method": "POST",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects"
							]
						}
					},
					"response": []
				},
				{
					"name": "Update name/description",
					"request": {
						"method": "PATCH",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects/:id",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects",
								":id"
							],
							"variable": [
								{
									"key": "id",
									"value": ""
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "Delete project and all its tasksDelete project and all its tasks",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects/:id",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects",
								":id"
							],
							"variable": [
								{
									"key": "id",
									"value": ""
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Task",
			"item": [
				{
					"name": "List tasks",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects/{id}/tasks",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects",
								"{id}",
								"tasks"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create a task",
					"request": {
						"method": "POST",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/projects/{id}/tasks",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"projects",
								"{id}",
								"tasks"
							]
						}
					},
					"response": []
				},
				{
					"name": "Update task",
					"request": {
						"method": "PATCH",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/tasks/{id}",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"tasks",
								"{id}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Delete task",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/tasks/{id}",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"tasks",
								"{id}"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "User",
			"item": [
				{
					"name": "User Details by Email",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRhc2tmbG93LmNvbSIsInVzZXJfaWQiOiIwYmZlYzRmNS04MGJlLTRmOTctYWY5OC01ODg5YmRmYmFkNDQiLCJlbWFpbCI6InRlc3RAdGFza2Zsb3cuY29tIiwiaWF0IjoxNzc2MDI2NTcyLCJleHAiOjE3NzYxMTI5NzJ9.anzsreCEfecEDsBG0MmgPRs8rpvBCxTQNydwq1w5Q3Q",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/users/by-email?email=test@taskflow.com",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"users",
								"by-email"
							],
							"query": [
								{
									"key": "email",
									"value": "test@taskflow.com"
								}
							]
						}
					},
					"response": []
				}
			]
		}
	]
}


```

📌 Design Decisions

Why this architecture?
Keeps business logic isolated
Easy to scale later
Standard industry structure
Tradeoffs
No caching (Redis)
No async/event system
Simple error handling


🚀 Future Improvements

## Notes on Bonus Requirements
- The bonus requirement of **at least 3 integration tests for auth/task endpoints was partially started but not fully completed within the given time window**
- Priority was given to completing core API functionality, authentication flow, and database design first

## Add database indexes on frequently queried columns such as:

user.email
task.project_id
task.assignee_id
task.status
project.owner_id

This was intentionally skipped due to time constraints to focus on core API functionality.

Add Integration tests
Add Redis caching
Improve error standardization
Add refresh tokens
Add rate limiting
Add full test coverage
Add Swagger/OpenAPI docs

🧾 Summary

TaskFlow is a clean, production-style backend system with:

JWT authentication
Resource-based authorization
PostgreSQL + Flyway migrations
Dockerized setup
Modular layered architecture
