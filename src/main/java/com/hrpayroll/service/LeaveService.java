package com.hrpayroll.service;

import com.hrpayroll.entity.Employee;
import com.hrpayroll.entity.LeaveRequest;
import com.hrpayroll.entity.LeaveStatus;
import com.hrpayroll.entity.LeaveType;
import com.hrpayroll.repository.EmployeeRepository;
import com.hrpayroll.repository.LeaveRequestRepository;
import com.hrpayroll.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public LeaveService(LeaveRequestRepository leaveRequestRepository, 
                        EmployeeRepository employeeRepository, 
                        LeaveTypeRepository leaveTypeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    // Lecture 10: Atomic transaction for submitting and validation leave
    @Transactional
    public LeaveRequest applyForLeave(Long employeeId, LeaveRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found."));
        
        // This line attempts to look up the LeaveType by the ID passed in the request.
        // It relies on the front-end/client providing a LeaveType object with at least the ID set.
        LeaveType type = leaveTypeRepository.findById(request.getLeaveType().getId())
                .orElseThrow(() -> new RuntimeException("Leave Type not found."));
        
        // --- Business Rule Validation (Lecture 4 concept) ---
        if (request.getDurationDays() > type.getDefaultDays()) {
            throw new IllegalArgumentException("Requested days exceed maximum allowed for this type.");
        }
        
        request.setEmployee(employee);
        request.setLeaveType(type);
        request.setStatus(LeaveStatus.PENDING); // Initial status
        
        return leaveRequestRepository.save(request);
    }
    
    // Lecture 10: Atomic transaction for status change
    @Transactional
    public LeaveRequest updateLeaveStatus(Long requestId, LeaveStatus status) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave Request not found."));
        
        request.setStatus(status);
        // Additional logic here: if status is APPROVED, deduct balance from employee record.
        
        return leaveRequestRepository.save(request);
    }
    
    // Lecture 10: Read operation
    @Transactional(readOnly = true)
    public List<LeaveRequest> getRequestsByStatus(LeaveStatus status) {
        return leaveRequestRepository.findByStatus(status);
    }
}
