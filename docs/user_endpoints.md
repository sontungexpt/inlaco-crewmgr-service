# User Endpoints Documentation

This document provides an overview of User-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Get User Profile**
- **URL**: `/api/v1/users/me`
- **Method**: `GET`
- **Description**: Retrieves the profile information of the currently authenticated user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  {
    "id": "string",
    "username": "string",
    "name": "string",
    "email": "string",
    "roles": ["USER", "ADMIN", "SAILOR"],
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z",
    "profile": {
      "phone": "string",
      "address": "string",
      "dateOfBirth": "1990-01-01",
      "nationality": "string",
      "avatar": "string"
    }
  }
  ```
- **Usage**:
  Send a `GET` request with Bearer token to retrieve the current user's profile information.

---

## **2. Update User Profile**
- **URL**: `/api/v1/users/me`
- **Method**: `PATCH`
- **Description**: Updates the profile information of the currently authenticated user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
  - `Content-Type`: `application/merge-patch+json`
- **Request Body**:
  ```json
  {
    "name": "string (optional)",
    "email": "string (optional)",
    "profile": {
      "phone": "string (optional)",
      "address": "string (optional)",
      "dateOfBirth": "1990-01-01 (optional)",
      "nationality": "string (optional)",
      "avatar": "string (optional)"
    }
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "username": "string",
    "name": "string",
    "email": "string",
    "roles": ["USER", "ADMIN", "SAILOR"],
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:30:00Z",
    "profile": {
      "phone": "string",
      "address": "string",
      "dateOfBirth": "1990-01-01",
      "nationality": "string",
      "avatar": "string"
    }
  }
  ```
- **Usage**:
  Send a `PATCH` request with only the fields you want to update. Only provide fields that need to be changed.

---

## **3. Change Password**
- **URL**: `/api/v1/users/change-password`
- **Method**: `POST`
- **Description**: Changes the password for the currently authenticated user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "currentPassword": "string",
    "newPassword": "string",
    "confirmPassword": "string"
  }
  ```
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with current password and new password to change the password.

---

## **4. Upload Avatar**
- **URL**: `/api/v1/users/avatar`
- **Method**: `POST`
- **Description**: Uploads an avatar image for the currently authenticated user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
  - `Content-Type`: `multipart/form-data`
- **Request Body**:
  - `file`: Image file (JPEG, PNG, GIF formats supported)
  - Max file size: 5MB
- **Response**:
  ```json
  {
    "assetId": "string",
    "fileName": "string",
    "contentType": "string",
    "size": "integer",
    "url": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with image file to upload user avatar.

---

## **5. Delete Account**
- **URL**: `/api/v1/users/me`
- **Method**: `DELETE`
- **Description**: Deletes the account of the currently authenticated user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request to permanently delete the user account. This action cannot be undone.

---

## **Business Rules**

- **Profile Ownership**: Users can only view and update their own profiles
- **Password Security**: 
  - Current password required for password changes
  - New password must meet complexity requirements
  - Password confirmation required
- **Avatar Management**:
  - Support for common image formats
  - File size limits enforced
  - Asset management integration
- **Account Deletion**:
  - Permanent deletion of all user data
  - Requires authentication confirmation
  - Cannot be undone
- **Role-Based Access**:
  - All endpoints require at least USER role
  - Admin endpoints have separate access controls

---

This document will be updated as new endpoints are added to the User module.
