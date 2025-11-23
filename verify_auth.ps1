# JWT Authentication Testing Script
# Test 1: Signup new HR user
Write-Host "Test 1: Creating new HR user via signup..." -ForegroundColor Cyan

try {
    $signupResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/signup" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body (@{username="hr.test"; password="securepass123"} | ConvertTo-Json)
    
    Write-Host "✓ Signup successful!" -ForegroundColor Green
    Write-Host "  Token: $($signupResponse.token.Substring(0,30))..." -ForegroundColor Yellow
    Write-Host "  Username: $($signupResponse.username)" -ForegroundColor Yellow
    Write-Host "  Role: $($signupResponse.role)" -ForegroundColor Yellow
    Write-Host ""
    
    $token = $signupResponse.token
} catch {
    Write-Host "✗ Signup failed: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 2: Login with existing HR user
Write-Host "Test 2: Logging in with seed data HR user (sarah.khan)..." -ForegroundColor Cyan

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body (@{username="sarah.khan"; password="pass456"} | ConvertTo-Json)
    
    Write-Host "✓ Login successful!" -ForegroundColor Green
    Write-Host "  Token: $($loginResponse.token.Substring(0,30))..." -ForegroundColor Yellow
    Write-Host "  Username: $($loginResponse.username)" -ForegroundColor Yellow
    Write-Host "  Role: $($loginResponse.role)" -ForegroundColor Yellow
    Write-Host ""
    
    $token = $loginResponse.token
} catch {
    Write-Host "✗ Login failed: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 3: Access protected route without token
Write-Host "Test 3: Accessing protected route without token..." -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/employees" -Method GET
    Write-Host "✗ Should have been blocked! Received: $response" -ForegroundColor Red
    Write-Host ""
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 401 -or $_.Exception.Response.StatusCode.value__ -eq 403) {
        Write-Host "✓ Correctly blocked with $($_.Exception.Response.StatusCode.value__) status" -ForegroundColor Green
        Write-Host ""
    } else {
        Write-Host "✗ Unexpected error: $_" -ForegroundColor Red
        Write-Host ""
    }
}

# Test 4: Access protected route with valid token
Write-Host "Test 4: Accessing protected route with valid JWT token..." -ForegroundColor Cyan

try {
    $employees = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/employees" `
        -Method GET `
        -Headers @{"Authorization"="Bearer $token"}
    
    Write-Host "✓ Access granted! Retrieved $($employees.Count) employees" -ForegroundColor Green
    Write-Host ""
    
    # Display employees
    foreach ($emp in $employees) {
        Write-Host "  - $($emp.firstName) $($emp.lastName) ($($emp.username)) - $($emp.role)" -ForegroundColor Yellow
    }
    Write-Host ""
} catch {
    Write-Host "✗ Failed to access protected route: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 5: Try to signup duplicate username
Write-Host "Test 5: Attempting to signup with duplicate username..." -ForegroundColor Cyan

try {
    $dupResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/signup" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body (@{username="sarah.khan"; password="newpass"} | ConvertTo-Json)
    
    Write-Host "✗ Should have been rejected! Received: $dupResponse" -ForegroundColor Red
    Write-Host ""
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 400) {
        Write-Host "✓ Correctly rejected duplicate username with 400 status" -ForegroundColor Green
        Write-Host ""
    } else {
        Write-Host "✗ Unexpected error: $_" -ForegroundColor Red
        Write-Host ""
    }
}

Write-Host "===== All Tests Complete =====" -ForegroundColor Cyan
