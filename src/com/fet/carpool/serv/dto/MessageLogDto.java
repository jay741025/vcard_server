package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fet.carpool.serv.persistence.MessageLog;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MessageLogDto {

//	private int seqNo;
	private String accountId;
	private String message;
	private String messageType;
//	private String messageFrom;
	private long messageTimeMillis;
	private boolean messageRead;
	private String longitude;
	private String latitude;
	
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public MessageLogDto() {
		super();
	}

	public MessageLogDto( MessageLog messageLog ) {
		super();
		
		if( messageLog != null ) {
			setAccountId(messageLog.getAccountId());
			setMessage(messageLog.getMessage());
			setMessageTimeMillis( messageLog.getMessageTimeMillis() );
			setMessageRead(messageLog.isMessageRead());
			setMessageType(messageLog.getMessageType());
			setLatitude(messageLog.getLatitude());
			setLongitude(messageLog.getLongitude());
		}
	}

	//	public int getSeqNo() {
//		return seqNo;
//	}
//	public void setSeqNo(int seqNo) {
//		this.seqNo = seqNo;
//	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
//	public String getMessageFrom() {
//		return messageFrom;
//	}
//	public void setMessageFrom(String messageFrom) {
//		this.messageFrom = messageFrom;
//	}
	public long getMessageTimeMillis() {
		return messageTimeMillis;
	}
	public void setMessageTimeMillis(long messageTimeMillis) {
		this.messageTimeMillis = messageTimeMillis;
	}
	public boolean isMessageRead() {
		return messageRead;
	}
	public void setMessageRead(boolean messageRead) {
		this.messageRead = messageRead;
	}
	
	
	
	
}
