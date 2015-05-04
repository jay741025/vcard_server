package com.fet.carpool.serv.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CAR_INFO")
public class Car {
	
	private int id ;
	private String accountId;
	private String accountName;
	private String accountPic;
	private String latitude;
	private String longitude;
	private int transport ;
	private int status ;
    private Date datetime;
    
    
    @Id
	@Column(name="Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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

	@Column(name = "Latitude", nullable = false)
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "Longitude", nullable = false)
	public String getLongitude() {
		return longitude;
	}
	
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@Column(name = "Transport", nullable = false)
	public int getTransport() {
		return transport;
	}
	public void setTransport(int transport) {
		this.transport = transport;
	}
	
	@Column(name = "Status", nullable = false)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name = "Datetime", nullable = false)
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
    
    
  
}
