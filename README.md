# FenixCommerce Backend API

A multi-tenant eCommerce order, fulfillment, and tracking management system built with Spring Boot.

## Features

- **Multi-tenant Architecture**: Support for multiple organizations, each with multiple websites
- **Full CRUD Operations**: Complete REST APIs for Organizations, Websites, Orders, Fulfillments, and Tracking
- **Pagination & Filtering**: Date range filtering, sorting, and pagination support
- **Search Capabilities**: Search by external IDs, names, codes, and more
- **Proper Error Handling**: Global exception handler with JSON error responses
- **Validation**: Request validation using Jakarta Bean Validation

## Technology Stack

- **Java 17+**
- **Spring Boot 4.0.1**
- **Spring Data JPA**
- **MySQL Database**
- **Maven**
- **Lombok**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (or compatible database)
- Git

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd fenixcommerce-backend
```

### 2. Database Setup

#### Option A: Using MySQL Command Line

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE fenixcommerce;

# Exit MySQL
exit
```

#### Option B: Database will be created automatically

The application is configured to create the database automatically if it doesn't exist.

### 3. Configure Database Connection

Edit `src/main/resources/application.yml` or set environment variables:

```bash
# Windows PowerShell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"

# Linux/Mac
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

Default configuration (if environment variables are not set):
- **URL**: `jdbc:mysql://localhost:3306/fenixcommerce`
- **Username**: `root`
- **Password**: (empty)

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Organizations

- `POST /organizations` - Create organization
- `GET /organizations` - List organizations (with pagination, filtering)
- `GET /organizations/search?externalId={id}` - Search by external ID
- `GET /organizations/{id}` - Get organization by ID
- `PUT /organizations/{id}` - Update organization (full replace)
- `PATCH /organizations/{id}` - Update organization (partial)
- `DELETE /organizations/{id}` - Delete organization

### Websites

- `POST /organizations/{orgId}/websites` - Create website
- `GET /organizations/{orgId}/websites` - List websites (with pagination, filtering)
- `GET /organizations/{orgId}/websites/search` - Search websites
- `GET /organizations/{orgId}/websites/{websiteId}` - Get website by ID
- `PUT /organizations/{orgId}/websites/{websiteId}` - Update website
- `PATCH /organizations/{orgId}/websites/{websiteId}` - Patch website
- `DELETE /organizations/{orgId}/websites/{websiteId}` - Delete website

### Orders

- `POST /orders` - Create/upsert order
- `GET /orders?orgId={uuid}&websiteId={uuid}` - Search orders (with pagination, filtering)
- `GET /orders/search?orgId={uuid}&externalOrderId={id}` - Search by external order ID
- `GET /orders/{orderId}` - Get order by ID
- `PUT /orders/{orderId}` - Update order (full replace)
- `PATCH /orders/{orderId}` - Update order (partial)
- `DELETE /orders/{orderId}` - Delete order

### Fulfillments

- `POST /orders/{orderId}/fulfillments` - Create fulfillment
- `GET /orders/{orderId}/fulfillments` - List fulfillments (with pagination, filtering)
- `GET /orders/{orderId}/fulfillments/search?externalFulfillmentId={id}` - Search by external ID
- `GET /orders/{orderId}/fulfillments/{fulfillmentId}` - Get fulfillment by ID
- `PUT /orders/{orderId}/fulfillments/{fulfillmentId}` - Update fulfillment
- `PATCH /orders/{orderId}/fulfillments/{fulfillmentId}` - Patch fulfillment
- `DELETE /orders/{orderId}/fulfillments/{fulfillmentId}` - Delete fulfillment

### Tracking

- `POST /fulfillments/{fulfillmentId}/tracking` - Create tracking
- `GET /fulfillments/{fulfillmentId}/tracking` - List tracking (with pagination, filtering)
- `GET /fulfillments/{fulfillmentId}/tracking/search?trackingNumber={number}` - Search by tracking number
- `GET /fulfillments/{fulfillmentId}/tracking/{trackingId}` - Get tracking by ID
- `PUT /fulfillments/{fulfillmentId}/tracking/{trackingId}` - Update tracking
- `PATCH /fulfillments/{fulfillmentId}/tracking/{trackingId}` - Patch tracking
- `DELETE /fulfillments/{fulfillmentId}/tracking/{trackingId}` - Delete tracking

## Query Parameters

### Pagination
- `page` - Page number (0-based, default: 0)
- `size` - Page size (default: 50, max: 500)
- `sort` - Sort field and direction (e.g., `createdAt,desc` or `name,asc`, default: `updatedAt,desc`)

### Date Filtering
- `from` - Start datetime (ISO-8601 format, inclusive)
- `to` - End datetime (ISO-8601 format, inclusive)

### Filtering
- Various filters available per endpoint (status, platform, carrier, etc.)

## Example API Calls

### Create Organization

```bash
curl -X POST http://localhost:8080/organizations \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corp",
    "status": "ACTIVE",
    "externalId": "EXT-001"
  }'
```

### Create Website

```bash
curl -X POST http://localhost:8080/organizations/{orgId}/websites \
  -H "Content-Type: application/json" \
  -d '{
    "code": "STORE-001",
    "name": "Main Store",
    "platform": "SHOPIFY",
    "domain": "store.example.com",
    "status": "ACTIVE"
  }'
```

### Create Order

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "orgId": "uuid-here",
    "websiteId": "uuid-here",
    "externalOrderId": "ORD-12345",
    "orderTotal": 99.99,
    "currency": "USD",
    "status": "CREATED",
    "financialStatus": "PENDING",
    "fulfillmentStatus": "UNFULFILLED"
  }'
```

### List Orders with Pagination

```bash
curl "http://localhost:8080/orders?orgId={uuid}&page=0&size=20&sort=createdAt,desc"
```

## Error Responses

All errors are returned in JSON format:

```json
{
  "timestamp": "2026-01-22T01:00:00Z",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Organization not found with id: ...",
  "path": "/organizations/123"
}
```

## Database Schema

The application uses the following main tables:
- `tenant` - Organizations
- `store` - Websites
- `orders` - Orders
- `fulfillments` - Fulfillments
- `tracking` - Tracking information

**Note**: The application uses `ddl-auto: validate`, which means it validates the schema but doesn't create/modify tables. Ensure your database schema matches the entity definitions.

## Project Structure

```
src/main/java/com/fenixcommerce/
├── entity/          # JPA entities
├── repository/      # Spring Data JPA repositories
├── service/         # Service interfaces
├── service/impl/    # Service implementations
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── exception/      # Custom exceptions and handlers
└── config/         # Configuration classes
```

## Development

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package
java -jar target/fenixcommerce-backend-0.0.1-SNAPSHOT.jar
```

## Configuration

Key configuration in `application.yml`:

- **Database**: MySQL connection settings
- **JPA**: Hibernate settings (ddl-auto: validate, show-sql: true)
- **Server**: Default port 8080

## Troubleshooting

### Database Connection Issues

1. Ensure MySQL is running:
   ```bash
   # Windows
   net start MySQL
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. Verify database credentials in `application.yml` or environment variables

3. Check if database exists:
   ```sql
   SHOW DATABASES;
   ```

### Port Already in Use

Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

## License

This project is part of a coding challenge for FenixCommerce.

## Author

Harsimran
