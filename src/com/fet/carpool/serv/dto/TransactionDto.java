package com.fet.carpool.serv.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TransactionDto {

    private int transNo;
    private String accountId;
    private String transType;
    private double amount;
    private String itemInfo;
    private String invoiceNo;
    private Date transTime;
    public int getTransNo() {
        return transNo;
    }
    public void setTransNo(int transNo) {
        this.transNo = transNo;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getTransType() {
        return transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getItemInfo() {
		return itemInfo;
	}
	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
	}
	public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
}
