# Contract Endpoints Documentation

This document provides an overview of all the available API endpoints for managing contracts in the `crewmgrservice` application.

## Data Transfer Objects (DTOs)

### ContractSearchCriteria
```json
{
  "type": "SUPPLY_CONTRACT|LABOR_CONTRACT",
  "signed": "boolean",
  "companyId": "string",
  "crewId": "string",
  "status": "DRAFT|SIGNED|ACTIVE|EXPIRED|CANCELLED"
}
```

### ContractResponse
```json
{
  "id": "string",
  "version": "integer",
  "type": "SUPPLY_CONTRACT|LABOR_CONTRACT",
  "status": "DRAFT|SIGNED|ACTIVE|EXPIRED|CANCELLED",
  "parties": [
    {
      "type": "LABOR|STATIC",
      "name": "string",
      "email": "string",
      "taxCode": "string",
      "identificationCardId": "string",
      "identificationCardIssuedDate": "2023-05-05T12:00:00Z",
      "identificationCardIssuedPlace": "string",
      "bankAccount": "string",
      "bankName": "string",
      "birthDate": "2023-05-05T12:00:00Z",
      "birthPlace": "string",
      "nationality": "string",
      "temporaryAddress": "string"
    }
  ],
  "createdAt": "2023-05-05T12:00:00Z",
  "updatedAt": "2023-05-05T12:00:00Z"
}
```

### ContractPatchRequest
```json
{
  "title": "string",
  "description": "string",
  "parties": [
    {
      "type": "LABOR|STATIC",
      "name": "string",
      "email": "string",
      "taxCode": "string",
      "identificationCardId": "string",
      "identificationCardIssuedDate": "2023-05-05T12:00:00Z",
      "identificationCardIssuedPlace": "string",
      "bankAccount": "string",
      "bankName": "string",
      "birthDate": "2023-05-05T12:00:00Z",
      "birthPlace": "string",
      "nationality": "string",
      "temporaryAddress": "string"
    }
  ]
}
```

## Enums

### ContractStatus
```java
public enum ContractStatus {
  DRAFT,
  SIGNED,
  ACTIVE,
  EXPIRED,
  CANCELLED;
  
  // Allowed transitions:
  // DRAFT → SIGNED, CANCELLED
  // SIGNED → ACTIVE, CANCELLED
  // ACTIVE → EXPIRED, CANCELLED
  // EXPIRED → (no transitions)
  // CANCELLED → (no transitions)
}
```

### ContractType
```java
public enum ContractType {
  SUPPLY_CONTRACT,
  LABOR_CONTRACT
}
```

### PartyType
```java
public enum PartyType {
  LABOR,
  STATIC
}
```

---

## **1. Update Contract**

- **URL**: `/api/v1/contracts/{id}`
- **Method**: `PATCH`
- **Description**: Updates a contract with the provided patch data.
- **Security**: Requires Bearer token authentication
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
  	"title": "string",
  	"description": "string",
  	"parties": [
  	  {
  	    "type": "LABOR|STATIC",
  	    "name": "string",
  	    "email": "string",
  	    "taxCode": "string",
  	    "identificationCardId": "string",
  	    "identificationCardIssuedDate": "2023-05-05T12:00:00Z",
  	    "identificationCardIssuedPlace": "string",
  	    "bankAccount": "string",
  	    "bankName": "string",
  	    "birthDate": "2023-05-05T12:00:00Z",
  	    "birthPlace": "string",
  	    "nationality": "string",
  	    "temporaryAddress": "string"
  	  }
  	]
  }
  ```
- **Response**:
  ```json
  {
  	"id": "string",
  	"field1": "value1",
  	"field2": "value2"
  }
  ```
- **Usage**:
  Send a `PATCH` request with the contract ID and the fields to update in the request body.

---

## **2. Activate Contract**

- **URL**: `/api/v1/contracts/active/{id}`
- **Method**: `POST`
- **Description**: Activates a contract by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the contract ID to activate the contract.

---

## **3. Get Contract Details**

- **URL**: `/api/v1/contracts/{id}`
- **Method**: `GET`
- **Description**: Retrieves the details of a specific contract by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `version` (optional): The version of the contract to retrieve.
- **Response**:
  ```json
  {
  	"id": "string",
  	"field1": "value1",
  	"field2": "value2"
  }
  ```
- **Usage**:
  Send a `GET` request with the contract ID to retrieve its details. Optionally, include the `version` query parameter to fetch a specific version.

---

## **4. Get Old Contract Versions**

- **URL**: `/api/v1/contracts/{id}/old-versions`
- **Method**: `GET`
- **Description**: Retrieves all old versions of a specific contract.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  [
  	{
  		"id": "string",
  		"version": "integer",
  		"field1": "value1",
  		"field2": "value2"
  	}
  ]
  ```
- **Usage**:
  Send a `GET` request with the contract ID to retrieve all its old versions.

---

## **5. Get Contract for Application**

- **URL**: `/api/v1/contracts/applications/{applicationId}`
- **Method**: `GET`
- **Description**: Retrieves the contract details associated with a specific application ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  {
  	"id": "string",
  	"applicationId": "string",
  	"field1": "value1",
  	"field2": "value2"
  }
  ```
- **Usage**:
  Send a `GET` request with the application ID to retrieve the associated contract details.

---

## **6. Get All Contracts**

- **URL**: `/api/v1/contracts`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all contracts based on search criteria.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `type` (optional): The type of contract (e.g., `LABOR_CONTRACT`).
  - `signed` (optional): Filter by signed status (`true` or `false`).
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
- **Response**:
  ```json
  {
  	"content": [
  		{
  			"id": "string",
  			"field1": "value1",
  			"field2": "value2"
  		}
  	],
  	"totalPages": "integer",
  	"totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional query parameters to retrieve a paginated list of contracts.

---

## **7. Get My Contracts**

- **URL**: `/api/v1/contracts/me`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of contracts belonging to the currently logged-in sailor.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
- **Response**:
  ```json
  {
    "content": [ ... ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve the contracts for the authenticated user.

---

This document will be updated as new endpoints are added to the Contract module.
