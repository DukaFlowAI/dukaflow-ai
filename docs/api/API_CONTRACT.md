# DukaFlow AI REST API Contract and Conventions

**Document status:** Draft for contributor review  
**Related GitHub issue:** #9 — Define REST API contract and conventions  
**Author:** JobMunyoki  
**Reviewer:** PURITY-CODES-dev  
**Target:** MVP API baseline  

---

## 1. Purpose

This document defines the initial REST API contract used by the DukaFlow AI React frontend and Spring Boot backend.

The contract covers:

- URL structure
- Authentication
- Authorization
- Request and response formats
- Pagination, sorting and filtering
- Validation and error responses
- Idempotency
- Resource endpoints
- Branch and business data isolation
- Health and documentation endpoints

The API contract is an implementation baseline. Exact DTO fields may be refined during coding, but breaking changes must be reviewed by both contributors.

---

## 2. API Design Principles

1. The backend is the authoritative source of business rules.
2. All protected endpoints require authentication.
3. Authorization is enforced on the backend.
4. Every business-owned resource is tenant-scoped.
5. Every branch-owned resource is branch-scoped.
6. Requests and responses use JSON unless an endpoint explicitly returns a file.
7. URLs use nouns rather than action verbs where practical.
8. HTTP methods communicate intent.
9. Errors use one consistent structure.
10. Large collections use pagination.
11. Multi-step business operations use database transactions.
12. Completed transactions use cancellation, reversal or return workflows rather than deletion.
13. API changes are versioned under `/api/v1`.
14. Sensitive values are never exposed in responses.
15. External payment callbacks are processed idempotently.

---

## 3. Base URL and Versioning

### Local development

```text
http://localhost:8080/api/v1
```

### Production pattern

```text
https://<backend-domain>/api/v1
```

### Versioning rule

The API version appears in the URL:

```text
/api/v1
```

Breaking changes require a new major version such as:

```text
/api/v2
```

Non-breaking additions may remain in the current version.

---

## 4. Content Type and Encoding

All JSON requests must send:

```http
Content-Type: application/json
Accept: application/json
```

Character encoding:

```text
UTF-8
```

CSV export endpoints return:

```http
Content-Type: text/csv
```

---

## 5. Authentication

Protected requests use a JWT bearer token:

```http
Authorization: Bearer <access-token>
```

The access token identifies the authenticated user. The backend must still load or verify the user's active state, business, role and branch permissions.

### 5.1 Login request

```http
POST /api/v1/auth/login
```

```json
{
  "identifier": "owner@dukaflow.example",
  "password": "user-supplied-password"
}
```

`identifier` may contain an email address or username.

### 5.2 Login response

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "<jwt-token>",
    "tokenType": "Bearer",
    "expiresInSeconds": 3600,
    "user": {
      "id": 12,
      "fullName": "Example Owner",
      "email": "owner@dukaflow.example",
      "role": {
        "id": 1,
        "name": "BUSINESS_OWNER"
      },
      "business": {
        "id": 4,
        "name": "Example Retailers"
      },
      "branches": [
        {
          "id": 7,
          "code": "MAIN",
          "name": "Main Branch"
        }
      ]
    }
  },
  "timestamp": "2026-08-03T13:00:00Z"
}
```

### 5.3 Authentication endpoints

| Method | Endpoint | Authentication | Purpose |
|---|---|---|---|
| POST | `/auth/login` | Public | Sign in |
| POST | `/auth/logout` | Required | End current session |
| GET | `/auth/me` | Required | Get current user profile |
| POST | `/auth/refresh` | Conditional | Refresh an access token |
| POST | `/auth/change-password` | Required | Change current password |

Password-reset endpoints may be added after email delivery is configured.

---

## 6. Authorization and Scope

### 6.1 Business scope

The backend derives `businessId` from the authenticated user.

Clients must not be allowed to switch tenant scope by submitting another business identifier.

Backend rule:

```text
resource.business_id = authenticated_user.business_id
```

### 6.2 Branch scope

Branch-restricted users may only use branches assigned to them.

Backend rule:

```text
resource.branch_id IN authenticated_user.assigned_branch_ids
```

### 6.3 Access-denied response

Unauthorized action:

```http
403 Forbidden
```

```json
{
  "success": false,
  "code": "ACCESS_DENIED",
  "message": "You do not have permission to perform this action.",
  "errors": [],
  "timestamp": "2026-08-03T13:05:00Z",
  "path": "/api/v1/users"
}
```

### 6.4 Role names

Initial role identifiers:

```text
BUSINESS_OWNER
ADMINISTRATOR
BRANCH_MANAGER
CASHIER
STOREKEEPER
ACCOUNTANT
```

Detailed permissions are defined by the approved role-permission matrix.

---

## 7. HTTP Method Conventions

| Method | Purpose |
|---|---|
| GET | Retrieve a resource or collection |
| POST | Create a resource or execute a non-idempotent business command |
| PUT | Replace a complete editable resource |
| PATCH | Update selected fields |
| DELETE | Remove an eligible draft or deactivate a master-data record |

Completed purchases, sales, payments, stock movements, expenses and audit logs are not physically deleted.

Business commands may use explicit sub-resources:

```text
POST /purchases/{id}/complete
POST /purchases/{id}/cancel
POST /sales/{id}/complete
POST /sales/{id}/cancel
POST /sales/{id}/returns
POST /stock-transfers/{id}/complete
```

---

## 8. Naming Conventions

### URLs

- Lowercase
- Hyphen-separated
- Plural resource names

Examples:

```text
/product-categories
/stock-movements
/expense-categories
```

### JSON fields

Use lower camel case:

```json
{
  "businessId": 4,
  "branchId": 7,
  "createdAt": "2026-08-03T13:00:00Z"
}
```

### Identifiers

Resource identifiers are numeric in the MVP:

```text
BIGINT UNSIGNED
```

Public opaque identifiers may be introduced later.

---

## 9. Standard Success Responses

### 9.1 Single resource

```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 101,
    "sku": "FEED-001",
    "name": "Dairy Meal 70kg"
  },
  "timestamp": "2026-08-03T13:00:00Z"
}
```

### 9.2 Created resource

```http
201 Created
Location: /api/v1/products/101
```

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 101,
    "sku": "FEED-001",
    "name": "Dairy Meal 70kg"
  },
  "timestamp": "2026-08-03T13:00:00Z"
}
```

### 9.3 No response body

Use:

```http
204 No Content
```

only when the frontend does not need confirmation data.

For most business operations, return an updated resource and a clear message.

---

## 10. Pagination, Sorting and Filtering

### 10.1 Pagination query parameters

```text
page=0
size=20
```

Rules:

- `page` is zero-based.
- Default `size` is 20.
- Maximum `size` is 100.
- Invalid pagination values return `400 Bad Request`.

### 10.2 Sorting

```text
sort=createdAt,desc
sort=name,asc
```

Multiple sorts may be supported:

```text
sort=branchName,asc&sort=createdAt,desc
```

Only approved sortable fields are accepted.

### 10.3 Common filters

```text
search=
branchId=
status=
fromDate=
toDate=
createdBy=
```

Example:

```http
GET /api/v1/sales?page=0&size=20&branchId=7&status=COMPLETED&fromDate=2026-08-01&toDate=2026-08-31&sort=saleDate,desc
```

### 10.4 Paginated response

```json
{
  "success": true,
  "message": "Sales retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true,
    "sort": [
      {
        "property": "saleDate",
        "direction": "DESC"
      }
    ]
  },
  "timestamp": "2026-08-03T13:00:00Z"
}
```

---

## 11. Validation

### 11.1 Validation rules

The backend validates:

- Required fields
- Length limits
- Numeric ranges
- Date ranges
- Unique values
- Valid status transitions
- Business and branch ownership
- Available stock
- Active-state requirements
- User permissions

Frontend validation improves usability but never replaces backend validation.

### 11.2 Validation error response

```http
400 Bad Request
```

```json
{
  "success": false,
  "code": "VALIDATION_ERROR",
  "message": "One or more fields are invalid.",
  "errors": [
    {
      "field": "name",
      "message": "Name is required."
    },
    {
      "field": "sellingPrice",
      "message": "Selling price must be greater than or equal to zero."
    }
  ],
  "timestamp": "2026-08-03T13:10:00Z",
  "path": "/api/v1/products"
}
```

---

## 12. Standard Error Contract

```json
{
  "success": false,
  "code": "RESOURCE_NOT_FOUND",
  "message": "The requested product was not found.",
  "errors": [],
  "timestamp": "2026-08-03T13:15:00Z",
  "path": "/api/v1/products/999"
}
```

### 12.1 HTTP status usage

| Status | Meaning |
|---:|---|
| 200 | Successful read or update |
| 201 | Resource created |
| 204 | Successful operation with no body |
| 400 | Validation or malformed request |
| 401 | Missing or invalid authentication |
| 403 | Authenticated but not authorized |
| 404 | Resource not found within permitted scope |
| 409 | Conflict, duplicate or invalid state transition |
| 422 | Semantically invalid business request, when used |
| 429 | Rate limit exceeded |
| 500 | Unexpected internal error |
| 503 | Service temporarily unavailable |

### 12.2 Standard error codes

```text
AUTHENTICATION_REQUIRED
INVALID_CREDENTIALS
TOKEN_EXPIRED
ACCESS_DENIED
VALIDATION_ERROR
RESOURCE_NOT_FOUND
DUPLICATE_RESOURCE
INVALID_STATE_TRANSITION
INSUFFICIENT_STOCK
BUSINESS_SCOPE_VIOLATION
BRANCH_SCOPE_VIOLATION
PAYMENT_CONFLICT
INTEGRATION_ERROR
INTERNAL_SERVER_ERROR
```

Production errors must not reveal stack traces, SQL statements or secrets.

---

## 13. Date, Time, Money and Quantity

### Date and time

Use ISO 8601:

```text
2026-08-03T13:20:00Z
```

Local calendar dates:

```text
2026-08-03
```

The backend stores timestamps consistently, preferably in UTC.

### Money

JSON money values are represented as numbers with fixed decimal precision:

```json
{
  "totalAmount": 12500.00
}
```

Java uses `BigDecimal`.

### Quantity

Quantities support up to three decimal places:

```json
{
  "quantity": 12.500
}
```

---

## 14. Idempotency and Concurrency

### 14.1 Transaction completion

The backend must prevent repeated completion of:

- Purchases
- Sales
- Stock transfers
- Returns
- Payment callbacks

A repeated completion request returns:

```http
409 Conflict
```

with:

```text
INVALID_STATE_TRANSITION
```

### 14.2 External callbacks

M-Pesa callback processing uses a unique provider request identifier.

The same callback must not update a payment twice.

### 14.3 Optimistic locking

Mutable high-risk records may include a version field:

```json
{
  "version": 3
}
```

Conflicting updates may return:

```http
409 Conflict
```

---

# 15. Endpoint Catalogue

## 15.1 Business Profile

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/business` | Get current business |
| PATCH | `/business` | Update current business |

The business scope comes from the authenticated user.

---

## 15.2 Branches

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/branches` | List permitted branches |
| POST | `/branches` | Create branch |
| GET | `/branches/{branchId}` | Get branch |
| PATCH | `/branches/{branchId}` | Update branch |
| POST | `/branches/{branchId}/activate` | Activate branch |
| POST | `/branches/{branchId}/deactivate` | Deactivate branch |

### Create branch request

```json
{
  "branchCode": "MAIN",
  "name": "Main Branch",
  "phone": "+254700000000",
  "email": "main@example.com",
  "addressLine": "Nairobi, Kenya"
}
```

---

## 15.3 Users

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/users` | List users |
| POST | `/users` | Create user |
| GET | `/users/{userId}` | Get user |
| PATCH | `/users/{userId}` | Update user |
| POST | `/users/{userId}/activate` | Activate user |
| POST | `/users/{userId}/deactivate` | Deactivate user |
| PUT | `/users/{userId}/branches` | Replace branch assignments |
| POST | `/users/{userId}/reset-password` | Administrative password reset |

### Create user request

```json
{
  "fullName": "Example Cashier",
  "email": "cashier@example.com",
  "username": "cashier1",
  "phone": "+254700000001",
  "roleId": 4,
  "branchIds": [7],
  "temporaryPassword": "temporary-password"
}
```

Responses never include `passwordHash` or any password value.

---

## 15.4 Roles

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/roles` | List active roles |
| GET | `/roles/{roleId}` | Get role |
| GET | `/roles/{roleId}/permissions` | Get role permissions |

Custom role creation may be deferred until after the MVP permission model is finalized.

---

## 15.5 Product Categories

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/product-categories` | List categories |
| POST | `/product-categories` | Create category |
| GET | `/product-categories/{categoryId}` | Get category |
| PATCH | `/product-categories/{categoryId}` | Update category |
| POST | `/product-categories/{categoryId}/activate` | Activate |
| POST | `/product-categories/{categoryId}/deactivate` | Deactivate |

---

## 15.6 Products

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/products` | List and search products |
| POST | `/products` | Create product |
| GET | `/products/{productId}` | Get product |
| PATCH | `/products/{productId}` | Update product |
| POST | `/products/{productId}/activate` | Activate product |
| POST | `/products/{productId}/deactivate` | Deactivate product |

### Create product request

```json
{
  "sku": "FEED-001",
  "name": "Dairy Meal 70kg",
  "description": "Animal feed product",
  "categoryId": 8,
  "unitOfMeasure": "BAG",
  "costPrice": 3200.00,
  "sellingPrice": 3650.00,
  "reorderLevel": 10.000
}
```

---

## 15.7 Inventory

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/inventory` | List inventory balances |
| GET | `/inventory/{inventoryId}` | Get inventory balance |
| GET | `/inventory/low-stock` | List low-stock products |
| GET | `/stock-movements` | List stock movements |
| POST | `/stock-adjustments` | Create stock adjustment |
| POST | `/stock-transfers` | Create stock transfer |
| GET | `/stock-transfers` | List transfers |
| GET | `/stock-transfers/{transferId}` | Get transfer |
| POST | `/stock-transfers/{transferId}/complete` | Complete transfer |
| POST | `/stock-transfers/{transferId}/cancel` | Cancel transfer |

### Stock adjustment request

```json
{
  "branchId": 7,
  "productId": 101,
  "adjustmentType": "INCREASE",
  "quantity": 5.000,
  "reason": "Physical count correction"
}
```

### Stock transfer request

```json
{
  "sourceBranchId": 7,
  "destinationBranchId": 9,
  "items": [
    {
      "productId": 101,
      "quantity": 4.000
    }
  ],
  "notes": "Replenish branch stock"
}
```

---

## 15.8 Suppliers

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/suppliers` | List suppliers |
| POST | `/suppliers` | Create supplier |
| GET | `/suppliers/{supplierId}` | Get supplier |
| PATCH | `/suppliers/{supplierId}` | Update supplier |
| POST | `/suppliers/{supplierId}/activate` | Activate |
| POST | `/suppliers/{supplierId}/deactivate` | Deactivate |
| GET | `/suppliers/{supplierId}/purchases` | Supplier purchase history |

---

## 15.9 Purchases

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/purchases` | List purchases |
| POST | `/purchases` | Create draft purchase |
| GET | `/purchases/{purchaseId}` | Get purchase |
| PATCH | `/purchases/{purchaseId}` | Update draft |
| POST | `/purchases/{purchaseId}/complete` | Complete purchase |
| POST | `/purchases/{purchaseId}/cancel` | Cancel purchase |

### Create purchase request

```json
{
  "branchId": 7,
  "supplierId": 22,
  "supplierInvoiceNumber": "SUP-INV-1008",
  "purchaseDate": "2026-08-03T10:00:00Z",
  "items": [
    {
      "productId": 101,
      "quantity": 20.000,
      "unitCost": 3200.00,
      "discountAmount": 0.00,
      "taxAmount": 0.00
    }
  ]
}
```

### Purchase response summary

```json
{
  "id": 301,
  "purchaseNumber": "PUR-2026-000301",
  "status": "DRAFT",
  "branchId": 7,
  "supplierId": 22,
  "subtotal": 64000.00,
  "discountTotal": 0.00,
  "taxTotal": 0.00,
  "totalAmount": 64000.00,
  "items": []
}
```

---

## 15.10 Customers

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/customers` | List customers |
| POST | `/customers` | Create customer |
| GET | `/customers/{customerId}` | Get customer |
| PATCH | `/customers/{customerId}` | Update customer |
| POST | `/customers/{customerId}/activate` | Activate |
| POST | `/customers/{customerId}/deactivate` | Deactivate |
| GET | `/customers/{customerId}/sales` | Customer sales history |

---

## 15.11 Sales

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/sales` | List sales |
| POST | `/sales` | Create sale |
| GET | `/sales/{saleId}` | Get sale |
| PATCH | `/sales/{saleId}` | Update eligible draft |
| POST | `/sales/{saleId}/complete` | Complete sale |
| POST | `/sales/{saleId}/cancel` | Cancel sale |
| POST | `/sales/{saleId}/returns` | Create sales return |
| GET | `/sales/{saleId}/receipt` | Get printable receipt data |

### Create sale request

```json
{
  "branchId": 7,
  "customerId": 44,
  "saleDate": "2026-08-03T13:30:00Z",
  "items": [
    {
      "productId": 101,
      "quantity": 2.000,
      "unitPrice": 3650.00,
      "discountAmount": 100.00,
      "taxAmount": 0.00
    }
  ],
  "payments": [
    {
      "paymentMethod": "MPESA",
      "amount": 7200.00,
      "externalReference": "TEST-REFERENCE"
    }
  ]
}
```

The backend recalculates all totals and never trusts client-calculated totals.

---

## 15.12 Payments

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/payments` | List payments |
| GET | `/payments/{paymentId}` | Get payment |
| POST | `/payments/mpesa/stk-push` | Initiate Daraja sandbox request |
| POST | `/payments/mpesa/callback` | Receive Daraja callback |
| POST | `/payments/{paymentId}/refund` | Record supported refund |

The callback endpoint is public to the provider but protected through callback validation, unique identifiers and idempotent processing.

---

## 15.13 Expense Categories

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/expense-categories` | List categories |
| POST | `/expense-categories` | Create category |
| PATCH | `/expense-categories/{categoryId}` | Update category |
| POST | `/expense-categories/{categoryId}/activate` | Activate |
| POST | `/expense-categories/{categoryId}/deactivate` | Deactivate |

---

## 15.14 Expenses

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/expenses` | List expenses |
| POST | `/expenses` | Create expense |
| GET | `/expenses/{expenseId}` | Get expense |
| PATCH | `/expenses/{expenseId}` | Update draft |
| POST | `/expenses/{expenseId}/complete` | Complete expense |
| POST | `/expenses/{expenseId}/cancel` | Cancel expense |

### Create expense request

```json
{
  "branchId": 7,
  "expenseCategoryId": 5,
  "expenseDate": "2026-08-03T08:00:00Z",
  "amount": 2500.00,
  "paymentMethod": "CASH",
  "description": "Shop electricity expense"
}
```

---

## 15.15 Dashboard and Reports

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/dashboard/summary` | Summary metrics |
| GET | `/reports/sales` | Sales report |
| GET | `/reports/inventory` | Inventory report |
| GET | `/reports/low-stock` | Low-stock report |
| GET | `/reports/purchases` | Purchase report |
| GET | `/reports/expenses` | Expense report |
| GET | `/reports/sales/export` | Export sales CSV |
| GET | `/reports/inventory/export` | Export inventory CSV |
| GET | `/reports/expenses/export` | Export expenses CSV |

Common parameters:

```text
branchId
fromDate
toDate
groupBy
```

---

## 15.16 Audit Logs

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/audit-logs` | List audit records |
| GET | `/audit-logs/{auditLogId}` | Get audit record |

Audit logs are read-only through the public application API.

Filters may include:

```text
userId
branchId
action
entityType
fromDate
toDate
```

---

## 15.17 Health and Documentation

| Method | Endpoint | Authentication | Purpose |
|---|---|---|---|
| GET | `/health` | Public or restricted | Basic service health |
| GET | `/actuator/health` | Restricted by environment | Spring health details |
| GET | `/v3/api-docs` | Development/restricted | OpenAPI JSON |
| GET | `/swagger-ui/index.html` | Development/restricted | Swagger UI |

Detailed health information must not expose secrets publicly.

---

## 16. CORS

Development frontend origin:

```text
http://localhost:5173
```

Production CORS must allow only approved frontend domains.

Allowed methods:

```text
GET, POST, PUT, PATCH, DELETE, OPTIONS
```

Allowed headers include:

```text
Authorization
Content-Type
Accept
```

Credentials and token strategy must be configured consistently.

---

## 17. Logging and Request Tracing

The backend should generate or accept a request identifier:

```http
X-Request-ID: <identifier>
```

Responses may echo it:

```http
X-Request-ID: <identifier>
```

Logs should include:

- Request ID
- Authenticated user ID when available
- Business ID
- Branch ID when applicable
- HTTP method
- Path
- Status
- Duration

Logs must not contain passwords, JWT tokens, database credentials or full sensitive payment data.

---

## 18. Open API Decisions

The following require final confirmation during implementation:

1. Access-token-only versus access-and-refresh-token flow.
2. Whether JWT is stored in memory or secure cookies.
3. Whether `422 Unprocessable Entity` is used for business-rule errors.
4. Whether custom roles are implemented in the MVP.
5. Whether stock transfers use dedicated transfer header and line-item endpoints.
6. Whether partial payments are supported.
7. Whether returns use dedicated return resources.
8. Whether receipt rendering returns JSON, HTML or PDF.
9. Whether M-Pesa callback routes remain under `/api/v1`.
10. Whether optimistic locking is required on all editable master-data records.

---

## 19. Issue #9 Acceptance Checklist

- [x] API base path is defined as `/api/v1`
- [x] Authentication endpoints are defined
- [x] Business and branch endpoints are defined
- [x] Product and category endpoints are defined
- [x] Inventory and stock-movement endpoints are defined
- [x] Supplier and purchase endpoints are defined
- [x] Customer and sales endpoints are defined
- [x] Expense and reporting endpoints are defined
- [x] Pagination and filtering conventions are documented
- [x] Validation-error response format is documented
- [x] Authorization requirements are documented
- [ ] API contract is reviewed by PURITY-CODES-dev
- [x] Final document is prepared for storage under `docs/api`

---

## 20. Review and Approval

| Contributor | Responsibility | Status |
|---|---|---|
| JobMunyoki | API contract author | Completed |
| PURITY-CODES-dev | API contract reviewer | Pending |

The API baseline is approved after review comments are resolved and the pull request is merged into `develop`.
