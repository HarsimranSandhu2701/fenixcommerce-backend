# Database Setup Guide

## Quick Setup

### 1. Create Database

```sql
CREATE DATABASE IF NOT EXISTS fenixcommerce
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE fenixcommerce;
```

### 2. Run Schema Script

Execute the SQL script located at:
```
src/main/resources/db/schema.sql
```

Or run directly:
```bash
mysql -u root -p fenixcommerce < src/main/resources/db/schema.sql
```

### 3. Verify Tables

```sql
SHOW TABLES;
-- Should show: tenant, store, orders, fulfillments, tracking
```

## Schema Overview

### Tables Created

1. **tenant** - Organizations
   - Primary Key: `tenant_id` (BINARY(16))
   - Unique: `tenant_name`

2. **store** - Websites
   - Primary Key: `store_id` (BINARY(16))
   - Foreign Key: `tenant_id` → tenant
   - Unique: `(tenant_id, store_code)` - code unique per tenant

3. **orders** - Orders
   - Primary Key: `order_id` (BINARY(16))
   - Foreign Keys: `tenant_id` → tenant, `store_id` → store
   - Unique: `(tenant_id, store_id, external_order_id)`

4. **fulfillments** - Fulfillments
   - Primary Key: `fulfillment_id` (BINARY(16))
   - Foreign Keys: `tenant_id` → tenant, `order_id` → orders
   - Unique: `(tenant_id, order_id, external_fulfillment_id)`

5. **tracking** - Tracking Information
   - Primary Key: `tracking_id` (BINARY(16))
   - Foreign Keys: `tenant_id` → tenant, `fulfillment_id` → fulfillments
   - Unique: `(tenant_id, tracking_number)`

### Important Notes

- All UUIDs are stored as `BINARY(16)` using `UUID_TO_BIN(..., 1)`
- The application uses `ddl-auto: validate` - it will NOT create/modify tables
- You MUST run the schema script before starting the application
- Foreign key constraints enforce referential integrity
- Unique constraints prevent duplicate external IDs per tenant

## Testing Database Connection

```sql
-- Test connection
SELECT DATABASE();

-- Check if tables exist
SELECT TABLE_NAME 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'fenixcommerce';

-- Verify a table structure
DESCRIBE tenant;
```

## Troubleshooting

### Error: Table doesn't exist
**Solution:** Run the schema.sql script

### Error: Foreign key constraint fails
**Solution:** Ensure parent records exist (e.g., tenant before store)

### Error: Duplicate entry for unique key
**Solution:** Check unique constraints - external IDs must be unique per tenant

## Sample Data (Optional)

```sql
-- Insert sample tenant
INSERT INTO tenant (tenant_id, tenant_name, status)
VALUES (UUID_TO_BIN(UUID(), 1), 'Test Organization', 'ACTIVE');

-- Get the tenant_id
SELECT BIN_TO_UUID(tenant_id, 1) AS tenant_uuid FROM tenant WHERE tenant_name = 'Test Organization';
```
