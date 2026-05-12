# Authentication Endpoints Documentation

This document provides an overview of Authentication-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **Authentication Endpoints**

### **1. Register a New User**

- **URL**: `/api/v1/auth/register`
- **Method**: `POST`
- **Description**: Registers a new user in the system.
- **Security**: Public endpoint (no authentication required)
- **Request Body**:
  ```json
  {
  	"username": "string",
  	"password": "string",
  	"confirmPassword": "string",
  	"name": "string"
  }
  ```
- **Response**: `202 Accepted`
  ```json
  {
  	"accessToken": "string",
  	"refreshToken": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the required fields in the body to register a new user. Returns HTTP 202 Accepted.

---

### **2. Login**

- **URL**: `/api/v1/auth/login`
- **Method**: `POST`
- **Description**: Logs the user into the system and returns authentication tokens.
- **Security**: Public endpoint (no authentication required)
- **Request Body**:
  ```json
  {
  	"username": "string",
  	"password": "string"
  }
  ```
- **Response**:
  ```json
  {
  	"accessToken": "string",
  	"refreshToken": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the username and password to log in and receive authentication tokens.

---

### **3. Logout**

- **URL**: `/api/v1/auth/logout`
- **Method**: `POST`
- **Description**: Logs the user out of the system.
- **Security**: Requires Bearer token with refresh token
- **Headers**:
  - `Authorization`: Bearer `<refreshToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the `refreshToken` in the `Authorization` header to log out.

---

### **4. Refresh Token**

- **URL**: `/api/v1/auth/refresh-token`
- **Method**: `POST`
- **Description**: Refreshes an expired JWT authentication token.
- **Security**: Requires Bearer token with refresh token
- **Headers**:
  - `Authorization`: Bearer `<refreshToken>`
- **Response**:
  ```json
  {
  	"accessToken": "string",
  	"refreshToken": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the `refreshToken` in the `Authorization` header to refresh the authentication token.

---

## **Two-Step Verification Endpoints**

### **1. Verify Two-Step Verification**

- **URL**: `/api/v1/auth/two-step-verification`
- **Method**: `GET`
- **Description**: Verifies the two-step verification token (email-based OTP).
- **Security**: Public endpoint (no authentication required)
- **Query Parameters**:
  - `token`: The verification token sent to user's email.
- **Response**: `204 No Content`
- **Usage**:
  Send a `GET` request with the `token` query parameter to verify the two-step verification.

---

### **2. Resend Two-Step Verification**

- **URL**: `/api/v1/auth/two-step-verification/resend`
- **Method**: `POST`
- **Description**: Resends the two-step verification token to the current user's email.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the `accessToken` in the `Authorization` header to resend the two-step verification token.

---

## **TOTP Endpoints**

### **1. Enable TOTP**

- **URL**: `/api/v1/auth/totp/enable`
- **Method**: `POST`
- **Description**: Enables TOTP two-factor authentication for the current user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "password": "string",
    "totpCode": "string"
  }
  ```
- **Response**:
  ```json
  {
    "secret": "string",
    "qrCode": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
    "backupCodes": ["123456", "789012", "345678"],
    "instructions": "1. Scan QR code with your authenticator app..."
  }
  ```
- **Usage**:
  Send a `POST` request with password and TOTP code to enable TOTP 2FA.

---

### **2. Disable TOTP**

- **URL**: `/api/v1/auth/totp/disable`
- **Method**: `POST`
- **Description**: Disables TOTP two-factor authentication for the current user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "password": "string",
    "totpCode": "string"
  }
  ```
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with password and TOTP code to disable TOTP 2FA.

---

### **3. Generate Backup Codes**

- **URL**: `/api/v1/auth/totp/backup-codes`
- **Method**: `POST`
- **Description**: Generates new backup codes for TOTP recovery.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "password": "string",
    "totpCode": "string"
  }
  ```
- **Response**:
  ```json
  {
    "backupCodes": ["123456", "789012", "345678", "901234", "567890"],
    "generatedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with password and TOTP code to generate new backup codes.

---

## **Well-Known Endpoints**

### **1. Asset Links**

- **URL**: `/.well-known/assetlinks.json`
- **Method**: `GET`
- **Description**: Provides the `assetlinks.json` file for app linking.
- **Response**:
  - JSON object containing asset links.
- **Usage**:
  Send a `GET` request to retrieve the `assetlinks.json` file.

---

This document will be updated as new endpoints are added to the system.
