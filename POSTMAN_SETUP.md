# Postman Setup for JWT Authentication

## 🔧 Correct Postman Configuration

### 1. Signup Endpoint

**Method:** POST  
**URL:** `http://localhost:5000/auth/signup`

**Headers:**
- `Content-Type: application/json`

**Body:** (Select **raw** and **JSON**)
```json
{
  "username": "Daniyal",
  "password": "dan"
}
```

**Expected Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "Daniyal",
  "role": "HR_MANAGER",
  "expiresAt": 1700000000000
}
```

---

### 2. Login Endpoint

**Method:** POST  
**URL:** `http://localhost:5000/auth/login`

**Headers:**
- `Content-Type: application/json`

**Body:** (Select **raw** and **JSON**)
```json
{
  "username": "sarah.khan",
  "password": "pass456"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "sarah.khan",
  "role": "HR_MANAGER",
  "expiresAt": 1700000000000
}
```

---

### 3. Accessing Protected Routes

**Method:** GET  
**URL:** `http://localhost:5000/api/v1/employees`

**Headers:**
- `Authorization: Bearer <YOUR_JWT_TOKEN>`

(Replace `<YOUR_JWT_TOKEN>` with the token you got from login/signup)

---

## ❌ Common Mistakes

### Mistake 1: Using x-www-form-urlencoded
**Wrong:** Body → x-www-form-urlencoded  
**Correct:** Body → raw → JSON

### Mistake 2: Missing Content-Type
**Wrong:** No Content-Type header  
**Correct:** Content-Type: application/json

### Mistake 3: Wrong Port
**Wrong:** http://localhost:8080  
**Correct:** http://localhost:5000 (updated port)

---

## ✅ Quick Test in PowerShell

If you prefer command line:

**Signup:**
```powershell
Invoke-RestMethod -Uri "http://localhost:5000/auth/signup" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Daniyal","password":"dan"}'
```

**Login:**
```powershell
Invoke-RestMethod -Uri "http://localhost:5000/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"sarah.khan","password":"pass456"}'
```
