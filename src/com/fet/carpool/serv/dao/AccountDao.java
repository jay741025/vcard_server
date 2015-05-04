package com.fet.carpool.serv.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.persistence.Account;

@Repository("accountDao")
public interface AccountDao {

    public Account getAccountById(String accountId);
    public List<Account> getAccountAll();
    public List<Account> queryAccount( Account condition );
    
    public void updateAccount(Account account);
    
    public void addNewAccount(Account account);
}
