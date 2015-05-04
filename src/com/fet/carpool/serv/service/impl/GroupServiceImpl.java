package com.fet.carpool.serv.service.impl;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.GroupChatDao;
import com.fet.carpool.serv.dao.GroupDao;
import com.fet.carpool.serv.dao.NotificationMappingDao;
import com.fet.carpool.serv.dto.GroupChatDto;
import com.fet.carpool.serv.dto.GroupDto;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.GroupService;

@Transactional
@Service("groupService")
public class GroupServiceImpl extends BaseService implements GroupService {

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private GroupChatDao groupChatDao;
	
	@Autowired
	private NotificationMappingDao notificationMappingDao;
	
	@Override
	public void createGroup(GroupDto group) {
		
		groupDao.createGroup(group);
	}

	@Override
	public void updateGroup(GroupDto group) {
		
		groupDao.updateGroup(group);
	}

	@Override
	public void deleteGroup(int groupId) {
		
		groupDao.deleteGroup(groupId);
	}

	@Override
	public List<GroupDto> getGroup(int groupId, String accountId) {
		
		return groupDao.getGroup(groupId, accountId);
	}

	@Override
	public void insertGroupChat(GroupChatDto groupChat) {
		
		boolean newRecord = groupChatDao.insertGroupChat(groupChat);
		// notification
		
		if( newRecord ) {
			
			List<GroupDto> groupList = getGroup(groupChat.getGroupId(),null);
			GroupDto group =groupList.get(0);
			JSONArray json = null;
			try {
				json = new JSONArray(group.getMember());
			} catch (JSONException e1) {
				throw new Exception( "insertGroupChat JSONException : " +e1.getMessage() );				
			}
			
			if (json.length() > 0) {
				
				
				for (int i = 0; i < json.length(); i++) {
					
					try {						
						if(!groupChat.getAccountId().equals(json.getString(i))){
							groupChatDao.addGroupChatNotRead(json.getString(i), groupChat.getGroupId());
							String msg = "(" + groupChat.getAccountId() + "/" + groupChat.getAccountName() +
									"/" + groupChat.getGroupId() +"/" 
										+ groupChat.getGroupName() +") ¡y"+groupChat.getMessage()+"¡z";
							
								sendDelayNotification(msg, 
										"groupChat", 
										notificationMappingDao.getByAccount(json.getString(i)),
										1);
							
						}
					} catch (JSONException e) {
						throw new Exception( "insertGroupChat JSONException : " +e.getMessage() );
					}
				}
			}
		}
		
	}

	@Override
	public List<GroupChatDto> getGroupChat(GroupChatDto groupChat, int start, int limit ) {
		
		return groupChatDao.getGroupChat(groupChat, start, limit);
	}
	@Override
	public List<GroupChatDto> getLastMsgByAccountId( String accountId){
		return groupChatDao.getLastMsgByAccountId(accountId);
	}

	public List<GroupChatDto> getLastMsgByGroupId( int GroupId){
		return groupChatDao.getLastMsgByGroupId(GroupId);
	}
	public boolean updateGroupRead(int groupId){
		return groupChatDao.updateGroupRead(groupId);
	}
	
	public int getGroupNotReadCount( String accountId ,int groupId){
		return groupChatDao.getGroupNotReadCount(accountId ,groupId);
	}
	@Override
	public boolean updateRead( String accountId ,int groupId) {
    	
    	return groupChatDao.updateRead(accountId,groupId);
    }
	
	@Override
	public int getCountAllNoReadChat( String accountId ){
		return groupChatDao.getGroupNotReadAllCount(accountId);		
	}
}
