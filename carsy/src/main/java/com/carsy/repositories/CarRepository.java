package com.carsy.repositories;

import com.carsy.entities.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<Car>> getAll() {
        try {
            List<Car> cars = jdbcTemplate.query("SELECT CarID, Brand, Model, Year, VIN FROM Car",
                    BeanPropertyRowMapper.newInstance(Car.class));
            return Optional.ofNullable(cars);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Car> getById(int carID) {
        try {
            Car car = jdbcTemplate.queryForObject("SELECT * FROM Car WHERE CarID = ?",
                    BeanPropertyRowMapper.newInstance(Car.class), carID);
            return Optional.ofNullable(car);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(Car car) {
        return jdbcTemplate.update("INSERT INTO Car(Brand, Model, Year, VIN) VALUES(?, ?, ?, ?)",
                car.getBrand(), car.getModel(), car.getYear(), car.getVin());
    }

    public int update(Car car) {
        return jdbcTemplate.update("UPDATE Car SET Brand=?, Model=?, Year=?, VIN=? WHERE CarID=?",
                car.getBrand(), car.getModel(), car.getYear(), car.getVin(), car.getCarID());
    }

    public int delete(int carID) {
        return jdbcTemplate.update("DELETE FROM Car WHERE CarID=?", carID);
    }
    
    public boolean isVinUnique(String vin) {
        String sql = "SELECT COUNT(*) FROM Car WHERE VIN = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, vin);
        return count == 0;
    }
}