package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.GroupChatDto;
import com.fet.carpool.serv.persistence.GroupChatNotRead;

public interface GroupChatDao {

	public boolean insertGroupChat( GroupChatDto groupChat );
	
	public List<GroupChatDto> getGroupChat( GroupChatDto groupChat, int start, int limit );
	
	public List<GroupChatDto> getLastMsgByAccountId(String accountId);
	public boolean addGroupChatNotRead(String accountId,int groupId);
	public int getGroupNotReadCount(String accountId,int groupId);
	public boolean updateRead(String accountId,int groupId);
	public int getGroupNotReadAllCount(String accountId);
	public List<GroupChatDto> getLastMsgByGroupId(int groupId);
	public boolean updateGroupRead(int groupId) ;
}
