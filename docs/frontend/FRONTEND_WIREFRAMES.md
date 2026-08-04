# DukaFlow AI Frontend Navigation and Wireframes

**Status:** Draft for review  
**Issue:** #8 — Create frontend navigation and wireframes  
**Author:** PURITY-CODES-dev  
**Reviewer:** JobMunyoki  

## 1. Purpose

This document defines the role-aware navigation, page structure, and low-fidelity wireframes for the DukaFlow AI MVP.

## 2. Main Navigation

```text
Dashboard

Sales
  New Sale
  Sales History
  Returns
  Payments

Inventory
  Products
  Categories
  Stock Balances
  Stock Movements
  Stock Adjustments
  Stock Transfers
  Low Stock

Purchases
  Suppliers
  Purchase Orders
  Purchase History

Customers

Expenses
  Expense Categories
  Expenses

Reports
  Sales Report
  Inventory Report
  Purchase Report
  Expense Report
  Low-Stock Report

Administration
  Branches
  Users
  Roles
  Audit Logs

Settings
  Business Profile
  System Settings
```

## 3. Role-Aware Navigation

| Role | Main access |
|---|---|
| Business Owner | All authorized modules |
| Administrator | Operational modules and administration |
| Branch Manager | Assigned-branch operations and reports |
| Cashier | Dashboard, Sales, Customers, Payments |
| Storekeeper | Dashboard, Products, Inventory, Suppliers, Purchases |
| Accountant | Dashboard, Sales, Payments, Purchases, Expenses, Reports |

The frontend may hide unauthorized links, but the backend must still enforce permissions.

## 4. Desktop Application Layout

```text
+----------------------------------------------------------------------------------+
| DukaFlow AI | Branch Selector | Search | Notifications | User Menu | Logout     |
+----------------------+-----------------------------------------------------------+
| Sidebar Navigation   | Page Title and Breadcrumbs                                |
|                      +-----------------------------------------------------------+
| Dashboard            | Main Page Content                                         |
| Sales                |                                                           |
| Inventory            |                                                           |
| Purchases            |                                                           |
| Customers            |                                                           |
| Expenses             |                                                           |
| Reports              |                                                           |
| Administration       |                                                           |
| Settings             |                                                           |
+----------------------+-----------------------------------------------------------+
```

## 5. Login Page

```text
+--------------------------------------------------------------+
|                       DukaFlow AI                            |
|                                                              |
|   Email or Username                                          |
|   [____________________________________________]             |
|                                                              |
|   Password                                                   |
|   [____________________________________________] [Show]      |
|                                                              |
|   [ Sign In ]                                                |
|                                                              |
|   Forgot password?                                           |
+--------------------------------------------------------------+
```

Required states:

- Loading
- Invalid credentials
- Session expired
- Server error

API:

```text
POST /api/v1/auth/login
GET  /api/v1/auth/me
```

## 6. Dashboard

```text
+--------------------------------------------------------------------------------+
| Dashboard                              Branch: Main Branch                     |
+--------------------------------------------------------------------------------+
| Today's Sales | Purchases | Expenses | Low-Stock Items | Active Customers     |
+--------------------------------------------------------------------------------+
| Sales Trend Chart                                                             |
+--------------------------------------+-----------------------------------------+
| Recent Sales                         | Low-Stock Products                      |
+--------------------------------------+-----------------------------------------+
| [New Sale] [New Purchase] [Stock Adjustment] [Add Expense]                    |
+--------------------------------------------------------------------------------+
```

API:

```text
GET /api/v1/dashboard/summary
```

## 7. Products List

```text
+--------------------------------------------------------------------------------+
| Products                                             [Add Product]             |
+--------------------------------------------------------------------------------+
| Search [________________] Category [All] Status [Active] [Filter]              |
+--------------------------------------------------------------------------------+
| SKU | Product Name | Category | Unit | Price | Reorder Level | Status         |
+--------------------------------------------------------------------------------+
| Previous | Page 1 of 8 | Next                                                  |
+--------------------------------------------------------------------------------+
```

Actions:

- Search and filter
- Add product
- View product
- Edit product
- Activate or deactivate product

## 8. Product Form

```text
SKU                  [____________________________]
Product Name         [____________________________]
Category             [Select category             v]
Unit of Measure      [Select unit                 v]
Cost Price           [____________________________]
Selling Price        [____________________________]
Reorder Level        [____________________________]
Description          [____________________________]

[Cancel]                                     [Save Product]
```

## 9. Inventory Balances

```text
+--------------------------------------------------------------------------------+
| Inventory Balances                          Branch [Main Branch v]              |
+--------------------------------------------------------------------------------+
| Search [____________] Category [All v] Stock Status [All v] [Filter]          |
+--------------------------------------------------------------------------------+
| Product | SKU | On Hand | Reserved | Available | Reorder | Status             |
+--------------------------------------------------------------------------------+
| [View Movements] [Adjust Stock] [Transfer Stock]                               |
+--------------------------------------------------------------------------------+
```

## 10. Stock Adjustment

```text
Branch              [Main Branch v]
Product             [Search product________________]
Adjustment Type     [Increase / Decrease v]
Quantity            [____________________________]
Reason              [____________________________]

Current Quantity: 25.000
New Quantity:     30.000

[Cancel]                              [Submit Adjustment]
```

## 11. Stock Transfer

```text
Source Branch       [Main Branch v]
Destination Branch  [Branch Two v]

Product             Quantity
[Search product]    [__________] [Add Item]

Transfer Items
Product              Available      Quantity
Dairy Meal 70kg      25.000         5.000

[Cancel]                               [Create Transfer]
```

## 12. Suppliers

```text
+--------------------------------------------------------------------------------+
| Suppliers                                             [Add Supplier]           |
+--------------------------------------------------------------------------------+
| Search [________________] Status [Active v] [Filter]                           |
+--------------------------------------------------------------------------------+
| Code | Supplier Name | Phone | Email | Tax PIN | Status | Actions             |
+--------------------------------------------------------------------------------+
```

## 13. Purchase Entry

```text
Branch [Main Branch v]      Supplier [Select Supplier v]
Supplier Invoice [________] Purchase Date [________]

Product | Quantity | Unit Cost | Discount | Tax | Line Total
Dairy   | 20.000   | 3200.00   | 0.00     | 0.00| 64000.00

Subtotal: KES 64,000.00
Total:    KES 64,000.00

[Save Draft] [Cancel]                           [Complete Purchase]
```

## 14. Sales / POS

```text
+--------------------------------------+-----------------------------------------+
| Search Product or SKU                | Sale Summary                            |
| [____________________________]       |                                         |
| Product Results                      | Product      Qty  Price  Total           |
| Dairy Meal 70kg                      | Dairy Meal   2    3650   7300            |
| Layers Mash 70kg                     |                                         |
|                                      | Customer [Walk-in Customer v]            |
|                                      | Total: KES 7,300.00                      |
|                                      | Payment Method [Cash v]                  |
|                                      | [Hold] [Cancel] [Complete Sale]          |
+--------------------------------------+-----------------------------------------+
```

Required behavior:

- Search by SKU or name
- Show stock availability
- Select customer
- Select payment method
- Prevent repeated submission
- Show receipt after completion

## 15. Customers

```text
+--------------------------------------------------------------------------------+
| Customers                                             [Add Customer]           |
+--------------------------------------------------------------------------------+
| Search [________________] Status [Active v] [Filter]                           |
+--------------------------------------------------------------------------------+
| Code | Customer Name | Phone | Email | Walk-In | Status | Actions             |
+--------------------------------------------------------------------------------+
```

## 16. Expenses

```text
+--------------------------------------------------------------------------------+
| Expenses                                              [Record Expense]         |
+--------------------------------------------------------------------------------+
| Branch [All v] Category [All v] Status [All v] Date [From] [To] [Filter]      |
+--------------------------------------------------------------------------------+
| Expense No. | Date | Branch | Category | Amount | Method | Status | Actions    |
+--------------------------------------------------------------------------------+
```

Expense form:

```text
Branch            [Main Branch v]
Category          [Electricity v]
Date              [____________]
Amount            [____________]
Payment Method    [Cash v]
Description       [____________________________]

[Cancel]                                  [Save Expense]
```

## 17. Reports

```text
Report Type [Sales Report v]
Branch      [All Branches v]
Date Range  [From] [To]
Group By    [Day v]

[Generate Report] [Export CSV]

Summary Cards
Chart
Detailed Results Table
```

## 18. Users

```text
+--------------------------------------------------------------------------------+
| Users                                                 [Add User]               |
+--------------------------------------------------------------------------------+
| Search [____________] Role [All v] Branch [All v] Status [Active v]           |
+--------------------------------------------------------------------------------+
| Name | Email | Role | Assigned Branches | Status | Last Login | Actions        |
+--------------------------------------------------------------------------------+
```

## 19. Branches

```text
+--------------------------------------------------------------------------------+
| Branches                                              [Add Branch]             |
+--------------------------------------------------------------------------------+
| Branch Code | Branch Name | Phone | Address | Status | Actions                |
+--------------------------------------------------------------------------------+
```

## 20. Audit Logs

```text
+--------------------------------------------------------------------------------+
| Audit Logs                                                                     |
+--------------------------------------------------------------------------------+
| User [All v] Branch [All v] Action [All v] Date [From] [To] [Filter]          |
+--------------------------------------------------------------------------------+
| Date | User | Branch | Action | Entity | Summary | View Details              |
+--------------------------------------------------------------------------------+
```

Audit logs are read-only.

## 21. Shared UI States

Every major page must support:

- Loading state
- Empty state
- Success message
- Field validation error
- Permission-denied state
- Server-error state
- Confirmation dialog

Confirmation is required for:

- Deactivation
- Cancellation
- Completing purchases
- Completing sales
- Stock adjustments
- Stock transfers
- Refunds

## 22. Responsive Behaviour

### Desktop

- Permanent sidebar
- Multi-column forms
- Full data tables

### Tablet

- Collapsible sidebar
- Horizontal table scrolling
- Filters in collapsible panels

### Mobile

- Navigation drawer
- Single-column forms
- Card-based records where needed
- Full-width primary actions

## 23. Reusable Components

```text
AppLayout
Sidebar
Topbar
PageHeader
BranchSelector
DataTable
SearchInput
FilterPanel
Pagination
StatusChip
EmptyState
LoadingState
ErrorAlert
ConfirmationDialog
FormField
PermissionGuard
```

## 24. API Mapping

| Screen | Endpoint |
|---|---|
| Login | `POST /auth/login` |
| Dashboard | `GET /dashboard/summary` |
| Products | `GET/POST/PATCH /products` |
| Inventory | `GET /inventory` |
| Stock adjustments | `POST /stock-adjustments` |
| Stock transfers | `GET/POST /stock-transfers` |
| Suppliers | `GET/POST/PATCH /suppliers` |
| Purchases | `GET/POST/PATCH /purchases` |
| Customers | `GET/POST/PATCH /customers` |
| Sales | `GET/POST /sales` |
| Expenses | `GET/POST/PATCH /expenses` |
| Reports | `GET /reports/...` |
| Users | `GET/POST/PATCH /users` |
| Branches | `GET/POST/PATCH /branches` |
| Audit logs | `GET /audit-logs` |

All paths use the `/api/v1` base path.

## 25. Issue #8 Acceptance Checklist

- [x] Main navigation is defined
- [x] Navigation is role-aware
- [x] Login wireframe is included
- [x] Dashboard wireframe is included
- [x] Product and inventory wireframes are included
- [x] Purchase workflow is included
- [x] Sales/POS workflow is included
- [x] Customer and expense screens are included
- [x] Reports and administration screens are included
- [x] Responsive behaviour is documented
- [x] Shared UI states are documented
- [x] API mapping is included
- [ ] Wireframes are reviewed by JobMunyoki

## 26. Approval

| Contributor | Responsibility | Status |
|---|---|---|
| PURITY-CODES-dev | Author | Completed |
| JobMunyoki | Reviewer | Pending |
