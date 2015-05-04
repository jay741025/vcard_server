package com.fet.carpool.serv.service;

import java.util.HashMap;
import java.util.List;


import com.fet.carpool.serv.dto.SendingMessageDto;
import com.fet.carpool.serv.persistence.Event;

public interface EventService {

    public List<Event> list(String id );
    public void saveEvent(String result);
    public HashMap<String, Object> sendingMessage(SendingMessageDto sendingMessage);
}
