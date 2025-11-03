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
                                      LeaveTypeRepository leaveTypeRepo) {
        return (args) -> {
            System.out.println("--- Populating Initial Test Data ---");

            // 1. Create Departments
            Department hr = new Department();
            hr.setName("Human Resources");
            hr.setDescription("Manages employee relations and hiring.");
            deptRepo.save(hr);

            Department finance = new Department();
            finance.setName("Finance");
            finance.setDescription("Handles budgeting and payroll.");
            deptRepo.save(finance);

            // 2. Create Job Titles (Base Salary is key for Payroll)
            JobTitle dev = new JobTitle();
            dev.setTitle("Software Developer");
            dev.setBaseSalary(80000.00); // Base salary for payroll calculation
            jobRepo.save(dev);

            JobTitle hrSpecialist = new JobTitle();
            hrSpecialist.setTitle("HR Specialist");
            hrSpecialist.setBaseSalary(55000.00);
            jobRepo.save(hrSpecialist);
            
            // 3. Create Leave Types
            LeaveType annual = new LeaveType();
            annual.setName("Annual Leave");
            annual.setDefaultDays(20);
            leaveTypeRepo.save(annual);
            
            LeaveType sick = new LeaveType();
            sick.setName("Sick Leave");
            sick.setDefaultDays(10);
            leaveTypeRepo.save(sick);

            // 4. Create Employees
            Employee emp1 = new Employee();
            emp1.setFirstName("Alex");
            emp1.setLastName("Chen");
            emp1.setHireDate(LocalDate.of(2022, 1, 15));
            emp1.setDateOfBirth(LocalDate.of(1990, 5, 20));
            emp1.setDepartment(finance); // ManyToOne mapping
            emp1.setJobTitle(dev); // ManyToOne mapping
            emp1.setUsername("alex.chen");
            emp1.setPasswordHash("pass123");
            emp1.setRole(UserRole.EMPLOYEE);
            empRepo.save(emp1);

            Employee emp2 = new Employee();
            emp2.setFirstName("Sarah");
            emp2.setLastName("Khan");
            emp2.setHireDate(LocalDate.of(2023, 8, 1));
            emp2.setDateOfBirth(LocalDate.of(1995, 10, 10));
            emp2.setDepartment(hr);
            emp2.setJobTitle(hrSpecialist);
            emp2.setUsername("sarah.khan");
            emp2.setPasswordHash("pass456");
            emp2.setRole(UserRole.HR_MANAGER);
            empRepo.save(emp2);
            
            System.out.println("--- Test Data Loaded. Application ready on port 8080. ---");
        };
    }
}
