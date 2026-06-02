# Notification Endpoints Documentation

This document provides an overview of Notification-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Send Notification**

- **URL**: `/api/v1/notifications/send`
- **Method**: `POST`
- **Description**: Sends a notification to specified recipients.
- **Security**: Requires `ADMIN` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "title": "string",
    "message": "string",
    "type": "EMAIL|PUSH|BOTH",
    "recipients": ["string"],
    "data": {
      "key": "value"
    }
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "status": "SENT|FAILED",
    "sentAt": "2023-05-05T12:00:00Z",
    "recipientCount": "integer",
    "failedCount": "integer"
  }
  ```
- **Usage**:
  Send a `POST` request with notification details to send notifications to multiple recipients.

---

## **2. Get My Notifications**

- **URL**: `/api/v1/notifications/me`
- **Method**: `GET`
- **Description**: Retrieves notifications for the currently authenticated user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0).
  - `size` (optional): Page size (default: 20).
  - `read` (optional): Filter by read status (true/false).
  - `type` (optional): Filter by notification type.
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "title": "string",
        "message": "string",
        "type": "EMAIL|PUSH",
        "read": "boolean",
        "createdAt": "2023-05-05T12:00:00Z",
        "data": {
          "mobilizationId": "string",
          "scheduleId": "string"
        }
      }
    ],
    "totalPages": "integer",
    "totalElements": "integer"
  }
  ```
- **Usage**:
  Send a `GET` request to retrieve notifications for the authenticated user.

---

## **3. Mark Notification as Read**

- **URL**: `/api/v1/notifications/{id}/read`
- **Method**: `PUT`
- **Description**: Marks a specific notification as read.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `id`: The ID of the notification.
- **Response**: `204 No Content`
- **Usage**:
  Send a `PUT` request with notification ID to mark it as read.

---

## **4. Mark All Notifications as Read**

- **URL**: `/api/v1/notifications/read-all`
- **Method**: `PUT`
- **Description**: Marks all notifications for the current user as read.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `PUT` request to mark all notifications as read.

---

## **5. Register Device Token**

- **URL**: `/api/v1/notifications/device-token`
- **Method**: `POST`
- **Description**: Registers a device token for push notifications.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Request Body**:
  ```json
  {
    "token": "string",
    "platform": "ANDROID|IOS",
    "deviceId": "string"
  }
  ```
- **Response**:
  ```json
  {
    "id": "string",
    "token": "string",
    "platform": "ANDROID|IOS",
    "deviceId": "string",
    "userId": "string",
    "registeredAt": "2023-05-05T12:00:00Z"
  }
  ```
- **Usage**:
  Send a `POST` request with device token details to enable push notifications.

---

## **6. Unregister Device Token**

- **URL**: `/api/v1/notifications/device-token/{token}`
- **Method**: `DELETE`
- **Description**: Unregisters a device token for push notifications.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Path Parameters**:
  - `token`: The device token to unregister.
- **Response**: `204 No Content`
- **Usage**:
  Send a `DELETE` request with device token to disable push notifications for that device.

---

## **7. Get My Device Tokens**

- **URL**: `/api/v1/notifications/device-tokens/me`
- **Method**: `GET`
- **Description**: Retrieves all device tokens registered for the current user.
- **Security**: Requires `USER` role
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**:
  ```json
  [
    {
      "id": "string",
      "token": "string",
      "platform": "ANDROID|IOS",
      "deviceId": "string",
      "registeredAt": "2023-05-05T12:00:00Z",
      "lastUsedAt": "2023-05-05T12:00:00Z"
    }
  ]
  ```
- **Usage**:
  Send a `GET` request to retrieve all registered device tokens for the authenticated user.

---

## **Event-Driven Notifications**

### Automatic Notification Triggers

The system automatically sends notifications for the following events:

#### Crew Mobilization Events
- **New Mobilization Created**: Crew members receive notification when assigned to a mobilization
- **Mobilization Updated**: Status changes trigger notifications to affected crew
- **Mobilization Cancelled**: Immediate notification to all assigned crew members

#### Ship Schedule Events
- **Schedule Created**: Vessel owners receive confirmation of schedule creation
- **Schedule Updated**: Changes to departure/arrival times trigger notifications
- **Schedule Departed**: Notification when ship departs from port
- **Schedule Arrived**: Notification when ship arrives at destination

#### Contract Events
- **Contract Created**: Users receive notification when contract is created for them
- **Contract Signed**: Confirmation when contract is successfully signed
- **Contract Activated**: Notification when contract becomes active

#### Application Events
- **Application Reviewed**: Candidates notified when their application is reviewed
- **Application Accepted**: Congratulations notification with next steps
- **Application Rejected**: Professional notification with feedback

### Notification Channels

#### Email Notifications
- **SMTP Configuration**: Configurable SMTP settings
- **Email Templates**: Customizable templates for different event types
- **Delivery Tracking**: Monitor email delivery status
- **Bounce Handling**: Automatic handling of bounced emails

#### Push Notifications
- **Firebase Integration**: Support for Firebase Cloud Messaging
- **APNS Support**: Apple Push Notification Service integration
- **Token Management**: Automatic cleanup of invalid tokens
- **Platform Detection**: Different handling for Android and iOS

---

## **Business Rules**

- **Notification Types**: EMAIL, PUSH, or BOTH for simultaneous delivery
- **Read Status Tracking**: Users can mark notifications as read/unread
- **Device Management**: Users can register/unregister multiple devices
- **Event-Driven**: Automatic notifications for system events
- **Role-Based Access**:
  - Admins can send manual notifications
  - Users can manage their own notifications and device tokens
- **Privacy**: Users can only access their own notifications and device tokens
- **Audit Trail**: All notification operations are logged for compliance

---

This document will be updated as new endpoints are added to the Notification module.
