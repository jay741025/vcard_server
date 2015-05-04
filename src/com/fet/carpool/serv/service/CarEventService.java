package com.fet.carpool.serv.service;

import java.util.HashMap;
import java.util.List;


import com.fet.carpool.serv.dto.CarDto;
import com.fet.carpool.serv.dto.CarEventDto;
import com.fet.carpool.serv.dto.SendingMessageDto;
import com.fet.carpool.serv.persistence.Car;
import com.fet.carpool.serv.persistence.CarEvent;
import com.fet.carpool.serv.persistence.Event;

public interface CarEventService {

    public List<CarEvent> list();
    public void setCarEvent(CarEventDto car);
    
}
