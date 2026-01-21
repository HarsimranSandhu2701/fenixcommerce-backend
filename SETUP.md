# Quick Setup Guide

## Prerequisites Check

1. **Java 17+**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   ```

## Quick Start (5 minutes)

### 1. Start MySQL

**Windows:**
```powershell
net start MySQL
```

**Linux/Mac:**
```bash
sudo systemctl start mysql
# or
brew services start mysql
```

### 2. Set Environment Variables (Optional)

**Windows PowerShell:**
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
```

**Linux/Mac:**
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

**Note:** If not set, defaults to `root` with empty password.

### 3. Run the Application

```bash
cd fenixcommerce-backend
mvn spring-boot:run
```

### 4. Verify It's Running

Open browser: http://localhost:8080

Or test with curl:
```bash
curl http://localhost:8080/organizations
```

## Troubleshooting

### MySQL Connection Error

**Error:** `Connection refused` or `Communications link failure`

**Solution:**
1. Check if MySQL is running
2. Verify MySQL is on port 3306
3. Check username/password
4. Ensure database `fenixcommerce` exists or can be created

**Create database manually:**
```sql
CREATE DATABASE fenixcommerce;
```

### Port 8080 Already in Use

**Solution:** Change port in `application.yml`:
```yaml
server:
  port: 8081
```

### Compilation Errors

**Solution:**
```bash
mvn clean install
```

## Next Steps

1. Read [README.md](README.md) for full documentation
2. Check [API_EXAMPLES.md](API_EXAMPLES.md) for API usage examples
3. Test the endpoints using Postman or curl

## Database Schema

The application uses `ddl-auto: validate`, which means:
- ✅ Validates existing schema
- ❌ Does NOT create/modify tables

**You need to:**
- Create tables manually, OR
- Use a database migration tool, OR
- Temporarily change to `ddl-auto: update` for development (not recommended for production)

## Production Deployment

1. Set proper database credentials via environment variables
2. Change `ddl-auto` to `validate` or `none`
3. Configure proper logging levels
4. Set up proper authentication/authorization
5. Use a production-grade database connection pool
