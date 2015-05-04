package com.fet.carpool.serv.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.CarDao;
import com.fet.carpool.serv.dao.CarEventDao;
import com.fet.carpool.serv.dao.EventDao;
import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.dto.CarDto;
import com.fet.carpool.serv.dto.CarEventDto;
import com.fet.carpool.serv.dto.SendingMessageDto;
import com.fet.carpool.serv.persistence.Car;
import com.fet.carpool.serv.persistence.CarEvent;
import com.fet.carpool.serv.persistence.Event;
import com.fet.carpool.serv.persistence.NotificationMapping;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.CarEventService;
import com.fet.carpool.serv.service.CarService;
import com.fet.carpool.serv.service.EventService;


@Transactional
@Service("carEventService")
public class CarEventServiceImpl extends BaseService implements CarEventService {

    @Autowired
    private CarEventDao carEventDao;

    @Autowired
	private NotificationMappingDao notificationMappingDao;
    
    protected Logger logger;

	@Override
	public List<CarEvent> list() {		
		return carEventDao.list();
	}

	@Override
	public void setCarEvent(CarEventDto carDto) {	
		
		CarEvent car = new CarEvent();
		car.setAccountId(carDto.getAccountId());
		car.setAccountName(carDto.getAccountName());
		car.setAccountPic(carDto.getAccountPic());
		car.setEventType(carDto.getEventType());
		car.setMessage(carDto.getMessage());	
		car.setToId(carDto.getToId());
		car.setToName(carDto.getToName());
		car.setDatetime(new Date());	
		carEventDao.addCarEvent(car);	
		
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String msg;
		try {
			msg = ow.writeValueAsString(car);
			sendDelayNotification(msg,
					"CarEvent", notificationMappingDao.getByAccount(carDto.getToId()),
					1) ;
			
		} catch (JsonGenerationException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
				
		
		
	}

	
    
    

}
