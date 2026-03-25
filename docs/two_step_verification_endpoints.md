# Two-Step Verification Endpoints

This document provides an overview of the Two-Step Verification-related API endpoints in the `crewmgrservice` application. Each endpoint includes its URL, HTTP method, description, request/response formats, and usage instructions.

---

## **1. Verify Two-Step Verification**
- **URL**: `/api/v1/auth/two-step-verification`
- **Method**: `GET`
- **Description**: Verifies the two-step verification token.
- **Query Parameters**:
  - `token`: The verification token.
- **Response**: `204 No Content`
- **Usage**:
  Send a `GET` request with the `token` query parameter to verify the two-step verification.

---

## **2. Resend Two-Step Verification**
- **URL**: `/api/v1/auth/two-step-verification/resend`
- **Method**: `POST`
- **Description**: Resends the two-step verification token to the current user.
- **Headers**:
  - `Authorization`: Bearer `<accessToken>`
- **Response**: `204 No Content`
- **Usage**:
  Send a `POST` request with the `accessToken` in the `Authorization` header to resend the two-step verification token.

---

This document will be updated as new endpoints are added to the Two-Step Verification module.