package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.persistence.MessageLog;

public interface MessageLogDao {

	public void addMessageLog( MessageLog messageLog );
	
	public void updateMessageLog( MessageLog messageLog );
	public void updateMessageLogAlreadyRead( MessageLog messageLog );
	
	public List<MessageLog> queryMessage( String accountId, String messageType, 
			long timestamp, long timestampEnd, int start, int limit );
	
	public MessageLog getFirstNotReadMessage( String accountId, String messageType );
}
