package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.CarDto;
import com.fet.carpool.serv.persistence.Account;
import com.fet.carpool.serv.persistence.Car;
import com.fet.carpool.serv.persistence.CarEvent;
import com.fet.carpool.serv.persistence.Event;


public interface CarEventDao {

    public List<CarEvent> list();   
    public Boolean findCarEventByAccountId( String accountId);      
    public void updateCarEvent(CarEvent carEvent);    
    public void addCarEvent(CarEvent carEvent);
    
}
