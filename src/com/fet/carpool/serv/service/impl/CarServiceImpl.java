package com.fet.carpool.serv.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.CarDao;
import com.fet.carpool.serv.dao.EventDao;
import com.fet.carpool.serv.dto.CarDto;
import com.fet.carpool.serv.dto.CarNearInfoDto;
import com.fet.carpool.serv.dto.SendingMessageDto;
import com.fet.carpool.serv.persistence.Car;
import com.fet.carpool.serv.persistence.Event;
import com.fet.carpool.serv.service.CarService;
import com.fet.carpool.serv.service.EventService;


@Transactional
@Service("carService")
public class CarServiceImpl implements CarService {

    @Autowired
    private CarDao carDao;    


	@Override
	public List<Car> list() {		
		return carDao.list();
	}

	@Override
	public void setCarInfo(CarDto carDto) {	
		
		Car heaveCar = carDao.findCarByAccountId(carDto.getAccountId());	
		
		if(heaveCar != null ){		
			heaveCar.setAccountName(carDto.getAccountName());
			heaveCar.setLatitude(carDto.getLatitude());
			heaveCar.setAccountPic(carDto.getAccountPic());
			heaveCar.setLongitude(carDto.getLongitude());
			heaveCar.setTransport(carDto.getTransport());
			heaveCar.setStatus(carDto.getStatus());
			heaveCar.setDatetime(new Date());		
			carDao.updateCarInfo(heaveCar);			
		}else{			
			Car car = new Car();
			car.setAccountId(carDto.getAccountId());
			car.setAccountName(carDto.getAccountName());
			car.setAccountPic(carDto.getAccountPic());
			car.setLatitude(carDto.getLatitude());
			car.setLongitude(carDto.getLongitude());
			car.setStatus(carDto.getStatus());
			car.setTransport(carDto.getTransport());
			car.setDatetime(new Date());
			carDao.addCarInfo(car);
		}
		
		
	}

	@Override
	public List<Car> getNearCar(CarNearInfoDto carNearInfo) {
		List<Car> carListDb =carDao.list() ;
		List<Car> carList = new ArrayList<Car>();
		for(Car car :carListDb){
			if(!car.getAccountId().equals(carNearInfo.getAccountId())  && car.getStatus() > 0 ){
				double d =Math.sqrt(Math.pow(Double.parseDouble(carNearInfo.getLatitude()) - Double.parseDouble(car.getLatitude()), 2) 
						+ Math.pow(Double.parseDouble(carNearInfo.getLongitude()) - Double.parseDouble(carNearInfo.getLongitude()), 2));
				if(d<=Double.parseDouble(carNearInfo.getDistance())){
					carList.add(car);
				}
			}
		}
		return carList ;
		
	}
    
    

}
