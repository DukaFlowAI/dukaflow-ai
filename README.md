# DukaFlow AI

DukaFlow AI is an AI-powered retail ERP and inventory-management platform for Kenyan small and medium-sized enterprises.

## Planned capabilities

- Secure authentication and role-based access
- Business and branch management
- Product, category and inventory management
- Supplier purchasing
- Customer sales and receipts
- Expenses and profitability reporting
- M-Pesa Daraja sandbox integration
- AI-assisted demand forecasting
- Audit logging and operational analytics

## Technology stack

| Layer | Technology |
|---|---|
| Frontend | React, TypeScript and Material UI |
| Backend | Java, Spring Boot and Spring Security |
| AI service | Python and FastAPI |
| Database | MySQL |
| DevOps | Docker, GitHub Actions and cloud deployment |

## Repository structure

```text
backend/       Spring Boot REST API
frontend/      React and TypeScript web application
ai-service/    FastAPI forecasting and analytics service
database/      Database migrations and sample data
docs/          Architecture, API and project-management documentation
.github/       Pull-request templates, issue templates and workflows
```

## Development status

The project is currently in **Phase 1: collaboration setup, architecture and task allocation**. Application source code has not yet been generated.

## Local setup

1. Clone the repository.
2. Copy `.env.example` to `.env`.
3. Replace all placeholder credentials in `.env`.
4. Follow the setup guide inside each service directory after that service has been generated.

## Collaboration workflow

1. Create or select a GitHub issue.
2. Branch from `develop`.
3. Use a branch name such as `feature/12-product-management`.
4. Commit small, focused changes.
5. Push the feature branch.
6. Open a pull request into `develop`.
7. Ask the other contributor to review it.
8. Merge only after testing and resolving review comments.

See [CONTRIBUTING.md](CONTRIBUTING.md) for complete contribution rules.
