# API Documentation

## Overview

The Data Masking Demo API provides RESTful endpoints for managing users, posts, comments, albums, photos, and todos with advanced data masking capabilities and external JWT authentication.

## Authentication

All endpoints (except public documentation endpoints) require JWT authentication.

### Authentication Header

```
Authorization: Bearer <your-jwt-token>
```

### Authentication Flow

1. Obtain a JWT token from the external authentication service
2. Include the token in the Authorization header
3. The external auth filter validates the token and extracts user information
4. Access is granted based on the user's roles from the external service

## Base URL

```
http://localhost:2007
```

## Configuration

The API uses environment variables for configuration. Set the following:

```bash
export EXTERNAL_AUTH_API_BASE_URL=https://your-auth-service-url
export SERVER_PORT=2007
```

Or configure via `application.yaml`:

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

## API Endpoints

### Users

#### Get All Users
```http
GET /users
```

**Description**: Retrieves a list of all users with data masking applied to sensitive fields.

**Authentication**: Required

**Response**: 
```json
[
  {
    "id": "1",
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "si*****@april.biz",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "9299*",
      "geo": {
        "lat": "-37.3159",
        "lng": "-81.1496---"
      }
    },
    "phone": "1-770-736-8031****",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  }
]
```

**Data Masking Applied**:
- Email: Shows first 2 and last 4 characters
- Phone: Shows first 3 and last 4 characters
- Address Zipcode: Shows only last character
- Address Geo Longitude: Shows first 3 and last 3 characters

#### Get User by ID
```http
GET /users/{id}
```

**Description**: Retrieves a specific user by their ID without data masking.

**Parameters**:
- `id` (path): User ID

**Authentication**: Required

**Response**: Same structure as above but without masking applied.

### Posts

#### Get All Posts
```http
GET /posts
```

**Description**: Retrieves a list of all posts.

**Authentication**: Required

**Response**:
```json
[
  {
    "userId": 1,
    "id": 1,
    "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
    "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
  }
]
```

#### Get Post by ID
```http
GET /posts/{id}
```

**Description**: Retrieves a specific post by its ID.

**Parameters**:
- `id` (path): Post ID

**Authentication**: Required

### Comments

#### Get All Comments
```http
GET /comments
```

**Description**: Retrieves a list of all comments.

**Authentication**: Required

**Response**:
```json
[
  {
    "postId": 1,
    "id": 1,
    "name": "id labore ex et quam laborum",
    "email": "Eliseo@gardner.biz",
    "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
  }
]
```

#### Get Comment by ID
```http
GET /comments/{id}
```

**Description**: Retrieves a specific comment by its ID.

**Parameters**:
- `id` (path): Comment ID

**Authentication**: Required

### Albums

#### Get All Albums
```http
GET /albums
```

**Description**: Retrieves a list of all albums.

**Authentication**: Required

**Response**:
```json
[
  {
    "userId": 1,
    "id": 1,
    "title": "quidem molestiae enim"
  }
]
```

#### Get Album by ID
```http
GET /albums/{id}
```

**Description**: Retrieves a specific album by its ID.

**Parameters**:
- `id` (path): Album ID

**Authentication**: Required

### Photos

#### Get All Photos
```http
GET /photos
```

**Description**: Retrieves a list of all photos.

**Authentication**: Required

**Response**:
```json
[
  {
    "albumId": 1,
    "id": 1,
    "title": "accusamus beatae ad facilis cum similique qui sunt",
    "url": "https://via.placeholder.com/600/92c952",
    "thumbnailUrl": "https://via.placeholder.com/150/92c952"
  }
]
```

#### Get Photo by ID
```http
GET /photos/{id}
```

**Description**: Retrieves a specific photo by its ID.

**Parameters**:
- `id` (path): Photo ID

**Authentication**: Required

### Todos

#### Get All Todos
```http
GET /todos
```

**Description**: Retrieves a list of all todos.

**Authentication**: Required

**Response**:
```json
[
  {
    "userId": 1,
    "id": 1,
    "title": "delectus aut autem",
    "completed": false
  }
]
```

#### Get Todo by ID
```http
GET /todos/{id}
```

**Description**: Retrieves a specific todo by its ID.

**Parameters**:
- `id` (path): Todo ID

**Authentication**: Required

## Error Responses

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found",
  "path": "/users/999"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/users"
}
```

## Data Masking Rules

The API applies data masking to sensitive information in the `/users` endpoint when using the `@ApplyMasking` annotation.

### Masking Configuration

| Field | Prefix | Suffix | Mask Character | Example |
|-------|--------|--------|----------------|---------|
| Email | 2 | 4 | * | `john@example.com` → `jo*******@example.com` |
| Phone | 3 | 4 | * | `+1234567890` → `+12*******890` |
| Zipcode | 0 | 1 | * | `12345` → `1234*` |
| Longitude | 3 | 3 | - | `-81.1496` → `-81.1496---` |

## Swagger UI

Interactive API documentation is available at:
```
http://localhost:2007/swagger-ui/index.html
```

## OpenAPI Specification

The OpenAPI 3.0 specification is available at:
```
http://localhost:2007/v3/api-docs
```

## Rate Limiting

Currently, no rate limiting is implemented. Consider adding rate limiting for production use.

## CORS

CORS configuration should be added if this API will be consumed by web applications from different domains.

## Examples

### Using curl

```bash
# Get all users with masking
curl -H "Authorization: Bearer <your-jwt-token>" \
     http://localhost:2007/users

# Get specific user without masking
curl -H "Authorization: Bearer <your-jwt-token>" \
     http://localhost:2007/users/1

# Get all posts
curl -H "Authorization: Bearer <your-jwt-token>" \
     http://localhost:2007/posts
```

### Using JavaScript fetch

```javascript
// Get masked users
const response = await fetch('http://localhost:2007/users', {
  headers: {
    'Authorization': 'Bearer ' + yourJwtToken
  }
});
const users = await response.json();

// Get specific user
const userResponse = await fetch('http://localhost:2007/users/1', {
  headers: {
    'Authorization': 'Bearer ' + yourJwtToken
  }
});
const user = await userResponse.json();
```

## Testing

Use the Swagger UI at `http://localhost:2007/swagger-ui/index.html` to test all endpoints interactively. You can also use tools like Postman or curl for API testing.
