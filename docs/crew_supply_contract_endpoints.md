# Crew Supply Contract Endpoints

This document provides an overview of the Crew Supply Contract endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request body (if applicable), and usage instructions.

---

## **1. Create Crew Supply Contract**
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

This document will be updated as new endpoints are added to the Crew Supply Contract module.