package com.hrpayroll.service;

import com.hrpayroll.dto.ShiftScheduleDTO;
import com.hrpayroll.entity.ShiftSchedule;
import com.hrpayroll.repository.ShiftScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Lecture 9: Service for Shift Schedule business logic
@Service
public class ShiftService {

    private final ShiftScheduleRepository shiftScheduleRepository;

    @Autowired
    public ShiftService(ShiftScheduleRepository shiftScheduleRepository) {
        this.shiftScheduleRepository = shiftScheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<ShiftScheduleDTO> getAllShifts() {
        return shiftScheduleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ShiftScheduleDTO getShiftById(Long id) {
        ShiftSchedule shift = shiftScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with ID: " + id));
        return convertToDTO(shift);
    }

    @Transactional(readOnly = true)
    public List<ShiftScheduleDTO> getShiftsByName(String name) {
        return shiftScheduleRepository.findByName(name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShiftScheduleDTO> getShiftsByTimeRange(String startTime, String endTime) {
        return shiftScheduleRepository.findByStartTimeAndEndTime(startTime, endTime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShiftScheduleDTO createShift(ShiftSchedule shift) {
        ShiftSchedule savedShift = shiftScheduleRepository.save(shift);
        return convertToDTO(savedShift);
    }

    @Transactional
    public ShiftScheduleDTO updateShift(Long id, ShiftSchedule shiftDetails) {
        ShiftSchedule shift = shiftScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with ID: " + id));
        
        shift.setName(shiftDetails.getName());
        shift.setStartTime(shiftDetails.getStartTime());
        shift.setEndTime(shiftDetails.getEndTime());
        
        ShiftSchedule updatedShift = shiftScheduleRepository.save(shift);
        return convertToDTO(updatedShift);
    }

    @Transactional
    public void deleteShift(Long id) {
        shiftScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with ID: " + id));
        shiftScheduleRepository.deleteById(id);
    }

    private ShiftScheduleDTO convertToDTO(ShiftSchedule shift) {
        ShiftScheduleDTO dto = new ShiftScheduleDTO();
        dto.setId(shift.getId());
        dto.setName(shift.getName());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        return dto;
    }
}
