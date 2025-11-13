# ESD_Project - HR and Payroll Management System

## 🎯 Project Overview
The HR and Payroll Management System is a comprehensive Spring Boot 3.3.0 application designed to streamline and automate human resource and payroll operations within an organization. It allows efficient management of employee records, attendance, leave, payroll processing, performance reviews, expense claims, and shift scheduling while ensuring accuracy and reducing manual effort.

**Status:** ✅ **ACTIVE & RUNNING** on http://localhost:8080

---

## ✨ Recent Enhancements (November 14, 2025)

### 🆕 New Features Added

**1. Performance Review Management**
- Complete CRUD operations for employee performance reviews
- Date range filtering for historical analysis
- Score tracking and comments management
- Automatic timestamp tracking

**2. Expense Claim Management**
- Submit and track employee expense reimbursements
- Status workflow: PENDING → APPROVED/REJECTED
- Date range filtering for expense reports
- BigDecimal support for monetary amounts

**3. Shift Schedule Management**
- Create and manage work shifts
- Time-based shift filtering
- Employee shift assignment support
- Flexible query capabilities

### 📊 New API Endpoints (50+ total)
- **Performance Reviews:** GET, POST, PUT, DELETE operations
- **Expense Claims:** Full CRUD + approval workflow
- **Shift Schedules:** Complete management API
- All endpoints follow RESTful conventions

---

## 🚀 Quick Start

### Prerequisites
- Java 17 LTS
- Maven 3.x
- Windows PowerShell or Terminal

### Running the Application
```bash
# Clone and navigate to project
cd e:\ESD_Project

# Build and run
mvn clean spring-boot:run

# Application starts on http://localhost:8080
```

### Testing the APIs
See **API_TESTING_GUIDE.md** for comprehensive examples with cURL commands.

---

## 📚 Documentation

- **[ENHANCEMENT_SUMMARY.md](ENHANCEMENT_SUMMARY.md)** - Detailed enhancement report
- **[API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)** - Complete API examples
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System architecture & design

---

## 🏗️ Project Structure

### Core Modules
- **Entities** (16 JPA entities with proper relationships)
- **Repositories** (10 Spring Data JPA repositories)
- **Services** (7 business logic services)
- **Controllers** (5 REST controllers)
- **DTOs** (5 Data Transfer Objects)

### Feature Modules
✅ Employee Management  
✅ Payroll Processing  
✅ Leave Management  
✅ Attendance Tracking  
✅ **Performance Reviews** (NEW)  
✅ **Expense Claims** (NEW)  
✅ **Shift Scheduling** (NEW)  

---

## 📖 API Overview

### Base URL
```
http://localhost:8080/api/v1
```

### Main Endpoints

#### Employees
```
GET    /employees              - List all employees
GET    /employees/{id}        - Get employee details
POST   /employees             - Create new employee
PUT    /employees/{id}        - Update employee
DELETE /employees/{id}        - Delete employee
```

#### Performance Reviews (NEW)
```
GET    /performance-reviews                        - List all reviews
POST   /performance-reviews/employee/{id}          - Create review
GET    /performance-reviews/employee/{id}          - Get reviews for employee
PUT    /performance-reviews/{id}                   - Update review
DELETE /performance-reviews/{id}                   - Delete review
```

#### Expense Claims (NEW)
```
GET    /expenses                      - List all claims
POST   /expenses/employee/{id}        - Submit claim
GET    /expenses/status/pending       - Get pending claims
PUT    /expenses/{id}/approve         - Approve claim
PUT    /expenses/{id}/reject          - Reject claim
DELETE /expenses/{id}                 - Delete claim
```

#### Shifts (NEW)
```
GET    /shifts                        - List all shifts
POST   /shifts                        - Create shift
GET    /shifts/search/name?name=...   - Search by name
PUT    /shifts/{id}                   - Update shift
DELETE /shifts/{id}                   - Delete shift
```

#### Payroll
```
POST   /payroll/process?month=X&year=Y  - Process monthly payroll
GET    /payroll/payslips/{id}           - Get payslip details
```

#### Leave
```
POST   /leave/{id}/request              - Apply for leave
GET    /leave/pending                   - Get pending requests
PUT    /leave/{id}/status               - Update request status
```

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 3.3.0 |
| Java Version | 17 LTS |
| Build Tool | Maven 3.x |
| Database | H2 (In-memory) |
| ORM | Hibernate 6.5.2 |
| Data Access | Spring Data JPA |
| REST | Spring Web |
| Web Server | Apache Tomcat 10.1.24 |

---

## 📊 Test Data

Pre-loaded sample data:
- **2 Departments:** HR, Finance
- **2 Job Titles:** Software Developer, HR Specialist
- **2 Employees:** Alex Chen, Sarah Khan
- **2 Leave Types:** Annual, Sick

Use these IDs for testing API endpoints.

---

## ⚙️ Configuration

### Application Properties
```properties
server.port=8080
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

### Database
- **Type:** H2 In-Memory
- **URL:** jdbc:h2:mem:hr_db
- **Console:** http://localhost:8080/h2-console
- **DDL Strategy:** Auto-create on startup, drop on shutdown

---

## 🧪 Testing

### Example API Call
```bash
# Get all performance reviews
curl http://localhost:8080/api/v1/performance-reviews

# Create new expense claim
curl -X POST http://localhost:8080/api/v1/expenses/employee/1 \
  -H "Content-Type: application/json" \
  -d '{"totalAmount": 250.50}'

# List all shifts
curl http://localhost:8080/api/v1/shifts
```

See **API_TESTING_GUIDE.md** for complete examples.

---

## 📈 Project Statistics

| Metric | Value |
|--------|-------|
| Total Classes | 48 |
| Java Files | 48 |
| Database Tables | 16 |
| REST Endpoints | 50+ |
| Lines of Code | 4,500+ |
| Relationships | 25+ |
| Build Time | ~5 seconds |

---

## ✅ Build Status

```
[INFO] BUILD SUCCESS
[INFO] Compiled: 48 source files
[INFO] Total time: 5.177 s
```

**Application Status:** ✅ RUNNING

---

## 🎯 Next Steps

### Phase 1: Security (High Priority)
- [ ] Add Spring Security
- [ ] Implement JWT authentication
- [ ] Role-based access control

### Phase 2: Validation (High Priority)
- [ ] Input validation with @Valid
- [ ] Custom exception handlers
- [ ] Error response formatting

### Phase 3: Documentation (Medium Priority)
- [ ] Swagger/OpenAPI integration
- [ ] API specification export
- [ ] Postman collection

### Phase 4: Testing (High Priority)
- [ ] Unit tests (JUnit 5)
- [ ] Integration tests
- [ ] 80%+ code coverage

### Phase 5: Production (Medium Priority)
- [ ] Logging framework (SLF4J)
- [ ] Database migrations (Flyway)
- [ ] Performance monitoring

---

## 🤝 Contributing

Contributions are welcome! Please follow the existing code patterns and maintain the layered architecture.

---

## 📝 License

This project is part of the ESD (Enterprise System Development) curriculum.

---

## 📞 Support

For detailed information, refer to:
- **ENHANCEMENT_SUMMARY.md** - What's new in this version
- **API_TESTING_GUIDE.md** - How to test the APIs
- **ARCHITECTURE.md** - System design and structure

---

**Last Updated:** November 14, 2025  
**Project Version:** 0.0.1-SNAPSHOT  
**Status:** ✅ Active Development
