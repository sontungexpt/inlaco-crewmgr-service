# Crew Rental Request Endpoints

This document provides an overview of the Crew Rental Request-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Crew Rental Request**
- **URL**: `/api/v1/crew-rental-requests`
- **Method**: `POST`
- **Description**: Creates a new crew rental request.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Parameters**:
  - `detailFileAssetId` (required): The asset ID of the detail file.
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
  Send a `POST` request with the required parameters and body to create a new crew rental request.

---

## **2. Review Crew Rental Request**
- **URL**: `/api/v1/crew-rental-requests/{requestId}/review`
- **Method**: `POST`
- **Description**: Allows an admin to review a crew rental request.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Parameters**:
  - `accepted` (required): A boolean indicating whether the request is accepted or rejected.
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the request ID and the `accepted` parameter to review the crew rental request.

---

## **3. Get All Crew Rental Requests**
- **URL**: `/api/v1/crew-rental-requests`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all crew rental requests based on search criteria.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
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
  Send a `GET` request with optional query parameters to retrieve a paginated list of crew rental requests.

---

## **4. Get Crew Rental Request by ID**
- **URL**: `/api/v1/crew-rental-requests/{id}`
- **Method**: `GET`
- **Description**: Retrieves the details of a specific crew rental request by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  {
    "id": "string",
    "field1": "value1",
    "field2": "value2"
  }
  ```
- **Usage**:
  Send a `GET` request with the request ID to retrieve its details.

---

This document will be updated as new endpoints are added to the Crew Rental Request module.