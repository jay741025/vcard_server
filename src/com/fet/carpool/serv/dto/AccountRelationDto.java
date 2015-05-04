package com.fet.carpool.serv.dto;

import com.fet.carpool.serv.persistence.Account;

public class AccountRelationDto extends AccountDto {

	private String fromAccountId;
	private boolean isFriend;

	public AccountRelationDto() {
		super();
	}
	public AccountRelationDto(Account account) {
		super(account);
	}

	
	public String getFromAccountId() {
		return fromAccountId;
	}
	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	public boolean isFriend() {
		return isFriend;
	}
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	
	
}
