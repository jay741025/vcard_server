package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AccountRechargeRequestBean {

    private String accountId;
    private String vcardSeqNo;
    private double amount;
    private String itemInfo;
    
    public String getItemInfo() {
		return itemInfo;
	}
	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
	}
	public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public double getAmount() {
        return amount;
    }
    public void setBalance(double amount) {
        this.amount = amount;
    }
	public String getVcardSeqNo() {
		return vcardSeqNo;
	}
	public void setVcardSeqNo(String vcardSeqNo) {
		this.vcardSeqNo = vcardSeqNo;
	}
    
    
}
