# Crew Supply Contract Endpoints

This document provides an overview of the Crew Supply Contract endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request body (if applicable), and usage instructions.

---

## **1. Create Crew Supply Contract**
- **URL**: `/api/v1/contracts/supplies/{supplyRequestId}`
- **Method**: `POST`
- **Description**: Creates a new crew supply contract for a sailor.
- **Security**: Requires Bearer token authentication and ADMIN role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `supplyRequestId`: The supply request ID
- **Request Body**:
  ```json
  {
    "type": "SUPPLY_CONTRACT",
    "title": "string",
    "initiator": {
      "id": "string",
      "name": "string",
      "type": "COMPANY|SAILOR"
    },
    "partners": [
      {
        "id": "string",
        "name": "string",
        "type": "COMPANY|SAILOR"
      }
    ],
    "contractFile": "assetId",
    "attachments": ["assetId1", "assetId2"],
    "customAttributes": [
      {
        "key": "string",
        "value": "string"
      }
    ],
    "activationDate": "2023-05-05T12:00:00Z",
    "expiredDate": "2023-06-05T12:00:00Z",
    "contractFreezeDelayMinutes": 5,
    "shipInfo": {
      "imoNumber": "string",
      "countryISO": "string",
      "name": "string",
      "description": "string",
      "image": "assetId",
      "type": "string"
    }
  }
  ```
- **Response**: Same as Contract Response in contract_endpoints.md
- **Usage**:
  Send a `POST` request with the supply request ID and contract details to create a new crew supply contract.

---

This document will be updated as new endpoints are added to the Crew Supply Contract module.