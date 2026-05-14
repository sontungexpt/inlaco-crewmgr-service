# Upload Endpoints Documentation

This document provides an overview of File Upload-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Get Upload Options (Signed Parameters)**

- **URL**: `/api/v1/upload`
- **Method**: `GET`
- **Description**: Retrieves signed upload parameters for direct file upload to Cloudinary. This endpoint provides the necessary parameters and signatures for secure client-side file uploads.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `strategy` (required): Asset type for upload organization (see Asset Types below)
  - Additional parameters can be passed as query parameters for specific upload strategies
- **Response**:
  ```json
  {
    "timestamp": "string",
    "signature": "string",
    "api_key": "string",
    "cloud_name": "string",
    "folder": "string",
    "public_id": "string",
    "tags": ["string"],
    "upload_preset": "string",
    "additional_params": {
      "key": "value"
    }
  }
  ```
- **Usage**:
  1. Call this endpoint to get signed parameters
  2. Use the returned parameters to upload directly to Cloudinary
  3. Cloudinary returns the file URL and metadata
  4. Store the asset information in your application

---

## **2. Upload File (Direct to Cloudinary)**

- **URL**: `https://api.cloudinary.com/v1_1/{cloud_name}/auto/upload`
- **Method**: `POST`
- **Description**: Uploads file directly to Cloudinary using signed parameters from the Get Upload Options endpoint.
- **Security**: Uses signed parameters from the API endpoint
- **Headers**:
  - `Content-Type`: `multipart/form-data`
- **Request Body**:
  - `file`: The file to upload (required)
  - `api_key`: From Get Upload Options response
  - `timestamp`: From Get Upload Options response
  - `signature`: From Get Upload Options response
  - `folder`: From Get Upload Options response
  - `public_id`: From Get Upload Options response (optional)
  - `upload_preset`: From Get Upload Options response (optional)
  - Additional parameters as provided by the signed response
- **Response**:
  ```json
  {
    "public_id": "string",
    "version": "integer",
    "signature": "string",
    "width": "integer",
    "height": "integer",
    "format": "string",
    "resource_type": "string",
    "created_at": "string",
    "tags": ["string"],
    "bytes": "integer",
    "type": "string",
    "etag": "string",
    "placeholder": "boolean",
    "url": "string",
    "secure_url": "string",
    "access_mode": "string",
    "original_filename": "string"
  }
  ```
- **Usage**:
  Upload directly to Cloudinary using the signed parameters obtained from the Get Upload Options endpoint.

---

## **Asset Types**

### Supported Asset Types

The following asset types are supported for organizing uploads in Cloudinary folders:

- **DEFAULT**: General purpose uploads
- **COURSE_WALLPAPER**: Course wallpaper images
- **TRAINING_PROVIDER_LOGO**: Training provider logos
- **RESUME**: Resume/CV files
- **CONTRACT_TEMPLATE**: Contract template documents
- **CREW_RENTAL_REQUEST_DETAIL_FILE**: Crew rental request detail files
- **CONTRACT_FILE**: Contract documents
- **SHIP_IMAGE**: Ship and vessel images
- **SHIP_DOCUMENT**: Ship-related documents
- **POST_IMAGE**: Post/announcement images
- **POST_ATTACHMENT**: Post/announcement attachments
- **CREW_PROFILE**: Crew member profile photos
- **CITIZEN_IDENTITY_CARD**: Citizen identity card documents
- **SOCIAL_INSURANCE**: Social insurance documents
- **ACCIDENT_INSURANCE**: Accident insurance documents
- **COMPANY_LOGO**: Company logo images

### Upload Strategies

The following upload strategies are defined in the system (subset of Asset Types):

- **DEFAULT**
- **COURSE_WALLPAPER**
- **TRAINING_PROVIDER_LOGO**
- **RESUME**
- **CONTRACT_TEMPLATE**
- **CREW_RENTAL_REQUEST_DETAIL_FILE**
- **CONTRACT_FILE**
- **SHIP_IMAGE**

---

## **Security Features**

### Cloudinary Integration

- **Signed Uploads**: All uploads use signed parameters to prevent unauthorized uploads
- **Cloud-Based Storage**: Files stored securely on Cloudinary infrastructure
- **CDN Delivery**: Automatic CDN integration for fast file delivery globally
- **Automatic Optimization**: Cloudinary automatically optimizes images and files

### Access Control

- **Role-Based Access**: Upload endpoints require `USER` role authentication
- **Signed Parameters**: Upload signatures prevent unauthorized file uploads
- **Folder Organization**: Files organized by asset type in separate Cloudinary folders
- **Secure URLs**: Cloudinary provides secure URLs with access controls

### File Management

- **Cloudinary Features**: Leverages Cloudinary's built-in security and optimization
- **Automatic Transformations**: Image resizing, format optimization, and quality adjustment
- **Backup & Redundancy**: Cloudinary provides automatic backup and redundancy
- **Global CDN**: Files delivered from edge locations worldwide

---

## **Business Rules**

- **Asset Type Organization**: Files organized by asset type in separate folders
- **Signed Upload Flow**: Two-step process: get signed parameters, then upload to Cloudinary
- **Client-Side Upload**: Files uploaded directly from client to Cloudinary (reduces server load)
- **Authentication Required**: All upload operations require valid authentication
- **Asset Type Validation**: Only supported asset types can be used for uploads
- **Cloudinary Limits**: Subject to Cloudinary's file size and format limitations
- **Integration**: Cloudinary URLs and metadata stored in application for reference

---

## **Usage Example**

### Complete Upload Flow

1. **Get Upload Options**:
   ```bash
   GET /api/v1/upload?strategy=COMPANY_LOGO
   Authorization: Bearer <token>
   ```

2. **Upload to Cloudinary**:
   ```bash
   POST https://api.cloudinary.com/v1_1/{cloud_name}/auto/upload
   Content-Type: multipart/form-data
   
   file: <binary_file_data>
   api_key: <from_step_1>
   timestamp: <from_step_1>
   signature: <from_step_1>
   folder: <from_step_1>
   ```

3. **Store Asset Information**:
   Use the Cloudinary response to store file metadata in your application database.

---

This document will be updated as new endpoints are added to the Upload module.
