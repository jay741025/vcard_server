package com.fet.carpool.serv.dao.impl;

import java.util.ArrayList;
import java.util.List;





import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.ChatLogDao;
import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.persistence.ChatLog;

@Repository("chatLogDao")
public class ChatLogDaoImpl extends BaseDao implements ChatLogDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addChatLog(ChatLog chatLog) {
		
		sessionFactory.getCurrentSession().save(chatLog);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getCountAllNoReadChat(String accountId){
		String sql = "SELECT COUNT(*) "
				+ "FROM VCARD_CHAT_LOG  "				
				+ "WHERE friend_id = :accountId and message_read = 0";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		//query.addScalar("friend_id", StandardBasicTypes.STRING);
		query.setString("accountId", accountId);
		return ((Number) query.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int getCountNoReadChat(String accountId,String friendId){
		String sql = "SELECT COUNT(*) "
				+ "FROM VCARD_CHAT_LOG  "				
				+ "WHERE friend_id = :accountId and " 
				+"account_id = :friendId and message_read = 0";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		//query.addScalar("friend_id", StandardBasicTypes.STRING);
		query.setString("accountId", accountId);
		query.setString("friendId", friendId);
		
		int count =((Number) query.uniqueResult()).intValue();		
		return count;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FriendChatDto> getChatLogByAccountId(String accountId, String friendId,
			long timestamp, long timestampEnd, int start, int limit ) {
		
		checkPageSize(limit);
		
		String sql = "SELECT a.ACCOUNT_ID as accountId, b.ACCOUNT_NAME as accountName "
				+ ", a.FRIEND_ID as friendId, c.ACCOUNT_NAME as friendName "
				+ ", a.MESSAGE as message, a.MESSAGE_READ as messageRead "
				+ ", a.TIMESTAMP as timestamp "
				+ "FROM VCARD_CHAT_LOG a "
				+ "LEFT JOIN VCARD_ACCOUNT b ON b.ACCOUNT_ID = a.account_id "
				+ "LEFT JOIN VCARD_ACCOUNT c ON c.ACCOUNT_ID = a.friend_id "
				+ "WHERE a.account_id = :accountId ";
		if( !StringUtils.isEmpty(friendId) )
			sql += "AND a.friend_id = :friendId ";
		if( timestamp > 0 )
			sql += "AND a.timestamp >= :timestamp ";
		if( timestampEnd > 0 )
			sql += "AND a.timestamp < :timestampEnd ";
		
		sql += "ORDER BY a.timestamp DESC ";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accountId", StandardBasicTypes.STRING);
		query.addScalar("accountName", StandardBasicTypes.STRING);
		query.addScalar("friendId", StandardBasicTypes.STRING);
		query.addScalar("friendName", StandardBasicTypes.STRING);
		query.addScalar("message", StandardBasicTypes.STRING);
		query.addScalar("messageRead", StandardBasicTypes.BOOLEAN);
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		query.setString("accountId", accountId);
		if( !StringUtils.isEmpty(friendId) )
			query.setString("friendId", friendId );
		if( timestamp > 0 )
			query.setLong("timestamp", timestamp);
		if( timestampEnd > 0 )
			query.setLong("timestampEnd", timestampEnd);

		query.setResultTransformer(Transformers.aliasToBean(FriendChatDto.class));
		query.setFirstResult(start);
		query.setMaxResults(limit);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<FriendChatDto> getLastRelatedMsgByAccountId_Old( String accountId ) {
		
		// 留意, 這是要查別人對 accountId 的記錄, 找的方向要注意
		String sql = "SELECT a.ACCOUNT_ID as friendId, b.ACCOUNT_NAME as friendName " 
				+ ", a.FRIEND_ID as accountId, c.ACCOUNT_NAME as accountName " 
				+ ", a.MESSAGE as message, a.MESSAGE_READ as messageRead " 
				+ ", a.TIMESTAMP as timestamp " 
				+ "FROM VCARD_CHAT_LOG a " 
				+ "LEFT JOIN VCARD_ACCOUNT b ON b.ACCOUNT_ID = a.account_id " 
				+ "LEFT JOIN VCARD_ACCOUNT c ON c.ACCOUNT_ID = a.friend_id " 
				+ "WHERE a.FRIEND_ID = :accountId AND a.ACCOUNT_ID <> a.FRIEND_ID "
				+ "  AND TIMESTAMP = " 
				+ "  (SELECT max(TIMESTAMP) from VCARD_CHAT_LOG d " 
				+ "  WHERE d.account_id = a.account_Id and d.friend_id = a.friend_id )";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accountId", StandardBasicTypes.STRING);
		query.addScalar("accountName", StandardBasicTypes.STRING);
		query.addScalar("friendId", StandardBasicTypes.STRING);
		query.addScalar("friendName", StandardBasicTypes.STRING);
		query.addScalar("message", StandardBasicTypes.STRING);
		query.addScalar("messageRead", StandardBasicTypes.BOOLEAN);
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		query.setString("accountId", accountId);
		query.setResultTransformer(Transformers.aliasToBean(FriendChatDto.class));
		query.setMaxResults(MAX_PAGE_SIZE);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FriendChatDto> getLastRelatedMsgByAccountId( String accountId ) {
		
		String sql = "SELECT a.ACCOUNT_ID as accountId, b.ACCOUNT_NAME as accountName " 
				+ ", a.FRIEND_ID as friendId, c.ACCOUNT_NAME as friendName " 
				+ ", a.MESSAGE as message, a.MESSAGE_READ as messageRead " 
				+ ", a.TIMESTAMP as timestamp " 
				+ "FROM VCARD_CHAT_LOG a " 
				+ "LEFT JOIN VCARD_ACCOUNT b ON b.ACCOUNT_ID = a.account_id " 
				+ "LEFT JOIN VCARD_ACCOUNT c ON c.ACCOUNT_ID = a.friend_id " 
				+ "WHERE ( a.ACCOUNT_ID = :accountId OR a.FRIEND_ID = :accountId ) "
				+ "  AND a.ACCOUNT_ID <> a.FRIEND_ID "
				+ "  AND TIMESTAMP = " 
				+ "  (SELECT max(TIMESTAMP) from VCARD_CHAT_LOG d " 
				+ "  WHERE d.account_id = a.account_Id and d.friend_id = a.friend_id ) "
				+ "ORDER BY a.ACCOUNT_ID, a.FRIEND_ID";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accountId", StandardBasicTypes.STRING);
		query.addScalar("accountName", StandardBasicTypes.STRING);
		query.addScalar("friendId", StandardBasicTypes.STRING);
		query.addScalar("friendName", StandardBasicTypes.STRING);
		query.addScalar("message", StandardBasicTypes.STRING);
		query.addScalar("messageRead", StandardBasicTypes.BOOLEAN);
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		query.setString("accountId", accountId);
		query.setResultTransformer(Transformers.aliasToBean(FriendChatDto.class));
		query.setMaxResults(MAX_PAGE_SIZE);
		
		List<FriendChatDto> talkList = query.list();
		List<FriendChatDto> resultList = new ArrayList<FriendChatDto>();
		// 1. 先把發話給每個人的最新一筆寫入
		for( FriendChatDto talk : talkList ) {
			if( accountId.equals(talk.getAccountId() ))
				resultList.add(talk);
		}
		// 2. 依各 friendId 回話給 accountId 的資料比對, 取較晚者
		for( FriendChatDto reply : talkList ) {
			if( accountId.equals(reply.getFriendId() ) ) {  // if reply to {accountId}
				boolean foundTalk = false;
				for( FriendChatDto t : resultList ) {
					if( t.getAccountId().equals(reply.getFriendId())  && t.getFriendId().equals(reply.getAccountId())) {
						if( reply.getTimestamp() > t.getTimestamp() )
							t.replace(reply);
						foundTalk = true;
						break;
					}
				}
				
				if( !foundTalk )
					resultList.add(reply);
			}
		}

		
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public FriendChatDto getLastSendMsg( String accountId, String friendId ) {
		
		String sql = "SELECT a.ACCOUNT_ID as accountId, b.ACCOUNT_NAME as accountName " 
				+ ", a.FRIEND_ID as friendId, c.ACCOUNT_NAME as friendName " 
				+ ", a.MESSAGE as message, a.MESSAGE_READ as messageRead " 
				+ ", a.TIMESTAMP as timestamp " 
				+ "FROM VCARD_CHAT_LOG a " 
				+ "LEFT JOIN VCARD_ACCOUNT b ON b.ACCOUNT_ID = a.account_id " 
				+ "LEFT JOIN VCARD_ACCOUNT c ON c.ACCOUNT_ID = a.friend_id " 
				+ "WHERE a.ACCOUNT_ID = :accountId AND a.FRIEND_ID = :friendId "
				+ "  AND a.ACCOUNT_ID <> a.FRIEND_ID "
				+ "ORDER BY a.TIMESTAMP DESC";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accountId", StandardBasicTypes.STRING);
		query.addScalar("accountName", StandardBasicTypes.STRING);
		query.addScalar("friendId", StandardBasicTypes.STRING);
		query.addScalar("friendName", StandardBasicTypes.STRING);
		query.addScalar("message", StandardBasicTypes.STRING);
		query.addScalar("messageRead", StandardBasicTypes.BOOLEAN);
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		query.setString("accountId", accountId);
		query.setString("friendId", friendId);
		query.setResultTransformer(Transformers.aliasToBean(FriendChatDto.class));
		query.setMaxResults(1);
		
		List<FriendChatDto> talkList = query.list();
		if( talkList == null || talkList.size() == 0 )
			return null;

		return talkList.get(0);
	}
	
	public boolean updateRead(String accountId,String friendId){
		String sql = "UPDATE ChatLog "
				+ " SET messageRead=true "
				+ " WHERE accountId = :accountId AND friendId = :friendId AND messageRead = false ";	
		
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setString("accountId", accountId);
		query.setString("friendId", friendId);	
		
		logger.debug("updateRead(), sql=" + query.getQueryString() );
		query.executeUpdate();
		return true;
	}

}
