package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.persistence.Friend;

public interface FriendDao {

	/**
	 * 
	 * @param friend
	 * @return true if add friend successfully, false if friend already exists
	 */
	public boolean addFriend( Friend friend );
	public void deletefriend( String accountId , String friendId );

	public boolean friendExists( String accountId, String friendId );
	
	public List<FriendChatDto> listFriend( String accountId );
}
