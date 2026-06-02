# TOTP Implementation Documentation

## Overview

This document describes the TOTP (Time-based One-Time Password) implementation for Inlaco Crew Management Service. The TOTP system provides enhanced security for sensitive operations such as API key creation and secret key viewing using standard authenticator apps.

## Architecture

### Domain Layer
- `TotpSecret`: Core domain model representing a TOTP secret
- `TotpException`: Custom exception for TOTP-related errors

### Application Layer
- `TotpUseCase`: Interface defining TOTP operations
- `TotpService`: Implementation of TOTP business logic
- QR code generation and verification

### Infrastructure Layer
- `TotpRepository`: Repository interface for TOTP persistence
- `TotpRepositoryAdapter`: MongoDB-based implementation
- `TotpSecretEntity`: MongoDB entity for TOTP storage
- `TotpCleanupScheduler`: Scheduled task for cleanup

### Presentation Layer
- `TotpController`: REST endpoints for TOTP operations
- DTOs for requests and responses

## Features

### TOTP Generation
- Cryptographically secure secret generation
- QR code generation for easy setup
- Purpose-based categorization
- MongoDB persistence

### Verification
- Standard TOTP algorithm (SHA1, 6 digits, 30-second window)
- Time-based code verification
- Usage tracking and statistics

### Security Features
- One-time verification per code
- Automatic cleanup of old secrets
- Purpose isolation
- Verification count tracking

### Integration Points
- API Key Creation with TOTP
- Secret Key Viewing with TOTP
- Extensible for future features

## API Endpoints

### API Key Integration
- `POST /api/v1/api-keys/setup-totp` - Setup TOTP for API key creation
- `POST /api/v1/api-keys/create-with-totp` - Create API key with TOTP verification
- `GET /api/v1/api-keys/{keyId}/secret` - View secret with TOTP
- `POST /api/v1/api-keys/{keyId}/setup-secret-totp` - Setup TOTP for secret viewing

*Note: TOTP management is integrated into API key workflows. Standalone TOTP endpoints are not currently implemented.*

## Configuration

### Application Configuration
```yaml
inlaco:
  totp:
    issuer: Inlaco
    algorithm: SHA1
    digits: 6
    period: 30
    time-step-tolerance: 1
    qr:
      width: 200
      height: 200
      margin: 1
    cleanup:
      cron: "0 0 2 * * *"  # Daily at 2 AM

  server:
    base-url: https://your-domain.com
```

### Dependencies
```xml
<!-- TOTP Spring Boot Starter -->
<dependency>
    <groupId>dev.samstevens.totp</groupId>
    <artifactId>totp-spring-boot-starter</artifactId>
    <version>1.7.1</version>
</dependency>
```

## Usage Examples

### 1. Create API Key with TOTP

```bash
# Step 1: Setup TOTP (returns QR code and secret)
curl -X POST http://localhost:8080/api/v1/api-keys/setup-totp \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json"

# Response includes:
# {
#   "secret": "JBSWY3DPEHPK3PXP",
#   "qrCode": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
#   "purposeId": "apikey-1234567890",
#   "purpose": "API_KEY_CREATION",
#   "instructions": "1. Scan QR code with your authenticator app..."
# }

# Step 2: Create API key with TOTP
curl -X POST http://localhost:8080/api/v1/api-keys/create-with-totp \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "Test Client",
    "description": "Test API key",
    "type": "EXTERNAL",
    "totpCode": "123456",
    "purposeId": "apikey-1234567890"
  }'
```

### 2. View Secret Key with TOTP

```bash
# Step 1: Setup TOTP for secret viewing
curl -X POST http://localhost:8080/api/v1/api-keys/{keyId}/setup-secret-totp \
  -H "Authorization: Bearer YOUR_TOKEN"

# Step 2: View secret with TOTP
curl -X GET "http://localhost:8080/api/v1/api-keys/{keyId}/secret?totpCode=123456&purposeId=secret-keyId-1234567890" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Security Considerations

1. **TOTP Algorithm**: Uses standard RFC 6238 implementation
2. **Secret Storage**: Encrypted storage in MongoDB
3. **Time Window**: 30-second codes with time drift tolerance
4. **Rate Limiting**: Built-in through time-based nature of TOTP
5. **Purpose Isolation**: Separate secrets for different purposes

## Authenticator App Support

The implementation supports all standard authenticator apps:
- Google Authenticator
- Microsoft Authenticator
- Authy
- 1Password
- LastPass Authenticator
- And any other RFC 6238 compliant app

## QR Code Generation

- Auto-configured through totp-spring-boot-starter
- Configurable dimensions (width, height, margin)
- Data URI for easy frontend integration
- Includes issuer and purpose information
- Built-in error correction

## Monitoring and Maintenance

### Cleanup Tasks
- Automatic cleanup runs daily at 2 AM (configurable)
- Removes secrets older than 30 days
- Maintains database performance

### Logging
- TOTP setup and verification events
- Failed attempts and security events
- Usage statistics and patterns

## Testing

The implementation includes comprehensive unit tests covering:
- TOTP secret generation and QR code creation
- Code verification with valid/invalid codes
- Security scenarios and edge cases
- Error handling and exception cases

Run tests with:
```bash
mvn test -Dtest=TotpServiceTest
```

## Migration from OTP

The TOTP implementation is designed to replace the previous email-based OTP system:

### Advantages of TOTP over OTP:
1. **No email dependency**: Works offline
2. **Instant delivery**: No email delays
3. **More secure**: No email interception risk
4. **User convenience**: Standard authenticator apps
5. **Better UX**: Familiar 2FA flow

### Migration Steps:
1. Deploy TOTP endpoints alongside existing OTP endpoints
2. Update frontend to use TOTP flow
3. Gradually deprecate OTP endpoints
4. Clean up old OTP code after transition period

## Future Enhancements

1. **Backup Codes**: Generate backup codes for account recovery
2. **Multiple Devices**: Support multiple authenticator devices
3. **TOTP Analytics**: Usage patterns and security metrics
4. **WebAuthn Integration**: Hardware security key support
5. **Risk-Based Authentication**: Adaptive authentication based on risk
