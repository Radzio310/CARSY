package com.carsyproject.controllers;

import com.carsyproject.models.Car;
import com.carsyproject.models.User;
import com.carsyproject.repository.CarRepository;
import com.carsyproject.repository.UserRepository;
import com.carsyproject.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.Console;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cars")
@Tag(name = "car", description = "the car API")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Add a car", description = "Add a new car to the current user's cars. Requires USER, MODERATOR, or ADMIN role.")
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        car.setUser(user);
        carRepository.save(car);
        return ResponseEntity.ok("Car added successfully.");
    }

    // TODO wyświetlanie danych o właścicielu
    @GetMapping("/allcars")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Get all cars", description = "Get all cars. Requires MODERATOR or ADMIN role.")
    public ResponseEntity<?> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Get user's cars", description = "Get all cars of a specific user. Requires MODERATOR or ADMIN role.")
    public ResponseEntity<?> getUserCars(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        Set<Car> cars = user.getCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/mycars")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Get my cars", description = "Get all cars of the current user. Requires USER, MODERATOR, or ADMIN role.")
    public ResponseEntity<?> getCurrentUser() {
        System.out.println("getCurrentUser");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        Set<Car> cars = user.getCars();
        return ResponseEntity.ok(cars);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete a car", description = "Delete a car by ID. Requires MODERATOR or ADMIN role.")
    public ResponseEntity<?> deleteCarByMod(@PathVariable Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Car is not found."));
        carRepository.delete(car);
        return ResponseEntity.ok("Car deleted successfully.");
    }

    @DeleteMapping("deletemycar/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete my car", description = "Delete a car of the current user by ID. Requires USER, MODERATOR, or ADMIN role.")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Car is not found."));
        if (!car.getUser().getId().equals(userDetails.getId())) {
            return new ResponseEntity<>("Error: You are not authorized to delete this car.", HttpStatus.UNAUTHORIZED);
        }
        carRepository.delete(car);
        return ResponseEntity.ok("Car deleted successfully.");
    }

//    @GetMapping("/user")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public String userAccess() {
//        return "User Content.";
//    }
}