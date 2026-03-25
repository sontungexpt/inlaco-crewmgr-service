# Contract Template Endpoints Documentation

This document provides an overview of all the available API endpoints for managing contract templates in the `crewmgrservice` application.

---

## **1. Get All Contract Templates**
- **URL**: `/api/v1/contract-templates`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all contract templates.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `type` (optional): The type of contract template.
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "type": "string",
        "name": "string"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional query parameters to retrieve a paginated list of contract templates.

---

## **2. Upload Contract Template**
- **URL**: `/api/v1/contract-templates`
- **Method**: `POST`
- **Description**: Uploads a new contract template.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Parameters**:
  - `templateFileAssetId` (required): The asset ID of the template file.
- **Request Body**:
  ```json
  {
    "name": "string",
    "type": "string",
    "description": "string"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "type": "string",
    "description": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the required parameters and body to upload a new contract template.

---

## **3. Remove Contract Template**
- **URL**: `/api/v1/contract-templates/{id}`
- **Method**: `DELETE`
- **Description**: Removes a contract template by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request with the template ID to remove the contract template.

---

This document will be updated as new endpoints are added to the system.