package com.carsy.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entities.CarHistory;
import com.carsy.repositories.CarHistoryRepository;
import com.carsy.repositories.ServiceTypeRepository;
import com.carsy.repositories.UserRepository;


@RestController
@RequestMapping("/carhistory")
public class CarHistoryController 
{
	@Autowired
	CarHistoryRepository carHistoryRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ServiceTypeRepository serviceTypeRepository;
	
	@GetMapping("")
    public ResponseEntity<Object> getAllCarHistories() 
	{
        Optional<List<CarHistory>> optionalCarHistories = carHistoryRepository.getAll();

        if (optionalCarHistories.isPresent()) {
            List<CarHistory> carHistoryList = optionalCarHistories.get();
            return ResponseEntity.status(HttpStatus.OK).body(carHistoryList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car histories not found.");
        }
    }

    @GetMapping("/{serviceID}")
    public ResponseEntity<Object> getCarHistoryById(@PathVariable("serviceID") int serviceID) 
    {
        Optional<CarHistory> optionalCarHistory = carHistoryRepository.getById(serviceID);

        if (optionalCarHistory.isPresent()) {
            CarHistory carHistory = optionalCarHistory.get();
            return ResponseEntity.status(HttpStatus.OK).body(carHistory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car history not found.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCarHistory(@RequestBody CarHistory carHistory) 
    {
    	if(!validateUserID(carHistory.getUserID()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong User ID.");
    	if(!validateServiceTypeID(carHistory.getServiceTypeID()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Service Type ID.");
//    	if(!validateCarID(carHistory.getCarID()))
//    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Car ID.");
    	
        carHistoryRepository.save(carHistory);
        return ResponseEntity.status(HttpStatus.OK).body("Success.");
    }

    @PutMapping("/update/{serviceID}")
    public ResponseEntity<String> updateCarHistory(@PathVariable("serviceID") int serviceID,
                                                   @RequestBody CarHistory updatedCarHistory) 
    {
        Optional<CarHistory> optionalCarHistory = carHistoryRepository.getById(serviceID);
        CarHistory carHistory;

        if (optionalCarHistory.isPresent()) {
            carHistory = optionalCarHistory.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car history not found.");
        }

        carHistory.setDate(updatedCarHistory.getDate());
        
        if(!validateServiceTypeID(carHistory.getServiceTypeID()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Service Type ID.");
        carHistory.setServiceTypeID(updatedCarHistory.getServiceTypeID());
        
        if(!validateUserID(carHistory.getUserID()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong User ID.");
        carHistory.setUserID(updatedCarHistory.getUserID());
        
//    	if(!validateCarID(carHistory.getCarID()))
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Car ID.");
        carHistory.setCarID(updatedCarHistory.getCarID());
        
        carHistoryRepository.update(carHistory);

        return ResponseEntity.status(HttpStatus.OK).body("Success.");
    }

    @PatchMapping("/update/{serviceID}")
    public ResponseEntity<String> partiallyUpdateCarHistory(@PathVariable("serviceID") int serviceID,
                                                           @RequestBody CarHistory updatedCarHistory) 
    {
        Optional<CarHistory> optionalCarHistory = carHistoryRepository.getById(serviceID);
        CarHistory carHistory;

        if (optionalCarHistory.isPresent()) {
            carHistory = optionalCarHistory.get();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car history not found.");
        }

        if (updatedCarHistory.getDate() != null) 
        {
            carHistory.setDate(updatedCarHistory.getDate());
        }
        if (updatedCarHistory.getServiceTypeID() != 0) 
        {
        	if(!validateServiceTypeID(carHistory.getServiceTypeID()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Service Type ID.");
            carHistory.setServiceTypeID(updatedCarHistory.getServiceTypeID());
        }
        if (updatedCarHistory.getUserID() != 0) 
        {
        	if(!validateUserID(carHistory.getUserID()))
        		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong User ID.");
            carHistory.setUserID(updatedCarHistory.getUserID());
        }
        if (updatedCarHistory.getCarID() != 0) 
        {
//        	if(!validateCarID(carHistory.getCarID()))
//    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong Car ID.");
            carHistory.setCarID(updatedCarHistory.getCarID());
        }

        carHistoryRepository.update(carHistory);

        return ResponseEntity.status(HttpStatus.OK).body("Success.");
    }
    
    @DeleteMapping("/delete/{serviceID}")
    public ResponseEntity<String> deleteCarHistory(@PathVariable("serviceID") int serviceID) 
    {
        Optional<CarHistory> optionalCarHistory = carHistoryRepository.getById(serviceID);

        if (optionalCarHistory.isPresent()) {
            carHistoryRepository.delete(serviceID);
            return ResponseEntity.status(HttpStatus.OK).body("Success.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car history not found.");
        }
    }
	
    private boolean validateUserID(int userID) 
    {
        if(userRepository.getById(userID) == null)
        	return false;
        	
        return true;
    }
    
//    private boolean isInvalidCarID(Long carID) {
//        return carID == null || !carRepository.existsById(carID);
//    }

    private boolean validateServiceTypeID(int serviceTypeID) {
        if(serviceTypeRepository.getById(serviceTypeID) == null)
        	return false;
        return true;
    }
	
}