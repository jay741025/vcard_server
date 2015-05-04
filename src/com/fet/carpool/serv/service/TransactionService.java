package com.fet.carpool.serv.service;

import java.util.List;

import com.fet.carpool.serv.dto.TransactionDto;

public interface TransactionService {

    public List<TransactionDto> listByAccountId(String accountId);

    public void saveTransaction(String accountId, String transType,
            String invoiceNo, double amount);
}
