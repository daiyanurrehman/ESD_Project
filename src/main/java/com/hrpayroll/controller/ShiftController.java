package com.hrpayroll.controller;

import com.hrpayroll.dto.ShiftScheduleDTO;
import com.hrpayroll.entity.ShiftSchedule;
import com.hrpayroll.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Lecture 5: REST Controller for Shift Schedule CRUD operations
@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    // GET all shifts
    @GetMapping
    public ResponseEntity<List<ShiftScheduleDTO>> getAllShifts() {
        List<ShiftScheduleDTO> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }

    // GET shift by ID
    @GetMapping("/{id}")
    public ResponseEntity<ShiftScheduleDTO> getShiftById(@PathVariable Long id) {
        ShiftScheduleDTO shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(shift);
    }

    // GET shifts by name
    @GetMapping("/search/name")
    public ResponseEntity<List<ShiftScheduleDTO>> getShiftsByName(@RequestParam String name) {
        List<ShiftScheduleDTO> shifts = shiftService.getShiftsByName(name);
        return ResponseEntity.ok(shifts);
    }

    // GET shifts by time range
    @GetMapping("/search/time")
    public ResponseEntity<List<ShiftScheduleDTO>> getShiftsByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        List<ShiftScheduleDTO> shifts = shiftService.getShiftsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(shifts);
    }

    // POST create new shift
    @PostMapping
    public ResponseEntity<ShiftScheduleDTO> createShift(@RequestBody ShiftSchedule shift) {
        ShiftScheduleDTO newShift = shiftService.createShift(shift);
        return new ResponseEntity<>(newShift, HttpStatus.CREATED);
    }

    // PUT update shift
    @PutMapping("/{id}")
    public ResponseEntity<ShiftScheduleDTO> updateShift(
            @PathVariable Long id,
            @RequestBody ShiftSchedule shiftDetails) {
        ShiftScheduleDTO updatedShift = shiftService.updateShift(id, shiftDetails);
        return ResponseEntity.ok(updatedShift);
    }

    // DELETE shift
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
}
