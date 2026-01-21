# Testing Guide

## Quick Test Checklist

### 1. Verify Application Starts
```bash
mvn spring-boot:run
```
✅ Should see: "Started FenixcommerceBackendApplication"

### 2. Test Basic Endpoints

#### Create Organization
```bash
curl -X POST http://localhost:8080/organizations \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Org", "status": "ACTIVE"}'
```
✅ Should return 201 Created with organization data

#### List Organizations
```bash
curl http://localhost:8080/organizations
```
✅ Should return 200 OK with paginated response

#### Get Organization by ID
```bash
curl http://localhost:8080/organizations/{id-from-previous-response}
```
✅ Should return 200 OK with organization data

### 3. Test Error Handling

#### Get Non-existent Organization
```bash
curl http://localhost:8080/organizations/00000000-0000-0000-0000-000000000000
```
✅ Should return 404 Not Found with error JSON

#### Create Organization with Invalid Data
```bash
curl -X POST http://localhost:8080/organizations \
  -H "Content-Type: application/json" \
  -d '{"name": "A"}'
```
✅ Should return 400 Bad Request with validation errors

## Complete Test Workflow

1. Create Organization → Save orgId
2. Create Website → Save websiteId
3. Create Order → Save orderId
4. Create Fulfillment → Save fulfillmentId
5. Create Tracking → Verify success
6. Test pagination on all list endpoints
7. Test search endpoints
8. Test update (PUT) and patch (PATCH)
9. Test delete operations

## Expected Results

- ✅ All endpoints return proper HTTP status codes
- ✅ Pagination works (page, size, totalElements, etc.)
- ✅ Date filtering works
- ✅ Search endpoints return correct results
- ✅ Error responses are in JSON format
- ✅ Validation errors are properly formatted
