# 📚 HR Payroll Management System - Complete API Reference

**Base URL:** `http://localhost:8080/api/v1`  
**Response Format:** JSON  
**Authentication:** Not yet implemented

---

## 📋 Table of Contents

1. [Employee Management API](#employee-management-api)
2. [Performance Review API](#performance-review-api)
3. [Expense Claims API](#expense-claims-api)
4. [Shift Schedule API](#shift-schedule-api)
5. [Leave Request API](#leave-request-api)
6. [Payroll API](#payroll-api)

---

## 👥 Employee Management API

**Base Path:** `/api/v1/employees`

### 1. Get All Employees

```http
GET /api/v1/employees
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "john.doe",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-05-15",
    "hireDate": "2020-01-10",
    "department": {
      "id": 1,
      "name": "IT"
    },
    "jobTitle": {
      "id": 1,
      "title": "Senior Developer",
      "baseSalary": 75000
    }
  },
  {
    "id": 2,
    "username": "jane.smith",
    "firstName": "Jane",
    "lastName": "Smith",
    "dateOfBirth": "1992-03-20",
    "hireDate": "2021-06-15",
    "department": {
      "id": 2,
      "name": "HR"
    },
    "jobTitle": {
      "id": 2,
      "title": "HR Manager",
      "baseSalary": 65000
    }
  }
]
```

### 2. Get Employee by ID

```http
GET /api/v1/employees/{id}
```

**Path Parameters:**
- `id` (Long) - Employee ID

**Example:** `GET /api/v1/employees/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john.doe",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "hireDate": "2020-01-10",
  "department": {
    "id": 1,
    "name": "IT"
  },
  "jobTitle": {
    "id": 1,
    "title": "Senior Developer",
    "baseSalary": 75000
  }
}
```

### 3. Create New Employee

```http
POST /api/v1/employees
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "alex.wilson",
  "password": "SecurePassword123",
  "firstName": "Alex",
  "lastName": "Wilson",
  "dateOfBirth": "1995-07-22",
  "hireDate": "2023-01-15",
  "department": {
    "id": 1
  },
  "jobTitle": {
    "id": 1
  }
}
```

**Response:** `201 CREATED`
```json
{
  "id": 3,
  "username": "alex.wilson",
  "firstName": "Alex",
  "lastName": "Wilson",
  "dateOfBirth": "1995-07-22",
  "hireDate": "2023-01-15",
  "department": {
    "id": 1,
    "name": "IT"
  },
  "jobTitle": {
    "id": 1,
    "title": "Senior Developer",
    "baseSalary": 75000
  }
}
```

### 4. Update Employee

```http
PUT /api/v1/employees/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Employee ID

**Request Body:**
```json
{
  "username": "john.doe",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "hireDate": "2020-01-10",
  "department": {
    "id": 2
  },
  "jobTitle": {
    "id": 2
  }
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john.doe",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "hireDate": "2020-01-10",
  "department": {
    "id": 2,
    "name": "HR"
  },
  "jobTitle": {
    "id": 2,
    "title": "HR Manager",
    "baseSalary": 65000
  }
}
```

### 5. Delete Employee

```http
DELETE /api/v1/employees/{id}
```

**Path Parameters:**
- `id` (Long) - Employee ID

**Response:** `204 NO CONTENT`

---

## ⭐ Performance Review API

**Base Path:** `/api/v1/performance-reviews`

### 1. Get All Performance Reviews

```http
GET /api/v1/performance-reviews
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "reviewerName": "Jane Smith",
    "rating": 4,
    "comments": "Excellent performance, great team player",
    "reviewDate": "2024-11-10",
    "department": "IT"
  },
  {
    "id": 2,
    "employeeName": "Jane Smith",
    "reviewerName": "John Doe",
    "rating": 5,
    "comments": "Outstanding leadership qualities",
    "reviewDate": "2024-11-12",
    "department": "HR"
  }
]
```

### 2. Get Performance Review by ID

```http
GET /api/v1/performance-reviews/{id}
```

**Path Parameters:**
- `id` (Long) - Performance Review ID

**Example:** `GET /api/v1/performance-reviews/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "reviewerName": "Jane Smith",
  "rating": 4,
  "comments": "Excellent performance, great team player",
  "reviewDate": "2024-11-10",
  "department": "IT"
}
```

### 3. Get Reviews by Employee ID

```http
GET /api/v1/performance-reviews/employee/{employeeId}
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Example:** `GET /api/v1/performance-reviews/employee/1`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "reviewerName": "Jane Smith",
    "rating": 4,
    "comments": "Excellent performance, great team player",
    "reviewDate": "2024-11-10",
    "department": "IT"
  },
  {
    "id": 3,
    "employeeName": "John Doe",
    "reviewerName": "Jane Smith",
    "rating": 4,
    "comments": "Good progress on project",
    "reviewDate": "2024-08-15",
    "department": "IT"
  }
]
```

### 4. Get Reviews by Date Range

```http
GET /api/v1/performance-reviews/employee/{employeeId}/date-range?startDate={startDate}&endDate={endDate}
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Query Parameters:**
- `startDate` (String, format: yyyy-MM-dd) - Start date
- `endDate` (String, format: yyyy-MM-dd) - End date

**Example:** 
```
GET /api/v1/performance-reviews/employee/1/date-range?startDate=2024-01-01&endDate=2024-12-31
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "reviewerName": "Jane Smith",
    "rating": 4,
    "comments": "Excellent performance, great team player",
    "reviewDate": "2024-11-10",
    "department": "IT"
  }
]
```

### 5. Create Performance Review

```http
POST /api/v1/performance-reviews/employee/{employeeId}
Content-Type: application/json
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Request Body:**
```json
{
  "reviewerName": "Jane Smith",
  "score": 4,
  "comments": "Excellent performance, great team player",
  "reviewDate": "2024-11-10"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 4,
  "employeeName": "John Doe",
  "reviewerName": "Jane Smith",
  "rating": 4,
  "comments": "Excellent performance, great team player",
  "reviewDate": "2024-11-10",
  "department": "IT"
}
```

### 6. Update Performance Review

```http
PUT /api/v1/performance-reviews/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Performance Review ID

**Request Body:**
```json
{
  "reviewerName": "Jane Smith",
  "score": 5,
  "comments": "Updated review - Exceptional performance",
  "reviewDate": "2024-11-10"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "reviewerName": "Jane Smith",
  "rating": 5,
  "comments": "Updated review - Exceptional performance",
  "reviewDate": "2024-11-10",
  "department": "IT"
}
```

### 7. Delete Performance Review

```http
DELETE /api/v1/performance-reviews/{id}
```

**Path Parameters:**
- `id` (Long) - Performance Review ID

**Response:** `204 NO CONTENT`

---

## 💰 Expense Claims API

**Base Path:** `/api/v1/expenses`

### 1. Get All Expense Claims

```http
GET /api/v1/expenses
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "description": "Client meeting travel",
    "totalAmount": 250.50,
    "status": "PENDING",
    "category": "Travel",
    "submissionDate": "2024-11-10",
    "approvalDate": null,
    "approverName": null
  },
  {
    "id": 2,
    "employeeName": "Jane Smith",
    "description": "Office supplies",
    "totalAmount": 125.75,
    "status": "APPROVED",
    "category": "Supplies",
    "submissionDate": "2024-11-08",
    "approvalDate": "2024-11-09",
    "approverName": "Admin"
  }
]
```

### 2. Get Expense Claim by ID

```http
GET /api/v1/expenses/{id}
```

**Path Parameters:**
- `id` (Long) - Expense Claim ID

**Example:** `GET /api/v1/expenses/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "description": "Client meeting travel",
  "totalAmount": 250.50,
  "status": "PENDING",
  "category": "Travel",
  "submissionDate": "2024-11-10",
  "approvalDate": null,
  "approverName": null
}
```

### 3. Get Expense Claims by Employee

```http
GET /api/v1/expenses/employee/{employeeId}
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Example:** `GET /api/v1/expenses/employee/1`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "description": "Client meeting travel",
    "totalAmount": 250.50,
    "status": "PENDING",
    "category": "Travel",
    "submissionDate": "2024-11-10",
    "approvalDate": null,
    "approverName": null
  }
]
```

### 4. Get Pending Expense Claims

```http
GET /api/v1/expenses/status/pending
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "description": "Client meeting travel",
    "totalAmount": 250.50,
    "status": "PENDING",
    "category": "Travel",
    "submissionDate": "2024-11-10",
    "approvalDate": null,
    "approverName": null
  }
]
```

### 5. Get Expense Claims by Date Range

```http
GET /api/v1/expenses/date-range?startDate={startDate}&endDate={endDate}
```

**Query Parameters:**
- `startDate` (String, format: yyyy-MM-dd) - Start date
- `endDate` (String, format: yyyy-MM-dd) - End date

**Example:**
```
GET /api/v1/expenses/date-range?startDate=2024-11-01&endDate=2024-11-30
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employeeName": "John Doe",
    "description": "Client meeting travel",
    "totalAmount": 250.50,
    "status": "PENDING",
    "category": "Travel",
    "submissionDate": "2024-11-10",
    "approvalDate": null,
    "approverName": null
  },
  {
    "id": 2,
    "employeeName": "Jane Smith",
    "description": "Office supplies",
    "totalAmount": 125.75,
    "status": "APPROVED",
    "category": "Supplies",
    "submissionDate": "2024-11-08",
    "approvalDate": "2024-11-09",
    "approverName": "Admin"
  }
]
```

### 6. Create Expense Claim

```http
POST /api/v1/expenses/employee/{employeeId}
Content-Type: application/json
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Request Body:**
```json
{
  "description": "Conference registration",
  "totalAmount": 500.00,
  "category": "Training",
  "claimDate": "2024-11-10"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 3,
  "employeeName": "John Doe",
  "description": "Conference registration",
  "totalAmount": 500.00,
  "status": "PENDING",
  "category": "Training",
  "submissionDate": "2024-11-10",
  "approvalDate": null,
  "approverName": null
}
```

### 7. Update Expense Claim

```http
PUT /api/v1/expenses/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Expense Claim ID

**Request Body:**
```json
{
  "description": "Updated conference registration",
  "totalAmount": 550.00,
  "category": "Training",
  "claimDate": "2024-11-10"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "description": "Updated conference registration",
  "totalAmount": 550.00,
  "status": "PENDING",
  "category": "Training",
  "submissionDate": "2024-11-10",
  "approvalDate": null,
  "approverName": null
}
```

### 8. Approve Expense Claim

```http
PUT /api/v1/expenses/{id}/approve
```

**Path Parameters:**
- `id` (Long) - Expense Claim ID

**Example:** `PUT /api/v1/expenses/1/approve`

**Response:** `200 OK`
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "description": "Client meeting travel",
  "totalAmount": 250.50,
  "status": "APPROVED",
  "category": "Travel",
  "submissionDate": "2024-11-10",
  "approvalDate": "2024-11-14",
  "approverName": "Admin"
}
```

### 9. Reject Expense Claim

```http
PUT /api/v1/expenses/{id}/reject
```

**Path Parameters:**
- `id` (Long) - Expense Claim ID

**Example:** `PUT /api/v1/expenses/1/reject`

**Response:** `200 OK`
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "description": "Client meeting travel",
  "totalAmount": 250.50,
  "status": "REJECTED",
  "category": "Travel",
  "submissionDate": "2024-11-10",
  "approvalDate": "2024-11-14",
  "approverName": "Admin"
}
```

### 10. Delete Expense Claim

```http
DELETE /api/v1/expenses/{id}
```

**Path Parameters:**
- `id` (Long) - Expense Claim ID

**Response:** `204 NO CONTENT`

---

## ⏰ Shift Schedule API

**Base Path:** `/api/v1/shifts`

### 1. Get All Shifts

```http
GET /api/v1/shifts
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Morning Shift",
    "startTime": "08:00",
    "endTime": "16:00",
    "description": "Regular morning shift"
  },
  {
    "id": 2,
    "name": "Evening Shift",
    "startTime": "16:00",
    "endTime": "00:00",
    "description": "Evening shift"
  },
  {
    "id": 3,
    "name": "Night Shift",
    "startTime": "00:00",
    "endTime": "08:00",
    "description": "Night shift"
  }
]
```

### 2. Get Shift by ID

```http
GET /api/v1/shifts/{id}
```

**Path Parameters:**
- `id` (Long) - Shift ID

**Example:** `GET /api/v1/shifts/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Morning Shift",
  "startTime": "08:00",
  "endTime": "16:00",
  "description": "Regular morning shift"
}
```

### 3. Search Shifts by Name

```http
GET /api/v1/shifts/search/name?name={name}
```

**Query Parameters:**
- `name` (String) - Shift name

**Example:**
```
GET /api/v1/shifts/search/name?name=Morning
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Morning Shift",
    "startTime": "08:00",
    "endTime": "16:00",
    "description": "Regular morning shift"
  }
]
```

### 4. Search Shifts by Time Range

```http
GET /api/v1/shifts/search/time?startTime={startTime}&endTime={endTime}
```

**Query Parameters:**
- `startTime` (String, format: HH:mm) - Start time
- `endTime` (String, format: HH:mm) - End time

**Example:**
```
GET /api/v1/shifts/search/time?startTime=08:00&endTime=16:00
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Morning Shift",
    "startTime": "08:00",
    "endTime": "16:00",
    "description": "Regular morning shift"
  }
]
```

### 5. Create New Shift

```http
POST /api/v1/shifts
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Weekend Shift",
  "startTime": "10:00",
  "endTime": "18:00",
  "description": "Weekend working hours"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 4,
  "name": "Weekend Shift",
  "startTime": "10:00",
  "endTime": "18:00",
  "description": "Weekend working hours"
}
```

### 6. Update Shift

```http
PUT /api/v1/shifts/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Shift ID

**Request Body:**
```json
{
  "name": "Morning Shift",
  "startTime": "07:00",
  "endTime": "15:00",
  "description": "Updated morning shift"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Morning Shift",
  "startTime": "07:00",
  "endTime": "15:00",
  "description": "Updated morning shift"
}
```

### 7. Delete Shift

```http
DELETE /api/v1/shifts/{id}
```

**Path Parameters:**
- `id` (Long) - Shift ID

**Response:** `204 NO CONTENT`

---

## 🏖️ Leave Request API

**Base Path:** `/api/v1/leave`

### 1. Apply for Leave

```http
POST /api/v1/leave/{employeeId}/request
Content-Type: application/json
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Request Body:**
```json
{
  "leaveType": {
    "id": 1
  },
  "numberOfDays": 5,
  "reason": "Annual vacation",
  "startDate": "2024-12-01",
  "endDate": "2024-12-05"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 1,
  "employee": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  },
  "leaveType": {
    "id": 1,
    "name": "Annual Leave"
  },
  "numberOfDays": 5,
  "reason": "Annual vacation",
  "status": "PENDING",
  "startDate": "2024-12-01",
  "endDate": "2024-12-05",
  "requestDate": "2024-11-14"
}
```

### 2. Get Pending Leave Requests

```http
GET /api/v1/leave/pending
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "employee": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe"
    },
    "leaveType": {
      "id": 1,
      "name": "Annual Leave"
    },
    "numberOfDays": 5,
    "reason": "Annual vacation",
    "status": "PENDING",
    "startDate": "2024-12-01",
    "endDate": "2024-12-05",
    "requestDate": "2024-11-14"
  }
]
```

### 3. Update Leave Request Status

```http
PUT /api/v1/leave/{requestId}/status?status={status}
```

**Path Parameters:**
- `requestId` (Long) - Leave Request ID

**Query Parameters:**
- `status` (Enum: PENDING, APPROVED, REJECTED) - New status

**Example:**
```
PUT /api/v1/leave/1/status?status=APPROVED
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "employee": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  },
  "leaveType": {
    "id": 1,
    "name": "Annual Leave"
  },
  "numberOfDays": 5,
  "reason": "Annual vacation",
  "status": "APPROVED",
  "startDate": "2024-12-01",
  "endDate": "2024-12-05",
  "requestDate": "2024-11-14"
}
```

---

## 💳 Payroll API

**Base Path:** `/api/v1/payroll`

### 1. Process Monthly Payroll

```http
POST /api/v1/payroll/process?month={month}&year={year}
```

**Query Parameters:**
- `month` (Integer, 1-12) - Month number
- `year` (Integer) - Year

**Example:**
```
POST /api/v1/payroll/process?month=11&year=2024
```

**Response:** `201 CREATED`
```json
[
  {
    "id": 1,
    "employee": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe"
    },
    "baseSalary": 75000.00,
    "grossSalary": 75000.00,
    "netSalary": 62500.00,
    "tax": 12500.00,
    "deductions": 0.00,
    "paymentDate": "2024-11-30",
    "paySlipItems": [
      {
        "itemType": {
          "id": 1,
          "name": "Bonus"
        },
        "amount": 0.00
      }
    ]
  },
  {
    "id": 2,
    "employee": {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith"
    },
    "baseSalary": 65000.00,
    "grossSalary": 65000.00,
    "netSalary": 54167.00,
    "tax": 10833.00,
    "deductions": 0.00,
    "paymentDate": "2024-11-30",
    "paySlipItems": []
  }
]
```

### 2. Get PaySlip by ID

```http
GET /api/v1/payroll/payslips/{id}
```

**Path Parameters:**
- `id` (Long) - PaySlip ID

**Example:** `GET /api/v1/payroll/payslips/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "employee": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  },
  "baseSalary": 75000.00,
  "grossSalary": 75000.00,
  "netSalary": 62500.00,
  "tax": 12500.00,
  "deductions": 0.00,
  "paymentDate": "2024-11-30",
  "paySlipItems": [
    {
      "itemType": {
        "id": 1,
        "name": "Bonus"
      },
      "amount": 0.00
    }
  ]
}
```

---

## 📊 HTTP Status Codes

| Code | Meaning |
|------|---------|
| `200` | OK - Request successful |
| `201` | CREATED - Resource created successfully |
| `204` | NO CONTENT - Deletion successful |
| `400` | BAD REQUEST - Invalid request parameters |
| `404` | NOT FOUND - Resource not found |
| `500` | INTERNAL SERVER ERROR - Server error |

---

## 🔍 Sample cURL Commands

### Get All Employees
```bash
curl -X GET "http://localhost:8080/api/v1/employees" \
  -H "Accept: application/json"
```

### Create Performance Review
```bash
curl -X POST "http://localhost:8080/api/v1/performance-reviews/employee/1" \
  -H "Content-Type: application/json" \
  -d '{
    "reviewerName": "Jane Smith",
    "score": 4,
    "comments": "Excellent performance",
    "reviewDate": "2024-11-10"
  }'
```

### Submit Expense Claim
```bash
curl -X POST "http://localhost:8080/api/v1/expenses/employee/1" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Travel expenses",
    "totalAmount": 250.50,
    "category": "Travel",
    "claimDate": "2024-11-10"
  }'
```

### Approve Expense Claim
```bash
curl -X PUT "http://localhost:8080/api/v1/expenses/1/approve" \
  -H "Accept: application/json"
```

### Get All Shifts
```bash
curl -X GET "http://localhost:8080/api/v1/shifts" \
  -H "Accept: application/json"
```

### Create Shift
```bash
curl -X POST "http://localhost:8080/api/v1/shifts" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Weekend Shift",
    "startTime": "10:00",
    "endTime": "18:00",
    "description": "Weekend working hours"
  }'
```

### Apply for Leave
```bash
curl -X POST "http://localhost:8080/api/v1/leave/1/request" \
  -H "Content-Type: application/json" \
  -d '{
    "leaveType": {
      "id": 1
    },
    "numberOfDays": 5,
    "reason": "Annual vacation",
    "startDate": "2024-12-01",
    "endDate": "2024-12-05"
  }'
```

### Process Payroll
```bash
curl -X POST "http://localhost:8080/api/v1/payroll/process?month=11&year=2024" \
  -H "Accept: application/json"
```

---

## 📝 Notes

- All timestamps are in `yyyy-MM-dd` format
- All times are in `HH:mm` format (24-hour)
- All monetary values are in decimal format (e.g., 250.50)
- Request bodies must include all required fields
- The API returns appropriate HTTP status codes for error conditions
- No authentication is currently implemented

---

**Last Updated:** November 14, 2025  
**API Version:** 1.0  
**Base URL:** http://localhost:8080/api/v1

