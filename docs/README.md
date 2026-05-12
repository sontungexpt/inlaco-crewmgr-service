# INLACO Crew Management Service - API Documentation

This directory contains comprehensive API documentation for the INLACO Crew Management Service. Each file provides detailed information about specific modules and their endpoints.

## Documentation Files

### Core Business Logic
- **[business_rules.md](./business_rules.md)** - Complete business rules and feature descriptions for all modules

### Authentication & Security
- **[auth_endpoints.md](./auth_endpoints.md)** - Authentication, JWT tokens, TOTP 2FA, and well-known endpoints
- **[api_key_endpoints.md](./api_key_endpoints.md)** - API key management with TOTP verification
- **[two_step_verification_endpoints.md](./two_step_verification_endpoints.md)** - Email-based two-step verification
- **[totp-implementation.md](./totp-implementation.md)** - TOTP implementation details and configuration

### Crew Management
- **[crew_endpoints.md](./crew_endpoints.md)** - Crew profile management and operations
- **[crew_mobilization_endpoints.md](./crew_mobilization_endpoints.md)** - Crew mobilization schedules and assignments
- **[crew_rental_request_endpoints.md](./crew_rental_request_endpoints.md)** - Crew rental request workflow and management

### Contract Management
- **[contract_endpoints.md](./contract_endpoints.md)** - Contract lifecycle management
- **[contract_template_endpoints.md](./contract_template_endpoints.md)** - Contract template management
- **[crew_supply_contract_endpoints.md](./crew_supply_contract_endpoints.md)** - Crew supply contract creation
- **[labor_contract_endpoints.md](./labor_contract_endpoints.md)** - Labor contract management

### Vessel & Schedule Management
- **[ship_endpoints.md](./ship_endpoints.md)** - Ship/vessel management and documentation
- **[ship_schedule_endpoints.md](./ship_schedule_endpoints.md)** - Ship schedule management with external API access

### Recruitment & Training
- **[recruitment_endpoints.md](./recruitment_endpoints.md)** - Job posting and application management
- **[course_endpoints.md](./course_endpoints.md)** - Training course management and completion tracking

### System Features
- **[user_endpoints.md](./user_endpoints.md)** - User profile management and account operations
- **[company_endpoints.md](./company_endpoints.md)** - Company management and vessel ownership
- **[post_endpoints.md](./post_endpoints.md)** - Content management and news posts
- **[notification_endpoints.md](./notification_endpoints.md)** - Multi-channel notification system
- **[upload_endpoints.md](./upload_endpoints.md)** - File upload and asset management
- **[well_known_endpoints.md](./well_known_endpoints.md)** - Well-known endpoints for app integration

## API Architecture

### Authentication Methods
- **JWT Bearer Tokens**: For internal user authentication
- **API Keys**: For external system integration
- **Two-Factor Authentication**: TOTP and email-based verification

### Common Patterns

#### Request/Response Format
- All endpoints use JSON for request/response bodies
- Consistent error handling with proper HTTP status codes
- Pagination support across all list endpoints

#### Security Headers
```http
Authorization: Bearer <accessToken>
X-API-Key: <apiKey>
Content-Type: application/json
```

#### Query Parameters
- `page`: Page number (default: 0)
- `size`: Page size (default varies by endpoint)
- `keyword`: Text search across relevant fields
- Filters: Module-specific filtering options

#### Response Structure
```json
{
  "content": [...],           // For paginated responses
  "totalPages": "integer",
  "totalElements": "integer",
  "number": "integer",       // Current page number
  "size": "integer"          // Page size
}
```

### Role-Based Access Control

| Role | Description | Access Level |
|-------|-------------|-------------|
| USER | Regular user | Personal resources, basic operations |
| ADMIN | System administrator | Full system access, all operations |
| SAILOR | Crew member | Crew-specific resources, personal schedules |

### Business Rules Summary

#### Crew Mobilization
- Multiple mobilizations allowed per contract
- No crew count restrictions (business flexibility)
- Admin-only creation, sailor self-view
- Excel export support

#### Crew Rental Requests
- Status flow: PENDING → APPROVED/REJECTED
- File attachments required (detail file, ship image)
- User creation, admin review workflow

#### Contracts
- Separate supply and labor contract types
- Version tracking with history
- Digital signing support
- Template-based creation

#### Ship Schedules
- External API access for third parties
- Dynamic search with MongoDB Criteria API
- Crew assignment management
- Status tracking throughout voyage

#### Authentication & Security
- TOTP implementation for enhanced security
- API key management with purpose isolation
- Comprehensive audit logging
- Rate limiting and security monitoring

## Getting Started

### 1. Authentication
Choose your authentication method based on use case:
- **Internal Applications**: Use JWT Bearer tokens
- **External Integration**: Use API keys
- **Enhanced Security**: Enable TOTP 2FA

### 2. API Exploration
Start with these key endpoints:
- `GET /api/v1/auth/me` - Get current user info
- `GET /api/v1/companies/me` - Get your company
- `GET /api/v1/schedules/me` - Get your schedules (for sailors)

### 3. Development Setup
- Base URL: `https://your-domain.com/api/v1`
- Content-Type: `application/json`
- Authentication: Include appropriate header
- Error Handling: Check HTTP status codes and response messages

## Support

For questions about API usage or to report issues:
- Technical Documentation: Refer to specific module documentation files
- Business Logic: See [business_rules.md](./business_rules.md)
- Implementation Details: See module-specific documentation files

## Version Information

- API Version: v1
- Documentation Last Updated: 2025-05-12
- Compatibility: Backward compatible within major version
