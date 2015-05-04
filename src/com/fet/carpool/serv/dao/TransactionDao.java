package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.persistence.Transaction;

public interface TransactionDao {

    public List<Transaction> listByAccountId(String accountId);
    
    public void saveTransaction(Transaction trans);
}
