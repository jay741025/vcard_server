package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.persistence.Event;


public interface EventDao {

    public List<Event> list(String id);    
    public void saveEvent(Event event);
}
