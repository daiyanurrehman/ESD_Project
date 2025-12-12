# API Documentation

Base URL: `http://localhost:5000`

- Content type: `application/json`
- Auth: JWT for all endpoints except `/auth/**`
- Auth header: `Authorization: Bearer <JWT>`
- Date format: `yyyy-MM-dd`

---

## 0. Common Error Model

- Error schema (from `GlobalExceptionHandler`):
```json
{
  "timestamp": "2025-12-04T10:15:30.123",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input parameters",
  "path": "/api/v1/..."
}
```

---

## 1. Authentication APIs

Security configuration permits `/auth/**` without JWT. All other endpoints require JWT.

### 1.1 POST /auth/login
1) Endpoint Name: Login

2) Full URL: `http://localhost:5000/auth/login`

3) HTTP Method: POST

4) Description: Authenticates user and returns a JWT token with metadata.

5) Prerequisites / Auth Requirements
- JWT token: no
- Role checks: none
- Headers: `Content-Type: application/json`

6) Sample Request JSON Payload
```json
{
  "username": "hr.manager",
  "password": "P@ssw0rd!"
}
```

7) Sample Response (Success)
```json
{
  "token": "eyJhbGciOi...",
  "username": "hr.manager",
  "role": "HR_MANAGER",
  "expiresAt": 1733692800000
}
```

8) Sample Response (Error Cases)
- 400 Validation Error
```json
{
  "timestamp": "2025-12-04T10:15:30.123",
  "status": 400,
  "error": "Validation Failed",
  "message": "Username and password are required",
  "path": "/auth/login"
}
```
- 401 Unauthorized (bad credentials)
```json
{
  "timestamp": "2025-12-04T10:15:30.123",
  "status": 401,
  "error": "Invalid Credentials",
  "message": "Username or password is incorrect",
  "path": "/auth/login"
}
```
- 500 Server Error
```json
{
  "timestamp": "2025-12-04T10:15:30.123",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred. Please contact support if the problem persists.",
  "path": "/auth/login"
}
```

9) Edge Cases to Test
- Missing username/password
- Leading/trailing spaces in username
- Locked/disabled user (if enforced in service)
- Expired/invalid token not applicable to login

10) Testing Checklist
- Validate request body schema
- Verify 200 response with token and role
- Verify 401 for wrong credentials
- Verify response headers `Content-Type: application/json`

### 1.2 POST /auth/signup
1) Endpoint Name: Signup

2) Full URL: `http://localhost:5000/auth/signup`

3) HTTP Method: POST

4) Description: Registers a new user account and returns a token (service-dependent default role).

5) Prerequisites / Auth Requirements
- JWT token: no
- Role checks: none
- Headers: `Content-Type: application/json`

6) Sample Request JSON Payload
```json
{
  "username": "new.employee",
  "password": "S3cure!Pass"
}
```

7) Sample Response (Success)
```json
{
  "token": "eyJhbGciOi...",
  "username": "new.employee",
  "role": "EMPLOYEE",
  "expiresAt": 1733692800000
}
```

8) Sample Response (Error Cases)
- 409 Conflict (duplicate username)
```json
{
  "timestamp": "2025-12-04T10:15:30.123",
  "status": 409,
  "error": "Data Integrity Violation",
  "message": "A record with this information already exists.",
  "path": "/auth/signup"
}
```
- 400 Validation Error / 500 Server Error as per common model

9) Edge Cases to Test
- Duplicate username
- Weak password (if validated in service)
- Long usernames / unicode

10) Testing Checklist
- Validate request shape
- Verify account is created
- Verify token returned
- Verify cannot create duplicate usernames

---

## 2. Employee APIs

Base Path: `/api/v1/employees`

All endpoints below require JWT. No role-level restrictions are enforced in `SecurityConfig` (any authenticated user). Business rules may still be applied in services.

### 2.1 GET /api/v1/employees
1) Endpoint Name: List Employees

2) Full URL: `http://localhost:5000/api/v1/employees`

3) Method: GET

4) Description: Returns all employees.

5) Prerequisites
- JWT token: yes
- Role checks: none (any authenticated)
- Headers: `Authorization: Bearer <JWT>`

6) Sample Request Query
No body. Optional future filters could be added.

7) Sample Response (Success)
```json
[
  {
    "id": 1,
    "username": "john.doe",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-05-15",
    "hireDate": "2020-01-10",
    "department": { "id": 1, "name": "IT" },
    "jobTitle": { "id": 1, "title": "Senior Developer", "baseSalary": 75000.0 }
  }
]
```

8) Sample Response (Error)
- 401 Unauthorized if missing/invalid JWT

9) Edge Cases to Test
- Empty dataset
- Large dataset
- Lazy-loaded relations serialization

10) Testing Checklist
- 200 with JSON array
- Each item has expected fields
- 401 when token missing/invalid

### 2.2 GET /api/v1/employees/{id}
1) Endpoint Name: Get Employee By Id

2) Full URL: `http://localhost:5000/api/v1/employees/1`

3) Method: GET

4) Description: Returns a single employee by id.

5) Prerequisites
- JWT token: yes
- Role checks: none

6) Sample Request
No body

7) Sample Response (Success)
```json
{
  "id": 1,
  "username": "john.doe",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "hireDate": "2020-01-10",
  "department": { "id": 1, "name": "IT" },
  "jobTitle": { "id": 1, "title": "Senior Developer", "baseSalary": 75000.0 }
}
```

8) Error Cases
- 404 Not Found if id missing
- 401 Unauthorized without token

9) Edge Cases
- Non-numeric id
- Id exists but user lacks access (not enforced here)

10) Checklist
- 200 for existing id
- 404 for missing id
- 401 without JWT

### 2.3 POST /api/v1/employees
1) Endpoint Name: Create Employee

2) Full URL: `http://localhost:5000/api/v1/employees`

3) Method: POST

4) Description: Creates a new employee account/profile.

5) Prerequisites
- JWT token: yes
- Role checks: none (recommend ADMIN/HR_MANAGER)
- Headers: `Content-Type: application/json`

6) Sample Request JSON Payload
```json
{
  "username": "alex.wilson",
  "passwordHash": "$2a$10$hashedValueHere",
  "role": "EMPLOYEE",
  "firstName": "Alex",
  "lastName": "Wilson",
  "dateOfBirth": "1995-07-22",
  "hireDate": "2023-01-15",
  "department": { "id": 1 },
  "jobTitle": { "id": 1 }
}
```

7) Sample Response (Success)
```json
{
  "id": 3,
  "username": "alex.wilson",
  "firstName": "Alex",
  "lastName": "Wilson",
  "dateOfBirth": "1995-07-22",
  "hireDate": "2023-01-15",
  "department": { "id": 1, "name": "IT" },
  "jobTitle": { "id": 1, "title": "Senior Developer", "baseSalary": 75000.0 }
}
```

8) Error Cases
- 400 invalid dates or missing fields
- 409 unique username conflict

9) Edge Cases
- Department/jobTitle id not existing
- Username casing/trim

10) Checklist
- Validate required fields
- 201 with created record
- 409 for duplicate username
- Verify DB insert

### 2.4 PUT /api/v1/employees/{id}
1) Endpoint Name: Update Employee

2) Full URL: `http://localhost:5000/api/v1/employees/1`

3) Method: PUT

4) Description: Updates an existing employee.

5) Prerequisites
- JWT token: yes
- Role checks: none (recommend ADMIN/HR_MANAGER)

6) Sample Request JSON Payload
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "hireDate": "2020-01-10",
  "department": { "id": 2 },
  "jobTitle": { "id": 2 }
}
```

7) Sample Response (Success)
```json
{
  "id": 1,
  "username": "john.doe",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "hireDate": "2020-01-10",
  "department": { "id": 2, "name": "HR" },
  "jobTitle": { "id": 2, "title": "HR Manager", "baseSalary": 65000.0 }
}
```

8) Error Cases
- 404 if id not found
- 400 invalid payload

9) Edge Cases
- Changing department/jobTitle to non-existent entities

10) Checklist
- 200 on success
- Verify DB update
- 404 for missing id

### 2.5 DELETE /api/v1/employees/{id}
1) Endpoint Name: Delete Employee

2) Full URL: `http://localhost:5000/api/v1/employees/1`

3) Method: DELETE

4) Description: Deletes an employee.

5) Prerequisites
- JWT token: yes
- Role checks: none (recommend ADMIN/HR_MANAGER)

6) Request: none

7) Response (Success)
- 204 No Content

8) Error Cases
- 404 Not Found

9) Edge Cases
- Deleting employee referenced by other tables

10) Checklist
- 204 on success
- Verify cascades/constraints
- 404 for missing id

---

## 3. Leave APIs

Base Path: `/api/v1/leave`

### 3.1 POST /api/v1/leave/{employeeId}/request
1) Endpoint Name: Apply for Leave

2) Full URL: `http://localhost:5000/api/v1/leave/1/request`

3) Method: POST

4) Description: Employee submits a leave request.

5) Prerequisites
- JWT token: yes
- Role checks: none (recommend EMPLOYEE to create; approval by HR_MANAGER)

6) Sample Request JSON Payload
```json
{
  "startDate": "2025-01-10",
  "endDate": "2025-01-14",
  "durationDays": 5,
  "reason": "Family trip",
  "status": "PENDING",
  "leaveType": { "id": 1 }
}
```

7) Sample Response (Success)
```json
{
  "id": 10,
  "employee": { "id": 1 },
  "startDate": "2025-01-10",
  "endDate": "2025-01-14",
  "durationDays": 5,
  "reason": "Family trip",
  "status": "PENDING",
  "leaveType": { "id": 1 }
}
```

8) Error Cases
- 400 invalid date range
- 404 employee/leaveType not found

9) Edge Cases
- Overlapping leaves
- Negative/zero durationDays

10) Checklist
- 201 created
- Validate date logic
- Verify DB insert

### 3.2 GET /api/v1/leave/pending
1) Endpoint Name: List Pending Leave Requests

2) Full URL: `http://localhost:5000/api/v1/leave/pending`

3) Method: GET

4) Description: Returns leave requests with status PENDING.

5) Prerequisites
- JWT token: yes
- Role checks: none (recommend HR_MANAGER)

6) Request: none

7) Response (Success)
```json
[
  { "id": 10, "status": "PENDING", "employee": { "id": 1 }, "startDate": "2025-01-10", "endDate": "2025-01-14" }
]
```

8) Error Cases
- 401 Unauthorized

9) Edge Cases
- Empty list

10) Checklist
- 200 array response
- 401 without token

### 3.3 PUT /api/v1/leave/{requestId}/status?status=APPROVED|REJECTED|PENDING
1) Endpoint Name: Update Leave Status

2) Full URL: `http://localhost:5000/api/v1/leave/10/status?status=APPROVED`

3) Method: PUT

4) Description: Approve or reject a leave request.

5) Prerequisites
- JWT token: yes
- Role checks: none (recommend HR_MANAGER)

6) Request: no body (uses query param `status`)

7) Response (Success)
```json
{
  "id": 10,
  "status": "APPROVED",
  "employee": { "id": 1 },
  "startDate": "2025-01-10",
  "endDate": "2025-01-14"
}
```

8) Error Cases
- 400 invalid status value
- 404 requestId not found

9) Edge Cases
- Idempotency: repeating same status

10) Checklist
- 200 on success
- 400 for invalid enum
- Verify DB update

---

## 4. Expense APIs

Base Path: `/api/v1/expenses`

### 4.1 GET /api/v1/expenses
1) Endpoint Name: List Expense Claims

2) URL: `http://localhost:5000/api/v1/expenses`
3) Method: GET
4) Description: Returns all expense claims (DTO).
5) Prerequisites: JWT yes; roles none.
6) Request: none
7) Success
```json
[
  {
    "id": 101,
    "employeeName": "John Doe",
    "description": "Conference travel",
    "totalAmount": 350.75,
    "status": "PENDING",
    "category": "Travel",
    "submissionDate": "2025-01-05",
    "approvalDate": null,
    "approverName": null
  }
]
```
8) Errors: 401
9) Edge Cases: empty list; large list
10) Checklist: 200 array; 401 without token

### 4.2 GET /api/v1/expenses/{id}
1) Endpoint Name: Get Expense Claim

2) URL: `http://localhost:5000/api/v1/expenses/101`
3) Method: GET
4) Description: Returns a single expense claim (DTO).
5) Prerequisites: JWT yes
6) Request: none
7) Success
```json
{
  "id": 101,
  "employeeName": "John Doe",
  "description": "Conference travel",
  "totalAmount": 350.75,
  "status": "PENDING",
  "category": "Travel",
  "submissionDate": "2025-01-05",
  "approvalDate": null,
  "approverName": null
}
```
8) Errors: 404, 401
9) Edge Cases: id that exists but belongs to another employee (not restricted in config)
10) Checklist: 200/404/401

### 4.3 GET /api/v1/expenses/employee/{employeeId}
1) Endpoint Name: List Claims by Employee

2) URL: `http://localhost:5000/api/v1/expenses/employee/1`
3) Method: GET
4) Description: Returns all claims for given employee.
5) Prerequisites: JWT yes
6) Request: none
7) Success: `[ExpenseClaimDTO,...]`
8) Errors: 404 if employee not found
9) Edge: empty for employee with no claims
10) Checklist: 200 array, 404, 401

### 4.4 GET /api/v1/expenses/status/pending
1) Endpoint Name: List Pending Claims (All)

2) URL: `http://localhost:5000/api/v1/expenses/status/pending`
3) Method: GET
4) Description: Returns pending claims.
5) Prerequisites: JWT yes (recommend HR_MANAGER)
6) Request: none
7) Success: `[ExpenseClaimDTO,...]`
8) Errors: 401
9) Edge: empty list
10) Checklist: 200 array

### 4.5 GET /api/v1/expenses/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
1) Endpoint Name: List Claims by Date Range

2) URL: `http://localhost:5000/api/v1/expenses/date-range?startDate=2025-01-01&endDate=2025-01-31`
3) Method: GET
4) Description: Returns claims submitted between dates.
5) Prerequisites: JWT yes
6) Request: query params as shown
7) Success: `[ExpenseClaimDTO,...]`
8) Errors: 400 invalid dates; 401
9) Edge: endDate before startDate
10) Checklist: 200 array; validate dates

### 4.6 POST /api/v1/expenses/employee/{employeeId}
1) Endpoint Name: Create Expense Claim

2) URL: `http://localhost:5000/api/v1/expenses/employee/1`
3) Method: POST
4) Description: Creates a new expense claim for an employee.
5) Prerequisites: JWT yes (recommend EMPLOYEE)
6) Sample Request JSON Payload
```json
{
  "claimDate": "2025-01-05",
  "totalAmount": 350.75,
  "status": "PENDING",
  "items": [
    {
      "description": "Flight to conference",
      "amount": 250.50,
      "category": { "id": 1 }
    },
    {
      "description": "Hotel",
      "amount": 100.25,
      "category": { "id": 1 }
    }
  ]
}
```
7) Success
```json
{
  "id": 201,
  "employeeName": "John Doe",
  "description": "Flight to conference; Hotel",
  "totalAmount": 350.75,
  "status": "PENDING",
  "category": "Travel",
  "submissionDate": "2025-01-05",
  "approvalDate": null,
  "approverName": null
}
```
8) Errors: 400 invalid item/category; 404 employee not found; 401
9) Edge: items empty; negative amounts; category missing
10) Checklist: 201 created; totals correct; DB insert and items linked

### 4.7 PUT /api/v1/expenses/{id}
1) Endpoint Name: Update Expense Claim

2) URL: `http://localhost:5000/api/v1/expenses/201`
3) Method: PUT
4) Description: Updates an expense claim.
5) Prerequisites: JWT yes
6) Request JSON
```json
{
  "claimDate": "2025-01-06",
  "totalAmount": 360.00
}
```
7) Success: `ExpenseClaimDTO`
8) Errors: 404, 400, 401
9) Edge: reducing total below sum(items)
10) Checklist: 200 on success; DB update

### 4.8 PUT /api/v1/expenses/{id}/approve
1) Endpoint Name: Approve Expense Claim

2) URL: `http://localhost:5000/api/v1/expenses/201/approve`
3) Method: PUT
4) Description: Approves a pending claim.
5) Prerequisites: JWT yes (recommend HR_MANAGER)
6) Request: none
7) Success: updated `ExpenseClaimDTO` with status
8) Errors: 404, 409 already approved/rejected, 401
9) Edge: Approval of non-pending claim
10) Checklist: 200 and status updated; idempotency

### 4.9 PUT /api/v1/expenses/{id}/reject
1) Endpoint Name: Reject Expense Claim

2) URL: `http://localhost:5000/api/v1/expenses/201/reject`
3) Method: PUT
4) Description: Rejects a pending claim.
5) Prerequisites: JWT yes (recommend HR_MANAGER)
6) Request: none
7) Success: updated `ExpenseClaimDTO` with status
8) Errors: 404, 409 already approved/rejected, 401
9) Edge: rejection reason (if maintained)
10) Checklist: 200 and status updated

### 4.10 DELETE /api/v1/expenses/{id}
1) Endpoint Name: Delete Expense Claim

2) URL: `http://localhost:5000/api/v1/expenses/201`
3) Method: DELETE
4) Description: Deletes a claim.
5) Prerequisites: JWT yes
6) Request: none
7) Success: 204 No Content
8) Errors: 404, 401
9) Edge: deleting with items
10) Checklist: 204; DB delete and cascade

---

## 5. Payroll APIs

Base Path: `/api/v1/payroll`

### 5.1 POST /api/v1/payroll/process?month={m}&year={y}
1) Endpoint Name: Process Monthly Payroll

2) URL: `http://localhost:5000/api/v1/payroll/process?month=11&year=2025`
3) Method: POST
4) Description: Generates monthly payslips for all employees.
5) Prerequisites: JWT yes (recommend ADMIN/HR_MANAGER)
6) Request: query params `month` (1-12), `year` (YYYY)
7) Success
```json
[
  {
    "id": 501,
    "employee": { "id": 1 },
    "payDate": "2025-11-30",
    "grossSalary": 75000.0,
    "totalDeductions": 5000.0,
    "netSalary": 70000.0,
    "lineItems": []
  }
]
```
8) Errors: 400 invalid month/year; 401
9) Edge: duplicate processing for same month
10) Checklist: 201/200 as implemented (controller returns 201); DB inserts created

### 5.2 GET /api/v1/payroll/payslips/{id}
1) Endpoint Name: Get Payslip

2) URL: `http://localhost:5000/api/v1/payroll/payslips/501`
3) Method: GET
4) Description: Returns a payslip by id.
5) Prerequisites: JWT yes
6) Request: none
7) Success: `PaySlip` JSON structure as above
8) Errors: 404, 401
9) Edge: accessing other employee’s payslip (not restricted in config)
10) Checklist: 200/404/401

---

## 6. Performance Review APIs

Base Path: `/api/v1/performance-reviews`

### 6.1 GET /api/v1/performance-reviews
1) Name: List Reviews
2) URL: `http://localhost:5000/api/v1/performance-reviews`
3) Method: GET
4) Description: Returns all reviews (DTO)
5) JWT: yes
6) Request: none
7) Success: `[PerformanceReviewDTO,...]`
8) Errors: 401
9) Edge: empty list
10) Checklist: 200 array

### 6.2 GET /api/v1/performance-reviews/{id}
1) Name: Get Review
2) URL: `http://localhost:5000/api/v1/performance-reviews/1`
3) Method: GET
4) Description: Single review (DTO)
5) JWT: yes
6) Request: none
7) Success: `PerformanceReviewDTO`
8) Errors: 404, 401
9) Edge: id format
10) Checklist: 200/404

### 6.3 GET /api/v1/performance-reviews/employee/{employeeId}
1) Name: Reviews by Employee
2) URL: `http://localhost:5000/api/v1/performance-reviews/employee/1`
3) Method: GET
4) Description: All reviews for employee (DTO)
5) JWT: yes
6) Request: none
7) Success: array of DTO
8) Errors: 404 employee not found
9) Edge: empty
10) Checklist: 200 array

### 6.4 GET /api/v1/performance-reviews/employee/{employeeId}/date-range
1) Name: Reviews by Date Range
2) URL: `http://localhost:5000/api/v1/performance-reviews/employee/1/date-range?startDate=2025-01-01&endDate=2025-12-31`
3) Method: GET
4) Description: Reviews within given dates
5) JWT: yes
6) Request: query params `startDate`, `endDate`
7) Success: array of DTO
8) Errors: 400 invalid dates; 401
9) Edge: end before start
10) Checklist: 200 array

### 6.5 POST /api/v1/performance-reviews/employee/{employeeId}
1) Name: Create Review
2) URL: `http://localhost:5000/api/v1/performance-reviews/employee/1`
3) Method: POST
4) Description: Creates a performance review (entity as request, DTO as response)
5) JWT: yes (recommend MANAGER/HR)
6) Request JSON
```json
{
  "reviewDate": "2025-02-01",
  "score": 4.5,
  "comments": "Consistent delivery and collaboration"
}
```
7) Success: `PerformanceReviewDTO`
8) Errors: 404 employee not found; 400 invalid fields; 401
9) Edge: duplicate date for same employee
10) Checklist: 201 created; DB insert

### 6.6 PUT /api/v1/performance-reviews/{id}
1) Name: Update Review
2) URL: `http://localhost:5000/api/v1/performance-reviews/10`
3) Method: PUT
4) Description: Updates a review
5) JWT: yes
6) Request JSON
```json
{
  "reviewDate": "2025-03-01",
  "score": 5,
  "comments": "Promotion recommended"
}
```
7) Success: DTO
8) Errors: 404, 400
9) Edge: score bounds
10) Checklist: 200; DB update

### 6.7 DELETE /api/v1/performance-reviews/{id}
1) Name: Delete Review
2) URL: `http://localhost:5000/api/v1/performance-reviews/10`
3) Method: DELETE
4) Description: Deletes a review
5) JWT: yes
6) Request: none
7) Success: 204
8) Errors: 404
9) Edge: cascading goals
10) Checklist: 204; DB delete

---

## 7. Shift APIs

Base Path: `/api/v1/shifts`

### 7.1 GET /api/v1/shifts
1) Name: List Shifts
2) URL: `http://localhost:5000/api/v1/shifts`
3) Method: GET
4) Description: Returns all shifts (DTO)
5) JWT: yes
6) Request: none
7) Success: `[ShiftScheduleDTO,...]`
8) Errors: 401
9) Edge: empty list
10) Checklist: 200 array

### 7.2 GET /api/v1/shifts/{id}
1) Name: Get Shift
2) URL: `http://localhost:5000/api/v1/shifts/1`
3) Method: GET
4) Description: One shift (DTO)
5) JWT: yes
6) Request: none
7) Success: `ShiftScheduleDTO`
8) Errors: 404, 401
9) Edge: id format
10) Checklist: 200/404

### 7.3 GET /api/v1/shifts/search/name?name=Day
1) Name: Search Shifts by Name
2) URL: `http://localhost:5000/api/v1/shifts/search/name?name=Day`
3) Method: GET
4) Description: Returns shifts with matching name
5) JWT: yes
6) Request: query `name`
7) Success: array DTO
8) Errors: 401
9) Edge: case sensitivity
10) Checklist: 200 array

### 7.4 GET /api/v1/shifts/search/time?startTime=09:00&endTime=17:00
1) Name: Search Shifts by Time Range
2) URL: `http://localhost:5000/api/v1/shifts/search/time?startTime=09:00&endTime=17:00`
3) Method: GET
4) Description: Returns shifts within a time range
5) JWT: yes
6) Request: query `startTime`, `endTime`
7) Success: array DTO
8) Errors: 400 invalid times; 401
9) Edge: boundary times
10) Checklist: 200 array

### 7.5 POST /api/v1/shifts
1) Name: Create Shift
2) URL: `http://localhost:5000/api/v1/shifts`
3) Method: POST
4) Description: Creates a shift schedule
5) JWT: yes (recommend ADMIN/HR_MANAGER)
6) Request JSON
```json
{
  "name": "Day Shift",
  "startTime": "09:00",
  "endTime": "17:00"
}
```
7) Success: `ShiftScheduleDTO`
8) Errors: 400 invalid time range; 409 duplicate name
9) Edge: start >= end
10) Checklist: 201 created; DB insert

### 7.6 PUT /api/v1/shifts/{id}
1) Name: Update Shift
2) URL: `http://localhost:5000/api/v1/shifts/1`
3) Method: PUT
4) Description: Updates a shift
5) JWT: yes
6) Request JSON
```json
{
  "name": "Evening Shift",
  "startTime": "12:00",
  "endTime": "20:00"
}
```
7) Success: DTO
8) Errors: 404, 400
9) Edge: overlapping shifts policy (if any)
10) Checklist: 200; DB update

### 7.7 DELETE /api/v1/shifts/{id}
1) Name: Delete Shift
2) URL: `http://localhost:5000/api/v1/shifts/1`
3) Method: DELETE
4) Description: Deletes a shift
5) JWT: yes
6) Request: none
7) Success: 204
8) Errors: 404
9) Edge: assigned employees
10) Checklist: 204; DB delete

---

## 8. Additional Notes

- Auth: As per `SecurityConfig`, only `/auth/**` is public. All other endpoints require a valid JWT. No role-based restrictions are enforced at the security layer; apply role checks in services/controllers if needed (e.g., HR_MANAGER approves leaves and expenses).
- Headers: Always send `Authorization: Bearer <JWT>` for secured routes.
- Port: The app runs on port `5000` (`server.port=5000`).
- Error handling: Centralized via `GlobalExceptionHandler` with consistent JSON error structure.


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

## 🏢 Department API

**Base Path:** `/api/v1/departments`

### 1. Get All Departments

```http
GET /api/v1/departments
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Human Resources",
    "description": "Manages employee relations and benefits"
  },
  {
    "id": 2,
    "name": "IT",
    "description": "Information Technology department"
  },
  {
    "id": 3,
    "name": "Finance",
    "description": "Financial planning and accounting"
  }
]
```

### 2. Get Department by ID

```http
GET /api/v1/departments/{id}
```

**Path Parameters:**
- `id` (Long) - Department ID

**Example:** `GET /api/v1/departments/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Human Resources",
  "description": "Manages employee relations and benefits"
}
```

### 3. Create Department

```http
POST /api/v1/departments
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Sales",
  "description": "Sales and business development"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 4,
  "name": "Sales",
  "description": "Sales and business development"
}
```

### 4. Update Department

```http
PUT /api/v1/departments/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Department ID

**Request Body:**
```json
{
  "name": "Sales & Marketing",
  "description": "Sales, marketing and business development"
}
```

**Response:** `200 OK`
```json
{
  "id": 4,
  "name": "Sales & Marketing",
  "description": "Sales, marketing and business development"
}
```

### 5. Delete Department

```http
DELETE /api/v1/departments/{id}
```

**Path Parameters:**
- `id` (Long) - Department ID

**Response:** `204 NO CONTENT`

---

## 💼 Job Title API

**Base Path:** `/api/v1/job-titles`

### 1. Get All Job Titles

```http
GET /api/v1/job-titles
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Senior Developer",
    "baseSalary": 75000.00
  },
  {
    "id": 2,
    "title": "HR Manager",
    "baseSalary": 65000.00
  },
  {
    "id": 3,
    "title": "Financial Analyst",
    "baseSalary": 60000.00
  }
]
```

### 2. Get Job Title by ID

```http
GET /api/v1/job-titles/{id}
```

**Path Parameters:**
- `id` (Long) - Job Title ID

**Example:** `GET /api/v1/job-titles/1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Senior Developer",
  "baseSalary": 75000.00
}
```

### 3. Create Job Title

```http
POST /api/v1/job-titles
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "DevOps Engineer",
  "baseSalary": 70000.00
}
```

**Response:** `201 CREATED`
```json
{
  "id": 4,
  "title": "DevOps Engineer",
  "baseSalary": 70000.00
}
```

### 4. Update Job Title

```http
PUT /api/v1/job-titles/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Job Title ID

**Request Body:**
```json
{
  "title": "Senior DevOps Engineer",
  "baseSalary": 80000.00
}
```

**Response:** `200 OK`
```json
{
  "id": 4,
  "title": "Senior DevOps Engineer",
  "baseSalary": 80000.00
}
```

### 5. Delete Job Title

```http
DELETE /api/v1/job-titles/{id}
```

**Path Parameters:**
- `id` (Long) - Job Title ID

**Response:** `204 NO CONTENT`

---

## 🕐 Attendance API

**Base Path:** `/api/v1/attendance`

### 1. Record Check-In

```http
POST /api/v1/attendance/employee/{employeeId}/check-in
Content-Type: application/json
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Request Body:**
```json
{
  "checkInTime": "08:30:00",
  "checkOutTime": null,
  "hoursWorked": 0.0,
  "status": "PRESENT"
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
  "workDate": "2024-12-12",
  "checkInTime": "08:30:00",
  "checkOutTime": null,
  "hoursWorked": 0.0,
  "status": "PRESENT"
}
```

### 2. Get Attendance by Date Range

```http
GET /api/v1/attendance/employee/{employeeId}?startDate={startDate}&endDate={endDate}
```

**Path Parameters:**
- `employeeId` (Long) - Employee ID

**Query Parameters:**
- `startDate` (String, format: yyyy-MM-dd) - Start date
- `endDate` (String, format: yyyy-MM-dd) - End date

**Example:**
```
GET /api/v1/attendance/employee/1?startDate=2024-12-01&endDate=2024-12-31
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
    "workDate": "2024-12-01",
    "checkInTime": "08:30:00",
    "checkOutTime": "17:00:00",
    "hoursWorked": 8.5,
    "status": "PRESENT"
  },
  {
    "id": 2,
    "employee": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe"
    },
    "workDate": "2024-12-02",
    "checkInTime": "08:45:00",
    "checkOutTime": "17:15:00",
    "hoursWorked": 8.5,
    "status": "PRESENT"
  }
]
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

