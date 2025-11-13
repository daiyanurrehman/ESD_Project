package com.hrpayroll.repository;

import com.hrpayroll.entity.ShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Lecture 8: JpaRepository for Shift Schedule entity
@Repository
public interface ShiftScheduleRepository extends JpaRepository<ShiftSchedule, Long> {
    // Find all shifts by name
    List<ShiftSchedule> findByName(String name);
    
    // Find shift by time range (morning, afternoon, night, etc.)
    List<ShiftSchedule> findByStartTimeAndEndTime(String startTime, String endTime);
}
