# Why Your Application Appears to "Crash"

## The Real Reason

**Port 8080 is already in use!**

When you see this error:
```
Web server failed to start. Port 8080 was already in use.
```

It means there's ALREADY a Java/Spring Boot instance running on port 8080. When you try to run `mvn clean spring-boot:run` again, it fails because Spring Boot can't bind to port 8080 (it's occupied).

**This is NOT a bug or crash** - it's correct behavior preventing duplicate instances.

---

## Two Solutions

### Option 1: Use the Already-Running Instance

If a Java process is already running on port 8080 and responding correctly, just use it!

**Test if it's working:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"sarah.khan","password":"pass456"}'
```

If this works, your app is already running fine!

---

### Option 2: Stop Old Instance and Start Fresh

If you want to restart the application:

**Step 1: Stop all Java processes**
```powershell
Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force
```

**Step 2: Wait 2 seconds**
```powershell
Start-Sleep -Seconds 2
```

**Step 3: Start the application**
```powershell
mvn spring-boot:run
```

---

## How to Avoid This Issue

**Rule:** Only run `mvn spring-boot:run` ONCE. Don't start it multiple times in different termina terminals.

**To check if it's already running:**
```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
```

If this returns results, the app is running. If empty, port 8080 is free.

---

## Summary

❌ **NOT a crash or bug**
✅ **Port conflict** - trying to run two instances on same port
✅ **Solution** - Either use the running instance OR stop it first before starting a new one
