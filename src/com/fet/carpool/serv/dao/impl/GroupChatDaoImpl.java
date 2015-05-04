package com.fet.carpool.serv.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.GroupChatDao;
import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.dto.GroupChatDto;
import com.fet.carpool.serv.dto.GroupDto;
import com.fet.carpool.serv.persistence.Friend;
import com.fet.carpool.serv.persistence.Group;
import com.fet.carpool.serv.persistence.GroupChat;
import com.fet.carpool.serv.persistence.GroupChatNotRead;

@Repository("groupChatDao")
public class GroupChatDaoImpl extends BaseDao implements GroupChatDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public boolean insertGroupChat(GroupChatDto groupChat) {
		
		GroupChat chat = new GroupChat();
		chat.setAccountId(groupChat.getAccountId());
		chat.setGroupId(groupChat.getGroupId());
		chat.setMessage(groupChat.getMessage());
		chat.setTimestamp(System.currentTimeMillis());
		sessionFactory.getCurrentSession().save(chat);
		groupChat.replaceValue(chat);
		return true;
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GroupChatDto> getGroupChat(GroupChatDto groupChat, int start,
			int limit) {
		
		checkPageSize(limit);	

		
		String sql = "SELECT a.ACCOUNT_ID as accountId, b.ACCOUNT_NAME as accountName "				
				+ ", a.MESSAGE as message, a.READ_COUNT as readCount "
				+ ", a.TIMESTAMP as timestamp "
				+ "FROM VCARD_GROUP_CHAT a "
				+ "LEFT JOIN VCARD_ACCOUNT b ON b.ACCOUNT_ID = a.account_id "				
				+ "WHERE a.group_id = :groupId ";
		//if( groupChat.getGroupId() >0  )
			//sql += "AND a.group_id = :groupId ";
		if( groupChat.getTimestampStart() > 0 )
			sql += "AND a.timestamp >= :timestamp ";
		if( groupChat.getTimestampEnd() > 0 )
			sql += "AND a.timestamp < :timestampEnd ";
		
		sql += "ORDER BY a.timestamp DESC ";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accountId", StandardBasicTypes.STRING);
		query.addScalar("accountName", StandardBasicTypes.STRING);		
		query.addScalar("message", StandardBasicTypes.STRING);		
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		//query.setString("accountId",  groupChat.getAccountId());
		if( groupChat.getGroupId() >0 )
			query.setInteger("groupId", groupChat.getGroupId() );
		if( groupChat.getTimestampStart() > 0 )
			query.setLong("timestamp", groupChat.getTimestampStart());
		if(  groupChat.getTimestampEnd() > 0 )
			query.setLong("timestamp", groupChat.getTimestampEnd());

		query.setResultTransformer(Transformers.aliasToBean(GroupChatDto.class));
		query.setFirstResult(start);
		query.setMaxResults(limit);
		
		return query.list();
		
		/*
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(GroupChat.class);
		if( !StringUtils.isEmpty( groupChat.getAccountId() ) )
			crit.add(Restrictions.eq("accountId", groupChat.getAccountId() ) );
		if( groupChat.getGroupId() > 0 )
			crit.add(Restrictions.eq("groupId", groupChat.getGroupId() ) );
		if( groupChat.getTimestampStart() > 0 )
			crit.add(Restrictions.ge("timestamp", groupChat.getTimestampStart() ) );
		if( groupChat.getTimestampEnd() > 0 )
			crit.add(Restrictions.lt("timestamp", groupChat.getTimestampEnd() ) );
		if( start > 0 )
			crit.setFirstResult(start);
		if( limit > 0 )
			crit.setMaxResults(limit);
		
		crit.addOrder(Order.desc("timestamp"));
		
		List<GroupChat> chatList = crit.list();
		List<GroupChatDto> resultList = new ArrayList<GroupChatDto>();
		for( GroupChat chat : chatList )
			resultList.add( new GroupChatDto(chat) );
		
		return resultList;
		*/
	}
	
	@SuppressWarnings("unchecked")
	public List<GroupChatDto> getLastMsgByAccountId(String accountId){
		// 留意, 這是要查別人對 accountId 的記錄, 找的方向要注意
		String sql = "SELECT a.group_id as groupId ,c.group_name as groupName ,c.member as member , a.ACCOUNT_ID as accountId, b.ACCOUNT_NAME as accountName " 
				+ ", a.MESSAGE as message, a.READ_COUNT as readCount " 
				+ ", a.TIMESTAMP as timestamp " 
				+ "FROM VCARD_GROUP_CHAT a " 
				+ "JOIN VCARD_ACCOUNT b ON b.ACCOUNT_ID = a.account_id " 
				+ "JOIN VCARD_GROUP c ON c.GROUP_ID = a.group_id "
				+ "WHERE a.ACCOUNT_ID = :accountId "
				+ "and timestamp in (select max(timestamp) from VCARD_GROUP_CHAT WHERE ACCOUNT_ID = :accountId group by group_id )";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("groupId", StandardBasicTypes.INTEGER);
		query.addScalar("accountId", StandardBasicTypes.STRING);
		query.addScalar("accountName", StandardBasicTypes.STRING);
		query.addScalar("member", StandardBasicTypes.STRING);
		query.addScalar("groupName", StandardBasicTypes.STRING);
		query.addScalar("message", StandardBasicTypes.STRING);		
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		query.setString("accountId", accountId);		
		query.setResultTransformer(Transformers.aliasToBean(GroupChatDto.class));
		query.setMaxResults(MAX_PAGE_SIZE);	
		
		return query.list();
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<GroupChatDto> getLastMsgByGroupId(int groupId){
		// 留意, 這是要查別人對 accountId 的記錄, 找的方向要注意
		String sql = "SELECT ACCOUNT_ID as accountId ,group_id as groupId , MESSAGE as message ,TIMESTAMP as timestamp " 
				+ "FROM VCARD_GROUP_CHAT  " 				
				+ "WHERE  timestamp in (select max(timestamp) from VCARD_GROUP_CHAT WHERE Group_ID = :groupId )";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		query.setInteger("groupId", groupId);	
		query.addScalar("groupId", StandardBasicTypes.INTEGER);		
		query.addScalar("message", StandardBasicTypes.STRING);		
		query.addScalar("timestamp", StandardBasicTypes.LONG);	
		query.addScalar("accountId", StandardBasicTypes.STRING);
		
		query.setResultTransformer(Transformers.aliasToBean(GroupChatDto.class));
		
		return query.list();
		
		
	}
	
	@Override
	public boolean addGroupChatNotRead(String accountId,int groupId) {
		
		if( GroupChatNotReadExists(accountId,groupId ) ) {
			String sql = "UPDATE GroupChatNotRead "
					+ " SET notRead=notRead+1 "
					+ " WHERE accountId = :accountId AND groupId = :groupId  ";	
			
			Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setString("accountId", accountId);
			query.setInteger("groupId", groupId);				
			logger.debug("addGroupChatNotRead(), sql=" + query.getQueryString() );
			query.executeUpdate();
			
		}else{
			GroupChatNotRead groupChatNotRead =new GroupChatNotRead();
			groupChatNotRead.setAccountId(accountId);
			groupChatNotRead.setGroupId(groupId);
			groupChatNotRead.setNotRead(1);
			sessionFactory.getCurrentSession().save(groupChatNotRead);
		}
		return true;
		
	}
	
	@SuppressWarnings("rawtypes")
	public boolean GroupChatNotReadExists( String accountId, int groupId ) {
		
		if( StringUtils.isEmpty(accountId) || groupId ==0 )
			return false;
			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(GroupChatNotRead.class);
		crit.add( Restrictions.eq( "accountId", accountId ) );
		crit.add( Restrictions.eq( "groupId", groupId ) );
		List list = crit.list();
		
		return list != null && list.size() > 0 ;
	}
	
	@SuppressWarnings("rawtypes")
	public int getGroupNotReadCount(String accountId,int groupId){
		
		if( StringUtils.isEmpty(accountId) || groupId ==0 )
			return 0;		
		
		String sql = "SELECT sum(not_read) as notRead FROM VCARD_GROUP_CHAT_NOTREAD" 
				+ " WHERE account_id = :accountId and group_id = :groupId";	
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("notRead", StandardBasicTypes.INTEGER);
		query.setString("accountId", accountId);
		query.setInteger("groupId", groupId);
		
		if(query.list().get(0)==null){
			return 0;
		}else{
			return (Integer)query.list().get(0) ;
		}
		
	}
	
	public boolean updateRead(String accountId,int groupId){
		
		String sql = "UPDATE GroupChatNotRead "
				+ " SET notRead=0 "
				+ " WHERE accountId = :accountId AND groupId = :groupId ";	
		
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setString("accountId", accountId);
		query.setInteger("groupId", groupId);	
		
		logger.debug("updateRead(), sql=" + query.getQueryString() );
		query.executeUpdate();
		return true;
	}
	
	public boolean updateGroupRead(int groupId){
		
		String sql = "UPDATE GroupChatNotRead "
				+ " SET notRead=0 "
				+ " WHERE  groupId = :groupId ";	
		
		Query query = sessionFactory.getCurrentSession().createQuery(sql);		
		query.setInteger("groupId", groupId);	
		
		logger.debug("updateRead(), sql=" + query.getQueryString() );
		query.executeUpdate();
		return true;
	}

	
	@SuppressWarnings("rawtypes")
	public int getGroupNotReadAllCount(String accountId){
		
		String sql = "SELECT sum(not_read) as notRead FROM VCARD_GROUP_CHAT_NOTREAD" 
				+ " WHERE account_id = :accountId";	
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("notRead", StandardBasicTypes.INTEGER);
		query.setString("accountId", accountId);
		if(query.list().get(0)==null){
			return 0 ;
		}else{
			return (Integer)query.list().get(0);
		}
		
	}

}
