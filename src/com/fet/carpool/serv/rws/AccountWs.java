package com.fet.carpool.serv.rws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.dto.AccountBalanceBean;
import com.fet.carpool.serv.dto.AccountChargeRequestBean;
import com.fet.carpool.serv.dto.AccountDto;
import com.fet.carpool.serv.dto.AccountRechargeRequestBean;
import com.fet.carpool.serv.dto.AccountRegistrationBean;
import com.fet.carpool.serv.dto.AccountRelationDto;
import com.fet.carpool.serv.dto.QueryResultBean;
import com.fet.carpool.serv.persistence.Account;
import com.fet.carpool.serv.service.AccountService;

@Component("accountWs")
@Path("account")
public class AccountWs extends MyVcardWs {

    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpServletRequest httpServletRequest;

    @Autowired
    private AccountService accountService;

    @GET
    @Path("list")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestString() {
        return "class : " + getClass().getName() + " - works";
    }

    @POST
    @Path("createIfNotFound")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto createIfNotFound() {
        return null;
    }
  
    @POST
    @Path("createSimAccountIfNotFound")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean createSimAccountIfNotFound( AccountRegistrationBean inputAccount ) {
    	
    	if( inputAccount == null || inputAccount.getAccountId() == null || inputAccount.getAccountId().length() < 12 )
    		return false;
    	
    	AccountDto respAccount = accountService.getAccountById(inputAccount.getAccountId());
    	if( respAccount == null ) { 
	    	respAccount = accountService.createAccount( inputAccount.getAccountId(), 
	    			Account.ACCOUNT_TYPE_SIM, inputAccount.getAccountName() ,inputAccount.getAccountPic());
	    	if( respAccount == null )	// failed
	    		return false;
    	} else if( inputAccount.getAccountName() != null ) {
    		// if exists, try to update accountName
    		accountService.updateAccountName(inputAccount.getAccountId(), inputAccount.getAccountName(), inputAccount.getAccountPic());
    	}
    	
    	// add regId
    	if( inputAccount.getRegClientId() != null )
    		accountService.updateRegistrationId(inputAccount.getAccountId(), 
    					inputAccount.getRegClientType(), 
    					inputAccount.getRegClientId() );
    	
    	return true;
    }
    
    @POST
    @Path("createMsisdnAccountIfNotFound")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean createMsisdnAccountIfNotFound( AccountRegistrationBean inputAccount ) {
    	
    	if( inputAccount == null || inputAccount.getAccountId() == null || inputAccount.getAccountId().length() != 10 )
    		return false;
    	
    	AccountDto respAccount = accountService.getAccountById(inputAccount.getAccountId());
    	if( respAccount != null ) 
    		return true;
    	
    	respAccount = accountService.createAccount( inputAccount.getAccountId(), 
    			Account.ACCOUNT_TYPE_MSISDN, inputAccount.getAccountName(),inputAccount.getAccountPic() );
    	if( respAccount == null )	// failed
    		return false;
    	
    	if( inputAccount.getRegClientId() != null )
    		accountService.updateRegistrationId(inputAccount.getAccountId(), 
    					inputAccount.getRegClientType(), 
    					inputAccount.getRegClientId() );
    	
    	return true;
    }
    
    @POST
    @Path("createOpenAccountIfNotFound")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean createOpenAccountIfNotFound( AccountRegistrationBean inputAccount ) {
    	
    	if( inputAccount == null || inputAccount.getAccountId() == null )
    		return false;
    	
    	String accountId = inputAccount.getAccountId();
    	int pos = accountId.indexOf('@');
    	if( pos == -1 || pos == 0 || pos == accountId.length() - 1 )
    		return false;
    	
    	AccountDto respAccount = accountService.getAccountById(accountId);
    	if( respAccount != null ) 
    		return true;
    	
    	respAccount = accountService.createAccount( inputAccount.getAccountId(), 
    			Account.ACCOUNT_TYPE_OPENID, inputAccount.getAccountName(),inputAccount.getAccountPic() );
    	if( respAccount == null )	// failed
    		return false;
    	
    	if( inputAccount.getRegClientId() != null )
    		accountService.updateRegistrationId(inputAccount.getAccountId(), 
    					inputAccount.getRegClientType(), 
    					inputAccount.getRegClientId() );
    	
    	return true;
    }
    
    @GET
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto getAccountById(@QueryParam("accountId") String accountId) {
        return accountService.getAccountById(accountId);
    }
    
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDto> getAll() {
        return accountService.getAccountAll();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public QueryResultBean getAccount(AccountRelationDto condition ) {
    	
    	List accountList = accountService.queryAccount(condition); 
    	QueryResultBean result = new QueryResultBean();
    	result.setResultList(accountList);
        return result;
    }
    

    
    @POST
    @Path("charge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountBalanceBean charge(AccountChargeRequestBean chargeRequest) {
        double balance = accountService.charge(chargeRequest);
        AccountBalanceBean response = new AccountBalanceBean();
        response.setAccountId(chargeRequest.getAccountId());
        response.setBalance(balance);
        return response;
    }

    @POST
    @Path("recharge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountBalanceBean recharge(AccountRechargeRequestBean request) {
        double balance = accountService.recharge(request.getAccountId(),
                request.getAmount(),request.getItemInfo());
        AccountBalanceBean response = new AccountBalanceBean();
        response.setAccountId(request.getAccountId());
        response.setBalance(balance);
        return response;
    }
    
    @POST
    @Path("rechargeByCardNo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountBalanceBean rechargeByCardNo(AccountRechargeRequestBean request) throws Exception {
    	
        double balance = accountService.rechargeByCardNoOut( request.getAccountId(), request.getVcardSeqNo() );
        
        AccountBalanceBean response = new AccountBalanceBean();
        response.setAccountId(request.getAccountId());
        response.setBalance(balance);
        return response;
    }
    
    
}
