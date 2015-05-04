package com.fet.carpool.serv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.AccountDao;
import com.fet.carpool.serv.dao.ChatLogDao;
import com.fet.carpool.serv.dao.FriendDao;
import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.persistence.Account;
import com.fet.carpool.serv.persistence.ChatLog;
import com.fet.carpool.serv.persistence.Friend;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.ChatService;

@Transactional
@Service("chatService")
public class ChatServiceImpl extends BaseService implements ChatService {

	@Autowired
	private FriendDao friendDao;
	
	@Autowired
	private ChatLogDao chatLogDao;
	
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private NotificationMappingDao notificationMappingDao;
	
	@Override
	public int getCountAllNoReadChat( String accountId ){		
		int count = chatLogDao.getCountAllNoReadChat( accountId );
		return count;
	}
	@Override
	public int getCountNoReadChat( String accountId ,String friendId){
		int count = chatLogDao.getCountNoReadChat( accountId ,friendId);
		return count;
	}
	@Override
	public boolean AddFriend(Friend friend) {
		
		Account a = accountDao.getAccountById( friend.getAccountId() );
		if( a == null )
			throw new Exception( "account Id does not exist : " + friend.getAccountId() );
		
		Account f = accountDao.getAccountById( friend.getFriendId() );
		if( f == null )
			throw new Exception( "friend Id does not exist : " + friend.getFriendId() );
		
		// notification
		boolean newRecord =  friendDao.addFriend(friend);
		if( newRecord ) {
			String msg = "(" + a.getAccountId() + "/" + a.getAccountName() + ") 已把你加為朋友";
			sendDelayNotification(msg, 
					"addFriend", 
					notificationMappingDao.getByAccount(friend.getFriendId()),
					1);
		}
		
		return newRecord;
	}

	@Override
	public void deleteFriend(String accountId ,String friendId) {
		
		friendDao.deletefriend(accountId ,friendId);
	}
	
	@Override
	public void AddChatLog(ChatLog chatLog) {
		
		Account a = accountDao.getAccountById( chatLog.getAccountId() );
		if( a == null )
			throw new Exception( "account Id does not exist : " + chatLog.getAccountId() );
		
		Account f = accountDao.getAccountById( chatLog.getFriendId() );
		if( f == null )
			throw new Exception( "friend Id does not exist : " + chatLog.getFriendId() );
		
		chatLogDao.addChatLog(chatLog);
		
		// notification
		String msg = "(" + a.getAccountId() + "/" + a.getAccountName() + ") 說『" + chatLog.getMessage() + "』";
		sendDelayNotification(msg, 
				"addChat", 
				notificationMappingDao.getByAccount(chatLog.getFriendId()),
				1);
	}

	@Override
	public List<FriendChatDto> getChatLogByAccountId(String accountId, String friendId, 
			long timestamp, long timestampEnd, int start, int limit ) {
		
		return chatLogDao.getChatLogByAccountId(accountId, friendId, timestamp, timestampEnd, start, limit );
	}

	@Override
	public List<FriendChatDto> getLastRelatedMsgByAccountId( String accountId ) {
		return chatLogDao.getLastRelatedMsgByAccountId(accountId);
	}

	@Override
    public List<FriendChatDto> listFriend( String accountId ) {
    	
    	return friendDao.listFriend(accountId);
    }
	
	@Override
	public boolean updateRead( String accountId ,String friendId) {
    	
    	return chatLogDao.updateRead(accountId,friendId);
    }
}
