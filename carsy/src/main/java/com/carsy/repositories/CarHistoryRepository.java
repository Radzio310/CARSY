package com.carsy.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entities.CarHistory;


@Repository
public class CarHistoryRepository 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Optional<List<CarHistory>> getAll() {
        try {
            List<CarHistory> carHistories = jdbcTemplate.query(
                    "SELECT serviceID, date, serviceTypeID, userID, carID FROM CarHistory",
                    BeanPropertyRowMapper.newInstance(CarHistory.class));
            return Optional.ofNullable(carHistories);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<CarHistory> getById(int serviceID) {
        try {
            CarHistory carHistory = jdbcTemplate.queryForObject(
                    "SELECT * FROM CarHistory WHERE serviceID = ?",
                    BeanPropertyRowMapper.newInstance(CarHistory.class), serviceID);
            return Optional.ofNullable(carHistory);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(CarHistory carHistory) {
        return jdbcTemplate.update("INSERT INTO CarHistory(date, serviceTypeID, userID, carID) VALUES(?, ?, ?, ?)",
                 carHistory.getDate(), carHistory.getServiceTypeID(), carHistory.getUserID(), carHistory.getCarID());
    }

    public int update(CarHistory carHistory) {
        return jdbcTemplate.update("UPDATE CarHistory SET date=?, serviceTypeID=?, userID=?, carID=? WHERE serviceID=?",
                carHistory.getDate(), carHistory.getServiceTypeID(), carHistory.getUserID(), carHistory.getCarID(), carHistory.getServiceID());
    }

    public int delete(int serviceID) {
        return jdbcTemplate.update("DELETE FROM CarHistory WHERE serviceID=?", serviceID);
    }
		
}