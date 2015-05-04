package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fet.carpool.serv.persistence.Account;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AccountDto {

    private String accountId;
    private String accountName;
    private String accountType;
    private double balance;
    private String accountPic;
    
    
    public String getAccountPic() {
		return accountPic;
	}

	public void setAccountPic(String accountPic) {
		this.accountPic = accountPic;
	}

	public AccountDto() {
		super();
	}
    
    public AccountDto( Account account ) {
    	if( account != null ) {
            setAccountId(account.getAccountId());
            setAccountName(account.getAccountName());
            setBalance(account.getBalance());
            setAccountType(account.getAccountType());
            setAccountPic(account.getAccountPic());
    	}
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
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
    
}
