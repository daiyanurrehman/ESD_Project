package com.hrpayroll;

import com.hrpayroll.entity.*;
import com.hrpayroll.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

// Lecture 4: Main entry point for the Spring Boot application
@SpringBootApplication
public class HrPayrollManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrPayrollManagementApplication.class, args);
    }

    // Populating initial data for testing the REST APIs and Payroll Service
    @Bean
    public CommandLineRunner loadData(EmployeeRepository empRepo,
            DepartmentRepository deptRepo,
            JobTitleRepository jobRepo,
            LeaveTypeRepository leaveTypeRepo,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return (args) -> {
            System.out.println("--- Populating Initial Test Data ---");

            // 1. Departments (idempotent)
            Department hr = deptRepo.findByName("Human Resources");
            if (hr == null) {
                hr = new Department();
                hr.setName("Human Resources");
                hr.setDescription("Manages employee relations and hiring.");
                hr = deptRepo.save(hr);
            }

            Department finance = deptRepo.findByName("Finance");
            if (finance == null) {
                finance = new Department();
                finance.setName("Finance");
                finance.setDescription("Handles budgeting and payroll.");
                finance = deptRepo.save(finance);
            }

            // 2. Job Titles (idempotent)
            JobTitle dev = jobRepo.findByTitle("Software Developer");
            if (dev == null) {
                dev = new JobTitle();
                dev.setTitle("Software Developer");
                dev.setBaseSalary(80000.00);
                dev = jobRepo.save(dev);
            }

            JobTitle hrSpecialist = jobRepo.findByTitle("HR Specialist");
            if (hrSpecialist == null) {
                hrSpecialist = new JobTitle();
                hrSpecialist.setTitle("HR Specialist");
                hrSpecialist.setBaseSalary(55000.00);
                hrSpecialist = jobRepo.save(hrSpecialist);
            }

            // 3. Leave Types (idempotent)
            LeaveType annual = leaveTypeRepo.findByName("Annual Leave");
            if (annual == null) {
                annual = new LeaveType();
                annual.setName("Annual Leave");
                annual.setDefaultDays(20);
                annual = leaveTypeRepo.save(annual);
            }

            LeaveType sick = leaveTypeRepo.findByName("Sick Leave");
            if (sick == null) {
                sick = new LeaveType();
                sick.setName("Sick Leave");
                sick.setDefaultDays(10);
                sick = leaveTypeRepo.save(sick);
            }

            // 4. Employees (idempotent via unique username)
            if (!empRepo.existsByUsername("alex.chen")) {
                Employee emp1 = new Employee();
                emp1.setFirstName("Alex");
                emp1.setLastName("Chen");
                emp1.setHireDate(LocalDate.of(2022, 1, 15));
                emp1.setDateOfBirth(LocalDate.of(1990, 5, 20));
                emp1.setDepartment(finance);
                emp1.setJobTitle(dev);
                emp1.setUsername("alex.chen");
                emp1.setPasswordHash(passwordEncoder.encode("pass123"));
                emp1.setRole(UserRole.EMPLOYEE);
                empRepo.save(emp1);
            }

            if (!empRepo.existsByUsername("sarah.khan")) {
                Employee emp2 = new Employee();
                emp2.setFirstName("Sarah");
                emp2.setLastName("Khan");
                emp2.setHireDate(LocalDate.of(2023, 8, 1));
                emp2.setDateOfBirth(LocalDate.of(1995, 10, 10));
                emp2.setDepartment(hr);
                emp2.setJobTitle(hrSpecialist);
                emp2.setUsername("sarah.khan");
                emp2.setPasswordHash(passwordEncoder.encode("pass456"));
                emp2.setRole(UserRole.HR_MANAGER);
                empRepo.save(emp2);
            }

            System.out.println("--- Test Data Loaded. Application ready on port 5000. ---");
        };
    }
}
