package com.fet.carpool.serv.service;

import java.util.List;

import com.fet.carpool.serv.dto.GroupChatDto;
import com.fet.carpool.serv.dto.GroupDto;

public interface GroupService {

	public void createGroup( GroupDto group );
	public void updateGroup( GroupDto group );
	public void deleteGroup( int groupId );
	public List<GroupDto> getGroup( int groupId, String accountId);
	
	public void insertGroupChat( GroupChatDto groupChat );
	public List<GroupChatDto> getGroupChat( GroupChatDto groupChat, int start, int limit );
	public List<GroupChatDto> getLastMsgByAccountId( String accountId);
	public List<GroupChatDto> getLastMsgByGroupId( int GroupId);
	public int getGroupNotReadCount( String accountId ,int groupId);
	public boolean updateRead( String accountId ,int groupId);
	public boolean updateGroupRead(int groupId);
	public int getCountAllNoReadChat( String accountId );
}
