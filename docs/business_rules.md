# Business Rules - Nghiệp Vụ

## Crew Mobilization - Điều Động Thuyền Viên

### Cho phép tạo nhiều đợt điều động cho một contract

**Lý do:**
1. **Linh hoạt trong chia nhỏ đợt điều động**: Công ty có thể chia từng đợt điều động khác nhau theo cách hoạt động thực tế của họ.

2. **Tuân theo business logic**: Việc kiểm tra số thuyền viên điều động có vượt quá request hay không là tuỳ theo business và có thể điều chỉnh sau này.

3. **Không giới hạn bởi request ban đầu**: Công ty muốn có sự linh hoạt để điều động theo từng đợt khác nhau mà không bị ràng buộc bởi việc kiểm tra giới hạn số lượng từ request ban đầu.

**Ghi chú:**
- Không cần kiểm tra giới hạn số lượng thuyền viên điều động so với request ban đầu
- Business logic có thể thay đổi trong tương lai về việc kiểm tra này
- Ưu tiên sự linh hoạt cho công ty trong việc tổ chức các đợt điều động

### Crew Mobilization Command Operations

**Create Mobilization:**
- Tạo mới một đợt điều động với thông tin tàu, thời gian, danh sách thuyền viên
- Yêu cầu shipImageAssetId và user context cho việc tạo
- Không giới hạn số lượng thuyền viên trong một đợt điều động
- Role: ADMIN chỉ có thể tạo điều động

**Query Operations:**
- Hỗ trợ tìm kiếm theo nhiều tiêu chí: keyword, accountId, shipIMO, status, startDate, endDate
- Sử dụng pagination cho các kết quả tìm kiếm (default: page=0, size=20)
- Hỗ trợ tìm kiếm chi tiết với thông tin thuyền viên đầy đủ
- Thuyền viên (SAILOR) có thể xem lịch trình của riêng mình qua endpoint `/mine`

**Excel Export:**
- Hỗ trợ export điều động ra file Excel
- Cả ADMIN và SAILOR đều có thể export

## Crew Rental Request - Yêu Cầu Thuê Thuyền Viên

### Review Process

**Review Request:**
- Admin có thể review và approve/reject request qua endpoint `/review`
- Yêu cầu reviewer context cho việc đánh giá
- Không có giới hạn về số lượng thuyền viên trong request
- Sử dụng boolean parameter `accepted` để approve/reject

**Create Request:**
- USER có thể tạo yêu cầu thuê thuyền viên mới
- Yêu cầu detailFileAssetId và shipImageAssetId
- Yêu cầu user context cho việc tạo
- Auto-assign status pending cho request mới

**Query Operations:**
- Admin có thể xem tất cả requests
- User có thể xem requests của riêng mình qua endpoint `/me`
- Hỗ trợ tìm kiếm theo nhiều tiêu chí với pagination

**Contract Signing:**
- Đánh dấu request đã ký hợp đồng với contractId tương ứng

## Contracts - Hợp Đồng

### Supply Contract

**Create Supply Contract:**
- Tạo hợp đồng cung cấp thuyền viên từ requestId
- Yêu cầu CrewSupplyContract thông tin và assets
- Yêu cầu creator context cho việc tạo
- Endpoint riêng: `/api/v1/crew-supply-contracts`

### Labor Contract

**Create Labor Contract:**
- Tạo hợp đồng lao động từ applicationId
- Yêu cầu LaborContract thông tin và assets
- Yêu cầu creator context cho việc tạo
- Endpoint riêng: `/api/v1/labor-contracts`

**Contract Lifecycle:**
- Hỗ trợ update contract qua PATCH endpoint
- Hỗ trợ signing contract
- Hỗ trợ query contract theo các tiêu chí
- Role-based access control cho các operations

## Ship Schedule - Lịch Trình Tàu

### Search và Query

**Flexible Search:**
- Hỗ trợ tìm kiếm theo ShipScheduleSearchCriteria
- Các tiêu chí tìm kiếm: keyword, clientId, shipImo, status, departureTime range
- Sử dụng MongoDB Criteria API để build query động
- Hỗ trợ keyword search trên shipName, shipImo, clientId

**Schedule Management:**
- Tạo lịch trình tàu với crew assignments
- Quản lý danh sách thuyền viên (add, remove, update crew list)
- Hỗ trợ tìm kiếm theo clientId, shipImo, date range
- Pagination cho tất cả các query operations (default: page=0, size=10)

**External API Access:**
- Hỗ trợ external API access qua API Key authentication
- Client có thể xem schedules của mình qua endpoint `/me`

## Authentication & Security

### User Authentication

**Registration & Login:**
- Public endpoints cho register và login
- Support username/password authentication
- Return JWT tokens (access + refresh)
- Password confirmation required cho registration

**Token Management:**
- Refresh token capability
- Logout functionality
- Bearer token authentication

### Two-Step Verification

**TOTP Implementation:**
- Hỗ trợ xác thực hai bước với TOTP
- Yêu cầu user context cho việc enable/disable
- Security considerations cho việc backup codes
- Endpoint riêng cho TOTP management

### API Key Management

**API Key Operations:**
- Tạo và quản lý API keys
- Hỗ trợ revoke và refresh API keys
- Rate limiting và security policies
- Support OTP và TOTP cho API key creation
- External API access với API Key authentication

## User Management

### User Profile

**Profile Management:**
- User có thể xem profile của mình
- Role-based access control (USER, ADMIN, SAILOR)
- User context tracking cho audit trail

### Company Management

**Company Operations:**
- Quản lý thông tin công ty
- Support cho vessel owner management
- Integration với ship schedules

## Recruitment & Job Applications

### Job Posting

**Post Management:**
- Tạo và quản lý recruitment posts
- Support cho job listings và applications

### Application Process

**Job Applications:**
- User có thể apply cho jobs
- Admin review process
- Application lifecycle management
- Support cho labor contract creation từ applications

## Course Management

**Training Courses:**
- Quản lý courses đào tạo
- Support cho crew certification
- Course completion tracking

## File Upload & Management

**Asset Management:**
- Support file upload cho contracts, requests, ships
- Asset ID tracking cho files
- Integration với các features khác

## Notification System

**Multi-channel Notifications:**
- Email notifications
- Push notifications (device tokens)
- Event-driven notifications cho:
  - New crew mobilization
  - Ship schedule changes
  - Contract lifecycle events

## General Business Rules

### User Context Requirements

**Audit Trail:**
- Tất cả các operation create/update đều yêu cầu user context
- Tracking người thực hiện các thay đổi
- Support cho audit và compliance
- @CurrentUser annotation cho dependency injection

### Data Validation

**Flexible Validation:**
- Validation rules có thể thay đổi theo business requirements
- Không hard-code validation logic
- Support cho dynamic business rules
- Jakarta validation annotations

### Search Pattern

**Criteria-based Search:**
- Sử dụng SearchCriteria pattern cho tất cả các repository
- Hỗ trợ flexible search với multiple criteria
- Pagination và sorting support
- @Filter annotation cho dynamic filtering

### Role-Based Access Control

**Permission Management:**
- ADMIN: Full access
- USER: Limited access (own data)
- SAILOR: Crew-specific access
- Method-level security với @RolesAllowed

### API Documentation

**OpenAPI Integration:**
- Comprehensive API documentation
- Security requirement definitions
- Endpoint descriptions and examples
- Swagger UI integration
