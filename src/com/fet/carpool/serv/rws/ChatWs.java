package com.fet.carpool.serv.rws;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.dto.GroupDto;
import com.fet.carpool.serv.dto.MessageLogDto;
import com.fet.carpool.serv.dto.QueryResultBean;
import com.fet.carpool.serv.persistence.ChatLog;
import com.fet.carpool.serv.persistence.Friend;
import com.fet.carpool.serv.persistence.MessageLog;
import com.fet.carpool.serv.service.ChatService;
import com.fet.carpool.serv.service.GroupService;
import com.fet.carpool.serv.service.NotificationService;

@Component("chatWs")
@Path("chat")
public class ChatWs extends MyVcardWs {

	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private HttpServletRequest httpServletRequest;

	@Autowired
	private ChatService chatService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private GroupService groupService;

	@POST
	@Path("addFriend")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FriendChatDto addFriend(FriendChatDto friend) {

		if (StringUtils.isEmpty(friend.getAccountId()))
			throw new Exception("account id is empty");
		if (StringUtils.isEmpty(friend.getFriendId()))
			throw new Exception("friend id is empty");
		Friend f = new Friend();
		f.setAccountId(friend.getAccountId());
		f.setFriendId(friend.getFriendId());
		f.setTimestamp(System.currentTimeMillis());
		chatService.AddFriend(f);
		

		return new FriendChatDto(f);
	}
	
	@GET
	@Path("DeleteFriend")
	@Produces(MediaType.TEXT_PLAIN)
	public String getGroup( @QueryParam("accountId") String accountId ,
			@QueryParam("friendId") String friendId) {
		
		chatService.deleteFriend(accountId,friendId);
		
        return "OK";
	}
	
	@POST
	@Path("addChat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FriendChatDto addChatLog(FriendChatDto chat) throws UnsupportedEncodingException {

		if (StringUtils.isEmpty(chat.getAccountId()))
			throw new Exception("account id is empty");
		if (StringUtils.isEmpty(chat.getFriendId()))
			throw new Exception("friend id is empty");
		if (StringUtils.isEmpty(chat.getMessage()))
			throw new Exception("message is empty");
		
		ChatLog chatLog = new ChatLog();
		chatLog.setAccountId(chat.getAccountId());
		chatLog.setFriendId(chat.getFriendId());
		chatLog.setMessage(URLDecoder.decode(chat.getMessage(), "UTF-8"));
		chatLog.setMessageRead(false);
		chatLog.setTimestamp(System.currentTimeMillis());
		chatService.AddChatLog(chatLog);
		return new FriendChatDto(chatLog);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("getChat")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getChatLog( @QueryParam("accountId") String accountId, 
			@QueryParam("friendId") String friendId,
			@QueryParam("timestamp") @DefaultValue("0") long timestamp,
			@QueryParam("timestampEnd") @DefaultValue("0") long timestampEnd,
			@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("limit") @DefaultValue("500") int limit ) {

		if (StringUtils.isEmpty(accountId))
			throw new Exception("account id is empty");
		if ( start < 0 || limit < 1 || limit > BaseDao.MAX_PAGE_SIZE )
			throw new Exception("invalid query range");
		

		List chatList = chatService.getChatLogByAccountId(accountId, friendId, timestamp, timestampEnd, start, limit );
		QueryResultBean result = new QueryResultBean();
		result.setResultList(chatList);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("UpdateRead")
	@Consumes(MediaType.TEXT_PLAIN)
	public String updateRead(@QueryParam("accountId") String accountId
			,@QueryParam("friendId") String friendId) {

		if (accountId == null)
			throw new Exception("accountId is empty");
		if (friendId == null)
			throw new Exception("friendId is empty");
		
		chatService.updateRead(accountId,friendId);

		return "OK";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("getLastMsg")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getLastRelatedMsgByAccountId(@QueryParam("accountId") String accountId ) {
		
		if (StringUtils.isEmpty(accountId))
			throw new Exception("account id is empty");
		
		List chatList = chatService.getLastRelatedMsgByAccountId(accountId);
		for(int i=0;i<chatList.size();i++){
			FriendChatDto chat =(FriendChatDto)chatList.get(i) ;
			logger.debug("chat" + chat.getMessage() );
			chat.setMessageNotReadCount(chatService.getCountNoReadChat(
					accountId ,(accountId.equals(chat.getAccountId())?chat.getFriendId():chat.getAccountId()) ));
		}
		
		QueryResultBean result = new QueryResultBean();
		result.setResultList(chatList);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Path("getCountAllNoReadChat")
	@Produces(MediaType.APPLICATION_JSON)
	public int getCountAllNoReadChat(@QueryParam("accountId") String accountId ) {
		
		if (StringUtils.isEmpty(accountId))
			throw new Exception("account id is empty");
		
		int count = chatService.getCountAllNoReadChat(accountId);	
		count +=groupService.getCountAllNoReadChat(accountId);
		return count;
	}
	
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
    @Path("listFriend")
    @Produces(MediaType.APPLICATION_JSON)
    public QueryResultBean getAccount(@QueryParam("accountId") String accountId ) {
    	
    	if( StringUtils.isEmpty( accountId ) )
    		throw new Exception( "accoundId is empty" );
    	
    	List friendList = chatService.listFriend(accountId); 
    	QueryResultBean result = new QueryResultBean();
    	result.setResultList(friendList);
        return result;
    }
	
}
