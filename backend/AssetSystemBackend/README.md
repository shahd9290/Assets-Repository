# Spring Boot Backend

### Overview
This Spring Boot application provides RESTful APIs for managing user data and supports dynamic database operations.

### Technologies used
- PostgreSQL
- Spring Boot Web & JPA
- Spring Security
- Java 17
- jjwt-api 0.11.5

### Database Configuration
Configure your PostgreSQL database by creating a database named your_database_name and updating the _application.properties_ file with the appropriate database connection details.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### API endpoints
`GET /api/health`: Gets the server status 