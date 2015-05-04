package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.persistence.ChatLog;

public interface ChatLogDao {

	public void addChatLog( ChatLog chatLog );
	
	/**
	 * 
	 * @param accountId
	 * @param friendId
	 * @param timestamp 
	 * @param timestampEnd
	 * @param start 0 - ???
	 * @param limit 1 - 500
	 * @return
	 */
	public List<FriendChatDto> getChatLogByAccountId( String accountId, String friendId, long timestamp,
			long timestampEnd, int start, int limit );
	
	public List<FriendChatDto> getLastRelatedMsgByAccountId( String accountId );
	public int getCountAllNoReadChat(String accountId);
	public int getCountNoReadChat(String accountId,String friendId);
	public boolean updateRead(String accountId,String friendId);
}
