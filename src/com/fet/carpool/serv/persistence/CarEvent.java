package com.fet.carpool.serv.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CAR_EVENT")
public class CarEvent {
	
	private int id ;
	private String accountId;
	private String accountName;
	private String accountPic;
	private String toId;
	private String toName;
	private int eventType;
	private String message;	
    private Date datetime;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "Id")
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "AccountId", nullable = false)
	public String getAccountId() {
		return accountId;
	}
	

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	@Column(name = "ToId", nullable = false)
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	@Column(name = "ToName", nullable = false)
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	@Column(name = "EventType", nullable = false)
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	@Column(name = "Message", nullable = false)
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	@Column(name = "AccountName", nullable = false)
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}	
	
	@Column(name = "AccountPic", nullable = false)
	public String getAccountPic() {
		return accountPic;
	}

	public void setAccountPic(String accountPic) {
		this.accountPic = accountPic;
	}

	@Column(name = "Datetime", nullable = false)
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
    
    
  
}
