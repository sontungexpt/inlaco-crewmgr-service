# Ship Endpoints Documentation

This document provides an overview of Ship-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Ship**

- **URL**: `/api/v1/ships`
- **Method**: `POST`
- **Description**: Creates a new ship/vessel in the system.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "name": "string",
    "imo": "string",
    "type": "string",
    "flag": "string",
    "builtYear": "integer",
    "tonnage": "number",
    "length": "number",
    "breadth": "number",
    "engineType": "string",
    "ownerId": "string",
    "image": "string",
    "documents": ["string"]
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "imo": "string",
    "type": "string",
    "flag": "string",
    "builtYear": "integer",
    "tonnage": "number",
    "length": "number",
    "breadth": "number",
    "engineType": "string",
    "ownerId": "string",
    "image": "string",
    "status": "ACTIVE|INACTIVE|MAINTENANCE",
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with ship details to create a new vessel.

---

## **2. Get All Ships**

- **URL**: `/api/v1/ships`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all ships.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `keyword` (optional): Search in ship name, IMO.
  - `type` (optional): Filter by ship type.
  - `flag` (optional): Filter by ship flag.
  - `ownerId` (optional): Filter by vessel owner.
  - `status` (optional): Filter by ship status.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "name": "string",
        "imo": "string",
        "type": "string",
        "flag": "string",
        "status": "ACTIVE|INACTIVE|MAINTENANCE",
        "builtYear": "integer",
        "tonnage": "number"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve paginated ships.

---

## **3. Get Ship by ID**

- **URL**: `/api/v1/ships/{id}`
- **Method**: `GET`
- **Description**: Retrieves detailed information about a specific ship.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `id`: The ID of the ship.
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "imo": "string",
    "type": "string",
    "flag": "string",
    "builtYear": "integer",
    "tonnage": "number",
    "length": "number",
    "breadth": "number",
    "engineType": "string",
    "ownerId": "string",
    "image": "string",
    "status": "ACTIVE|INACTIVE|MAINTENANCE",
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z",
    "documents": [
      {
        "id": "string",
        "name": "string",
        "type": "CERTIFICATE|REGISTRATION|INSURANCE",
        "url": "string",
        "expiryDate": "2023-12-31T23:59:59Z"
      }
    ],
    "specifications": {
      "crewCapacity": "integer",
      "maxSpeed": "number",
      "fuelType": "string",
      "classification": "string"
    }
  }
  ```
- **Usage**:
  Send a `GET` request with ship ID to retrieve detailed vessel information.

---

## **4. Update Ship**

- **URL**: `/api/v1/ships/{id}`
- **Method**: `PATCH`
- **Description**: Updates an existing ship with provided patch data.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
  - `Content-Type`: `application/merge-patch+json`
- **Path Parameters**:
  - `id`: The ID of the ship to update.
- **Request Body**:
  ```json
  {
    "name": "string (optional)",
    "imo": "string (optional)",
    "type": "string (optional)",
    "flag": "string (optional)",
    "builtYear": "integer (optional)",
    "tonnage": "number (optional)",
    "status": "ACTIVE|INACTIVE|MAINTENANCE (optional)"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "imo": "string",
    "type": "string",
    "flag": "string",
    "builtYear": "integer",
    "tonnage": "number",
    "status": "ACTIVE|INACTIVE|MAINTENANCE",
    "updatedAt": "2023-05-05T12:30:00Z"
  }
  ```
- **Usage**:
  Send a `PATCH` request with ship ID and fields to update. Only provide fields you wish to change.

---

## **5. Delete Ship**

- **URL**: `/api/v1/ships/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a ship by its ID.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the ship to delete.
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request with ship ID to permanently delete the vessel.

---

## **6. Get My Ships**

- **URL**: `/api/v1/ships/me`
- **Method**: `GET`
- **Description**: Retrieves ships belonging to the authenticated vessel owner.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `keyword` (optional): Search in ship name, IMO.
  - `type` (optional): Filter by ship type.
  - `status` (optional): Filter by ship status.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "name": "string",
        "imo": "string",
        "type": "string",
        "flag": "string",
        "status": "ACTIVE|INACTIVE|MAINTENANCE",
        "builtYear": "integer",
        "tonnage": "number"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve ships for the authenticated vessel owner.

---

## **7. Upload Ship Document**

- **URL**: `/api/v1/ships/{id}/documents`
- **Method**: `POST`
- **Description**: Uploads a document for a specific ship.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
  - `Content-Type`: `multipart/form-data`
- **Path Parameters**:
  - `id`: The ID of the ship.
- **Request Body**:
  - `file`: Document file (required)
  - `type`: Document type (CERTIFICATE|REGISTRATION|INSURANCE)
  - `name`: Document name (required)
  - `expiryDate`: Document expiry date (optional)
- **Response**:
  ```json
  {
    "id": "string",
    "shipId": "string",
    "name": "string",
    "type": "CERTIFICATE|REGISTRATION|INSURANCE",
    "url": "string",
    "expiryDate": "2023-12-31T23:59:59Z",
    "uploadedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with document file to upload ship documentation.

---

## **8. Get Ship Documents**

- **URL**: `/api/v1/ships/{id}/documents`
- **Method**: `GET`
- **Description**: Retrieves all documents for a specific ship.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `id`: The ID of the ship.
- **Query Parameters**:
  - `type` (optional): Filter by document type.
- **Response**:
  ```json
  [
    {
      "id": "string",
      "shipId": "string",
      "name": "string",
      "type": "CERTIFICATE|REGISTRATION|INSURANCE",
      "url": "string",
      "expiryDate": "2023-12-31T23:59:59Z",
      "uploadedAt": "2023-05-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request with ship ID to retrieve all vessel documents.

---

## **Business Rules**

- **Ship Status Flow**: ACTIVE → INACTIVE/MAINTENANCE → ACTIVE
- **IMO Validation**: IMO numbers must be unique and properly formatted
- **Document Management**:
  - Support for certificates, registrations, and insurance
  - Expiry date tracking for document renewal
  - File upload integration with asset management
- **Role-Based Access**:
  - Admins can create, update, delete, and view all ships
  - Vessel owners can only view their own ships via `/me` endpoint
  - External API access limited to user's own ships
- **Data Validation**:
  - Required fields enforced at creation time
  - IMO number format validation
  - Year and tonnage range validation
- **Audit Trail**: All operations track user context for compliance

---

This document will be updated as new endpoints are added to the Ship module.
