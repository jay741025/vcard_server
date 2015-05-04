package com.fet.carpool.serv.service;

import java.util.List;

import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.persistence.ChatLog;
import com.fet.carpool.serv.persistence.Friend;

public interface ChatService {


	/**
	 * 
	 * @param friend
	 * @return true if add friend successfully, false if friend already exists
	 */
	public boolean AddFriend( Friend friend );
	
	public void AddChatLog( ChatLog friend );
	public void deleteFriend( String accountId ,String friendId );
	public List<FriendChatDto> getChatLogByAccountId( String accountId, String friendId, long timestamp, long timestampEnd, int start, int limit );
	
	public List<FriendChatDto> getLastRelatedMsgByAccountId( String accountId );
	
    public List<FriendChatDto> listFriend( String accountId );
    public int getCountAllNoReadChat( String accountId );
    public int getCountNoReadChat( String accountId ,String friendId);
    public boolean updateRead( String accountId ,String friendId);

}
