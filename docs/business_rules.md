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

**Query Operations:**
- Hỗ trợ tìm kiếm theo nhiều tiêu chí: keyword, accountId, shipIMO, status, startDate, endDate
- Sử dụng pagination cho các kết quả tìm kiếm
- Hỗ trợ tìm kiếm chi tiết với thông tin thuyền viên đầy đủ

## Crew Rental Request - Yêu Cầu Thuê Thuyền Viên

### Review Process

**Review Request:**
- Admin có thể review và approve/reject request
- Yêu cầu reviewer context cho việc đánh giá
- Không có giới hạn về số lượng thuyền viên trong request

**Create Request:**
- Tạo yêu cầu thuê thuyền viên mới
- Yêu cầu detailFileAssetId và shipImageAssetId
- Yêu cầu user context cho việc tạo

**Contract Signing:**
- Đánh dấu request đã ký hợp đồng với contractId tương ứng

## Contracts - Hợp Đồng

### Supply Contract

**Create Supply Contract:**
- Tạo hợp đồng cung cấp thuyền viên từ requestId
- Yêu cầu CrewSupplyContract thông tin và assets
- Yêu cầu creator context cho việc tạo

### Labor Contract

**Create Labor Contract:**
- Tạo hợp đồng lao động từ applicationId
- Yêu cầu LaborContract thông tin và assets
- Yêu cầu creator context cho việc tạo

**Contract Lifecycle:**
- Hỗ trợ update contract
- Hỗ trợ signing contract
- Hỗ trợ query contract theo các tiêu chí

## Ship Schedule - Lịch Trình Tàu

### Search và Query

**Flexible Search:**
- Hỗ trợ tìm kiếm theo ShipScheduleSearchCriteria
- Các tiêu chí tìm kiếm: keyword, clientId, shipImo, status, departureTime range
- Sử dụng MongoDB Criteria API để build query động
- Hỗ trợ keyword search trên shipName, shipImo, clientId

**Schedule Management:**
- Tạo, cập nhật, xóa lịch trình tàu
- Quản lý danh sách thuyền viên (add, remove, update crew list)
- Hỗ trợ tìm kiếm theo clientId, shipImo, date range
- Pagination cho tất cả các query operations

## Authentication & Security

### Two-Step Verification

**TOTP Implementation:**
- Hỗ trợ xác thực hai bước với TOTP
- Yêu cầu user context cho việc enable/disable
- Security considerations cho việc backup codes

### API Key Management

**API Key Operations:**
- Tạo và quản lý API keys
- Hỗ trợ revoke và refresh API keys
- Rate limiting và security policies

## General Business Rules

### User Context Requirements

**Audit Trail:**
- Tất cả các operation create/update đều yêu cầu user context
- Tracking người thực hiện các thay đổi
- Support cho audit và compliance

### Data Validation

**Flexible Validation:**
- Validation rules có thể thay đổi theo business requirements
- Không hard-code validation logic
- Support cho dynamic business rules

### Search Pattern

**Criteria-based Search:**
- Sử dụng SearchCriteria pattern cho tất cả các repository
- Hỗ trợ flexible search với multiple criteria
- Pagination và sorting support
