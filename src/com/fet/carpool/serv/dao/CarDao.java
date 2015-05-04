package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.CarDto;
import com.fet.carpool.serv.persistence.Account;
import com.fet.carpool.serv.persistence.Car;
import com.fet.carpool.serv.persistence.Event;


public interface CarDao {

    public List<Car> list();   
    public Car findCarByAccountId( String accountId);      
    public void updateCarInfo(Car car);    
    public void addCarInfo(Car car);
    
}
