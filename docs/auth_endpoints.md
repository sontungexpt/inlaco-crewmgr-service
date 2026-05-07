# API Endpoints Documentation

This document provides an overview of all the available API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request body (if applicable), and usage instructions.

---

## **Authentication Endpoints**

### **1. Register a New User**

- **URL**: `/api/v1/auth/register`
- **Method**: `POST`
- **Description**: Registers a new user in the system.
- **Security**: Public endpoint (no authentication required)
- **Request Body**:
  ```json
  {
  	"username": "string",
  	"password": "string",
  	"confirmPassword": "string",
  	"name": "string"
  }
  ```
- **Response**: `202 Accepted`
  ```json
  {
  	"accessToken": "string",
  	"refreshToken": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the required fields in the body to register a new user. Returns HTTP 202 Accepted.

---

### **2. Login**

- **URL**: `/api/v1/auth/login`
- **Method**: `POST`
- **Description**: Logs the user into the system and returns authentication tokens.
- **Security**: Public endpoint (no authentication required)
- **Request Body**:
  ```json
  {
  	"username": "string",
  	"password": "string"
  }
  ```
- **Response**:
  ```json
  {
  	"accessToken": "string",
  	"refreshToken": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the username and password to log in and receive authentication tokens.

---

### **3. Logout**

- **URL**: `/api/v1/auth/logout`
- **Method**: `POST`
- **Description**: Logs the user out of the system.
- **Security**: Requires Bearer token with refresh token
- **Headers**:
  - `Authorization`: Bearer `<refreshToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the `refreshToken` in the `Authorization` header to log out.

---

### **4. Refresh Token**

- **URL**: `/api/v1/auth/refresh-token`
- **Method**: `POST`
- **Description**: Refreshes an expired JWT authentication token.
- **Security**: Requires Bearer token with refresh token
- **Headers**:
  - `Authorization`: Bearer `<refreshToken>`
- **Response**:
  ```json
  {
  	"accessToken": "string",
  	"refreshToken": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the `refreshToken` in the `Authorization` header to refresh the authentication token.

---

## **Two-Step Verification Endpoints**

### **1. Verify Two-Step Verification**

- **URL**: `/api/v1/auth/two-step-verification`
- **Method**: `GET`
- **Description**: Verifies the two-step verification token.
- **Query Parameters**:
  - `token`: The verification token.
- **Response**: `204 No Content`
- **Usage**:
  Send a `GET` request with the `token` query parameter to verify the two-step verification.

---

### **2. Resend Two-Step Verification**

- **URL**: `/api/v1/auth/two-step-verification/resend`
- **Method**: `POST`
- **Description**: Resends the two-step verification token to the current user.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the `accessToken` in the `Authorization` header to resend the two-step verification token.

---

## **Well-Known Endpoints**

### **1. Asset Links**

- **URL**: `/.well-known/assetlinks.json`
- **Method**: `GET`
- **Description**: Provides the `assetlinks.json` file for app linking.
- **Response**:
  - JSON object containing asset links.
- **Usage**:
  Send a `GET` request to retrieve the `assetlinks.json` file.

---

---

## **Contract Endpoints**

### **1. Update Contract**

- **URL**: `/api/v1/contracts/{id}`
- **Method**: `PATCH`
- **Description**: Updates a contract with the provided patch data.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
  	"field1": "value1",
  	"field2": "value2"
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

### **2. Activate Contract**

- **URL**: `/api/v1/contracts/active/{id}`
- **Method**: `POST`
- **Description**: Activates a contract by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the contract ID to activate the contract.

---

### **3. Get Contract Details**

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

### **4. Get Old Contract Versions**

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

### **5. Get Contract for Application**

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

### **6. Get All Contracts**

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

### **7. Get My Contracts**

- **URL**: `/api/v1/contracts/me`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of contracts belonging to the currently logged-in user.
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

---

---

## **Crew Supply Contract Endpoints**

### **1. Create Crew Supply Contract**

- **URL**: `/api/v1/contracts/supplies/{requestId}`
- **Method**: `POST`
- **Description**: Creates a new crew supply contract for a sailor.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Parameters**:
  - `contractFileAssetId` (required): The asset ID of the contract file.
  - `shipImageAssetId` (required): The asset ID of the ship image.
- **Request Body**:
  ```json
  {
  	"field1": "value1",
  	"field2": "value2"
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
  Send a `POST` request with the required parameters and body to create a new crew supply contract.

---

## **Labor Contract Endpoints**

### **1. Create Labor Contract**

- **URL**: `/api/v1/contracts/labors/{applicationId}`
- **Method**: `POST`
- **Description**: Creates a new labor contract for a sailor.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
  	"field1": "value1",
  	"field2": "value2",
  	"contractFile": "fileAssetId",
  	"attachments": ["attachment1", "attachment2"]
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
  Send a `POST` request with the application ID and the required fields in the body to create a new labor contract.

---

## **Contract Template Endpoints**

### **1. Get All Contract Templates**

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

### **2. Upload Contract Template**

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

### **3. Remove Contract Template**

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
