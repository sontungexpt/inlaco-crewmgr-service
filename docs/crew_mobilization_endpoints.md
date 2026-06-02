# Crew Mobilization Endpoints Documentation

This document provides an overview of Crew Mobilization-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Crew Mobilization**
- **URL**: `/api/v1/schedules`
- **Method**: `POST`
- **Description**: Creates a new crew mobilization schedule with crew assignments.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "title": "string",
    "description": "string",
    "shipInfo": {
      "shipName": "string",
      "shipImo": "string",
      "image": "string"
    },
    "startDate": "2023-05-05T12:00:00Z",
    "endDate": "2023-05-15T12:00:00Z",
    "crews": [
      {
        "crewId": "string",
        "position": "string",
        "joinDate": "2023-05-05T12:00:00Z"
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "title": "string",
    "description": "string",
    "status": "ACTIVE|COMPLETED|CANCELLED",
    "shipInfo": {
      "shipName": "string",
      "shipImo": "string",
      "image": "string"
    },
    "startDate": "2023-05-05T12:00:00Z",
    "endDate": "2023-05-15T12:00:00Z",
    "createdAt": "2023-05-05T10:00:00Z",
    "crews": [
      {
        "crewId": "string",
        "name": "string",
        "position": "string",
        "joinDate": "2023-05-05T12:00:00Z"
      }
    ]
  }
  ```
- **Usage**:
  Send a `POST` request with mobilization details and crew assignments to create a new schedule.

---

## **2. Get All Crew Mobilizations**
- **URL**: `/api/v1/schedules`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all crew mobilizations based on search criteria.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `keyword` (optional): Search in title, description, ship name.
  - `accountId` (optional): Filter by account ID.
  - `shipIMO` (optional): Filter by ship IMO number.
  - `status` (optional): Filter by mobilization status.
  - `startDate` (optional): Filter by start date range (from).
  - `endDate` (optional): Filter by end date range (to).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "title": "string",
        "status": "ACTIVE|COMPLETED|CANCELLED",
        "shipInfo": {
          "shipName": "string",
          "shipImo": "string"
        },
        "startDate": "2023-05-05T12:00:00Z",
        "endDate": "2023-05-15T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve paginated mobilizations.

---

## **3. Get Crew Mobilization by ID**
- **URL**: `/api/v1/schedules/{id}`
- **Method**: `GET`
- **Description**: Retrieves detailed information about a specific crew mobilization.
- **Security**: Requires `ADMIN` or `SAILOR` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the crew mobilization.
- **Response**:
  ```json
  {
    "id": "string",
    "title": "string",
    "description": "string",
    "status": "ACTIVE|COMPLETED|CANCELLED",
    "shipInfo": {
      "shipName": "string",
      "shipImo": "string",
      "image": "string"
    },
    "startDate": "2023-05-05T12:00:00Z",
    "endDate": "2023-05-15T12:00:00Z",
    "createdAt": "2023-05-05T10:00:00Z",
    "crews": [
      {
        "crewId": "string",
        "name": "string",
        "position": "string",
        "email": "string",
        "phone": "string",
        "joinDate": "2023-05-05T12:00:00Z"
      }
    ]
  }
  ```
- **Usage**:
  Send a `GET` request with mobilization ID to retrieve detailed information.

---

## **4. Get My Crew Mobilizations**
- **URL**: `/api/v1/schedules/mine`
- **Method**: `GET`
- **Description**: Retrieves crew mobilizations that include the current authenticated sailor.
- **Security**: Requires `SAILOR` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `keyword` (optional): Search in title, description, ship name.
  - `status` (optional): Filter by mobilization status.
  - `startDate` (optional): Filter by start date range (from).
  - `endDate` (optional): Filter by end date range (to).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "title": "string",
        "status": "ACTIVE|COMPLETED|CANCELLED",
        "shipInfo": {
          "shipName": "string",
          "shipImo": "string"
        },
        "startDate": "2023-05-05T12:00:00Z",
        "endDate": "2023-05-15T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve mobilizations for the authenticated sailor.

---

## **5. Export Crew Mobilization to Excel**
- **URL**: `/api/v1/schedules/{id}/export`
- **Method**: `GET`
- **Description**: Exports a crew mobilization to Excel format.
- **Security**: Requires `ADMIN` or `SAILOR` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the crew mobilization to export.
- **Response**:
  - Content-Type: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
  - Content-Disposition: `attachment; filename=schedule.xlsx`
  - Binary Excel file data
- **Usage**:
  Send a `GET` request with mobilization ID to download Excel file.

---

## **Business Rules**

- **Multiple Mobilizations**: Allowed to create multiple mobilizations for the same contract
- **No Quantity Limits**: No restrictions on crew count per mobilization
- **Role-Based Access**:
  - Admins can create, view all, and export mobilizations
  - Sailors can only view their own mobilizations and export them
- **Status Flow**: ACTIVE → COMPLETED/CANCELLED
- **Audit Trail**: All operations track user context for compliance
- **File Requirements**: Requires ship image asset for mobilization creation

---

This document will be updated as new endpoints are added to the Crew Mobilization module.
