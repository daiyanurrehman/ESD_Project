package com.hrpayroll.repository;

import com.hrpayroll.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Lecture 8: JpaRepository extends CrudRepository and PagingAndSortingRepository
@Repository // Marks this interface as a Data Access component
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Lecture 8: Custom Query Creation by Method Name
    List<Employee> findByHireDateAfter(LocalDate date);
    
    // Finds employees by their job title's name (Nested Property Query - Lecture 8)
    List<Employee> findByJobTitle_Title(String jobTitle);
}
