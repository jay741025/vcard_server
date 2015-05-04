package com.fet.carpool.serv.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TransactionListBean {

    private String accountId;
    private int count;
    
    private List<TransactionDto> transList;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TransactionDto> getTransList() {
        return transList;
    }

    public void setTransList(List<TransactionDto> transList) {
        this.transList = transList;
    }
}
