# Labor Contract Endpoints

This document provides an overview of the Labor Contract-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Labor Contract**
- **URL**: `/api/v1/contracts/labors/{applicationId}`
- **Method**: `POST`
- **Description**: Creates a new labor contract for a sailor.
- **Security**: Requires Bearer token authentication and ADMIN role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `applicationId`: The application ID
- **Request Body**:
  ```json
  {
    "type": "LABOR_CONTRACT",
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
    "position": "string",
    "workingLocation": "string",
    "basicSalary": "string",
    "allowance": "string",
    "receiveMethod": "string",
    "payday": "string",
    "salaryReviewPeriod": "string"
  }
  ```
- **Response**: Same as Contract Response in contract_endpoints.md
- **Usage**:
  Send a `POST` request with the application ID and the required fields in the body to create a new labor contract.

---

This document will be updated as new endpoints are added to the Labor Contract module.