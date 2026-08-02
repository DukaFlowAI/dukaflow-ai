# DukaFlow AI MVP Functional and Non-Functional Requirements

**Document status:** Draft for contributor review  
**Related GitHub issue:** #4 — Finalize MVP functional and non-functional requirements  
**Target release:** Minimum Viable Product (MVP)  
**Reviewers:** JobMunyoki and PURITY-CODES-dev  

---

## 1. Purpose

This document defines the functional and non-functional requirements for the first usable release of DukaFlow AI.

DukaFlow AI is a multi-branch retail ERP platform for small and medium-sized businesses in Kenya. The MVP focuses on secure user access, branches, products, stock, suppliers, purchases, customers, sales, expenses, reports, and audit records.

AI forecasting, automatic M-Pesa processing, and direct eTIMS integration are excluded from the MVP and will be implemented after the core retail workflows are stable.

---

## 2. Product Goals

The MVP must:

1. Centralize retail operations in one secure system.
2. Maintain accurate product and stock records.
3. Record purchases and increase stock correctly.
4. Record sales and reduce stock correctly.
5. Support multiple branches under one business.
6. Restrict users according to role and assigned branch.
7. Track expenses and basic business performance.
8. Preserve an audit trail for important actions.
9. Provide a responsive browser-based interface.
10. Establish a stable foundation for future M-Pesa and AI modules.

---

## 3. Target Users

### 3.1 Business Owner

Can view and manage all branches, users, products, purchases, sales, expenses, reports, and audit records.

### 3.2 Administrator

Manages users, roles, branches, products, categories, and system settings.

### 3.3 Branch Manager

Oversees inventory, purchases, sales, expenses, and reports for assigned branches.

### 3.4 Cashier

Creates sales, records payments, issues receipts, and views permitted products and customers.

### 3.5 Storekeeper

Manages stock receipts, transfers, adjustments, and inventory counts.

### 3.6 Accountant

Reviews purchases, sales, expenses, payments, and financial summaries.

---

## 4. MVP Scope

The MVP includes:

- Authentication and session management
- Role-based access control
- Business and branch management
- User management
- Product and category management
- Inventory and stock movements
- Supplier management
- Purchase management
- Customer management
- Sales management
- Expense management
- Basic reports and dashboard metrics
- Audit logging
- Data validation and error handling
- Responsive web interface

---

## 5. Out of Scope for the MVP

The following are not part of the MVP:

- AI demand forecasting
- Automated reorder recommendations
- Fraud or anomaly detection
- Production M-Pesa transactions
- Direct production eTIMS integration
- Full accounting general ledger
- Payroll and human-resource management
- Native Android or iOS applications
- Offline synchronization
- International tax and currency support
- Customer e-commerce storefront
- Supplier self-service portal

---

## 6. Business Rules

### BR-001: Business data isolation

A user must only access data belonging to the business to which the user is assigned.

### BR-002: Branch data isolation

A branch-restricted user must only access records for assigned branches.

### BR-003: Stock cannot become negative

A sale or stock-out transaction must not reduce available stock below zero.

### BR-004: Completed transactions are preserved

Completed purchases and sales must not be physically deleted. Corrections must use cancellation, reversal, return, or adjustment workflows.

### BR-005: Stock movement traceability

Every stock increase or decrease must create a stock-movement record containing its source, quantity, branch, user, and timestamp.

### BR-006: Unique business identifiers

Each business must use unique product SKUs, branch codes, purchase numbers, sale numbers, and receipt numbers.

### BR-007: Monetary precision

Money calculations must use decimal arithmetic rather than floating-point arithmetic.

### BR-008: Sensitive actions are audited

User creation, role changes, stock adjustments, cancellations, and other sensitive operations must be logged.

---

# 7. Functional Requirements

## 7.1 Authentication and Account Access

### FR-AUTH-001: User sign-in

The system shall allow an active user to sign in using an email address or username and password.

### FR-AUTH-002: Invalid login handling

The system shall reject invalid credentials without revealing whether the username or password was incorrect.

### FR-AUTH-003: JWT authentication

The backend shall issue a secure authentication token after successful login.

### FR-AUTH-004: Sign-out

The user shall be able to sign out and end the active session.

### FR-AUTH-005: Disabled accounts

The system shall prevent disabled users from signing in.

### FR-AUTH-006: Password protection

Passwords shall be stored using a secure one-way password-hashing algorithm.

### FR-AUTH-007: Current-user profile

An authenticated user shall be able to view their profile, role, business, and assigned branches.

---

## 7.2 Role-Based Access Control

### FR-RBAC-001: Role assignment

An authorized administrator shall be able to assign a supported role to a user.

### FR-RBAC-002: Permission enforcement

The backend shall enforce permissions for every protected endpoint.

### FR-RBAC-003: User-interface restrictions

The frontend shall hide or disable navigation items and actions that the current user is not authorized to use.

### FR-RBAC-004: Branch restrictions

The system shall restrict branch-level records according to the user's assigned branches.

### FR-RBAC-005: Access-denied response

The system shall return a clear access-denied response when a user attempts an unauthorized action.

---

## 7.3 Business and Branch Management

### FR-BUS-001: Business profile

An authorized user shall be able to view and update business contact information, tax details, and receipt information.

### FR-BRA-001: Create branch

An authorized user shall be able to create a branch with a unique branch code.

### FR-BRA-002: Update branch

An authorized user shall be able to update branch details.

### FR-BRA-003: Activate or deactivate branch

An authorized user shall be able to activate or deactivate a branch without deleting its historical data.

### FR-BRA-004: Assign users to branches

An authorized user shall be able to assign one or more branches to a user.

### FR-BRA-005: Branch filtering

Authorized users with access to multiple branches shall be able to filter operational data by branch.

---

## 7.4 User Management

### FR-USR-001: Create user

An authorized administrator shall be able to create a user with a name, email, role, business, and branch assignments.

### FR-USR-002: Update user

An authorized administrator shall be able to update a user's permitted profile fields, role, and branch assignments.

### FR-USR-003: Activate or deactivate user

An authorized administrator shall be able to activate or deactivate a user.

### FR-USR-004: User listing

An authorized user shall be able to list and search users within the business.

### FR-USR-005: Unique email

A user's email address shall be unique within the system.

---

## 7.5 Product and Category Management

### FR-PRO-001: Create category

An authorized user shall be able to create a product category.

### FR-PRO-002: Update category

An authorized user shall be able to update or deactivate a category.

### FR-PRO-003: Create product

An authorized user shall be able to create a product with:

- Product name
- SKU
- Category
- Unit of measure
- Cost price
- Selling price
- Reorder level
- Active status

### FR-PRO-004: Update product

An authorized user shall be able to update permitted product information.

### FR-PRO-005: Unique SKU

The system shall prevent duplicate product SKUs within the same business.

### FR-PRO-006: Product search

Users shall be able to search products by name, SKU, and category.

### FR-PRO-007: Product activation

An authorized user shall be able to activate or deactivate a product without deleting transaction history.

---

## 7.6 Inventory Management

### FR-INV-001: Branch inventory

The system shall maintain a separate stock balance for each product at each branch.

### FR-INV-002: Stock movement records

The system shall record every stock movement, including purchase receipt, sale, return, adjustment, and transfer.

### FR-INV-003: Stock adjustment

An authorized user shall be able to create a stock adjustment with a reason.

### FR-INV-004: Stock transfer

An authorized user shall be able to transfer stock between active branches.

### FR-INV-005: Transfer integrity

A completed transfer shall reduce stock at the source branch and increase stock at the destination branch as one controlled transaction.

### FR-INV-006: Inventory listing

Users shall be able to view stock quantities by product and branch.

### FR-INV-007: Low-stock identification

The system shall identify products whose available quantity is at or below their reorder level.

### FR-INV-008: Inventory history

Authorized users shall be able to view stock-movement history for a selected product and branch.

---

## 7.7 Supplier Management

### FR-SUP-001: Create supplier

An authorized user shall be able to create a supplier with contact and business information.

### FR-SUP-002: Update supplier

An authorized user shall be able to update or deactivate a supplier.

### FR-SUP-003: Supplier search

Users shall be able to search suppliers by name, phone number, email, or reference code.

### FR-SUP-004: Supplier purchase history

Authorized users shall be able to view purchases associated with a supplier.

---

## 7.8 Purchase Management

### FR-PUR-001: Create purchase

An authorized user shall be able to create a purchase for a supplier and branch.

### FR-PUR-002: Purchase items

A purchase shall contain one or more products, quantities, unit costs, and line totals.

### FR-PUR-003: Purchase total

The system shall calculate subtotal, discounts, applicable taxes, and total amount.

### FR-PUR-004: Purchase workflow

A purchase shall use controlled statuses such as Draft, Completed, and Cancelled.

### FR-PUR-005: Stock increase

Completing a purchase shall increase inventory for the selected branch.

### FR-PUR-006: Prevent duplicate completion

The system shall prevent the same purchase from increasing stock more than once.

### FR-PUR-007: Purchase reference

Each purchase shall receive a unique purchase number within the business.

### FR-PUR-008: Purchase listing

Users shall be able to filter purchases by supplier, branch, status, and date.

### FR-PUR-009: Purchase cancellation

An authorized user shall be able to cancel a completed purchase through a controlled reversal where permitted.

---

## 7.9 Customer Management

### FR-CUS-001: Create customer

An authorized user shall be able to create a customer profile.

### FR-CUS-002: Walk-in customer

The system shall support sales to a configurable walk-in customer.

### FR-CUS-003: Update customer

An authorized user shall be able to update or deactivate a customer.

### FR-CUS-004: Customer search

Users shall be able to search customers by name, phone number, email, or customer code.

### FR-CUS-005: Customer sales history

Authorized users shall be able to view a customer's sales history.

---

## 7.10 Sales Management

### FR-SAL-001: Create sale

An authorized cashier or manager shall be able to create a sale for a selected branch.

### FR-SAL-002: Sale items

A sale shall contain one or more products, quantities, selling prices, discounts, and line totals.

### FR-SAL-003: Stock validation

The system shall validate available stock before completing a sale.

### FR-SAL-004: Sale total

The system shall calculate subtotal, discounts, applicable taxes, and final total.

### FR-SAL-005: Payment method

The user shall record a payment method, initially including Cash, Card, Bank Transfer, and M-Pesa Manual Reference.

### FR-SAL-006: Stock reduction

Completing a sale shall reduce inventory at the selected branch.

### FR-SAL-007: Prevent duplicate completion

The system shall prevent the same sale from reducing stock more than once.

### FR-SAL-008: Receipt number

Each completed sale shall receive a unique receipt number within the business.

### FR-SAL-009: Receipt view

The system shall generate a printable receipt view for a completed sale.

### FR-SAL-010: Sale listing

Users shall be able to filter sales by branch, cashier, customer, payment method, status, and date.

### FR-SAL-011: Sale cancellation

An authorized user shall be able to cancel a completed sale through a controlled stock-restoration workflow.

### FR-SAL-012: Sales return

An authorized user shall be able to record a return against an eligible completed sale.

---

## 7.11 Expense Management

### FR-EXP-001: Create expense category

An authorized user shall be able to create and manage expense categories.

### FR-EXP-002: Record expense

An authorized user shall be able to record an expense with category, branch, amount, date, description, and payment method.

### FR-EXP-003: Update draft expense

An authorized user shall be able to update an expense before it is finalized.

### FR-EXP-004: Expense listing

Users shall be able to filter expenses by category, branch, date, and creator.

### FR-EXP-005: Expense cancellation

An authorized user shall be able to cancel an expense while retaining its audit history.

---

## 7.12 Dashboard and Reporting

### FR-REP-001: Dashboard summary

The dashboard shall show authorized summary metrics for a selected date range and branch scope.

### FR-REP-002: Core metrics

The dashboard shall include:

- Total sales
- Total purchases
- Total expenses
- Number of completed sales
- Low-stock product count
- Current inventory value estimate

### FR-REP-003: Recent activity

The dashboard shall display recent sales, purchases, and stock movements according to user permissions.

### FR-REP-004: Sales report

Authorized users shall be able to view sales totals grouped by date, branch, product, and payment method.

### FR-REP-005: Inventory report

Authorized users shall be able to view current stock and low-stock products by branch.

### FR-REP-006: Expense report

Authorized users shall be able to view expense totals by category, branch, and date range.

### FR-REP-007: Export

Selected tabular reports should be exportable as CSV.

---

## 7.13 Audit Logging

### FR-AUD-001: Audit record creation

The system shall create audit records for security-sensitive and business-critical actions.

### FR-AUD-002: Audit content

An audit record shall include:

- Acting user
- Business
- Branch where applicable
- Action
- Entity type
- Entity identifier
- Timestamp
- Summary of the change

### FR-AUD-003: Audit protection

Ordinary users shall not be able to modify or delete audit records.

### FR-AUD-004: Audit viewing

Authorized users shall be able to filter audit logs by user, action, entity, branch, and date.

---

## 7.14 Validation and Error Handling

### FR-VAL-001: Required-field validation

The system shall validate required fields before processing requests.

### FR-VAL-002: Business-rule validation

The system shall reject requests that violate stock, uniqueness, status, permission, or branch rules.

### FR-VAL-003: Consistent error format

The API shall return errors using a consistent JSON structure.

### FR-VAL-004: User-friendly messages

The frontend shall show clear and actionable error messages without exposing internal implementation details.

### FR-VAL-005: Transaction rollback

A failed multi-step business operation shall not leave partial database changes.

---

# 8. Non-Functional Requirements

## 8.1 Security

### NFR-SEC-001: Encrypted transport

Production traffic shall use HTTPS.

### NFR-SEC-002: Password hashing

Passwords shall be hashed using BCrypt or an equivalent secure algorithm.

### NFR-SEC-003: Backend authorization

The backend shall enforce authorization independently of frontend controls.

### NFR-SEC-004: Secret management

Database passwords, JWT secrets, and API keys shall not be committed to Git.

### NFR-SEC-005: Input protection

The application shall validate and sanitize untrusted input.

### NFR-SEC-006: Safe error handling

Production errors shall not expose stack traces, SQL statements, credentials, or sensitive configuration.

### NFR-SEC-007: Token expiration

Authentication tokens shall expire after a configurable period.

### NFR-SEC-008: Auditability

Sensitive administrative and stock-related operations shall be traceable to the acting user.

---

## 8.2 Performance

### NFR-PER-001: Standard API response time

Under normal demonstration load, at least 95% of ordinary API requests should complete within two seconds, excluding external services and large exports.

### NFR-PER-002: Dashboard response time

The main dashboard should load summary data within three seconds under normal demonstration load.

### NFR-PER-003: Pagination

Large list endpoints shall use pagination.

### NFR-PER-004: Database indexing

Frequently filtered and joined fields shall use appropriate database indexes.

---

## 8.3 Availability and Reliability

### NFR-REL-001: Transaction integrity

Purchases, sales, transfers, returns, and cancellations shall use database transactions.

### NFR-REL-002: Consistent state

A failed operation shall not leave inventory and transaction records inconsistent.

### NFR-REL-003: Health checks

The backend shall expose a health-check endpoint.

### NFR-REL-004: Recoverable deployment

Deployment documentation shall include procedures for restarting services and restoring configuration.

### NFR-REL-005: Backup expectation

The production design shall support regular database backups.

---

## 8.4 Usability and Accessibility

### NFR-USA-001: Responsive design

The application shall support common desktop, tablet, and mobile browser widths.

### NFR-USA-002: Consistent navigation

Navigation, labels, buttons, validation, and feedback shall be consistent across modules.

### NFR-USA-003: Form feedback

Forms shall clearly indicate required fields, errors, loading states, and successful completion.

### NFR-USA-004: Keyboard access

Core forms and navigation should be usable with a keyboard.

### NFR-USA-005: Readability

Text, controls, and status indicators shall maintain adequate contrast and readable sizing.

---

## 8.5 Maintainability

### NFR-MAI-001: Modular architecture

The system shall separate frontend, backend, AI service, database, and documentation concerns.

### NFR-MAI-002: Coding standards

Each codebase shall use documented formatting, naming, and linting conventions.

### NFR-MAI-003: Automated tests

Core business logic shall have automated tests before the MVP is considered stable.

### NFR-MAI-004: API documentation

Backend endpoints shall be documented using OpenAPI or an equivalent specification.

### NFR-MAI-005: Configuration separation

Environment-specific values shall be supplied through environment variables and configuration files.

### NFR-MAI-006: Contributor workflow

All feature work shall use GitHub issues, branches, pull requests, and peer review.

---

## 8.6 Compatibility and Portability

### NFR-COM-001: Browser support

The frontend shall support current versions of Chrome, Edge, and Firefox.

### NFR-COM-002: Container support

The system shall be designed to run locally through Docker Compose.

### NFR-COM-003: Database compatibility

The MVP shall use MySQL as its primary relational database.

### NFR-COM-004: API independence

The frontend shall communicate with the backend through documented HTTP APIs rather than direct database access.

---

## 8.7 Data Quality and Privacy

### NFR-DAT-001: Referential integrity

The database shall enforce valid relationships using primary keys, foreign keys, and constraints.

### NFR-DAT-002: Timestamps

Important records shall store creation and modification timestamps.

### NFR-DAT-003: Soft deactivation

Users, products, branches, suppliers, and customers should be deactivated rather than physically deleted where transaction history depends on them.

### NFR-DAT-004: Minimum necessary data

The MVP shall collect only information required for its workflows.

### NFR-DAT-005: Tenant-isolation tests

Automated tests shall verify that users cannot access another business's records.

---

## 9. MVP Acceptance Criteria

The MVP will be considered ready when:

- [ ] Users can sign in securely.
- [ ] Roles and permissions are enforced.
- [ ] Multiple branches can be configured.
- [ ] Products and categories can be managed.
- [ ] Purchases increase branch stock correctly.
- [ ] Sales reduce branch stock correctly.
- [ ] Stock adjustments and transfers are traceable.
- [ ] Suppliers and customers can be managed.
- [ ] Expenses can be recorded and reported.
- [ ] Dashboard metrics are displayed according to permissions.
- [ ] Important actions appear in the audit log.
- [ ] Core backend business logic has automated tests.
- [ ] The frontend and backend can run together locally.
- [ ] The project can be demonstrated using non-sensitive sample data.
- [ ] Both contributors approve the MVP requirements baseline.

---

## 10. Assumptions

- The MVP supports one currency per business, with Kenyan shillings as the default.
- A user belongs to one business.
- A user may be assigned to one or more branches within that business.
- Internet access is required.
- Tax calculations will initially be configurable and simplified.
- M-Pesa may be recorded as a manual payment reference before automatic Daraja integration.
- AI features will be added after sufficient clean transaction data exists.

---

## 11. Dependencies

The MVP depends on:

- Java and Spring Boot backend
- React and TypeScript frontend
- MySQL database
- Spring Security and JWT
- Docker Compose
- GitHub issues, branches, pull requests, and project board
- Approved role and permission matrix
- Approved database ERD
- Approved API contract

---

## 12. Known Risks

### RISK-001: Scope growth

Adding M-Pesa, AI, eTIMS, payroll, or full accounting during the MVP may delay delivery.

**Mitigation:** Keep excluded modules in later-release issues.

### RISK-002: Inventory inconsistency

Incorrect transaction handling may produce inaccurate stock balances.

**Mitigation:** Use database transactions, stock-movement records, and automated tests.

### RISK-003: Cross-business data exposure

Incorrect filtering may expose another business's data.

**Mitigation:** Enforce business scope in backend authorization and repository queries, with isolation tests.

### RISK-004: Collaboration conflicts

Parallel changes may conflict or introduce inconsistent conventions.

**Mitigation:** Use assigned issues, small branches, pull requests, peer review, and shared documentation.

### RISK-005: Short sprint duration

Three-day sprints may be too short for large design tasks.

**Mitigation:** Split large tasks into smaller deliverables and move unfinished work forward transparently.

---

## 13. Review and Approval

| Contributor | Responsibility | Status |
|---|---|---|
| JobMunyoki | Requirements author and backend perspective | Pending |
| PURITY-CODES-dev | Reviewer and frontend/user-experience perspective | Pending |

The requirements baseline is approved only after both contributors review the document and agreed changes are merged into the `develop` branch.
