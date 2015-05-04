package com.fet.carpool.serv.service;

import java.util.List;



import com.fet.carpool.serv.dto.AccountChargeRequestBean;
import com.fet.carpool.serv.dto.AccountDto;

public interface AccountService {

    public AccountDto getAccountById(String accountId);
    public List<AccountDto> getAccountAll();
    public List<AccountDto> queryAccount(AccountDto condition );
        
    public double charge(AccountChargeRequestBean chargeRequest);
    
    public double recharge(String accountId, double amount, String itemInfo);
    public double rechargeByCardNoOut(String accountId, String cardNoOut ) throws Exception;
    
    public void updateBalance(String accountId, double amount);
    public void updateAccountName(String accountId, String accountName, String accountPic);
    
    public AccountDto createAccount( String accountId, String accountType, String accountName , String accountPic);
    public void updateRegistrationId( String accountId, String clientType, String registrationId );
}
