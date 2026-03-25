# Crew Endpoints Documentation

This document provides an overview of all the available API endpoints for managing crew profiles in the `crewmgrservice` application.

---

## **1. Get Crew Profile by ID**
- **URL**: `/api/v1/sailors/{profileId}`
- **Method**: `GET`
- **Description**: Retrieves the details of a specific crew profile by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "email": "string",
    "phone": "string",
    "address": "string",
    "position": "string"
  }
  ```
- **Usage**:
  Send a `GET` request with the crew profile ID to retrieve its details.

---

## **2. Update Crew Profile**
- **URL**: `/api/v1/sailors/{id}`
- **Method**: `PATCH`
- **Description**: Updates a crew profile with the provided patch data.
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
  Send a `PATCH` request with the crew profile ID and the fields to update in the request body.

---

## **3. Get All Crew Profiles**
- **URL**: `/api/v1/sailors`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all crew profiles based on search criteria.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `position` (optional): Filter by position.
  - `status` (optional): Filter by status.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "name": "string",
        "email": "string",
        "phone": "string",
        "address": "string",
        "position": "string"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional query parameters to retrieve a paginated list of crew profiles.

---

This document will be updated as new endpoints are added to the Crew module.