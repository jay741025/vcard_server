package com.fet.carpool.serv.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.CarDao;
import com.fet.carpool.serv.dao.CarEventDao;
import com.fet.carpool.serv.dao.EventDao;
import com.fet.carpool.serv.dto.CarDto;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.persistence.Car;
import com.fet.carpool.serv.persistence.CarEvent;
import com.fet.carpool.serv.persistence.Event;



@Repository("carEventDao")
public class CarEventDaoImpl extends BaseDao implements CarEventDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public List<CarEvent> list() {
		String sql = "SELECT * FROM CAR_EVENT " ;
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);		
		query.setResultTransformer(Transformers.aliasToBean(CarEvent.class));		
		List<CarEvent> list = query.list();
		if( list == null || list.size() == 0 )
			return null;
		
		return list;
	}

	

	@Override
	public Boolean findCarEventByAccountId(String accountId) {
		String sql = "SELECT * FROM CAR_EVENT WHERE accountId = :accountId" ;
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString("accountId", accountId);
		query.setResultTransformer(Transformers.aliasToBean(CarEvent.class));	
		
		@SuppressWarnings("unchecked")
		List<Car> list = query.list();
		if( list == null || list.size() == 0 )
			return false;
		
		return true;
	}



	@Override
	public void updateCarEvent(CarEvent car) {
		sessionFactory.getCurrentSession().update(car);
	}



	@Override
	public void addCarEvent(CarEvent car) {
		sessionFactory.getCurrentSession().save(car);
	}

    

}
