# Assignment Manager Client

A desktop application built with **JavaFX** and **MaterialFX** for managing university courses, assignments, submissions, and grades. The client communicates with a Spring Boot REST backend to enable professors and students to collaborate on academic workflows.

---

## Features

- **Role-based access** — Login with student or professor credentials; UI adapts to permissions
- **Course management** — Create, enroll, unenroll; browse available and enrolled courses
- **Assignment workflow** — Create assignments, upload files, submit work, receive grades and feedback
- **Grade tracking** — View grades per assignment; professors can grade and comment submissions
- **File upload/download** — Chunked file transfer for reliable uploads of any size
- **Calendar integration** — Visual assignment timeline for deadlines
- **Email notifications** — Send email alerts for assignment updates
- **University hierarchy** — Navigate universities → faculties → departments → careers → courses
- **Token-based authentication** — Auto-refresh of expired tokens with background validation
- **Configurable backend URL** — Centralized via `config.properties` (no hardcoded URLs)

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| UI Framework | JavaFX 21 |
| UI Components | MaterialFX 11.16.1 |
| Build Tool | Maven (wrapper included) |
| HTTP Client | `java.net.http.HttpClient` |
| JSON | Jackson (`jackson-databind` + `jackson-datatype-jsr310`) |
| DTO Mapping | ModelMapper 3.2.1 |
| Boilerplate | Lombok |
| Validation | Jakarta Validation / Hibernate Validator |

---

## Prerequisites

- **Java 17+** (JDK)
- **Maven** (or use the included `mvnw` wrapper)
- A running instance of the [Assignment Manager API](https://github.com/anomalyco/Assignment_Manager) (Spring Boot backend)

---

## Setup

### 1. Clone

```bash
git clone <repository-url>
cd Assignment_Manager_Client
```

### 2. Backend Connection

The backend URL defaults to `http://localhost:8080` and can be changed in:

```properties
# src/main/resources/config.properties
backend.url=http://localhost:8080
```

### 3. Build & Run

```bash
# Compile
./mvnw compile

# Launch
./mvnw javafx:run
```

---

## Project Structure

```
src/main/java/org/una/programmingIII/Assignment_Manager_Client/
├── App.java                     # Application entry point
├── Controller/                  # 19 controllers (MVC)
│   ├── LogInController.java
│   ├── MainViewController.java
│   ├── CourseViewController.java
│   ├── AssignmentViewController.java
│   └── ...
├── Service/                     # 12 service classes
│   ├── AuthenticationService.java
│   ├── AssignmentService.java
│   ├── FileService.java
│   └── ...
├── Dto/                         # Data transfer objects
├── Util/                        # Utilities (ConfigLoader, SessionManager, etc.)
├── Mapper/                      # Generic mapper (ModelMapper)
├── Exception/                   # Custom exceptions
└── Interfaces/                  # Observer pattern interfaces
```

---

## Architecture

Monolithic MVC desktop client:

```
User Input → Controller → Service → HttpClient → REST API
                              ↓
                        ObjectMapper ← DTOs
                              ↓
                        JavaFX Properties → View
```

- **Controllers** handle UI events and bind data to FXML views
- **Services** encapsulate HTTP communication with the backend API
- **Util** classes provide singleton utilities (`SessionManager`, `AppContext`, `ConfigLoader`)
- **DTOs** model the data contract between client and server

---

## Known Limitations

- No automated tests (planned for future iterations)
- Backend must be running locally or on a reachable network
- Some FXML files target JavaFX 22 API but are loaded by JavaFX 21 runtime (non-critical warning)

---

## License

This project is developed for academic purposes at the Universidad Nacional de Costa Rica (UNA) — Programming III course.
