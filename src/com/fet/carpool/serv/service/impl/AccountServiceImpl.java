package com.fet.carpool.serv.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.AccountDao;
import com.fet.carpool.serv.dao.CardInfoDao;
import com.fet.carpool.serv.dao.FriendDao;
import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.dao.TransactionDao;
import com.fet.carpool.serv.dto.AccountChargeRequestBean;
import com.fet.carpool.serv.dto.AccountDto;
import com.fet.carpool.serv.dto.AccountRelationDto;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.persistence.Account;
import com.fet.carpool.serv.persistence.NotificationMapping;
import com.fet.carpool.serv.persistence.Transaction;
import com.fet.carpool.serv.service.AccountService;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.util.DesUtil;
import com.fet.carpool.serv.util.StringUtil;

@Transactional
@Service("accountService")
public class AccountServiceImpl extends BaseService implements AccountService {

	public static final int MAX_BALANCE_AMOUNT = 10000;
	public static final int MIN_BALANCE_AMOUNT = 0;
	
    @Autowired
    private TransactionDao transDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private CardInfoDao cardInfoDao;
    @Autowired
    private NotificationMappingDao notificationMappingDao;
    @Autowired
    private FriendDao friendDao;
    
    @Override
    public AccountDto getAccountById(String accountId) {
        
        Account account = accountDao.getAccountById(accountId);
        if (account != null) 
            return new AccountDto(account);
        
        return null;
    }
    
    @Override
    public List<AccountDto> getAccountAll() {
        
    	List<Account> accountList = accountDao.getAccountAll();
    	if( accountList == null )
        	return null;
        
        // 
        List<AccountDto> resultList = new ArrayList<AccountDto>();
        boolean checkRelation = false;
        String fromAccountId = null;
        
        
        for( Account a : accountList ) {
        	
        	if( !checkRelation ) {
        		resultList.add( new AccountDto(a) );
        		continue;
        	}
        	
        	AccountRelationDto accountRelation = new AccountRelationDto(a);
        	accountRelation.setFromAccountId(fromAccountId);
        	accountRelation.setFriend( friendDao.friendExists(fromAccountId, a.getAccountId()));
        	resultList.add(accountRelation);
        }
        	
        return resultList;
        
       
    }

    public List<AccountDto> queryAccount(AccountDto condition ) {
        
        Account account = new Account();
        account.setAccountId(condition.getAccountId());
        account.setAccountName(condition.getAccountName());
        account.setAccountType(condition.getAccountType());
        List<Account> accountList = accountDao.queryAccount(account);
        if( accountList == null )
        	return null;
        
        // 
        List<AccountDto> resultList = new ArrayList<AccountDto>();
        boolean checkRelation = false;
        String fromAccountId = null;
        if( condition instanceof AccountRelationDto ) {
        	checkRelation = true;
        	fromAccountId = ( (AccountRelationDto) condition).getFromAccountId(); 
        }
        
        for( Account a : accountList ) {
        	
        	if( !checkRelation ) {
        		resultList.add( new AccountDto(a) );
        		continue;
        	}
        	
        	AccountRelationDto accountRelation = new AccountRelationDto(a);
        	accountRelation.setFromAccountId(fromAccountId);
        	accountRelation.setFriend( friendDao.friendExists(fromAccountId, a.getAccountId()));
        	resultList.add(accountRelation);
        }
        	
        return resultList;
    }

    
    
    @Override
    public double charge(AccountChargeRequestBean chargeRequest) {
        
        Account account = accountDao.getAccountById(chargeRequest.getAccountId());
        if (account == null) {
            throw new Exception("Account not found: " + chargeRequest.getAccountId());
        }
        
        if( MIN_BALANCE_AMOUNT > account.getBalance() - chargeRequest.getAmount() )
        	throw new Exception( "餘額不足, 目前餘額為 " + account.getBalance() );
        
        Transaction tran = new Transaction();
        tran.setAccountId(chargeRequest.getAccountId());
        tran.setTransType("charge");
        tran.setInvoiceNo(null);
        tran.setAmount(chargeRequest.getAmount());
        tran.setItemInfo(chargeRequest.getItemInfo());
        tran.setTransTime(new Date());

        transDao.saveTransaction(tran);
        
        double newBalance = account.getBalance() - chargeRequest.getAmount();
        account.setBalance(newBalance);
        account.setSysUpdateDate(new Date());
        accountDao.updateAccount(account);
        
        // send notification
        sendTransactionNotification( tran );
        
        return newBalance;
    }

    // 本區段與使用儲值卡來加值相關 -------------------------------------------
    @Override
    public double recharge(String accountId, double amount, String itemInfo) {
        
        Account account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new Exception("Account not found: " + accountId);
        }
        
        Transaction tran = new Transaction();
        tran.setAccountId(accountId);
        tran.setTransType("recharge");
        tran.setInvoiceNo(null);
        tran.setAmount(amount);
        tran.setItemInfo(itemInfo);
        tran.setTransTime(new Date());

        transDao.saveTransaction(tran);
        
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        account.setSysUpdateDate(new Date());
        accountDao.updateAccount(account);
        
        // send notification
        sendTransactionNotification( tran );
        
        return newBalance;
    }
    
	@Override
	public double rechargeByCardNoOut(String accountId, String cardNoOut) throws Exception {
		
		// decode cardNo
		String[] codeArray = decodeCardNo( cardNoOut );
		String accCode = codeArray[0];
//		String seqCode = codeArray[1];
		String cardNoIn = codeArray[2];
		
		// accCode must exist in specific table
		
		// check card exist and status
//		CardInfoDto cardInfo = cardInfoDao.queryBySeqNo(accCode, seqCode);
		CardInfoDto cardInfo = cardInfoDao.queryByCardNoIn(accCode, cardNoIn);
		if( cardInfo == null )
//			throw new VCardException( "card seqNo does not exist" );
			throw new Exception( "card no does not exist" );
		
		if( !cardInfo.isStatusActive() )
			throw new Exception( "this card can not be used to recharge" );
		
		// check account 
        Account account = accountDao.getAccountById(accountId);
        if (account == null) 
            throw new Exception("Account not found: " + accountId);

        // check balance
        if( MAX_BALANCE_AMOUNT <  account.getBalance() + cardInfo.getAmount() )
        	throw new Exception("加值上限為 " + MAX_BALANCE_AMOUNT );
        
		// update card status to used
        cardInfo.setStatusToUsed();
        cardInfoDao.updateCardStatus(cardInfo);
        
		// add balance
        double newBalance = account.getBalance() + cardInfo.getAmount();
        account.setBalance(newBalance);
        account.setSysUpdateDate(new Date());
        accountDao.updateAccount(account);
        
		// add transaction log
        Transaction tran = new Transaction();
        tran.setAccountId(accountId);
        tran.setTransType("recharge");
        tran.setInvoiceNo(null);
        tran.setItemInfo("rechargeByCardNo");
        tran.setAmount(cardInfo.getAmount());
        tran.setTransTime(new Date());
        transDao.saveTransaction(tran);
        
        // send notification
        sendTransactionNotification( tran );
        
		return newBalance;
	}
	
	/**
	 * 
	 * @param cardNo A string array which has accCode, seqCode and internal_card_no after decode
	 * @return
	 * @throws Exception 
	 */
	protected String[] decodeCardNo( String cardNo ) throws Exception, Exception {
		
		if( cardNo == null )
			throw new Exception( "empty cardNo" );
		
		if( cardNo.length() != 20 )
			throw new Exception( "invalid cardNo : length" );
		
		char[] c = cardNo.toUpperCase().toCharArray();
		for( int i = 0 ; i < c.length ; i++ ) {
			if( ( c[i] >= '0' && c[i] <= '9' ) ||( c[i] >= 'A' && c[i] <= 'F' ) )
				continue;
			throw new Exception("invalid cardNo : format" );
		}
		
		String encryptedCardSeq = new String( c, 0, 16 );
		String inputCheckCode = new String( c, 16, 4 );
		
		// 1. decrypt data
		byte[] encryptedData = StringUtil.toBytes(encryptedCardSeq);
		byte[] decryptedData = DesUtil.Des3DecodeCBC(SERIALNO_OUT_KEY, SERIALNO_OUT_IV, encryptedData );
		String decruptedStr = StringUtil.toHexString(decryptedData);
		logger.debug( "decode from " + cardNo + " to " + decruptedStr );
		
		// 2. calculate checkCode
		byte[] checkCodeData = DesUtil.Des3EncodeCBC(SERIALNO_MAC_KEY, SERIALNO_MAC_IV, decryptedData);
		String checkCodeString = StringUtil.toHexString(checkCodeData);
		String calcCheckCode = checkCodeString.substring( checkCodeString.length() - 4 );
		if( !calcCheckCode.equals(inputCheckCode ) ) 
			throw new Exception("invalid checkCode" );
		
		// 3. get cardNoIn
		byte[] inData = DesUtil.Des3EncodeCBC(SERIALNO_IN_KEY, SERIALNO_IN_IV, decryptedData);
		String cardNoIn = StringUtil.toHexString(inData);
		
		// 4. get target number
		String[] result = new String[3];
		result[0] = decruptedStr.substring(4, 10);
		result[1] = decruptedStr.substring(10);
		result[2] = cardNoIn;
		
		return result;
	}
	
	// end of 本區段與使用儲值卡來加值相關 -------------------------------------------
	
	protected void sendTransactionNotification( Transaction trans ) {
		
		if( trans == null )
			return;
		
		String msg = null;
		if( "recharge".equals( trans.getTransType() ) ) {
			msg = "您已於 " + NOTIFICATION_DATE_FORMAT.format( trans.getTransTime() )
					+ " 在 IT-MCS Testing 儲值 " + trans.getAmount() + " 元至帳戶 "
					+ trans.getAccountId() + ", 若有問題請來電 02-99999999";
		} else if( "charge".equals( trans.getTransType() ) ) {
			msg = "您已於 " + NOTIFICATION_DATE_FORMAT.format( trans.getTransTime() )
					+ " 以帳戶 " + trans.getAccountId() 
					+ " 在 IT-MCS Testing 消費 " + trans.getAmount() + " 元, 若有問題請來電 02-99999999";
		} else	// undefined
			return;
		
		// get regId
		List<NotificationMapping> regList = notificationMappingDao.getByAccount(trans.getAccountId());
		JSONArray regIds = new JSONArray();
		for( NotificationMapping regDef : regList ) {
			if( "android".equalsIgnoreCase(regDef.getClientType()) )
				regIds.put(regDef.getRegistrationId());
		}
		
		if( regIds.length() == 0 )
			return;
		
		// set message data
		try {
			JSONObject data = new JSONObject();
			data.put("message", URLEncoder.encode(msg,GCM_NOTIFICATION_CHARSET) );
			data.put("timestamp", NOTIFICATION_DATE_FORMAT.format(trans.getTransTime()) );
			data.put("messageType", trans.getTransType());
			data.put("accountId", trans.getAccountId());
			
			JSONObject json = new JSONObject();
			json.put("data", data);
			json.put("registration_ids", regIds);
			String jsonString = json.toString();
			if( logger.isDebugEnabled() ) 
				logger.debug("gcm request body = " + jsonString);
			
			// send to ...
			logger.info( "send notification to " + trans.getAccountId() );
			sendDelayNotificationToGcm( jsonString, 1 );
			
		} catch (Exception e) {
			logger.error( "send notification failed", e );
		}
	}
	
	
    @Override
    public void updateBalance(String accountId, double amount) {
        Account account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new Exception("Account not found: " + accountId);
        }
        double newBalance = amount;
        account.setBalance(newBalance);
        account.setSysUpdateDate(new Date());
        accountDao.updateAccount(account);
        
    }
    
    @Override
    public void updateAccountName(String accountId, String accountName, String accountPic) {
        Account account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new Exception("Account not found: " + accountId);
        }
        account.setAccountName(accountName);
        account.setAccountPic(accountPic);
        account.setSysUpdateDate(new Date());
        accountDao.updateAccount(account);
    }

    public AccountDto createAccount( String accountId, String accountType, String accountName, String accountPic ) {
    	
    	Account account = new Account();
    	account.setAccountType(accountType);
    	account.setAccountId(accountId);
    	account.setAccountName(accountName);
    	account.setPassword(String.valueOf("111111"));
    	account.setStatus(Account.STATUS_ACVICE);
    	account.setBalance(0);
    	account.setAccountPic(accountPic);
    	account.setSysCreationDate(new Date());
    	account.setSysUpdateDate( account.getSysCreationDate() );
    	accountDao.addNewAccount(account);
    	return new AccountDto(account);
    	
    }
    
//    protected AccountDto copyAttribute(Account account) {
//    	if( account == null )
//    		return null;
//        AccountDto dto = new AccountDto();
//        dto.setAccountId(account.getAccountId());
//        dto.setAccountName(account.getAccountName());
//        dto.setBalance(account.getBalance());
//        dto.setAccountType(account.getAccountType());
//        return dto;
//    }

	@Override
	public void updateRegistrationId(String accountId, String clientType,
			String registrationId) {
		
		if( accountId == null )
			throw new Exception( "Invalid account id");
		
		if( registrationId == null )
			return; 	// dp nothing
		
		boolean regIdExist = notificationMappingDao.isRegistrationIdEnabled(accountId, registrationId);
		if( !regIdExist ) {
			
			NotificationMapping notificationMapping = new NotificationMapping();
			notificationMapping.setAccountId(accountId);
			notificationMapping.setClientType(clientType);
			notificationMapping.setRegistrationId(registrationId);
			notificationMapping.setEnabledStr("Y");
			notificationMapping.setUpdateDatetime(new Date());
			notificationMappingDao.insertNotificationMapping(notificationMapping);
		}else{			
			
			List<NotificationMapping> notificationList =notificationMappingDao.getByAccount(accountId);
			NotificationMapping notification =notificationList.get(0);
			notification.setRegistrationId(registrationId);
			notification.setEnabledStr("Y");
			notification.setUpdateDatetime(new Date());
			notificationMappingDao.updateNotificationMapping(notification);
		}
	}


}
