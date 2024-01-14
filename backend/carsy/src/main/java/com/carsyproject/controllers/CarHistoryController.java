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
        // Parse the date from the payload
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) payload.get("date"));

        // Get the serviceTypeId and carId from the payload
        Long serviceTypeId = Long.valueOf((Integer) payload.get("serviceTypeId"));
        Long carId = Long.valueOf((Integer) payload.get("carId"));

        // Find the ServiceType and Car entities with the given IDs
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Error: ServiceType is not found."));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Error: Car is not found."));

        // Create a new CarHistory entity and set its fields
        CarHistory carHistory = new CarHistory();
        carHistory.setDate(date);
        carHistory.setServiceType(serviceType);
        carHistory.setCar(car);

        // Save the CarHistory entity and return it
        return carHistoryRepository.save(carHistory);
    }

    @GetMapping("/{carId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Get car history", description = "Get car history for a specific car. Requires USER, MODERATOR, or ADMIN role.")
    public List<CarHistory> getMyCarHistory(@PathVariable Long carId) {
        // Get the logged in user's ID
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        // Check if the logged in user is a moderator or admin
        boolean isModeratorOrAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_MODERATOR") || role.getAuthority().equals("ROLE_ADMIN"));

        // Check if the car with the given ID is owned by the logged in user
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        if (!car.getUser().getId().equals(userId) && !isModeratorOrAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this car's history");
        }

        // Return the history of the car
        return carHistoryRepository.findByCar(car);
    }

    @PostMapping("/appointment")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Making an appointment", description = "Format: yyyy-MM-dd HH. Requires USER, MODERATOR, or ADMIN role.")
    public CarHistory addAppointment(@RequestBody Map<String, Object> payload) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        dateFormat.setLenient(false);
        Date date = dateFormat.parse((String) payload.get("date"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 7 || hour > 17) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments can only be scheduled between 7:00 and 17:00");
        }

        if (carHistoryRepository.findByDate(date).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is already taken");
        }

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

}