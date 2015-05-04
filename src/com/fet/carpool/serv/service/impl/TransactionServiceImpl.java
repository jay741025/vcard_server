package com.fet.carpool.serv.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.TransactionDao;
import com.fet.carpool.serv.dto.TransactionDto;
import com.fet.carpool.serv.persistence.Transaction;
import com.fet.carpool.serv.service.TransactionService;

@Transactional
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao transDao;

    @Override
    public List<TransactionDto> listByAccountId(String accountId) {

        List<Transaction> trans = transDao.listByAccountId(accountId);
        List<TransactionDto> tranDtos = new ArrayList<TransactionDto>(
                trans.size());
        for (Transaction tran : trans) {
            TransactionDto dto = new TransactionDto();
            dto.setTransNo(tran.getTransNo());
            dto.setAccountId(tran.getAccountId());
            dto.setInvoiceNo(tran.getInvoiceNo());
            dto.setTransType(tran.getTransType());
            dto.setAmount(tran.getAmount());
            dto.setItemInfo(tran.getItemInfo());
            dto.setTransTime(tran.getTransTime());
            tranDtos.add(dto);
        }
        return tranDtos;
    }

    @Override
    public void saveTransaction(String accountId, String transType,
            String invoiceNo, double amount) {
        
        Transaction tran = new Transaction();
        tran.setAccountId(accountId);
        tran.setTransType(transType);
        tran.setInvoiceNo(invoiceNo);
        tran.setAmount(amount);
        tran.setTransTime(new Date());

        transDao.saveTransaction(tran);
    }

}
