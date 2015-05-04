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
import com.fet.carpool.serv.dao.EventDao;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.persistence.Event;



@Repository("eventDao")
public class EventDaoImpl extends BaseDao implements EventDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> list(String id) {
    	String sql = "SELECT * FROM LINE_EVENT" 
				+ " WHERE id > :id";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);		
		query.setString("id", id);
		query.setResultTransformer(Transformers.aliasToBean(Event.class));
		
		List<Event> list = query.list();
		if( list == null || list.size() == 0 )
			return null;
		
		return list;
    }

    @Override
    public void saveEvent(Event event) {
        sessionFactory.getCurrentSession().saveOrUpdate(event);
    }

}
