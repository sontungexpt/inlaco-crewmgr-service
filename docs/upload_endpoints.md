# Upload Endpoints Documentation

This document provides an overview of File Upload-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Upload File**

- **URL**: `/api/v1/upload`
- **Method**: `POST`
- **Description**: Uploads a file to the system and returns an asset ID for reference.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
  - `Content-Type`: `multipart/form-data`
- **Request Body**:
  - `file`: The file to upload (required)
  - `category` (optional): File category for organization (e.g., CONTRACT, AVATAR, SHIP_IMAGE, DOCUMENT)
  - `description` (optional): File description for metadata
- **Supported File Types**:
  - Images: JPEG, PNG, GIF, WebP
  - Documents: PDF, DOC, DOCX, XLS, XLSX
  - Archives: ZIP, RAR
- **File Size Limits**:
  - Images: Max 10MB
  - Documents: Max 50MB
  - Archives: Max 100MB
- **Response**:
  ```json
  {
    "id": "string",
    "fileName": "string",
    "originalFileName": "string",
    "contentType": "string",
    "size": "integer",
    "category": "string",
    "description": "string",
    "url": "string",
    "uploadedAt": "2023-05-05T12:00:00Z",
    "uploadedBy": "string"
  }
  ```
- **Usage**:
  Send a `POST` request with file data to upload and receive an asset ID for reference.

---

## **2. Get File Info**

- **URL**: `/api/v1/upload/{assetId}`
- **Method**: `GET`
- **Description**: Retrieves metadata about a specific uploaded file.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `assetId`: The ID of the uploaded file.
- **Response**:
  ```json
  {
    "id": "string",
    "fileName": "string",
    "originalFileName": "string",
    "contentType": "string",
    "size": "integer",
    "category": "string",
    "description": "string",
    "url": "string",
    "uploadedAt": "2023-05-05T12:00:00Z",
    "uploadedBy": "string",
    "downloadCount": "integer",
    "lastAccessedAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `GET` request with asset ID to retrieve file metadata.

---

## **3. Download File**

- **URL**: `/api/v1/upload/{assetId}/download`
- **Method**: `GET`
- **Description**: Downloads the actual file content.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `assetId`: The ID of the file to download.
- **Response**:
  - Content-Type: File's original content type
  - Content-Disposition: `attachment; filename=<originalFileName>`
  - Binary file data
- **Usage**:
  Send a `GET` request with asset ID to download the file.

---

## **4. Get File Preview**

- **URL**: `/api/v1/upload/{assetId}/preview`
- **Method**: `GET`
- **Description**: Returns a preview of the file (for images and PDFs).
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `assetId`: The ID of the file to preview.
- **Query Parameters**:
  - `width` (optional): Width for image preview (default: 300).
  - `height` (optional): Height for image preview (default: 300).
  - `quality` (optional): Image quality 0-100 (default: 80).
- **Response**:
  - Images: Resized image data with appropriate content type
  - PDFs: First page as image or thumbnail
  - Other formats: 404 Not Supported
- **Usage**:
  Send a `GET` request with asset ID to get file preview.

---

## **5. Delete File**

- **URL**: `/api/v1/upload/{assetId}`
- **Method**: `DELETE`
- **Description**: Deletes an uploaded file from the system.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Path Parameters**:
  - `assetId`: The ID of the file to delete.
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request with asset ID to permanently delete the file.

---

## **6. List My Files**

- **URL**: `/api/v1/upload/me`
- **Method**: `GET`
- **Description**: Retrieves files uploaded by the current user.
- **Security**: Requires authentication (Bearer token or API Key)
- **Headers**:
  - `Authorization`: Bearer `<accessToken>` OR `X-API-Key: <apiKey>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `category` (optional): Filter by file category.
  - `contentType` (optional): Filter by content type.
  - `startDate` (optional): Filter by upload date (from).
  - `endDate` (optional): Filter by upload date (to).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "fileName": "string",
        "originalFileName": "string",
        "contentType": "string",
        "size": "integer",
        "category": "string",
        "url": "string",
        "uploadedAt": "2023-05-05T12:00:00Z",
        "downloadCount": "integer"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve files uploaded by the authenticated user.

---

## **7. Get All Files (Admin)**

- **URL**: `/api/v1/upload`
- **Method**: `GET`
- **Description**: Retrieves all uploaded files in the system (admin only).
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `category` (optional): Filter by file category.
  - `contentType` (optional): Filter by content type.
  - `uploadedBy` (optional): Filter by uploader ID.
  - `startDate` (optional): Filter by upload date (from).
  - `endDate` (optional): Filter by upload date (to).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "fileName": "string",
        "originalFileName": "string",
        "contentType": "string",
        "size": "integer",
        "category": "string",
        "uploadedBy": "string",
        "uploadedAt": "2023-05-05T12:00:00Z",
        "downloadCount": "integer"
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request with optional filters to retrieve all uploaded files.

---

## **File Categories**

### Supported Categories

- **CONTRACT**: Contract documents and templates
- **AVATAR**: User profile pictures and avatars
- **SHIP_IMAGE**: Ship photos and vessel images
- **DOCUMENT**: General documents and certificates
- **CREW_DOCUMENT**: Crew member documents and certificates
- **RECRUITMENT**: Job posting attachments and resumes
- **COURSE**: Training materials and certificates
- **SCHEDULE**: Schedule-related documents
- **OTHER**: Miscellaneous files

### Category-Based Access Control

- **Public Categories**: Some files may be publicly accessible
- **Private Categories**: Require authentication and ownership verification
- **Admin-Only Categories**: Only accessible by administrators
- **Role-Based Access**: Different access levels based on user roles

---

## **Security Features**

### File Validation

- **Virus Scanning**: Automatic scanning of uploaded files
- **Content Type Verification**: Server-side validation of file types
- **Size Enforcement**: Strict enforcement of size limits
- **Name Sanitization**: Removal of malicious characters from filenames

### Access Control

- **Ownership Verification**: Users can only access their own files
- **Asset ID System**: Non-sequential, hard-to-guess identifiers
- **Signed URLs**: Temporary signed URLs for secure downloads
- **Audit Logging**: All file operations are logged

### Storage Management

- **Automatic Cleanup**: Removal of unused files after 30 days
- **Storage Quotas**: Per-user and system-wide storage limits
- **Backup Strategy**: Regular backups of uploaded files
- **CDN Integration**: Content delivery network for fast downloads

---

## **Business Rules**

- **File Ownership**: Users can only access and manage their own files
- **Admin Access**: Administrators can access all files in the system
- **Category Restrictions**: Some file types restricted to specific user roles
- **Size Limits**: Enforced at upload time with clear error messages
- **Audit Trail**: All file operations tracked with user context
- **Retention Policy**: Files automatically cleaned up based on access patterns
- **Integration**: Asset IDs used throughout system for file references

---

This document will be updated as new endpoints are added to the Upload module.
