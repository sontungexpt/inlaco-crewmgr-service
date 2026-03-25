# Course Endpoints Documentation

This document provides an overview of all the available API endpoints for managing courses in the `crewmgrservice` application.

---

## **1. Get All Courses**
- **URL**: `/api/v1/courses`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all courses in the system.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - Additional filters can be applied using the `CourseSearchCriteria`.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "name": "string",
        "description": "string",
        "startDate": "string",
        "endDate": "string"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional query parameters to retrieve a paginated list of courses.

---

## **2. Get Course Details**
- **URL**: `/api/v1/courses/{id}`
- **Method**: `GET`
- **Description**: Retrieves the details of a specific course by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "startDate": "string",
    "endDate": "string",
    "members": [
      {
        "id": "string",
        "name": "string",
        "status": "string"
      }
    ]
  }
  ```
- **Usage**:
  Send a `GET` request with the course ID to retrieve its details.

---

## **3. Create a New Course**
- **URL**: `/api/v1/courses`
- **Method**: `POST`
- **Description**: Creates a new course in the system.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "startDate": "string",
    "endDate": "string"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "startDate": "string",
    "endDate": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with the required fields in the body to create a new course.

---

## **4. Update a Course**
- **URL**: `/api/v1/courses/{id}`
- **Method**: `PATCH`
- **Description**: Updates an existing course by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "startDate": "string",
    "endDate": "string"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "startDate": "string",
    "endDate": "string"
  }
  ```
- **Usage**:
  Send a `PATCH` request with the course ID and the fields to update in the request body.

---

## **5. Delete a Course**
- **URL**: `/api/v1/courses/{id}`
- **Method**: `PATCH`
- **Description**: Deletes a course by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `PATCH` request with the course ID to delete the course.

---

## **6. Force Cancel a Course**
- **URL**: `/api/v1/courses/force-cancel/{id}`
- **Method**: `POST`
- **Description**: Cancels a course by its ID.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the course ID to force cancel the course.

---

## **7. Get Course Members**
- **URL**: `/api/v1/courses/{courseId}/members`
- **Method**: `GET`
- **Description**: Retrieves all sailors enrolled in a specific course.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
- **Response**:
  ```json
  [
    {
      "id": "string",
      "name": "string",
      "status": "string"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request with the course ID to retrieve all enrolled sailors.

---

## **8. Register for a Course**
- **URL**: `/api/v1/courses/registration/{id}`
- **Method**: `POST`
- **Description**: Registers the current user for a course.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the course ID to register for the course.

---

## **9. Cancel Course Registration**
- **URL**: `/api/v1/courses/registration/cancellation/{id}`
- **Method**: `POST`
- **Description**: Cancels the current user's registration for a course.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the course ID to cancel the registration.

---

## **10. Mark Course Completion**
- **URL**: `/api/v1/courses/{courseId}/completation/{userId}`
- **Method**: `POST`
- **Description**: Marks a course as completed for a specific user.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
+- **Response**: `204 No Content`
+- **Usage**:
+  Send a `POST` request with the course ID and user ID to mark the course as completed for the user.

---

This document will be updated as new endpoints are added to the Course module.