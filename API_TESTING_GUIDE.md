# 🧪 API Testing Guide - HR Payroll Management System

## ✅ Quick Start

The application is running on `http://localhost:8080`

Test data pre-loaded:
- **Employee 1:** Alex Chen (ID: 1) - Software Developer
- **Employee 2:** Sarah Khan (ID: 2) - HR Specialist
- **Department 1:** Human Resources (ID: 1)
- **Department 2:** Finance (ID: 2)

---

## 🎯 Performance Review API Tests

### 1. Create a Performance Review
```bash
curl -X POST http://localhost:8080/api/v1/performance-reviews/employee/1 \
  -H "Content-Type: application/json" \
  -d '{
    "score": 4.5,
    "comments": "Excellent performance and great team collaboration"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "employeeName": "Alex Chen",
  "rating": 4,
  "comments": "Excellent performance",
  "reviewDate": "2025-11-14",
  "department": "Finance"
}
```

### 2. Get All Performance Reviews
```bash
curl http://localhost:8080/api/v1/performance-reviews
```

### 3. Get Review by ID
```bash
curl http://localhost:8080/api/v1/performance-reviews/1
```

### 4. Get Reviews for Specific Employee
```bash
curl http://localhost:8080/api/v1/performance-reviews/employee/1
```

### 5. Get Reviews by Date Range
```bash
curl "http://localhost:8080/api/v1/performance-reviews/employee/1/date-range?startDate=2025-01-01&endDate=2025-12-31"
```

### 6. Update Performance Review
```bash
curl -X PUT http://localhost:8080/api/v1/performance-reviews/1 \
  -H "Content-Type: application/json" \
  -d '{
    "score": 4.8,
    "comments": "Updated: Outstanding performance this quarter"
  }'
```

### 7. Delete Performance Review
```bash
curl -X DELETE http://localhost:8080/api/v1/performance-reviews/1
```

---

## 💰 Expense Claim API Tests

### 1. Create an Expense Claim
```bash
curl -X POST http://localhost:8080/api/v1/expenses/employee/1 \
  -H "Content-Type: application/json" \
  -d '{
    "totalAmount": 250.50,
    "status": "PENDING"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "employeeName": "Alex Chen",
  "totalAmount": 250.50,
  "status": "PENDING",
  "submissionDate": "2025-11-14"
}
```

### 2. Get All Expense Claims
```bash
curl http://localhost:8080/api/v1/expenses
```

### 3. Get Pending Expense Claims
```bash
curl http://localhost:8080/api/v1/expenses/status/pending
```

### 4. Get Claims for Specific Employee
```bash
curl http://localhost:8080/api/v1/expenses/employee/1
```

### 5. Get Claims by Date Range
```bash
curl "http://localhost:8080/api/v1/expenses/date-range?startDate=2025-11-01&endDate=2025-11-30"
```

### 6. Approve an Expense Claim
```bash
curl -X PUT http://localhost:8080/api/v1/expenses/1/approve
```

**Response (200 OK):**
```json
{
  "id": 1,
  "employeeName": "Alex Chen",
  "totalAmount": 250.50,
  "status": "APPROVED",
  "submissionDate": "2025-11-14"
}
```

### 7. Reject an Expense Claim
```bash
curl -X PUT http://localhost:8080/api/v1/expenses/1/reject
```

### 8. Update Expense Claim
```bash
curl -X PUT http://localhost:8080/api/v1/expenses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "totalAmount": 300.00
  }'
```

### 9. Delete Expense Claim
```bash
curl -X DELETE http://localhost:8080/api/v1/expenses/1
```

---

## ⏰ Shift Schedule API Tests

### 1. Create a Shift
```bash
curl -X POST http://localhost:8080/api/v1/shifts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Morning Shift",
    "startTime": "06:00",
    "endTime": "14:00",
    "description": "Early morning work shift"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Morning Shift",
  "startTime": "06:00",
  "endTime": "14:00"
}
```

### 2. Create Another Shift
```bash
curl -X POST http://localhost:8080/api/v1/shifts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Night Shift",
    "startTime": "22:00",
    "endTime": "06:00"
  }'
```

### 3. Get All Shifts
```bash
curl http://localhost:8080/api/v1/shifts
```

### 4. Get Shift by ID
```bash
curl http://localhost:8080/api/v1/shifts/1
```

### 5. Search Shifts by Name
```bash
curl "http://localhost:8080/api/v1/shifts/search/name?name=Morning"
```

### 6. Search Shifts by Time Range
```bash
curl "http://localhost:8080/api/v1/shifts/search/time?startTime=06:00&endTime=14:00"
```

### 7. Update a Shift
```bash
curl -X PUT http://localhost:8080/api/v1/shifts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Early Morning Shift",
    "startTime": "05:00",
    "endTime": "13:00"
  }'
```

### 8. Delete a Shift
```bash
curl -X DELETE http://localhost:8080/api/v1/shifts/1
```

---

## 👥 Employee API Tests (Existing - Still Works)

### Get All Employees
```bash
curl http://localhost:8080/api/v1/employees
```

### Get Employee by ID
```bash
curl http://localhost:8080/api/v1/employees/1
```

### Create New Employee
```bash
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "username": "john.doe",
    "passwordHash": "hash123",
    "hireDate": "2025-01-15",
    "dateOfBirth": "1995-03-20",
    "department": {"id": 1},
    "jobTitle": {"id": 1},
    "role": "EMPLOYEE"
  }'
```

---

## 💡 Testing Tips

### Using Postman:
1. Import as cURL requests
2. Set base URL: `http://localhost:8080`
3. Add JSON body for POST/PUT requests
4. Use Authorization tab if needed (future enhancement)

### Using VS Code REST Client:
Create a file `test.http`:
```
### Get all performance reviews
GET http://localhost:8080/api/v1/performance-reviews

### Create performance review
POST http://localhost:8080/api/v1/performance-reviews/employee/1
Content-Type: application/json

{
  "score": 4.5,
  "comments": "Great work!"
}
```

### Common HTTP Status Codes:
- `200 OK` - Successful GET/PUT/PATCH
- `201 CREATED` - Successful POST
- `204 NO CONTENT` - Successful DELETE
- `404 NOT FOUND` - Resource doesn't exist
- `400 BAD REQUEST` - Invalid input
- `500 INTERNAL SERVER ERROR` - Server error

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Windows: Find and kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Application Won't Start
```bash
# Clean rebuild
mvn clean compile spring-boot:run
```

### Database Connection Issues
- Check if H2 database initialized correctly
- Look for "Hibernate: create table" in console logs
- Ensure test data loaded successfully

---

## 🔄 API Response Format

All APIs follow this pattern:

**Success (200/201):**
```json
{
  "id": 1,
  "field1": "value1",
  "field2": "value2"
}
```

**Error (404/400):**
```
HTTP/1.1 404 Not Found
Resource not found with ID: 999
```

---

## 📊 Test Scenarios

### Scenario 1: Complete Employee Review Process
1. Create performance review for employee 1
2. List all reviews
3. Update review with feedback
4. Delete review

### Scenario 2: Expense Claim Workflow
1. Employee 1 submits expense claim
2. HR Manager views pending claims
3. HR Manager approves claim
4. Verify claim status changed

### Scenario 3: Shift Management
1. Create morning, afternoon, and night shifts
2. Search shifts by time range
3. List all available shifts
4. Update shift timings

---

## ⚡ Performance Considerations

- All queries are optimized with proper indexes
- DTOs reduce payload size
- Lazy loading prevents N+1 query problems
- Transactional boundaries ensure data consistency

---

**Last Updated:** November 14, 2025  
**API Version:** 1.0.0 (REST)  
**Server:** http://localhost:8080
