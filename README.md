# Data Masking Demo

A Spring Boot application demonstrating advanced data masking techniques with external authentication integration. This project showcases how to implement custom annotation-based data masking for sensitive information while securing APIs with external JWT authentication.

## 🚀 Features

- **Custom Data Masking**: Annotation-based masking system with configurable prefix/suffix preservation
- **External Authentication**: Integration with external authentication service via JWT tokens
- **RESTful API**: Complete CRUD operations for users, posts, comments, albums, photos, and todos
- **Spring Security**: Stateless authentication with role-based access control
- **OpenAPI Documentation**: Auto-generated API documentation with Swagger UI
- **AOP Integration**: Aspect-oriented programming for seamless data masking application

## 📋 Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- External authentication service URL
- Environment variables for configuration

## 🛠️ Technology Stack

- **Spring Boot 4.0.5**
- **Spring Security** - Authentication and authorization
- **Spring AOP** - Aspect-oriented programming for data masking
- **SpringDoc OpenAPI 3.0.2** - API documentation
- **Java 17** - Latest Java features
- **Gradle** - Build automation

## 🏗️ Project Structure

```
src/main/java/com/walter/datamaskingdemo/
├── annotation/masking/          # Custom masking annotations
│   ├── ApplyMasking.java       # Method-level masking trigger
│   ├── Mask.java              # Field-level masking configuration
│   └── Maskable.java          # Interface for maskable entities
├── api/                       # REST controllers and DTOs
│   ├── UsersController.java
│   ├── PostsController.java
│   ├── CommentsController.java
│   ├── AlbumsController.java
│   ├── PhotosController.java
│   └── TodosController.java
├── client/                    # External API clients
│   ├── AuthClient.java
│   ├── UsersClient.java
│   └── ...
├── config/                    # Configuration classes
│   ├── ApiClientConfig.java
│   └── SecurityConfig.java
├── security/                  # Security components
│   ├── ExternalAuthFilter.java
│   └── UserInfoResponse.java
├── service/                   # Business logic layer
└── aspect/                    # AOP aspects for masking
```

## 🔐 Data Masking System

The project implements a sophisticated data masking system using custom annotations:

### Annotations

- `@Maskable`: Marks classes that can have their fields masked
- `@Mask`: Configures masking for specific fields
  - `prefix`: Number of characters to show at the beginning
  - `suffix`: Number of characters to show at the end
  - `maskChar`: Character to use for masking (default: '*')
- `@ApplyMasking`: Applied to methods to trigger masking

### Example Usage

```java
@Maskable
public record User(
    String id,
    String name,
    String username,
    @Mask(prefix = 2, suffix = 4, maskChar = '*') String email,
    @Mask(prefix = 3, suffix = 4, maskChar = '*') String phone,
    // ...
) {}
```

This would transform:
- Email: `john.doe@example.com` → `jo*******@example.com`
- Phone: `+1234567890` → `+12*******890`

## 🔒 Authentication & Security

### External Authentication Flow

1. Client sends JWT token in `Authorization` header
2. `ExternalAuthFilter` intercepts the request
3. Token is validated against external auth service
4. User information and roles are extracted
5. Authentication context is established

### Security Configuration

- **Stateless Sessions**: No server-side session storage
- **JWT Bearer Tokens**: Standard JWT authentication
- **Role-Based Access**: Authorities mapped from external service
- **Public Endpoints**: Swagger UI and public APIs accessible without auth

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd data-masking-demo
```

### 2. Build the Application

```bash
./gradlew build
```

### 3. Set Environment Variables

```bash
export EXTERNAL_AUTH_API_BASE_URL=https://user-service-mobile-banking.c2.tracom.dev
export SERVER_PORT=2007
```

### 4. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:2007` (or your configured port)

### 5. Access API Documentation

Open your browser and navigate to:
- **Swagger UI**: `http://localhost:2007/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:2007/v3/api-docs`

## 📚 API Endpoints

### Users
- `GET /users` - Get all users (with data masking applied)
- `GET /users/{id}` - Get user by ID

### Posts
- `GET /posts` - Get all posts
- `GET /posts/{id}` - Get post by ID

### Comments
- `GET /comments` - Get all comments
- `GET /comments/{id}` - Get comment by ID

### Albums
- `GET /albums` - Get all albums
- `GET /albums/{id}` - Get album by ID

### Photos
- `GET /photos` - Get all photos
- `GET /photos/{id}` - Get photo by ID

### Todos
- `GET /todos` - Get all todos
- `GET /todos/{id}` - Get todo by ID

## 🔧 Configuration

### External Authentication Service

The application uses environment variables for configuration. Set the following environment variables:

```bash
export EXTERNAL_AUTH_API_BASE_URL=https://your-auth-service-url
export SERVER_PORT=2007
```

Or create an `application.yaml` file:

```yaml
server:
    port: ${SERVER_PORT}
spring:
    application:
        name: data-masking-demo

external:
    auth:
        api:
            base-url: ${EXTERNAL_AUTH_API_BASE_URL}
```

The `SecurityConfig.java` automatically reads these values using `@Value` annotations:

```java
@Value("${external.auth.api.base-url}")
private String externalAuthApiBaseUrl;
```

### Data Masking Configuration

Customize masking behavior by modifying the `@Mask` annotations on your DTOs:

```java
@Mask(prefix = 2, suffix = 4, maskChar = '*') // Show 2 chars at start, 4 at end
@Mask(prefix = 0, suffix = 1, maskChar = '#')  // Show only last character
@Mask(prefix = 4, suffix = 0, maskChar = 'X')  // Show only first 4 characters
```

## 🧪 Testing

### Run Tests

```bash
./gradlew test
```

### Test with Authentication

To test authenticated endpoints, include a valid JWT token:

```bash
curl -H "Authorization: Bearer <your-jwt-token>" \
     http://localhost:2007/users
```

### Environment Variable Testing

Test with different configurations:

```bash
# Development
EXTERNAL_AUTH_API_BASE_URL=https://dev-auth.example.com \
SERVER_PORT=8080 \
./gradlew bootRun

# Production
EXTERNAL_AUTH_API_BASE_URL=https://prod-auth.example.com \
SERVER_PORT=80 \
./gradlew bootRun
```

## 📝 Development Notes

### Adding New Masking Rules

1. Ensure your DTO is marked with `@Maskable`
2. Add `@Mask` annotation to fields requiring masking
3. Apply `@ApplyMasking` to controller methods that should return masked data

### Customizing the Masking Logic

The masking logic is implemented in the AOP aspect. To customize:
1. Locate the masking aspect class
2. Modify the masking algorithm as needed
3. Ensure backward compatibility with existing annotations

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) for details.

## 👤 Author

**Walter Kivindu**
- Email: wkmulwa@gmail.com

## 📞 Support

For questions and support, please contact the author or open an issue in the repository.
