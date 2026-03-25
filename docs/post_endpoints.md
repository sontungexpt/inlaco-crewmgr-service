# Post Endpoints Documentation

This document provides an overview of all the available API endpoints for managing posts in the `crewmgrservice` application.

---

## **1. Retrieve a Post by ID**

- **URL**: `/api/v1/posts/{id}`
- **Method**: `GET`
- **Description**: Retrieves a single post from the server by its ID.
- **Details**: This is a public endpoint and does not require authentication.
- **Response**: `200 OK`
  ```json
  {
    "id": "string",
    "title": "string",
    "content": "string",
    "type": "string",
    "author": {
      "id": "string",
      "name": "string"
    },
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
  ```
- **Usage**:
  Send a `GET` request to `/api/v1/posts/{id}` where `{id}` is the unique identifier of the post you want to retrieve.

---

## **2. Retrieve All Posts**

- **URL**: `/api/v1/posts`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all posts.
- **Details**: This is a public endpoint and does not require authentication. Results can be filtered and paginated.
- **Query Parameters**:
  - `page` (optional): Page number (default: `0`).
  - `size` (optional): Number of items per page (default: `10`).
  - `criteria` (optional): `PostSearchCriteria` object for filtering posts. This can include fields like `title`, `type`, `authorId`, etc. (e.g., `?title=example&type=NEWS`).
- **Response**: `200 OK`
  ```json
  {
    "content": [
      {
        "id": "string",
        "title": "string",
        "content": "string",
        "type": "string",
        "author": {
          "id": "string",
          "name": "string"
        },
        "createdAt": "datetime",
        "updatedAt": "datetime"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer",
    "number": "integer",
    "size": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to `/api/v1/posts` with optional query parameters for pagination and filtering.

---

## **3. Create a New Post**

- **URL**: `/api/v1/posts`
- **Method**: `POST`
- **Description**: Creates a new post with the provided data. The `type` field identifies the post type (e.g., "NEWS", "RECRUITMENT").
- **Details**: Requires authentication with an access token. This endpoint is public for "dev" profiles but requires authentication otherwise. The current user is automatically set as the author.
- **Headers**:
  - `Authorization`: `Bearer <accessToken>`
- **Request Body**:
  ```json
  {
    "title": "string",
    "content": "string",
    "type": "string"
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "id": "string",
    "title": "string",
    "content": "string",
    "type": "string",
    "author": {
      "id": "string",
      "name": "string"
    },
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
  ```
- **Usage**:
  Send a `POST` request with the post data in the request body. Ensure a valid access token is provided in the `Authorization` header.

---

## **4. Update a Post**

- **URL**: `/api/v1/posts/{id}`
- **Method**: `PATCH`
- **Description**: Updates an existing post identified by its ID.
- **Details**: Requires authentication with an access token. Uses `application/merge-patch+json` for partial updates. The user making the request must have the necessary permissions.
- **Headers**:
  - `Authorization`: `Bearer <accessToken>`
  - `Content-Type`: `application/merge-patch+json`
- **Request Body**:
  ```json
  {
    "title": "string (optional)",
    "content": "string (optional)",
    "type": "string (optional)"
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "id": "string",
    "title": "string",
    "content": "string",
    "type": "string",
    "author": {
      "id": "string",
      "name": "string"
    },
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
  ```
- **Usage**:
  Send a `PATCH` request with the post ID and the fields to update in the request body. Only provide the fields you wish to change.

---

## **5. Delete a Post**

- **URL**: `/api/v1/posts/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a post identified by its ID.
- **Details**: Requires authentication with an access token. The user making the request must have the necessary permissions (e.g., ADMIN role).
- **Headers**:
  - `Authorization`: `Bearer <accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request with the post ID. No request body is required.

---

This document will be updated as new endpoints are added to the Post module.