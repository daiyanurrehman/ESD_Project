package com.hrpayroll.controller;

import com.hrpayroll.entity.LeaveRequest;
import com.hrpayroll.entity.LeaveStatus;
import com.hrpayroll.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave")
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // POST /api/v1/leave/{employeeId}/request (Apply for leave)
    @PostMapping("/{employeeId}/request")
    public ResponseEntity<LeaveRequest> applyForLeave(
            @PathVariable Long employeeId,
            @RequestBody LeaveRequest request) {
        
        LeaveRequest newRequest = leaveService.applyForLeave(employeeId, request);
        return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }
    
    // GET /api/v1/leave/pending (Get all pending requests for HR Manager)
    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequest>> getPendingRequests() {
        List<LeaveRequest> requests = leaveService.getRequestsByStatus(LeaveStatus.PENDING);
        return ResponseEntity.ok(requests);
    }
    
    // PUT /api/v1/leave/{requestId}/status (Approve/Reject)
    @PutMapping("/{requestId}/status")
    public ResponseEntity<LeaveRequest> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestParam LeaveStatus status) { // Lecture 8: Using @RequestParam for status
        
        LeaveRequest updatedRequest = leaveService.updateLeaveStatus(requestId, status);
        return ResponseEntity.ok(updatedRequest);
    }
}
