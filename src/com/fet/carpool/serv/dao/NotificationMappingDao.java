package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.persistence.NotificationMapping;

public interface NotificationMappingDao {

	public List<NotificationMapping> getByAccount( String accountId );
	
	public boolean isRegistrationIdEnabled( String accountId, String registrationId );
	
	public void updateNotificationMapping( NotificationMapping notificationMapping );
	
	public void insertNotificationMapping( NotificationMapping notificationMapping );
	
}
