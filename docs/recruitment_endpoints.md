# Recruitment Endpoints Documentation

This document provides an overview of Recruitment-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Recruitment Post**

- **URL**: `/api/v1/posts`
- **Method**: `POST`
- **Description**: Creates a new recruitment post. Use `type: "RECRUITMENT"` to specify recruitment post type.
- **Security**: Requires authentication (Bearer token)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "type": "RECRUITMENT",
    "title": "string",
    "content": "string",
    "description": "string",
    "position": "string",
    "expectedSalary": "string",
    "active": true,
    "workLocation": "string",
    "recruitmentStartDate": "2023-06-05T12:00:00Z",
    "recruitmentEndDate": "2023-07-05T12:00:00Z",
    "company": "string",
    "imageAssetId": "string",
    "attachmentAssetIds": ["string"]
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "type": "RECRUITMENT",
    "title": "string",
    "content": "string",
    "description": "string",
    "position": "string",
    "expectedSalary": "string",
    "active": true,
    "workLocation": "string",
    "recruitmentStartDate": "2023-06-05T12:00:00Z",
    "recruitmentEndDate": "2023-07-05T12:00:00Z",
    "company": "string",
    "image": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    },
    "attachments": [
      {
        "id": "string",
        "fileName": "string",
        "originalFileName": "string",
        "contentType": "string",
        "size": "number",
        "url": "string"
      }
    ],
    "authorId": "string",
    "updatedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with `type: "RECRUITMENT"` and recruitment post details to create a new job posting.

---

## **2. Get All Recruitment Posts**

- **URL**: `/api/v1/recruitment/posts`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all recruitment posts.
- **Security**: Public endpoint (no authentication required)
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 10).
  - `position` (optional): Filter by job position.
  - `location` (optional): Filter by location.
  - `status` (optional): Filter by post status.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "title": "string",
        "position": "string",
        "location": "string",
        "status": "ACTIVE|EXPIRED|CLOSED",
        "expirationDate": "2023-06-05T12:00:00Z",
        "company": "string",
        "createdAt": "2023-05-05T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve recruitment posts.

---

## **3. Get Recruitment Post by ID**

- **URL**: `/api/v1/recruitment/posts/{id}`
- **Method**: `GET`
- **Description**: Retrieves detailed information about a specific recruitment post.
- **Security**: Public endpoint (no authentication required)
- **Path Parameters**:
  - `id`: The ID of the recruitment post.
- **Response**:
  ```json
  {
    "id": "string",
    "title": "string",
    "description": "string",
    "position": "string",
    "requirements": "string",
    "salary": "string",
    "location": "string",
    "status": "ACTIVE|EXPIRED|CLOSED",
    "expirationDate": "2023-06-05T12:00:00Z",
    "company": "string",
    "createdAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z",
    "author": {
      "id": "string",
      "name": "string",
      "email": "string"
    }
  }
  ```
- **Usage**:
  Send a `GET` request with post ID to retrieve detailed recruitment information.

---

## **4. Apply for Job**

- **URL**: `/api/v1/applications/recruitment/{recruitmentPostId}`
- **Method**: `POST`
- **Description**: Submits a job application for a specific recruitment post.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `recruitmentPostId`: The ID of the recruitment post.
- **Query Parameters**:
  - `resumeAssetId` (required): Asset ID of uploaded resume file.
- **Request Body**:
  ```json
  {
    "fullName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string",
    "gender": "MALE|FEMALE|OTHER",
    "languageSkills": "string",
    "experiences": "string",
    "resume": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    }
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "accountId": "string",
    "recruitmentPostId": "string",
    "fullName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string",
    "gender": "MALE|FEMALE|OTHER",
    "languageSkills": "string",
    "experiences": "string",
    "resume": {
      "id": "string",
      "fileName": "string",
      "originalFileName": "string",
      "contentType": "string",
      "size": "number",
      "url": "string"
    },
    "status": "APPLIED|SCREENING|INTERVIEW_SCHEDULED|INTERVIEWED|OFFERED|CONFIRMED|CONTRACT_PENDING_SIGNATURE|CONTRACT_SIGNED|HIRED|REJECTED|WITHDRAWN",
    "appliedAt": "2023-05-05T12:00:00Z",
    "updatedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  1. Upload resume using upload endpoints to get `resumeAssetId`
  2. Send a `POST` request with application details and `resumeAssetId` as query parameter.

---

## **5. Get My Job Applications**

- **URL**: `/api/v1/applications/mine`
- **Method**: `GET`
- **Description**: Retrieves job applications submitted by the current user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `status` (optional): Filter by application status.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "accountId": "string",
        "recruitmentPostId": "string",
        "fullName": "string",
        "email": "string",
        "phoneNumber": "string",
        "address": "string",
        "gender": "MALE|FEMALE|OTHER",
        "languageSkills": "string",
        "experiences": "string",
        "resume": {
          "id": "string",
          "fileName": "string",
          "originalFileName": "string",
          "contentType": "string",
          "size": "number",
          "url": "string"
        },
        "status": "APPLIED|SCREENING|INTERVIEW_SCHEDULED|INTERVIEWED|OFFERED|CONFIRMED|CONTRACT_PENDING_SIGNATURE|CONTRACT_SIGNED|HIRED|REJECTED|WITHDRAWN",
        "appliedAt": "2023-05-05T12:00:00Z",
        "updatedAt": "2023-05-05T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer",
    "size": "integer",
    "number": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve job applications submitted by the authenticated user.

---

## **6. Admin Review Applications**

- **URL**: `/api/v1/admin/applications/{id}/review`
- **Method**: `POST`
- **Description**: Allows an admin to review a job application.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the job application.
- **Query Parameters**:
  - `status` (required): New application status.
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with new status to update application status.

**Note**: Certain statuses cannot be set directly via review:
- `HIRED`
- `CONTRACT_SIGNED` 
- `CONTRACT_PENDING_SIGNATURE`

---

## **7. Get All Applications (Admin)**

- **URL**: `/api/v1/applications`
- **Method**: `GET`
- **Description**: Retrieves all job applications for admin review.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `status` (optional): Filter by application status.
  - `postId` (optional): Filter by recruitment post ID.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "accountId": "string",
        "recruitmentPostId": "string",
        "fullName": "string",
        "email": "string",
        "phoneNumber": "string",
        "address": "string",
        "gender": "MALE|FEMALE|OTHER",
        "languageSkills": "string",
        "experiences": "string",
        "resume": {
          "id": "string",
          "fileName": "string",
          "originalFileName": "string",
          "contentType": "string",
          "size": "number",
          "url": "string"
        },
        "status": "APPLIED|SCREENING|INTERVIEW_SCHEDULED|INTERVIEWED|OFFERED|CONFIRMED|CONTRACT_PENDING_SIGNATURE|CONTRACT_SIGNED|HIRED|REJECTED|WITHDRAWN",
        "appliedAt": "2023-05-05T12:00:00Z",
        "updatedAt": "2023-05-05T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer",
    "size": "integer",
    "number": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve all job applications for admin review.

---

## **Business Rules**

- **Post Status Flow**: ACTIVE → EXPIRED/CLOSED
- **Application Status Flow**: PENDING → REVIEWED → ACCEPTED/REJECTED
- **Role-Based Access**:
  - Admins can create posts and review applications
  - Users can apply for jobs and view their applications
  - Public access for viewing posts
- **Document Management**:
  - Resume file upload required for applications
  - Additional documents supported
  - File size and format restrictions apply
- **Expiration Management**:
  - Posts automatically expire after specified date
  - Expired posts cannot receive new applications
- **Audit Trail**: All operations track user context for compliance

---

This document will be updated as new endpoints are added to the Recruitment module.
