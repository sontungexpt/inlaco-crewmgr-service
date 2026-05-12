# Ship Schedule Endpoints Documentation

This document provides an overview of Ship Schedule-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Ship Schedule**
- **URL**: `/api/v1/ship-schedules`
- **Method**: `POST`
- **Description**: Creates a new ship schedule with crew assignments.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Request Body**:
  ```json
  {
    "shipName": "string",
    "shipImo": "string",
    "departureTime": "2023-05-05T12:00:00Z",
    "arrivalTime": "2023-05-15T12:00:00Z",
    "port": "string",
    "vesselOwnerId": "string",
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
    "shipName": "string",
    "shipImo": "string",
    "departureTime": "2023-05-05T12:00:00Z",
    "arrivalTime": "2023-05-15T12:00:00Z",
    "port": "string",
    "status": "SCHEDULED|DEPARTED|ARRIVED|CANCELLED",
    "vesselOwnerId": "string",
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
  Send a `POST` request with schedule details and crew assignments to create a new ship schedule.

---

## **2. Get Ship Schedule by ID**
- **URL**: `/api/v1/ship-schedules/{id}`
- **Method**: `GET`
- **Description**: Retrieves detailed information about a specific ship schedule.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `id`: The ID of the ship schedule.
- **Response**:
  ```json
  {
    "id": "string",
    "shipName": "string",
    "shipImo": "string",
    "departureTime": "2023-05-05T12:00:00Z",
    "arrivalTime": "2023-05-15T12:00:00Z",
    "port": "string",
    "status": "SCHEDULED|DEPARTED|ARRIVED|CANCELLED",
    "vesselOwnerId": "string",
    "createdAt": "2023-05-05T10:00:00Z",
    "updatedAt": "2023-05-05T11:00:00Z",
    "crews": [
      {
        "crewId": "string",
        "name": "string",
        "position": "string",
        "email": "string",
        "phone": "string",
        "joinDate": "2023-05-05T12:00:00Z"
      }
    ],
    "scheduleDetails": {
      "cargo": "string",
      "voyageNumber": "string",
      "additionalNotes": "string"
    }
  }
  ```
- **Usage**:
  Send a `GET` request with schedule ID to retrieve detailed information.

---

## **3. Get All Ship Schedules**
- **URL**: `/api/v1/ship-schedules`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all ship schedules based on search criteria.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 10).
  - `keyword` (optional): Search in ship name, ship IMO, client ID.
  - `clientId` (optional): Filter by vessel owner ID.
  - `shipImo` (optional): Filter by ship IMO number.
  - `status` (optional): Filter by schedule status.
  - `departureTimeFrom` (optional): Filter by departure time range (from).
  - `departureTimeTo` (optional): Filter by departure time range (to).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "shipName": "string",
        "shipImo": "string",
        "status": "SCHEDULED|DEPARTED|ARRIVED|CANCELLED",
        "departureTime": "2023-05-05T12:00:00Z",
        "arrivalTime": "2023-05-15T12:00:00Z",
        "port": "string"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve paginated ship schedules.

---

## **4. Get My Ship Schedules**
- **URL**: `/api/v1/ship-schedules/me`
- **Method**: `GET`
- **Description**: Retrieves ship schedules belonging to the authenticated vessel owner.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 10).
  - `keyword` (optional): Search in ship name, ship IMO.
  - `shipImo` (optional): Filter by ship IMO number.
  - `status` (optional): Filter by schedule status.
  - `departureTimeFrom` (optional): Filter by departure time range (from).
  - `departureTimeTo` (optional): Filter by departure time range (to).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "shipName": "string",
        "shipImo": "string",
        "status": "SCHEDULED|DEPARTED|ARRIVED|CANCELLED",
        "departureTime": "2023-05-05T12:00:00Z",
        "arrivalTime": "2023-05-15T12:00:00Z",
        "port": "string"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve ship schedules for the authenticated vessel owner.

---

## **External API Access**

### External API Controller
- **URL**: `/api/v1/external/ship-schedules`
- **Method**: `GET`
- **Description**: External API endpoint for third-party access to ship schedules.
- **Security**: Requires API Key authentication
- **Headers**:
  - `X-API-Key`: `<apiKey>`
- **Query Parameters**: Same as internal Get All Ship Schedules
- **Response**: Same format as Get All Ship Schedules
- **Usage**:
  External systems can access ship schedules using API key authentication.

---

## **Business Rules**

- **Flexible Search**: MongoDB Criteria API for dynamic query building
- **Multi-Authentication**: Supports both Bearer tokens and API keys
- **Role-Based Access**:
  - Internal users can access all schedules
  - External API users limited to their own schedules
  - Vessel owners can only see their schedules via `/me` endpoint
- **Status Flow**: SCHEDULED → DEPARTED → ARRIVED/CANCELLED
- **Crew Management**: Full CRUD operations on crew assignments
- **Audit Trail**: All operations track user context for compliance

---

This document will be updated as new endpoints are added to the Ship Schedule module.
