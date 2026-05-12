# Company Endpoints Documentation

This document provides an overview of Company-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

## Data Transfer Objects (DTOs)

### CreateCompanyRequest
```json
{
  "name": "string",
  "description": "string",
  "registrationNumber": "string",
  "taxId": "string",
  "address": "string",
  "phoneNumber": "string",
  "email": "string",
  "website": "string",
  "logo": {
    "id": "string",
    "fileName": "string",
    "originalFileName": "string",
    "contentType": "string",
    "size": "number",
    "url": "string"
  },
  "status": "ACTIVE|INACTIVE|SUSPENDED"
}
```
**Validation Rules:**
- `name`: Required, max 200 characters
- `description`: Optional, max 1000 characters
- `registrationNumber`: Required, max 50 characters
- `taxId`: Optional, max 50 characters
- `address`: Optional, max 500 characters
- `phoneNumber`: Optional, must match pattern `^[+]?[0-9]{10,15}$`
- `email`: Optional, valid email format, max 100 characters
- `website`: Optional, max 200 characters

### CompanyResponse
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "registrationNumber": "string",
  "taxId": "string",
  "address": "string",
  "phoneNumber": "string",
  "email": "string",
  "website": "string",
  "logo": {
    "id": "string",
    "fileName": "string",
    "originalFileName": "string",
    "contentType": "string",
    "size": "number",
    "url": "string"
  },
  "status": "ACTIVE|INACTIVE|SUSPENDED",
  "createdBy": "string",
  "updatedBy": "string",
  "createdAt": "2023-05-05T12:00:00Z",
  "updatedAt": "2023-05-05T12:00:00Z"
}
```

## Enums

### CompanyStatus
```java
public enum CompanyStatus {
  ACTIVE,
  INACTIVE,
  SUSPENDED
}
```

### CompanyType
```java
public enum CompanyType {
  SHIPPING_COMPANY,
  CREW_AGENCY,
  RECRUITMENT_AGENCY,
  TRAINING_CENTER,
  PORT_AUTHORITY,
  INSURANCE_COMPANY,
  SUPPLIER,
  OTHER
}
```

---

## **1. Create Company**

- **URL**: `/api/v1/companies`
- **Method**: `POST`
- **Description**: Creates a new company in the system.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "registrationNumber": "string",
    "taxId": "string",
    "address": "string",
    "phoneNumber": "string",
    "email": "string",
    "website": "string",
    "logo": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    },
    "status": "ACTIVE|INACTIVE|SUSPENDED"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "registrationNumber": "string",
    "taxId": "string",
    "address": "string",
    "phoneNumber": "string",
    "email": "string",
    "website": "string",
    "logo": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    },
    "status": "ACTIVE|INACTIVE|SUSPENDED",
    "createdBy": "string",
    "updatedBy": "string",
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with company details to create a new company.

---

## **2. Get All Companies**

- **URL**: `/api/v1/companies`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all companies.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `search` (optional): Search term for company name.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "name": "string",
        "description": "string",
        "registrationNumber": "string",
        "taxId": "string",
        "address": "string",
        "phoneNumber": "string",
        "email": "string",
        "website": "string",
        "logo": {
          "id": "string",
          "fileName": "string",
          "originalFileName": "string",
          "contentType": "string",
          "size": "number",
          "url": "string"
        },
        "status": "ACTIVE|INACTIVE|SUSPENDED",
        "createdBy": "string",
        "updatedBy": "string",
        "createdAt": "2023-05-05T12:00:00Z",
        "updatedAt": "2023-05-05T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer",
    "size": "integer",
    "number": "integer",
    "sort": {
      "empty": "boolean",
      "sorted": "boolean",
      "unsorted": "boolean"
    },
    "first": "boolean",
    "last": "boolean",
    "numberOfElements": "integer",
    "pageable": {
      "sort": {
        "empty": "boolean",
        "sorted": "boolean",
        "unsorted": "boolean"
      },
      "pageNumber": "integer",
      "pageSize": "integer",
      "offset": "integer",
      "paged": "boolean",
      "unpaged": "boolean"
    },
    "empty": "boolean"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve paginated companies.

---

## **3. Get Company by ID**

- **URL**: `/api/v1/companies/{id}`
- **Method**: `GET`
- **Description**: Retrieves detailed information about a specific company.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `id`: The ID of the company.
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "registrationNumber": "string",
    "taxId": "string",
    "address": "string",
    "phoneNumber": "string",
    "email": "string",
    "website": "string",
    "logo": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    },
    "status": "ACTIVE|INACTIVE|SUSPENDED",
    "createdBy": "string",
    "updatedBy": "string",
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `GET` request with company ID to retrieve detailed information.

---

## **4. Update Company**

- **URL**: `/api/v1/companies/{id}`
- **Method**: `PUT`
- **Description**: Updates an existing company.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the company to update.
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "registrationNumber": "string",
    "taxId": "string",
    "address": "string",
    "phoneNumber": "string",
    "email": "string",
    "website": "string",
    "logo": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    },
    "status": "ACTIVE|INACTIVE|SUSPENDED"
  }
  ```
- **Response**: Same as Create Company Response
- **Usage**:
  Send a `PUT` request with company ID and updated details.

---

## **5. Delete Company**

- **URL**: `/api/v1/companies/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a company.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the company to delete.
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request with company ID to delete the company.

---

## **6. Get Active Companies**

- **URL**: `/api/v1/companies/active`
- **Method**: `GET`
- **Description**: Retrieves all active companies.
- **Security**: Requires authentication
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: Array of CompanyResponse objects
  ```json
  [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "registrationNumber": "string",
      "taxId": "string",
      "address": "string",
      "phoneNumber": "string",
      "email": "string",
      "website": "string",
      "logo": {
        "id": "string",
        "fileName": "string",
        "originalFileName": "string",
        "contentType": "string",
        "size": "number",
        "url": "string"
      },
      "status": "ACTIVE|INACTIVE|SUSPENDED",
      "createdBy": "string",
      "updatedBy": "string",
      "createdAt": "2023-05-05T12:00:00Z",
      "updatedAt": "2023-05-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request to retrieve all active companies.

---

## **7. Change Company Status**

- **URL**: `/api/v1/companies/{id}/status`
- **Method**: `PATCH`
- **Description**: Changes the status of a company.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the company.
- **Query Parameters**:
  - `status`: The new status (ACTIVE|INACTIVE|SUSPENDED)
- **Response**: Same as Create Company Response
- **Usage**:
  Send a `PATCH` request with company ID and new status.

This document will be updated as new endpoints are added to the Company module.

---

## **Business Rules**

- **Company Status Flow**: ACTIVE → INACTIVE/SUSPENDED
- **Role-Based Access**:
  - Admins can create, update, delete, and view all companies
  - Users can only view their own company via `/me` endpoint
  - External API access limited to user's own company
- **Vessel Management**: Companies can have multiple vessels/ships associated
- **Contact Management**: Support for multiple company contacts
- **Audit Trail**: All operations track user context for compliance
- **Data Validation**: Required fields enforced at creation time
- **Logo Management**: Support for company logo uploads and updates

---

This document will be updated as new endpoints are added to the Company module.
