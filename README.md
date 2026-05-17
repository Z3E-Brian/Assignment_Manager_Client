# Assignment Manager Client

Aplicación de escritorio construida con **JavaFX** y **MaterialFX** para la gestión de cursos, asignaciones, entregas y calificaciones a nivel universitario. El cliente se comunica con un backend Spring Boot REST.

---

## Características

- **Acceso por roles** — Inicio de sesión como estudiante o profesor; la UI se adapta a los permisos
- **Gestión de cursos** — Crear, matricular, desmatricular; explorar cursos disponibles e inscritos
- **Flujo de asignaciones** — Crear tareas, subir archivos, entregar trabajos, recibir notas y retroalimentación
- **Seguimiento de notas** — Ver calificaciones por asignación; los profesores califican y comentan entregas
- **Subida/descarga de archivos** — Transferencia confiable de archivos
- **Calendario integrado** — Línea de tiempo visual de asignaciones y fechas límite
- **Notificaciones por correo** — Alertas de nuevas asignaciones, entregas y calificaciones
- **Jerarquía universitaria** — Navegar: universidades → facultades → departamentos → carreras → cursos
- **Autenticación JWT** — Refresco automático de tokens expirados
- **URL del backend configurable** — Centralizada en `config.properties`

---

## Tecnologías Usadas

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 17 |
| UI | JavaFX 21 |
| Componentes UI | MaterialFX 11.16.1 |
| Build | Maven (wrapper incluido) |
| Cliente HTTP | `java.net.http.HttpClient` |
| JSON | Jackson (`jackson-databind` + `jackson-datatype-jsr310`) |
| Mapeo DTO | ModelMapper 3.2.1 |
| Validación | Jakarta Validation / Hibernate Validator |
| Código boilerplate | Lombok |

---

## Requisitos Previos

- **Java 17+ JDK** (si no lo tienes: [Descargar JDK 17](https://adoptium.net/temurin/releases/?version=17))
- **Maven 3.8+** (o usa el wrapper `mvnw.cmd` incluido)
- Una instancia del backend corriendo: [Assignment Manager API](https://github.com/JustinMdz/Assignment_Manager)

---

## Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/Assignment_Manager_Client.git
cd Assignment_Manager_Client
```

### 2. Configurar conexión al backend

Editar `src/main/resources/config.properties`:

```properties
# URL del backend Spring Boot (por defecto localhost:8080)
backend.url=http://localhost:8080
```

Si el backend corre en otro puerto o IP, cámbialo aquí.

### 3. Posibles descargas adicionales

Si el proyecto no compila por dependencias faltantes:

| Recurso | Descarga |
|---------|----------|
| **JDK 17** | https://adoptium.net/temurin/releases/?version=17 |
| **JavaFX SDK 21** (solo si hay errores de módulos) | https://gluonhq.com/products/javafx/ |
| **Apache Maven 3.8.5+** | https://maven.apache.org/download.cgi |
| **Lombok** (si el IDE no lo reconoce) | https://projectlombok.org/download |

---

## Cómo Ejecutar

### Desde consola (recomendado)

```bash
# Compilar
./mvnw.cmd compile

# Ejecutar
./mvnw.cmd javafx:run
```

### Desde VS Code

1. Instalar extensiones:
   - [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
   - [Maven for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-maven)
   - [Lombok Annotations Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-lombok)

2. Abrir la carpeta del proyecto en VS Code

3. En el panel **Maven** (vista lateral), ir a: `Assignment_Manager_Client > Plugins > javafx > javafx:run` y dar clic

4. O crear un launch config (`.vscode/launch.json`):

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Run JavaFX",
            "request": "launch",
            "mainClass": "org.una.programmingIII.Assignment_Manager_Client.App",
            "vmArgs": "--module-path \"C:/ruta/a/javafx-sdk-21/lib\" --add-modules javafx.controls,javafx.fxml",
            "projectName": "Assignment_Manager_Client"
        }
    ]
}
```

> **Nota:** Si usas JDK 17+ con JavaFX 21 como dependencia de Maven, no necesitas el `--module-path`. Solo si descargaste el SDK manualmente.

---

## Referencia del Backend

El cliente necesita el backend corriendo en:

```
http://localhost:8080
```

Endpoints principales que consume:

| Método | Endpoint | Propósito |
|--------|----------|-----------|
| POST | `/auth/login` | Inicio de sesión |
| POST | `/auth/refreshToken` | Refrescar token JWT |
| GET | `/api/users` | Listar usuarios |
| GET | `/api/courses` | Listar cursos |
| GET | `/api/courses/professor/{id}` | Cursos de un profesor |
| POST | `/api/assignments` | Crear asignación |
| POST | `/api/submissions` | Crear entrega |
| POST | `/api/files` | Subir archivo |

Swagger UI del backend: `http://localhost:8080/doc/swagger-ui.html`

---

## Estructura del Proyecto

```
src/main/java/org/una/programmingIII/Assignment_Manager_Client/
├── App.java                          # Punto de entrada JavaFX
├── Controller/                       # 19 controladores (MVC)
├── Service/                          # 12 clases de servicio (HTTP)
├── Dto/                              # Objetos de transferencia de datos
│   └── Input/                        # DTOs para entrada de datos
├── Util/                             # Utilidades (ConfigLoader, SessionManager, etc.)
├── Mapper/                           # Mapeo genérico con ModelMapper
├── Exception/                        # Excepciones personalizadas
└── Interfaces/                       # Patrón Observer
```

---

## Notas

- **Seed data:** El backend incluye un `DataSeeder` que crea datos de prueba si la BD está vacía.
- **Usuarios de prueba:** `admin@test.com`, `carlos.mendoza@test.com`, `juan.perez@test.com` — contraseña: `123456`
- **Backend:** PostgreSQL + Spring Boot 3.3.4. Ver README del backend para docker-compose y configuración.
- Este proyecto fue desarrollado como parte del curso Programación III en la Universidad Nacional de Costa Rica (UNA).
