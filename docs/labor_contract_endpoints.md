# Labor Contract Endpoints

This document provides an overview of the Labor Contract-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Labor Contract**
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

This document will be updated as new endpoints are added to the Labor Contract module.