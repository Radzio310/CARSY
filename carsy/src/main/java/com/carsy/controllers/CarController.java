package com.carsy.controllers;

import com.carsy.entities.Car;
import com.carsy.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.Year;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/cars")
public class CarController 
{
	
	@Autowired
    CarRepository carRepository;

    @GetMapping("")
    public ResponseEntity<Object> getAllCars() 
    {
        Optional<List<Car>> optionalCars = carRepository.getAll();
        if (optionalCars.isPresent()) {
            List<Car> carList = optionalCars.get();
            return ResponseEntity.status(HttpStatus.OK).body(carList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cars not found.");
        }
    }

    @GetMapping("/{carID}")
    public ResponseEntity<Object> getCarById(@PathVariable("carID") int carID) 
    {
        Optional<Car> optionalCar = carRepository.getById(carID);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            return ResponseEntity.status(HttpStatus.OK).body(car);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCar(@RequestBody Car car) 
    {
    	
    	if(!validateBrand(car.getBrand()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Brand.");
    	if(!validateYear(car.getYear()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Year.");

    	if(!validateVin(car.getVin()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong VIN number.");
    	
        carRepository.save(car);
        return ResponseEntity.status(HttpStatus.OK).body("Success.");
    }

    @PutMapping("/update/{carID}")
    public ResponseEntity<String> updateCar(@PathVariable("carID") int carID, @RequestBody Car updatedCar) 
    {
        Optional<Car> optionalCar = carRepository.getById(carID);
        Car car = optionalCar.orElse(null);

        if (car != null) 
        {
        	if(!validateBrand(car.getBrand()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Brand.");
            car.setBrand(updatedCar.getBrand());
            
            car.setModel(updatedCar.getModel());
            
            if(!validateYear(car.getYear()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Year.");
            car.setYear(updatedCar.getYear());
            
            if(!validateVin(car.getVin().toUpperCase()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong VIN number.");
            car.setVin(updatedCar.getVin());
            
            carRepository.update(car);
            return ResponseEntity.status(HttpStatus.OK).body("Success.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }
    }

    @PatchMapping("/update/{carID}")
    public ResponseEntity<String> partiallyUpdateCar(@PathVariable("carID") int carID, @RequestBody Car updatedCar) 
    {
        Optional<Car> optionalCar = carRepository.getById(carID);
        Car car = optionalCar.orElse(null);

        if (car != null) 
        {
        	if(!validateBrand(car.getBrand()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Brand.");
            car.setBrand(updatedCar.getBrand());
            
            car.setModel(updatedCar.getModel());
            
            if(!validateYear(car.getYear()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Year.");
            car.setYear(updatedCar.getYear());
            
            if(!validateVin(car.getVin().toUpperCase()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong VIN number.");
            car.setVin(updatedCar.getVin());

            carRepository.update(car);
            return ResponseEntity.status(HttpStatus.OK).body("Success.");
        }
        else 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }
    }

    @DeleteMapping("/delete/{carID}")
    public ResponseEntity<String> deleteCar(@PathVariable("carID") int carID) 
    {
        Optional<Car> optionalCar = carRepository.getById(carID);
        if (optionalCar.isPresent()) {
            carRepository.delete(carID);
            return ResponseEntity.status(HttpStatus.OK).body("Success.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }
    }
    
    private boolean validateBrand(String brand)
    {
    	return brand != null && !brand.isEmpty() && brand.matches("[a-zA-Z0-9\\s-]+");
    }
    
    private boolean validateYear(String year) 
    {
        if (year == null || year.isEmpty()) 
        {
            return false;
        }
        
        int currentYear = Year.now().getValue();
   
        if (Pattern.matches("\\d{4}", year)) 
        {
            int parsedYear = Integer.parseInt(year);
            return parsedYear >= 1850 && parsedYear <= currentYear;
        }
        
        return false;
    }
    
    private boolean validateVin(String vin) 
    {
    	if (vin == null || vin.length() != 17 || !vin.matches("[A-HJ-NPR-Z0-9]{17}")) {
            return false;
        }

        return vin.equals(vin.toUpperCase()) && carRepository.isVinUnique(vin);
    }
}