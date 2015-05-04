package com.fet.carpool.serv.rws;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.common.util.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dto.AccountDto;
import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.dto.GroupChatDto;
import com.fet.carpool.serv.dto.GroupDto;
import com.fet.carpool.serv.dto.QueryResultBean;
import com.fet.carpool.serv.service.AccountService;
import com.fet.carpool.serv.service.GroupService;

@Component("groupWs")
@Path("group")
public class GroupWs {

	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private GroupService groupService;
	@Autowired
    private AccountService accountService;
	
	@POST
	@Path("CreateGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GroupDto createGroup(GroupDto group) throws UnsupportedEncodingException {

		if (StringUtils.isEmpty(group.getGroupName()))
			throw new Exception("group name is empty");
		if (StringUtils.isEmpty(group.getMember()))
			throw new Exception("member is empty");
		if (StringUtils.isEmpty(group.getCreatorAccountId()))
			throw new Exception("creator id is empty");
		
		group.setMember(URLDecoder.decode(group.getMember(), "UTF-8"));
		groupService.createGroup(group);
		
		group.setMember(null);
		group.setCreatorAccountId(null);
		return group;
	}
	
	@POST
	@Path("UpdateGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateGroup(GroupDto group) throws UnsupportedEncodingException {

		if (StringUtils.isEmpty(group.getGroupName()) 
				&& StringUtils.isEmpty(group.getMember()) 
				&& StringUtils.isEmpty(group.getCreatorAccountId())) 
			throw new Exception("update nothing");
		
		if( group.getMember() != null )
			group.setMember(URLDecoder.decode(group.getMember(), "UTF-8"));
		groupService.updateGroup(group);
		
		return "OK";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("GetGroup")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getGroup( @QueryParam("accountId") String accountId, 
			@QueryParam("groupId") @DefaultValue("0") int groupId) throws UnsupportedEncodingException {

		if (StringUtils.isEmpty(accountId) && groupId == 0 )  
			throw new Exception("no query criteria");
		
		List groupList = groupService.getGroup(groupId, accountId);
		for( int i = 0 ; groupList != null &&  i < groupList.size() ; i++ ) {
			GroupDto group = (GroupDto)groupList.get(i);
			group.setMember( URLEncoder.encode(group.getMember(), "UTF-8" ) );
		}
		
    	QueryResultBean result = new QueryResultBean();
    	result.setResultList(groupList);
        return result;
	}
	
	@GET
	@Path("DeleteGroup")
	@Produces(MediaType.TEXT_PLAIN)
	public String getGroup( @QueryParam("groupId") int groupId) {
		
		groupService.deleteGroup(groupId);
		groupService.updateGroupRead(groupId);
        return "OK";
	}
	
	
	
	@POST
	@Path("InsertGroupChat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertGroupChat(GroupChatDto groupChat) throws UnsupportedEncodingException {

		if (groupChat.getGroupId() <= 0)
			throw new Exception("group Id is invalid");
		if (StringUtils.isEmpty(groupChat.getMessage()))
			throw new Exception("message is empty");
		if (StringUtils.isEmpty(groupChat.getAccountId()))
			throw new Exception("account id is empty");
		
		if( groupChat.getMessage() != null )
			groupChat.setMessage(URLDecoder.decode(groupChat.getMessage(), "UTF-8"));
		groupService.insertGroupChat(groupChat);
		groupService.updateRead(groupChat.getAccountId(),groupChat.getGroupId());
		
		return String.valueOf( groupChat.getSeqNo() );
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("GetGroupChat")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getGroupChat (@QueryParam("accountId") String accountId, 
			@QueryParam("groupId") @DefaultValue("0") int groupId, 
			@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("limit") @DefaultValue("500") int limit,
			@QueryParam("timestampStart") @DefaultValue("0") long timestampStart,
			@QueryParam("timestampEnd") @DefaultValue("0") long timestampEnd ) throws UnsupportedEncodingException {

		if (StringUtils.isEmpty(accountId) && groupId == 0 )  
			throw new Exception("no query criteria");
		
		GroupChatDto crit = new GroupChatDto();
		crit.setAccountId(accountId);
		crit.setGroupId(groupId);
		crit.setTimestampStart(timestampStart);
		crit.setTimestampEnd(timestampEnd);
		List chatList = groupService.getGroupChat(crit, start, limit);
		
		for( int i = 0 ; chatList != null && i < chatList.size() ; i++ ) {
			GroupChatDto chat = (GroupChatDto)chatList.get(i);
			chat.setMessage(URLEncoder.encode(chat.getMessage(), "UTF-8" ) );
		}
		
    	QueryResultBean result = new QueryResultBean();
    	result.setResultList(chatList);
        return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
    @Path("GroupMember")
    @Produces(MediaType.APPLICATION_JSON)
    public QueryResultBean getGroupMember(@QueryParam("groupId") @DefaultValue("0") int groupId ) {
    	
    	if( groupId == 0 )
    		throw new Exception( "groupId is empty" );
    	
    	List groupList = groupService.getGroup(groupId, "");
    	if(groupList.size() ==0){
    		QueryResultBean result = new QueryResultBean();
    		return result;
    	}
    	GroupDto group = (GroupDto)groupList.get(0);
    	JSONArray json = null;
    	
    	List AccountDtoList = new ArrayList<AccountDto>();
    			
		try {
			json = new JSONArray(group.getMember());		
			if (json.length() > 0) {
				for (int i = 0; i < json.length(); i++) {
					try {
						AccountDtoList.add(accountService.getAccountById(json.getString(i)));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		
		}catch (JSONException e1) {
			throw new Exception( "getGroupMember Exception" +e1.getMessage());
		}
		
    	QueryResultBean result = new QueryResultBean();
    	result.setResultList(AccountDtoList);
        return result;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("getLastMsg")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getLastRelatedMsgByAccountId(@QueryParam("accountId") String accountId ) {
		
		if (StringUtils.isEmpty(accountId))
			throw new Exception("account id is empty");
		
		/*
		List groupList = groupService.getLastMsgByAccountId(accountId);
		List groupChatDto = new ArrayList<GroupChatDto>();
		for( int i = 0 ; groupList != null && i < groupList.size() ; i++ ) {
			GroupChatDto chat = (GroupChatDto)groupList.get(i);
			if(chat.getMember().indexOf(accountId)>-1){
				chat.setNotRead(groupService.getGroupNotReadCount(accountId, chat.getGroupId()));			
				groupChatDto.add(chat);
			}
		}
		*/
		
		List<GroupDto> groupList = groupService.getGroup(0, accountId);
		List groupChatDto = new ArrayList<GroupChatDto>();
		for( int i = 0 ; groupList != null && i < groupList.size() ; i++ ) {
			GroupChatDto chat = new GroupChatDto();
			
			chat.setGroupId(groupList.get(i).getGroupId());
			chat.setGroupName(groupList.get(i).getGroupName());
			chat.setMember(groupList.get(i).getMember());
			chat.setAccountId(accountId);			
			chat.setNotRead(groupService.getGroupNotReadCount(accountId, chat.getGroupId()));
			List<GroupChatDto> LastMsgByGroup=groupService.getLastMsgByGroupId(chat.getGroupId());
			chat.setMessage(LastMsgByGroup.get(0).getMessage());	
			chat.setTimestamp(LastMsgByGroup.get(0).getTimestamp());
			groupChatDto.add(chat);
			
		}	
		
		
		
		QueryResultBean result = new QueryResultBean();
		result.setResultList(groupChatDto);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("UpdateRead")
	@Consumes(MediaType.TEXT_PLAIN)
	public String updateRead(@QueryParam("accountId") String accountId
			,@QueryParam("groupId") @DefaultValue("0") int groupId) {

		if (accountId == null)
			throw new Exception("accountId is empty");
		if( groupId == 0 )
    		throw new Exception( "groupId is empty" );
		
		groupService.updateRead(accountId,groupId);

		return "OK";
	}
	
}
