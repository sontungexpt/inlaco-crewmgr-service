# Recruitment Endpoints Documentation

This document provides an overview of Recruitment-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Create Recruitment Post**

- **URL**: `/api/v1/recruitment/posts`
- **Method**: `POST`
- **Description**: Creates a new recruitment post for job openings.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "title": "string",
    "description": "string",
    "position": "string",
    "requirements": "string",
    "salary": "string",
    "location": "string",
    "expirationDate": "2023-06-05T12:00:00Z",
    "company": "string"
  }
  ```
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
    "author": {
      "id": "string",
      "name": "string"
    }
  }
  ```
- **Usage**:
  Send a `POST` request with recruitment post details to create a new job posting.

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

- **URL**: `/api/v1/recruitment/posts/{postId}/apply`
- **Method**: `POST`
- **Description**: Submits a job application for a specific recruitment post.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `postId`: The ID of the recruitment post.
- **Request Body**:
  ```json
  {
    "coverLetter": "string",
    "expectedSalary": "string",
    "availableDate": "2023-05-15T12:00:00Z",
    "resumeFile": "string",
    "additionalDocuments": ["string"]
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "postId": "string",
    "applicantId": "string",
    "status": "PENDING|REVIEWED|ACCEPTED|REJECTED",
    "coverLetter": "string",
    "expectedSalary": "string",
    "availableDate": "2023-05-15T12:00:00Z",
    "appliedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with application details to apply for a job.

---

## **5. Get My Job Applications**

- **URL**: `/api/v1/recruitment/applications/me`
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
        "postId": "string",
        "postTitle": "string",
        "company": "string",
        "position": "string",
        "status": "PENDING|REVIEWED|ACCEPTED|REJECTED",
        "appliedAt": "2023-05-05T12:00:00Z",
        "reviewedAt": "2023-05-06T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve job applications submitted by the authenticated user.

---

## **6. Admin Review Applications**

- **URL**: `/api/v1/recruitment/applications/{applicationId}/review`
- **Method**: `POST`
- **Description**: Allows an admin to review a job application.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `applicationId`: The ID of the job application.
- **Request Body**:
  ```json
  {
    "status": "ACCEPTED|REJECTED",
    "reviewNotes": "string"
  }
  ```
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with review decision to update application status.

---

## **7. Get All Applications (Admin)**

- **URL**: `/api/v1/recruitment/applications`
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
        "postId": "string",
        "postTitle": "string",
        "applicant": {
          "id": "string",
          "name": "string",
          "email": "string"
        },
        "status": "PENDING|REVIEWED|ACCEPTED|REJECTED",
        "appliedAt": "2023-05-05T12:00:00Z",
        "reviewedAt": "2023-05-06T12:00:00Z"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
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
