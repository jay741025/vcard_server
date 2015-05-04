package com.fet.carpool.serv.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.persistence.NotificationMapping;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.NotificationService;

@Transactional
@Service("notificationService")
public class NotificationServiceImpl extends BaseService implements NotificationService{

    @Autowired
    private NotificationMappingDao notificationMappingDao;
    
	@Override
	public void sendNotification(String msg, String msgType, String accountId) {
		
		// get regId
		List<NotificationMapping> regList = notificationMappingDao.getByAccount(accountId);
		if( regList == null )
			return;
		
		JSONArray regIds = new JSONArray();
		for( NotificationMapping regDef : regList ) {
			if( "android".equalsIgnoreCase(regDef.getClientType()) )
				regIds.put(regDef.getRegistrationId());
		}
		
		if( regIds.length() == 0 )
			return;
		
		// set message data
		try {
			JSONObject data = new JSONObject();
			data.put("message", URLEncoder.encode(msg,GCM_NOTIFICATION_CHARSET) );
			data.put("timestamp", NOTIFICATION_DATE_FORMAT.format(new Date()) );
			data.put("messageType", msgType);
			data.put("accountId", accountId);
			
			JSONObject json = new JSONObject();
			json.put("data", data);
			json.put("registration_ids", regIds);
			String jsonString = json.toString();
			if( logger.isDebugEnabled() ) 
				logger.debug("gcm request body = " + jsonString);
			
			// send to ...
			logger.info( "send notification to " + accountId );
			sendDelayNotificationToGcm( jsonString, 1 );
			
		} catch (Exception e) {
			logger.error( "send notification failed", e );
		}
	}

	
}
