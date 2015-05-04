package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fet.carpool.serv.persistence.GroupChat;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CarEventDto {

	private String accountId;
	private String accountName;
	private String accountPic;
	private String toId;
	private String toName;
	private int eventType;
	private String message;	
	
	
	public CarEventDto() {
		super();
	}

	
	public String getAccountPic() {
		return accountPic;
	}


	public void setAccountPic(String accountPic) {
		this.accountPic = accountPic;
	}


	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	
	public String getToId() {
		return toId;
	}


	public void setToId(String toId) {
		this.toId = toId;
	}


	public String getToName() {
		return toName;
	}


	public void setToName(String toName) {
		this.toName = toName;
	}	

	public int getEventType() {
		return eventType;
	}


	public void setEventType(int eventType) {
		this.eventType = eventType;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	

	
	
}
