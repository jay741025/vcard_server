package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AccountRegistrationBean {

	private String accountId;
	private String accountType;
	private String accountName;
	private String msisdn;
	private String sim;
	private String accountPic;
	
	private String regClientType;
	
	private String regClientId;
	
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
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public String getRegClientType() {
		return regClientType;
	}
	public void setRegClientType(String regClientType) {
		this.regClientType = regClientType;
	}
	public String getRegClientId() {
		return regClientId;
	}
	public void setRegClientId(String regClientId) {
		this.regClientId = regClientId;
	}
	
	
}
