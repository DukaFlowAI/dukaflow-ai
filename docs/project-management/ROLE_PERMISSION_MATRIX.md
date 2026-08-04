# DukaFlow AI Role and Permission Matrix

**Status:** Draft for review  
**Issue:** #5 — Define user roles and permission matrix  
**Author:** PURITY-CODES-dev  
**Reviewer:** JobMunyoki  

## 1. Purpose

This document defines the initial user roles and permission boundaries for the DukaFlow AI MVP.

## 2. Access Levels

| Access level | Meaning |
|---|---|
| `FULL` | Full business-wide access |
| `MANAGE` | Create, view, update, complete, cancel or deactivate where applicable |
| `OWN_BRANCH` | Access limited to assigned branches |
| `OWN_RECORDS` | Access limited to records created by the user |
| `VIEW` | Read-only access |
| `NONE` | No access |

## 3. Roles

- **Business Owner:** Full authority across the business.
- **Administrator:** Manages users, branches, master data and operations.
- **Branch Manager:** Manages assigned-branch operations.
- **Cashier:** Handles sales, customers and supported payments.
- **Storekeeper:** Handles products, stock, suppliers and purchases.
- **Accountant:** Handles expenses, payments, reporting and financial oversight.

## 4. Permission Matrix

| Permission | Owner | Administrator | Branch Manager | Cashier | Storekeeper | Accountant |
|---|---|---|---|---|---|---|
| View dashboard | FULL | FULL | OWN_BRANCH | OWN_BRANCH | OWN_BRANCH | FULL |
| Manage business profile | FULL | VIEW | NONE | NONE | NONE | VIEW |
| Manage branches | FULL | MANAGE | OWN_BRANCH | NONE | NONE | VIEW |
| View users | FULL | FULL | OWN_BRANCH | NONE | NONE | VIEW |
| Create/update users | FULL | MANAGE | OWN_BRANCH | NONE | NONE | NONE |
| Deactivate users | FULL | MANAGE | OWN_BRANCH | NONE | NONE | NONE |
| View roles | FULL | FULL | VIEW | NONE | VIEW | VIEW |
| Manage roles | FULL | MANAGE | NONE | NONE | NONE | NONE |
| View categories | FULL | FULL | OWN_BRANCH | VIEW | FULL | VIEW |
| Manage categories | FULL | MANAGE | OWN_BRANCH | NONE | MANAGE | VIEW |
| View products | FULL | FULL | OWN_BRANCH | OWN_BRANCH | FULL | VIEW |
| Manage products | FULL | MANAGE | OWN_BRANCH | NONE | MANAGE | VIEW |
| View inventory | FULL | FULL | OWN_BRANCH | OWN_BRANCH | OWN_BRANCH | FULL |
| View stock movements | FULL | FULL | OWN_BRANCH | OWN_RECORDS | OWN_BRANCH | VIEW |
| Create stock adjustment | FULL | MANAGE | OWN_BRANCH | NONE | OWN_BRANCH | NONE |
| Approve stock adjustment | FULL | MANAGE | OWN_BRANCH | NONE | NONE | NONE |
| Create stock transfer | FULL | MANAGE | OWN_BRANCH | NONE | OWN_BRANCH | NONE |
| Complete stock transfer | FULL | MANAGE | OWN_BRANCH | NONE | OWN_BRANCH | NONE |
| Cancel stock transfer | FULL | MANAGE | OWN_BRANCH | NONE | NONE | NONE |
| View suppliers | FULL | FULL | OWN_BRANCH | NONE | FULL | VIEW |
| Manage suppliers | FULL | MANAGE | OWN_BRANCH | NONE | MANAGE | VIEW |
| View purchases | FULL | FULL | OWN_BRANCH | NONE | OWN_BRANCH | FULL |
| Create/update purchases | FULL | MANAGE | OWN_BRANCH | NONE | OWN_BRANCH | VIEW |
| Complete purchases | FULL | MANAGE | OWN_BRANCH | NONE | OWN_BRANCH | VIEW |
| Cancel purchases | FULL | MANAGE | OWN_BRANCH | NONE | NONE | VIEW |
| View customers | FULL | FULL | OWN_BRANCH | OWN_BRANCH | VIEW | VIEW |
| Manage customers | FULL | MANAGE | OWN_BRANCH | OWN_BRANCH | NONE | VIEW |
| View sales | FULL | FULL | OWN_BRANCH | OWN_RECORDS | VIEW | FULL |
| Create/complete sales | FULL | MANAGE | OWN_BRANCH | OWN_BRANCH | NONE | VIEW |
| Cancel sales | FULL | MANAGE | OWN_BRANCH | NONE | NONE | VIEW |
| Create sales return | FULL | MANAGE | OWN_BRANCH | OWN_RECORDS | NONE | VIEW |
| Approve sales return | FULL | MANAGE | OWN_BRANCH | NONE | NONE | VIEW |
| View payments | FULL | FULL | OWN_BRANCH | OWN_RECORDS | NONE | FULL |
| Process payment | FULL | MANAGE | OWN_BRANCH | OWN_BRANCH | NONE | VIEW |
| Process refund | FULL | MANAGE | OWN_BRANCH | NONE | NONE | VIEW |
| View expenses | FULL | FULL | OWN_BRANCH | NONE | VIEW | FULL |
| Create/manage expenses | FULL | MANAGE | OWN_BRANCH | NONE | NONE | MANAGE |
| View reports | FULL | FULL | OWN_BRANCH | OWN_RECORDS | OWN_BRANCH | FULL |
| Export reports | FULL | FULL | OWN_BRANCH | NONE | NONE | FULL |
| View audit logs | FULL | FULL | OWN_BRANCH | NONE | NONE | VIEW |
| Update system settings | FULL | MANAGE | NONE | NONE | NONE | VIEW |

## 5. Security Rules

1. Every user belongs to one business.
2. Branch-restricted users may access only assigned branches.
3. The backend enforces all permissions.
4. Frontend-hidden controls do not replace backend authorization.
5. Deactivated users cannot sign in.
6. Completed transactions and audit logs cannot be physically deleted.
7. Cashiers cannot approve sensitive adjustments or cancellations.
8. Audit logs are read-only.
9. Cross-business access must return no data.
10. Denied actions return `403 ACCESS_DENIED`.

## 6. Frontend Behaviour

The React frontend should:

- Show only authorized navigation items.
- Hide or disable unavailable actions.
- Limit branch selectors to assigned branches.
- Display clear permission-denied messages.
- Refresh permissions after login or role changes.

## 7. Backend Enforcement

The Spring Boot backend should:

- Resolve the user from the JWT.
- Load business, role and branch assignments.
- Validate permissions before service execution.
- Apply tenant and branch scope to queries.
- Audit sensitive actions.
- Prevent privilege escalation through request payloads.

## 8. Open Decisions

1. Whether custom roles are supported in the MVP.
2. Whether permissions are code-defined or database-driven.
3. Which actions require dual approval.
4. Whether Branch Managers may deactivate users.
5. Whether Cashiers may create returns without approval.
6. Whether Storekeepers may complete purchases independently.

## 9. Issue #5 Acceptance Checklist

- [x] MVP roles are defined
- [x] Permission levels are defined
- [x] Business-level permissions are documented
- [x] Branch-level restrictions are documented
- [x] Sensitive actions are restricted
- [x] Frontend responsibilities are documented
- [x] Backend enforcement responsibilities are documented
- [ ] Matrix is reviewed by JobMunyoki
- [x] Document is prepared for `docs/project-management`

## 10. Review and Approval

| Contributor | Responsibility | Status |
|---|---|---|
| PURITY-CODES-dev | Permission matrix author | Completed |
| JobMunyoki | Reviewer | Pending |
