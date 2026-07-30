# API Conventions

## Base path

```text
/api/v1
```

## Data format

Requests and responses will use JSON unless a documented endpoint returns a file.

## HTTP methods

- `GET`: retrieve resources
- `POST`: create resources
- `PUT`: replace a complete resource
- `PATCH`: update part of a resource
- `DELETE`: remove or deactivate a resource

## Planned response structure

Successful response:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {}
}
```

Error response:

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "Name is required"
    }
  ],
  "timestamp": "2026-07-31T00:00:00Z"
}
```

The final error structure will be confirmed during backend architecture design.
