# Quick Start Guide

## Prerequisites Check

```bash
# Check Java version (need 17+)
java -version

# Check Maven
mvn -version

# Check MySQL
mysql --version
```

## Step-by-Step Setup

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

### 2. Create Database and Run Schema

```bash
# Login to MySQL
mysql -u root -p

# Run these commands:
CREATE DATABASE IF NOT EXISTS fenixcommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE fenixcommerce;
SOURCE src/main/resources/db/schema.sql;
EXIT;
```

**Or use command line:**
```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

### 3. Configure Database (Optional)

If your MySQL credentials are different, set environment variables:

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

### 4. Run the Application

```bash
cd fenixcommerce-backend
mvn spring-boot:run
```

**Expected output:**
```
Started FenixcommerceBackendApplication in X.XXX seconds
Tomcat started on port 8080
```

### 5. Test the Application

**Open new terminal and test:**

```bash
# Test Organizations endpoint
curl http://localhost:8080/organizations

# Should return: {"data":[],"page":0,"size":50,"totalElements":0,"totalPages":0,"hasNext":false}
```

## Common Issues & Solutions

### Issue: MySQL Connection Refused

**Solution:**
1. Ensure MySQL is running: `net start MySQL` (Windows) or `sudo systemctl start mysql` (Linux)
2. Check MySQL is on port 3306: `netstat -an | findstr 3306` (Windows)
3. Verify credentials in `application.yml` or environment variables

### Issue: Table doesn't exist

**Solution:**
Run the schema script:
```bash
mysql -u root -p fenixcommerce < src/main/resources/db/schema.sql
```

### Issue: Port 8080 already in use

**Solution:**
Change port in `application.yml`:
```yaml
server:
  port: 8081
```

### Issue: Compilation errors

**Solution:**
```bash
mvn clean compile
```

## Verification Checklist

- [ ] MySQL is running
- [ ] Database `fenixcommerce` exists
- [ ] Tables created (tenant, store, orders, fulfillments, tracking)
- [ ] Application starts without errors
- [ ] Can access `http://localhost:8080/organizations`

## Next Steps

1. Test creating an organization
2. Test creating a website
3. Test creating an order
4. Review API_EXAMPLES.md for detailed API usage

## Quick Test Commands

```bash
# Create Organization
curl -X POST http://localhost:8080/organizations \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Test Org\",\"status\":\"ACTIVE\"}"

# List Organizations
curl http://localhost:8080/organizations

# Get Organization (replace {id} with actual UUID)
curl http://localhost:8080/organizations/{id}
```
