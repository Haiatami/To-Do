# To-Do List REST API

Xây dựng Backend Api cho ứng dụng danh sách việc cần làm bằng Spring Boot, MySql, Redis, Docker.

## Hướng dẫn thiết lập

### Điều kiện tiên quyết

*   Java 21 or higher
*   Maven or Gradle
*   Docker
*   MySQL Database
*   Redis

### Thiết lập cơ sở dữ liệu

1.  Đảm bảo bạn đã cài đặt trên máy tính và chạy MySQL hoặc dùng docker(docker).
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/todolist
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update
    ```
2.  Create a database named `todolist` (or as configured in `application.properties`).
3.  The application is configured to automatically create tables on startup (using `spring.jpa.hibernate.ddl-auto: update`).

### Running the Application (without Docker)

1.  Clone the repository.
2.  Navigate to the project directory in your terminal.
3.  Configure database connection in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/todolist
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update
    ```
    *(Adjust database credentials as needed)*
4.  *(Optional) If using Redis caching, configure Redis connection in `application.properties` or `RedisConfig.java`.*
5.  Run the application using Maven: `mvn spring-boot:run` or Gradle: `gradle bootRun`.
6.  The API will be accessible at `http://localhost:8080/api/tasks`.

### Running the Application with Docker Compose

1.  Ensure Docker and Docker Compose are installed.
2.  Clone the repository.
3.  Navigate to the project directory in your terminal.
4.  Build and run the application using Docker Compose: `docker-compose up --build`.
5.  The API will be accessible at `http://localhost:8080/api/tasks`. The MySQL database and Redis (if enabled) will also be started in Docker containers.

### API Endpoints

**Tasks:**

*   `POST /api/tasks`: Create a new task (Request body: `TaskDto` in JSON).
*   `GET /api/tasks/{id}`: Get task by ID.
*   `GET /api/tasks`: List all tasks with pagination and optional filtering (query parameters: `page`, `size`, `title`, `status`, `priority`, `dueDateFrom`, `dueDateTo`).
*   `PUT /api/tasks/{id}`: Update task details (Request body: `TaskDto` in JSON).
*   `PATCH /api/tasks/{id}/status`: Update task status (query parameter: `status=TODO|IN_PROGRESS|COMPLETED|BLOCKED`).
*   `DELETE /api/tasks/{id}`: Delete task.

**Task Dependencies:**

*   `POST /api/tasks/{taskId}/dependencies/{dependencyTaskId}`: Add a dependency.
*   `DELETE /api/tasks/{taskId}/dependencies/{dependencyTaskId}`: Remove a dependency.
*   `GET /api/tasks/{taskId}/dependencies`: Get all dependencies (direct and indirect).
