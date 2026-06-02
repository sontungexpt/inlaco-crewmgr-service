# API Key Management Endpoints Documentation

This document provides an overview of all available API endpoints for managing API keys in `crewmgrservice` application.

---

## **1. Setup TOTP for API Key Creation**

- **URL**: `/api/v1/api-keys/setup-totp`
- **Method**: `POST`
- **Description**: Sets up TOTP authentication for creating new API keys.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Response**:
  ```json
  {
    "secret": "JBSWY3DPEHPK3PXP",
    "qrCode": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
    "purposeId": "apikey-V1StGXR8_Z5jdHi6B-myT1683225600000",
    "purpose": "API_KEY_CREATION",
    "instructions": "1. Scan QR code with your authenticator app..."
  }
  ```
- **Usage**:
  Send a `POST` request to set up TOTP for API key creation. Returns QR code and secret for authenticator app setup.

---

## **2. Create API Key with OTP**

- **URL**: `/api/v1/api-keys/create-with-otp`
- **Method**: `POST`
- **Description**: Creates a new API key with OTP verification (supports both TOTP and normal OTP).
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Request Body**:
  ```json
  {
    "clientName": "string",
    "description": "string",
    "type": "EXTERNAL",
    "totpCode": "123456",
    "totpPurposeId": "apikey-V1StGXR8_Z5jdHi6B-myT1683225600000",
    "otpCode": "123456",
    "otpPurposeId": "otp-purpose-id",
    "otpSenderType": "EMAIL|SMS"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "keyId": "sk_V1StGXR8_Z5jdHi6B",
    "clientName": "string",
    "description": "string",
    "type": "EXTERNAL",
    "active": true,
    "createdAt": "2023-05-05T12:00:00Z",
    "createdBy": "string",
    "expiresAt": "2023-06-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with API key details and TOTP verification code to create a new API key.

---

## **3. Setup TOTP for Secret Viewing**

- **URL**: `/api/v1/api-keys/{keyId}/setup-secret-totp`
- **Method**: `POST`
- **Description**: Sets up TOTP authentication for viewing API key secret.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Path Parameters**:
  - `keyId`: The ID of the API key
- **Response**:
  ```json
  {
    "secret": "JBSWY3DPEHPK3PXP",
    "qrCode": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
    "purposeId": "secret-sk_V1StGXR8_Z5jdHi6B-V1StGXR8_Z5jdHi6B-myT1683225600000",
    "purpose": "SECRET_KEY_VIEWING",
    "instructions": "1. Scan QR code with your authenticator app..."
  }
  ```
- **Usage**:
  Send a `POST` request with API key ID to set up TOTP for viewing the secret.

---

## **4. Get API Key Secret with TOTP**

- **URL**: `/api/v1/api-keys/{keyId}/secret`
- **Method**: `GET`
- **Description**: Retrieves the secret key value with TOTP verification.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Path Parameters**:
  - `keyId`: The ID of the API key
- **Query Parameters**:
  - `totpCode`: The TOTP verification code
  - `purposeId`: The purpose ID from secret viewing setup
- **Response**:
  ```json
  {
    "id": "string",
    "keyId": "sk_V1StGXR8_Z5jdHi6B",
    "keySecret": "V1StGXR8_Z5jdHi6BmyT1683225600000",
    "clientName": "string",
    "description": "string",
    "type": "EXTERNAL",
    "active": true,
    "createdAt": "2023-05-05T12:00:00Z",
    "createdBy": "string",
    "expiresAt": "2023-06-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `GET` request with API key ID, TOTP code, and purpose ID to retrieve the secret.

---

## **5. Get All API Keys**

- **URL**: `/api/v1/api-keys`
- **Method**: `GET`
- **Description**: Retrieves all API keys in the system.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Response**:
  ```json
  [
    {
      "id": "string",
      "keyId": "sk_V1StGXR8_Z5jdHi6B",
      "clientName": "string",
      "description": "string",
      "type": "EXTERNAL",
      "active": true,
      "createdAt": "2023-05-05T12:00:00Z",
      "createdBy": "string",
      "expiresAt": "2023-06-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request to retrieve all API keys.

---

## **6. Get API Keys by Client Name**

- **URL**: `/api/v1/api-keys/client/{clientName}`
- **Method**: `GET`
- **Description**: Retrieves API keys for a specific client.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Path Parameters**:
  - `clientName`: The name of the client
- **Response**:
  ```json
  [
    {
      "id": "string",
      "keyId": "sk_V1StGXR8_Z5jdHi6B",
      "clientName": "string",
      "description": "string",
      "type": "EXTERNAL",
      "active": true,
      "createdAt": "2023-05-05T12:00:00Z",
      "createdBy": "string",
      "expiresAt": "2023-06-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request with client name to retrieve API keys for that client.

---

## **7. Get Active API Keys**

- **URL**: `/api/v1/api-keys/active`
- **Method**: `GET`
- **Description**: Retrieves all active API keys.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Response**:
  ```json
  [
    {
      "id": "string",
      "keyId": "sk_V1StGXR8_Z5jdHi6B",
      "clientName": "string",
      "description": "string",
      "type": "EXTERNAL",
      "active": true,
      "createdAt": "2023-05-05T12:00:00Z",
      "createdBy": "string",
      "expiresAt": "2023-06-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request to retrieve all active API keys.

---

## **8. Get Inactive API Keys**

- **URL**: `/api/v1/api-keys/inactive`
- **Method**: `GET`
- **Description**: Retrieves all inactive API keys.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Response**:
  ```json
  [
    {
      "id": "string",
      "keyId": "sk_V1StGXR8_Z5jdHi6B",
      "clientName": "string",
      "description": "string",
      "type": "EXTERNAL",
      "active": false,
      "createdAt": "2023-05-05T12:00:00Z",
      "createdBy": "string",
      "expiresAt": "2023-06-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request to retrieve all inactive API keys.

---

## **9. Get API Keys by Creator**

- **URL**: `/api/v1/api-keys/creator/{createdBy}`
- **Method**: `GET`
- **Description**: Retrieves API keys created by a specific user.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Path Parameters**:
  - `createdBy`: The ID of the user who created the keys
- **Response**:
  ```json
  [
    {
      "id": "string",
      "keyId": "sk_V1StGXR8_Z5jdHi6B",
      "clientName": "string",
      "description": "string",
      "type": "EXTERNAL",
      "active": true,
      "createdAt": "2023-05-05T12:00:00Z",
      "createdBy": "string",
      "expiresAt": "2023-06-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request with creator ID to retrieve API keys created by that user.

---

## **10. Activate API Key**

- **URL**: `/api/v1/api-keys/{keyId}/activate`
- **Method**: `PUT`
- **Description**: Activates an API key.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Path Parameters**:
  - `keyId`: The ID of the API key to activate
- **Response**: `204 No Content`
- **Usage**:
  Send a `PUT` request with API key ID to activate it.

---

## **11. Deactivate API Key**

- **URL**: `/api/v1/api-keys/{keyId}/deactivate`
- **Method**: `PUT`
- **Description**: Deactivates an API key.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Path Parameters**:
  - `keyId`: The ID of the API key to deactivate
- **Response**: `204 No Content`
- **Usage**:
  Send a `PUT` request with API key ID to deactivate it.

---

## **12. Validate API Key**

- **URL**: `/api/v1/api-keys/validate`
- **Method**: `GET`
- **Description**: Validates API key credentials.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Security**: Requires `ADMIN` role
- **Query Parameters**:
  - `keyId`: The API key ID
  - `keySecret`: The API key secret
- **Response**:
  ```json
  true
  ```
- **Usage**:
  Send a `GET` request with key ID and secret to validate credentials.

---

## **Security Notes**

1. **Authentication**: All endpoints require Bearer token authentication
2. **Authorization**: All endpoints require `ADMIN` role except where noted
3. **TOTP**: API key creation and secret viewing require TOTP verification
4. **Purpose IDs**: Each TOTP setup generates a unique purpose ID for security
5. **Rate Limiting**: Built-in through TOTP time-based nature

---

This document will be updated as new endpoints are added to the system.
