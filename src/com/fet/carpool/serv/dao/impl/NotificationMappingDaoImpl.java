package com.fet.carpool.serv.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.persistence.NotificationMapping;

@Repository("notificationMappingDao")
public class NotificationMappingDaoImpl extends BaseDao implements
		NotificationMappingDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationMapping> getByAccount(String accountId) {
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria( NotificationMapping.class );
		crit.add(Restrictions.eq("accountId", accountId));
		crit.add( Restrictions.eq("enabledStr", "Y"));
		//crit.addOrder(Order.desc("seq_no"));
		crit.setFirstResult(0);
		crit.setMaxResults(1);
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isRegistrationIdEnabled(String accountId, String registrationId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria( NotificationMapping.class );
		crit.add(Restrictions.eq("accountId", accountId));
		//crit.add(Restrictions.eq("registrationId", registrationId));
		crit.add( Restrictions.eq("enabledStr", "Y"));
		List<NotificationMapping> list = crit.list();
		if( list == null || list.size() == 0 )
			return false;
		return true;
	}

	@Override
	public void updateNotificationMapping( NotificationMapping notificationMapping) {
		
		sessionFactory.getCurrentSession().update(notificationMapping);
	}

	@Override
	public void insertNotificationMapping( NotificationMapping notificationMapping) {
		
		sessionFactory.getCurrentSession().save(notificationMapping);
	}

	
}
