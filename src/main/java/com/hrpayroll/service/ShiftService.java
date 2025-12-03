package com.hrpayroll.service;

import com.hrpayroll.dto.ShiftScheduleDTO;
import com.hrpayroll.entity.ShiftSchedule;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.ShiftScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Lecture 9: Service for Shift Schedule business logic
@Service
@SuppressWarnings("null")
public class ShiftService {

    private static final Logger logger = LoggerFactory.getLogger(ShiftService.class);
    
    private final ShiftScheduleRepository shiftScheduleRepository;

    @Autowired
    public ShiftService(ShiftScheduleRepository shiftScheduleRepository) {
        this.shiftScheduleRepository = shiftScheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<ShiftScheduleDTO> getAllShifts() {
        try {
            return shiftScheduleRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Error retrieving all shifts", e);
            throw new DatabaseException("Failed to retrieve shifts", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving shifts", e);
            throw new DatabaseException("An unexpected error occurred while retrieving shifts", e);
        }
    }

    @Transactional(readOnly = true)
    public ShiftScheduleDTO getShiftById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Shift ID cannot be null");
            }
            ShiftSchedule shift = shiftScheduleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Shift", "id", id));
            return convertToDTO(shift);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving shift with ID: {}", id, e);
            throw new DatabaseException("Failed to retrieve shift", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving shift with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while retrieving shift", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ShiftScheduleDTO> getShiftsByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new ValidationException("Shift name cannot be null or empty");
            }
            return shiftScheduleRepository.findByName(name)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving shifts by name: {}", name, e);
            throw new DatabaseException("Failed to retrieve shifts", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving shifts by name: {}", name, e);
            throw new DatabaseException("An unexpected error occurred while retrieving shifts", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ShiftScheduleDTO> getShiftsByTimeRange(String startTime, String endTime) {
        try {
            if (startTime == null || startTime.trim().isEmpty()) {
                throw new ValidationException("Start time cannot be null or empty");
            }
            if (endTime == null || endTime.trim().isEmpty()) {
                throw new ValidationException("End time cannot be null or empty");
            }
            return shiftScheduleRepository.findByStartTimeAndEndTime(startTime, endTime)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving shifts by time range", e);
            throw new DatabaseException("Failed to retrieve shifts", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving shifts by time range", e);
            throw new DatabaseException("An unexpected error occurred while retrieving shifts", e);
        }
    }

    @Transactional
    public ShiftScheduleDTO createShift(ShiftSchedule shift) {
        try {
            if (shift == null) {
                throw new ValidationException("Shift cannot be null");
            }
            if (shift.getName() == null || shift.getName().trim().isEmpty()) {
                throw new ValidationException("Shift name is required");
            }
            ShiftSchedule savedShift = shiftScheduleRepository.save(shift);
            return convertToDTO(savedShift);
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error creating shift", e);
            throw new DatabaseException("Failed to create shift", e);
        } catch (Exception e) {
            logger.error("Unexpected error creating shift", e);
            throw new DatabaseException("An unexpected error occurred while creating shift", e);
        }
    }

    @Transactional
    public ShiftScheduleDTO updateShift(Long id, ShiftSchedule shiftDetails) {
        try {
            if (id == null) {
                throw new ValidationException("Shift ID cannot be null");
            }
            if (shiftDetails == null) {
                throw new ValidationException("Shift details cannot be null");
            }
            
            ShiftSchedule shift = shiftScheduleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Shift", "id", id));

            if (shiftDetails.getName() != null) {
                shift.setName(shiftDetails.getName());
            }
            if (shiftDetails.getStartTime() != null) {
                shift.setStartTime(shiftDetails.getStartTime());
            }
            if (shiftDetails.getEndTime() != null) {
                shift.setEndTime(shiftDetails.getEndTime());
            }

            ShiftSchedule updatedShift = shiftScheduleRepository.save(shift);
            return convertToDTO(updatedShift);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error updating shift with ID: {}", id, e);
            throw new DatabaseException("Failed to update shift", e);
        } catch (Exception e) {
            logger.error("Unexpected error updating shift with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while updating shift", e);
        }
    }

    @Transactional
    public void deleteShift(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Shift ID cannot be null");
            }
            
            if (!shiftScheduleRepository.existsById(id)) {
                throw new ResourceNotFoundException("Shift", "id", id);
            }
            
            shiftScheduleRepository.deleteById(id);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error deleting shift with ID: {}", id, e);
            throw new DatabaseException("Failed to delete shift", e);
        } catch (Exception e) {
            logger.error("Unexpected error deleting shift with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while deleting shift", e);
        }
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
