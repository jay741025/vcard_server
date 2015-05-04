package com.fet.carpool.serv.service.impl;

import java.net.URLEncoder;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.MessageLogDao;
import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.persistence.MessageLog;
import com.fet.carpool.serv.persistence.NotificationMapping;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.MessageLogService;

@Transactional
@Service("messageLogService")
public class MessageLogServiceImpl extends BaseService implements
		MessageLogService {

	@Autowired
	private MessageLogDao messageLogDao;
	
	@Autowired
	private NotificationMappingDao notificationMappingDao;

	@Override
	public void addMessageLog(MessageLog messageLog) {

		messageLogDao.addMessageLog(messageLog);
		sendMessageProcessNotification( messageLog );
	}

	@Override
	public void updateMessageLog(MessageLog messageLog) {

		messageLogDao.updateMessageLog(messageLog);
	}

	@Override
	public void updateMessageLogAlreadyRead(MessageLog messageLog) {
		messageLogDao.updateMessageLogAlreadyRead(messageLog);
	}

	@Override
	public List<MessageLog> queryMessage(String accountId,
			String messageType, long timestamp, long timestampEnd, int start, int limit ) {
		return messageLogDao
				.queryMessage(accountId, messageType, timestamp, timestampEnd, start, limit );
	}

	@Override
	public MessageLog getFirstNotReadMessage(String accountId,
			String messageType) {
		return messageLogDao.getFirstNotReadMessage(accountId, messageType);
	}

	
	protected void sendMessageProcessNotification( MessageLog messageLog ) {
		
		if( messageLog == null )
			return;
		
		String msg = null;
		if( MessageLog.MESSAGE_TYPE_REPLY.equals( messageLog.getMessageType() ) ) {
			msg = "IT-MCS 於 " + NOTIFICATION_DATE_FORMAT.format( messageLog.getMessageTime() )
					+ " 回覆訊息給你, 以下為訊息內容 「" + messageLog.getMessage() + "」";
		} else	// not support
			return;
		
		// get regId
		
		logger.info( "messageLog.getAccountId() " + messageLog.getAccountId() );
		List<NotificationMapping> regList = notificationMappingDao.getByAccount(messageLog.getAccountId());
		JSONArray regIds = new JSONArray();
		for( NotificationMapping regDef : regList ) {
			if( "android".equalsIgnoreCase(regDef.getClientType()) ){
				regIds.put(regDef.getRegistrationId());
				logger.info( "regIds " + regDef.getRegistrationId() );
			}
				
		}
		
		if( regIds.length() == 0 )
			return;
		
		// set message data
		try {
			JSONObject data = new JSONObject();
			data.put("message", URLEncoder.encode(msg,GCM_NOTIFICATION_CHARSET) );
			//data.put("message", URLEncoder.encode(messageLog.getMessage(),GCM_NOTIFICATION_CHARSET) );
			data.put("timestamp", NOTIFICATION_DATE_FORMAT.format(messageLog.getMessageTime()) );
			data.put("messageType", messageLog.getMessageType() );
			data.put("accountId", messageLog.getAccountId());
			
			JSONObject json = new JSONObject();
			json.put("data", data);
			json.put("registration_ids", regIds);
			String jsonString = json.toString();
			
			// send to ...
			logger.info( "send notification to " + messageLog.getAccountId() );
//			sendNotificationToGcm( jsonString );
			sendDelayNotificationToGcm(jsonString, 1);
			
		} catch (Exception e) {
			logger.error( "send notification failed", e );
		}
	}
}
