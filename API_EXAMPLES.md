# API Examples

This document provides detailed examples for using the FenixCommerce Backend API.

## Base URL

```
http://localhost:8080
```

## Authentication

Currently, the API does not require authentication. In production, implement proper authentication/authorization.

## Common Response Formats

### Success Response (200 OK)
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Example",
  ...
}
```

### Paginated Response
```json
{
  "data": [...],
  "page": 0,
  "size": 50,
  "totalElements": 100,
  "totalPages": 2,
  "hasNext": true
}
```

### Error Response
```json
{
  "timestamp": "2026-01-22T01:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "validationErrors": {
    "name": "Name is required"
  }
}
```

## Organizations API

### 1. Create Organization

```bash
POST /organizations
Content-Type: application/json

{
  "name": "Acme Corporation",
  "status": "ACTIVE",
  "externalId": "ORG-001"
}
```

**Response:** `201 Created`

### 2. List Organizations

```bash
GET /organizations?page=0&size=20&sort=name,asc&status=ACTIVE
```

**Query Parameters:**
- `from` - Start date (ISO-8601)
- `to` - End date (ISO-8601)
- `name` - Name contains (case-insensitive)
- `status` - ACTIVE or INACTIVE
- `page` - Page number (0-based)
- `size` - Page size (1-500)
- `sort` - Sort field and direction

### 3. Search Organizations by External ID

```bash
GET /organizations/search?externalId=ORG-001&page=0&size=10
```

### 4. Get Organization by ID

```bash
GET /organizations/{id}
```

### 5. Update Organization (Full)

```bash
PUT /organizations/{id}
Content-Type: application/json

{
  "name": "Acme Corporation Updated",
  "status": "ACTIVE",
  "externalId": "ORG-001"
}
```

### 6. Patch Organization (Partial)

```bash
PATCH /organizations/{id}
Content-Type: application/json

{
  "status": "INACTIVE"
}
```

### 7. Delete Organization

```bash
DELETE /organizations/{id}
```

**Response:** `204 No Content`

## Websites API

### 1. Create Website

```bash
POST /organizations/{orgId}/websites
Content-Type: application/json

{
  "code": "STORE-001",
  "name": "Main Store",
  "platform": "SHOPIFY",
  "domain": "store.example.com",
  "status": "ACTIVE"
}
```

**Response:** `201 Created`

### 2. List Websites

```bash
GET /organizations/{orgId}/websites?page=0&size=20&platform=SHOPIFY&status=ACTIVE
```

**Query Parameters:**
- `from`, `to` - Date range
- `status` - ACTIVE or INACTIVE
- `platform` - SHOPIFY, NETSUITE, CUSTOM, MAGENTO, OTHER
- `code` - Website code
- `domain` - Website domain
- `page`, `size`, `sort` - Pagination

### 3. Search Websites

```bash
GET /organizations/{orgId}/websites/search?code=STORE-001&page=0&size=10
```

**Query Parameters:**
- `websiteId` - UUID
- `code` - Website code
- `domain` - Website domain

## Orders API

### 1. Create/Upsert Order

```bash
POST /orders
Content-Type: application/json

{
  "orgId": "550e8400-e29b-41d4-a716-446655440000",
  "websiteId": "660e8400-e29b-41d4-a716-446655440000",
  "externalOrderId": "ORD-12345",
  "externalOrderNumber": "12345",
  "status": "CREATED",
  "financialStatus": "PENDING",
  "fulfillmentStatus": "UNFULFILLED",
  "customerEmail": "customer@example.com",
  "orderTotal": 99.99,
  "currency": "USD",
  "orderCreatedAt": "2026-01-22T10:00:00",
  "orderUpdatedAt": "2026-01-22T10:00:00"
}
```

**Note:** If an order with the same `orgId`, `websiteId`, and `externalOrderId` exists, it will be updated.

**Response:** `201 Created` (new) or `200 OK` (updated)

### 2. Search Orders

```bash
GET /orders?orgId={uuid}&websiteId={uuid}&page=0&size=20&status=CREATED
```

**Query Parameters:**
- `orgId` - **Required** - Organization UUID
- `websiteId` - Optional - Website UUID
- `from`, `to` - Date range (ISO-8601)
- `status` - CREATED, CANCELLED, CLOSED
- `financialStatus` - UNKNOWN, PENDING, PAID, PARTIALLY_PAID, REFUNDED, PARTIALLY_REFUNDED, VOIDED
- `fulfillmentStatus` - UNFULFILLED, PARTIAL, FULFILLED, CANCELLED, UNKNOWN
- `page`, `size`, `sort` - Pagination

### 3. Search Orders by External ID

```bash
GET /orders/search?orgId={uuid}&externalOrderId=ORD-12345&page=0&size=10
```

**Query Parameters:**
- `orgId` - **Required**
- `websiteId` - Optional
- `externalOrderId` - Optional
- `externalOrderNumber` - Optional

## Fulfillments API

### 1. Create Fulfillment

```bash
POST /orders/{orderId}/fulfillments
Content-Type: application/json

{
  "externalFulfillmentId": "FUL-001",
  "status": "CREATED",
  "carrier": "UPS",
  "serviceLevel": "GROUND",
  "shippedAt": "2026-01-22T12:00:00",
  "deliveredAt": null
}
```

**Response:** `201 Created`

### 2. List Fulfillments

```bash
GET /orders/{orderId}/fulfillments?page=0&size=20&status=SHIPPED&carrier=UPS
```

**Query Parameters:**
- `from`, `to` - Date range
- `status` - CREATED, SHIPPED, DELIVERED, CANCELLED, FAILED, UNKNOWN
- `carrier` - Carrier name
- `page`, `size`, `sort` - Pagination

## Tracking API

### 1. Create Tracking

```bash
POST /fulfillments/{fulfillmentId}/tracking
Content-Type: application/json

{
  "trackingNumber": "1Z999AA10123456784",
  "carrier": "UPS",
  "trackingUrl": "https://www.ups.com/track?tracknum=1Z999AA10123456784",
  "status": "IN_TRANSIT",
  "isPrimary": true,
  "lastEventAt": "2026-01-22T14:00:00Z"
}
```

**Response:** `201 Created`

### 2. List Tracking

```bash
GET /fulfillments/{fulfillmentId}/tracking?page=0&size=20&status=IN_TRANSIT
```

**Query Parameters:**
- `from`, `to` - Date range
- `status` - LABEL_CREATED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, EXCEPTION, UNKNOWN
- `carrier` - Carrier name
- `trackingNumber` - Tracking number
- `page`, `size`, `sort` - Pagination

## Complete Workflow Example

### Step 1: Create Organization
```bash
curl -X POST http://localhost:8080/organizations \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Company",
    "status": "ACTIVE"
  }'
```

**Response:** Save the `id` from the response (e.g., `org-id-123`)

### Step 2: Create Website
```bash
curl -X POST http://localhost:8080/organizations/{org-id-123}/websites \
  -H "Content-Type: application/json" \
  -d '{
    "code": "MAIN-STORE",
    "name": "Main Store",
    "platform": "SHOPIFY",
    "status": "ACTIVE"
  }'
```

**Response:** Save the `id` from the response (e.g., `website-id-456`)

### Step 3: Create Order
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "orgId": "org-id-123",
    "websiteId": "website-id-456",
    "externalOrderId": "ORD-001",
    "orderTotal": 150.00,
    "currency": "USD",
    "status": "CREATED",
    "financialStatus": "PENDING",
    "fulfillmentStatus": "UNFULFILLED"
  }'
```

**Response:** Save the `id` from the response (e.g., `order-id-789`)

### Step 4: Create Fulfillment
```bash
curl -X POST http://localhost:8080/orders/{order-id-789}/fulfillments \
  -H "Content-Type: application/json" \
  -d '{
    "externalFulfillmentId": "FUL-001",
    "status": "SHIPPED",
    "carrier": "UPS"
  }'
```

**Response:** Save the `id` from the response (e.g., `fulfillment-id-101`)

### Step 5: Create Tracking
```bash
curl -X POST http://localhost:8080/fulfillments/{fulfillment-id-101}/tracking \
  -H "Content-Type: application/json" \
  -d '{
    "trackingNumber": "1Z999AA10123456784",
    "carrier": "UPS",
    "status": "IN_TRANSIT",
    "isPrimary": true
  }'
```

## Date Format

All dates should be in ISO-8601 format:
- Date-time: `2026-01-22T10:00:00` or `2026-01-22T10:00:00Z`
- Date only: `2026-01-22`

## Enum Values

### OrgStatus / WebsiteStatus
- `ACTIVE`
- `INACTIVE`

### Platform
- `SHOPIFY`
- `NETSUITE`
- `CUSTOM`
- `MAGENTO`
- `OTHER`

### OrderStatus
- `CREATED`
- `CANCELLED`
- `CLOSED`

### FinancialStatus
- `UNKNOWN`
- `PENDING`
- `PAID`
- `PARTIALLY_PAID`
- `REFUNDED`
- `PARTIALLY_REFUNDED`
- `VOIDED`

### FulfillmentOverallStatus
- `UNFULFILLED`
- `PARTIAL`
- `FULFILLED`
- `CANCELLED`
- `UNKNOWN`

### FulfillmentStatus (Tracking)
- `CREATED`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`
- `FAILED`
- `UNKNOWN`

### TrackingStatus
- `LABEL_CREATED`
- `IN_TRANSIT`
- `OUT_FOR_DELIVERY`
- `DELIVERED`
- `EXCEPTION`
- `UNKNOWN`
