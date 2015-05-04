package com.fet.carpool.serv.rws;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dto.MessageLogDto;
import com.fet.carpool.serv.dto.QueryResultBean;
import com.fet.carpool.serv.persistence.MessageLog;
import com.fet.carpool.serv.service.MessageLogService;

@Component("messageWs")
@Path("message")
public class MessageWs extends MyVcardWs {

	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private HttpServletRequest httpServletRequest;

	@Autowired
	private MessageLogService messageLogService;


	public MessageWs() {
		super();

	}

	@POST
	@Path("AddAccountMesageTo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MessageLogDto addClientMesageLog(MessageLogDto message) throws UnsupportedEncodingException {

		if (message == null)
			throw new Exception("invalid input data");
		if (StringUtils.isEmpty(message.getAccountId()))
			throw new Exception("accountId is empty");
		if (StringUtils.isEmpty(message.getMessage()))
			throw new Exception("accountId is empty");

		message.setMessage(
				URLDecoder.decode( message.getMessage(), "UTF-8" ) );
		
		// prototype 不檢查帳號是否有效

		// save client message
		MessageLog messageLog = new MessageLog();
		messageLog.setAccountId(message.getAccountId());
		messageLog.setMessage( message.getMessage());
		messageLog.setMessageType(MessageLog.MESSAGE_TYPE_CLIENT);
		messageLog.setMessageTime(new Date());
		messageLog.setMessageRead(false);
		messageLog.setLatitude(message.getLatitude());
		messageLog.setLongitude(message.getLongitude());
		messageLog.setMessageRead(false);
		messageLogService.addMessageLog(messageLog);

		// generate a auto-reply message
		String autoReplyString = "已收到您的留言『" +  messageLog.getMessage()+ "』，感謝您的來信。";
		MessageLog replyMessageLog = new MessageLog();
		replyMessageLog.setAccountId(message.getAccountId());
		replyMessageLog.setMessage(autoReplyString);
		replyMessageLog.setMessageType(MessageLog.MESSAGE_TYPE_REPLY);
		replyMessageLog.setMessageTime(messageLog.getMessageTime());
		replyMessageLog.setMessageRead(false);
		messageLogService.addMessageLog(replyMessageLog);
		
		return new MessageLogDto(messageLog);
	}

	@POST
	@Path("AddAccountMesageReply")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MessageLogDto addReplyMesageLog(MessageLogDto message) throws UnsupportedEncodingException {

		if (message == null)
			throw new Exception("invalid input data");
		if (StringUtils.isEmpty(message.getAccountId()))
			throw new Exception("accountId is empty");
		if (StringUtils.isEmpty(message.getMessage()))
			throw new Exception("accountId is empty");

		message.setMessage(
				URLDecoder.decode( message.getMessage(), "UTF-8" ) );
		
		// prototype 不檢查帳號是否有效

		MessageLog messageLog = new MessageLog();
		messageLog.setAccountId(message.getAccountId());
		messageLog.setMessage(message.getMessage());
		messageLog.setMessageType(MessageLog.MESSAGE_TYPE_REPLY);
		messageLog.setMessageTime(new Date());
		messageLog.setMessageRead(false);
		messageLogService.addMessageLog(messageLog);

		return new MessageLogDto(messageLog);
	}

	@POST
	@Path("UpdateAccountMsgTo")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateClientMessageAlreadyRead(MessageLogDto message) {

		if (message == null)
			throw new Exception("invalid input data");
		if (StringUtils.isEmpty(message.getAccountId()))
			throw new Exception("accountId is empty");

		// prototype 不檢查帳號是否有效

		MessageLog messageLog = new MessageLog();
		messageLog.setAccountId(message.getAccountId());
		messageLog.setMessageType(MessageLog.MESSAGE_TYPE_CLIENT);
		if( message.getMessageTimeMillis() > 0 )
			messageLog.setMessageTime( new Date( message.getMessageTimeMillis() ) );
		messageLogService.updateMessageLogAlreadyRead(messageLog);

		return "OK";
	}

	@POST
	@Path("UpdateAccountMsgReply")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateReplyMessageAlreadyRead(MessageLogDto message) {

		if (message == null)
			throw new Exception("invalid input data");
		if (StringUtils.isEmpty(message.getAccountId()))
			throw new Exception("accountId is empty");

		// prototype 不檢查帳號是否有效

		MessageLog messageLog = new MessageLog();
		messageLog.setAccountId(message.getAccountId());
		messageLog.setMessageType(MessageLog.MESSAGE_TYPE_REPLY);
		if( message.getMessageTimeMillis() > 0 )
			messageLog.setMessageTime( new Date( message.getMessageTimeMillis() ) );
		messageLogService.updateMessageLogAlreadyRead(messageLog);

		return "OK";
	}

	@GET
	@Path("ListAccountMsgTo")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getClientMessageList(
			@QueryParam("accountId") String accountId,
			@QueryParam("timestamp") @DefaultValue("0") long timestamp,
			@QueryParam("timestampEnd") @DefaultValue("0") long timestampEnd,
			@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("limit") @DefaultValue("500") int limit) {

		if (StringUtils.isEmpty(accountId))
			throw new Exception("accountId is empty");
		if ( start < 0 || limit < 1 || limit > BaseDao.MAX_PAGE_SIZE )
			throw new Exception("invalid query range");
		
		List<MessageLog> logList = messageLogService.queryMessage(accountId,
				MessageLog.MESSAGE_TYPE_CLIENT, timestamp, timestampEnd, start, limit );
		
		List<Object> dtoList = new ArrayList<Object>();
		for( MessageLog log : logList )
			dtoList.add(new MessageLogDto(log));
		
		QueryResultBean resultBean = new QueryResultBean();
		resultBean.setResultList(dtoList);
		return resultBean;
	}
	
	@GET
	@Path("ListAccountMsgReply")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getReplyMessageList(
			@QueryParam("accountId") String accountId,
			@QueryParam("timestamp") @DefaultValue("0") long timestamp,
			@QueryParam("timestampEnd") @DefaultValue("0") long timestampEnd,
			@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("limit") @DefaultValue("500") int limit) {

		if (StringUtils.isEmpty(accountId))
			throw new Exception("accountId is empty");
		if ( start < 0 || limit < 1 || limit > BaseDao.MAX_PAGE_SIZE )
			throw new Exception("invalid query range");
		
		List<MessageLog> logList = messageLogService.queryMessage(accountId,
				MessageLog.MESSAGE_TYPE_REPLY, timestamp, timestampEnd, start, limit);
		
		List<Object> dtoList = new ArrayList<Object>();
		for( MessageLog log : logList )
			dtoList.add(new MessageLogDto(log));
		
		QueryResultBean resultBean = new QueryResultBean();
		resultBean.setResultList(dtoList);
		return resultBean;
	}
	
	@GET
	@Path("NotReadMsg")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResultBean getFirstNotReadClientMsg() {
		
		MessageLog log = messageLogService.getFirstNotReadMessage(null, MessageLog.MESSAGE_TYPE_CLIENT);
		QueryResultBean resultBean = new QueryResultBean();
		if( log != null ) {
			List<Object> dtoList = new ArrayList<Object>();
			dtoList.add(log);
			resultBean.setResultList(dtoList);
		}
		return resultBean;
	}
			
			
}
