package com.carsyproject.controllers;

import com.carsyproject.models.Car;
import com.carsyproject.models.CarHistory;
import com.carsyproject.models.ServiceType;
import com.carsyproject.repository.CarHistoryRepository;
import com.carsyproject.repository.CarRepository;
import com.carsyproject.repository.ServiceTypeRepository;
import com.carsyproject.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/carhistory")
@Tag(name = "carhistory", description = "the car history API")
public class CarHistoryController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private CarHistoryRepository carHistoryRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Get all car histories", description = "Get all car histories. Requires MODERATOR or ADMIN role.")
    public List<CarHistory> getAllCarHistories() {
        return carHistoryRepository.findAll();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Add a car history", description = "Add a new car history. Requires MODERATOR or ADMIN role.")
    public CarHistory addCarHistory(@RequestBody Map<String, Object> payload) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) payload.get("date"));

        Long serviceTypeId = Long.valueOf((Integer) payload.get("serviceTypeId"));
        Long carId = Long.valueOf((Integer) payload.get("carId"));

        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Error: ServiceType is not found."));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Error: Car is not found."));

        CarHistory carHistory = new CarHistory();
        carHistory.setDate(date);
        carHistory.setServiceType(serviceType);
        carHistory.setCar(car);

        return carHistoryRepository.save(carHistory);
    }

    @GetMapping("/{carId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Get car history", description = "Get car history for a specific car. Requires USER, MODERATOR, or ADMIN role.")
    public List<CarHistory> getMyCarHistory(@PathVariable Long carId) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        boolean isModeratorOrAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_MODERATOR") || role.getAuthority().equals("ROLE_ADMIN"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        if (!car.getUser().getId().equals(userId) && !isModeratorOrAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this car's history");
        }

        return carHistoryRepository.findByCar(car);
    }

    @PostMapping("/appointment")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Making an appointment", description = "Format: yyyy-MM-dd HH. Requires USER, MODERATOR, or ADMIN role.")
    public CarHistory addAppointment(@RequestBody Map<String, Object> payload) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        dateFormat.setLenient(false);
        Date date = dateFormat.parse((String) payload.get("date"));


        Date currentDate = new Date();
        if (date.before(currentDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment date cannot be in the past");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (hour < 7 || hour > 17) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments can only be scheduled between 7:00 and 17:00");
        }

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments cannot be scheduled on weekends");
        }

        if (carHistoryRepository.findByDate(date).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is already taken");
        }

        Long serviceTypeId = Long.valueOf((Integer) payload.get("serviceTypeId"));
        Long carId = Long.valueOf((Integer) payload.get("carId"));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Error: ServiceType is not found."));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Error: Car is not found."));

        if (!car.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to schedule an appointment for this car");
        }

        CarHistory carHistory = new CarHistory();
        carHistory.setDate(date);
        carHistory.setServiceType(serviceType);
        carHistory.setCar(car);

        return carHistoryRepository.save(carHistory);
    }

    @DeleteMapping("/appointment/delete/{appointmentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete an appointment", description = "Delete an appointment. Requires USER, MODERATOR, or ADMIN role.")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        CarHistory carHistory = carHistoryRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (carHistory.getDate().before(new Date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete past appointments");
        }

        if (!carHistory.getCar().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this appointment");
        }

        carHistoryRepository.delete(carHistory);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/appointment/edit/{appointmentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Edit an appointment", description = "Edit an appointment. Requires USER, MODERATOR, or ADMIN role.")
    public CarHistory editAppointment(@PathVariable Long appointmentId, @RequestBody Map<String, Object> payload) throws ParseException {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        CarHistory carHistory = carHistoryRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (!carHistory.getCar().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this appointment");
        }

        if (payload.containsKey("date")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            dateFormat.setLenient(false);
            Date date = dateFormat.parse((String) payload.get("date"));

            if (carHistory.getDate().before(new Date())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot edit past appointments");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour < 7 || hour > 17) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments can only be scheduled between 7:00 and 17:00");
            }

            if (carHistoryRepository.findByDate(date).isPresent() && !carHistoryRepository.findByDate(date).get().getServiceID().equals(appointmentId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is already taken");
            }

            carHistory.setDate(date);
        }

        if (payload.containsKey("serviceTypeId")) {
            // Get the serviceTypeId from the payload
            Long serviceTypeId = Long.valueOf((Integer) payload.get("serviceTypeId"));

            ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                    .orElseThrow(() -> new RuntimeException("Error: ServiceType is not found."));

            carHistory.setServiceType(serviceType);
        }

        return carHistoryRepository.save(carHistory);
    }

}