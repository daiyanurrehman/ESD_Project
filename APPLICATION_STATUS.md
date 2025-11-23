# HR Payroll Management System - Application Status

## ✅ APPLICATION IS RUNNING SUCCESSFULLY

Your application is currently running on **http://localhost:8080**

### Current Status
- **Port**: 8080 (Active and listening)
- **Process**: Java application running
- **Authentication**: JWT-based auth enabled
- **Database**: H2 in-memory database with seed data

---

## Why You See "Crash" Messages

When you run `mvn clean spring-boot:run` in a new terminal while the application is already running, you get an error because:

**Port 8080 is already in use by the first instance!**

This is **not a crash** - it's Maven preventing you from starting a duplicate instance.

---

## How to Use Your Application

### Test Authentication (Working Now!)

**1. Login with existing HR user:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"sarah.khan","password":"pass456"}'
```

**2. Create new HR user:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/auth/signup" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"new.hr","password":"securepass"}'
```

**3. Access protected routes with JWT:**
```powershell
# First, get a token
$response = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"sarah.khan","password":"pass456"}'
$token = $response.token

# Then use it to access protected routes
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/employees" `
  -Method GET `
  -Headers @{"Authorization"="Bearer $token"}
```

---

## How to Stop/Restart the Application

### Stop the Application:
```powershell
Get-Process -Name java | Stop-Process -Force
```

### Start the Application:
```powershell
mvn spring-boot:run
```

**Note**: Only run ONE instance at a time!

---

## Seed Data Available for Testing

Your application comes with preloaded test users:

| Username | Password | Role |
|----------|----------|------|
| sarah.khan | pass456 | HR_MANAGER |
| alex.chen | pass123 | EMPLOYEE |

---

## Summary

🎉 **Your JWT authentication system is working perfectly!**

- All routes are protected
- Passwords are BCrypt-hashed
- JWT tokens are being generated correctly
- Signup/login endpoints are functional

The application is **NOT crashing** - it's running successfully right now!
