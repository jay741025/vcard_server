package com.fet.carpool.serv.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.MessageLogDao;
import com.fet.carpool.serv.persistence.MessageLog;

@Repository("messageLogDao")
public class MessageLogDaoImpl extends BaseDao implements MessageLogDao {

	@Autowired
    private SessionFactory sessionFactory;
	
    @Override
	public void addMessageLog(MessageLog messageLog) {
    	
		sessionFactory.getCurrentSession().save(messageLog);
	}

	@Override
	public void updateMessageLog(MessageLog messageLog) {
		sessionFactory.getCurrentSession().update(messageLog);
	}
	
	@Override
	public void updateMessageLogAlreadyRead(MessageLog messageLog) {
		
		String sql = "UPDATE MessageLog "
				+ " SET messageRead=true "
				+ " WHERE accountId = :accountId AND messageType = :messageType AND messageRead = false ";
		if( messageLog.getMessageTime() != null )
			sql += " AND messageTime <= :messageTime";
		
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setString("accountId", messageLog.getAccountId());
		query.setString("messageType", messageLog.getMessageType());
		if( messageLog.getMessageTime() != null )
			query.setTimestamp("messageTime", messageLog.getMessageTime());
		
		logger.debug("updateMessageLogAlreadyRead(), sql=" + query.getQueryString() );
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageLog> queryMessage( String accountId, String messageType, 
			long timestamp, long timestampEnd, int start, int limit ) {
		
		checkPageSize(limit);
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria( MessageLog.class );
		if( accountId != null )
			crit.add(Restrictions.eq( "accountId", accountId ));
		if( messageType != null )
			crit.add(Restrictions.eq( "messageType", messageType ));
		if( timestamp > 0 )
			crit.add(Restrictions.ge( "messageTimeMillis", timestamp ));
		if( timestampEnd > 0 )
			crit.add(Restrictions.lt( "messageTimeMillis", timestampEnd ));
		
		crit.addOrder(Order.desc("messageTimeMillis"));
		crit.setFirstResult(start);
		crit.setMaxResults(limit);
		
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public MessageLog getFirstNotReadMessage(String accountId,
			String messageType) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria( MessageLog.class );
		if( accountId != null )
			crit.add(Restrictions.eq( "accountId", accountId ));
		if( messageType != null )
			crit.add(Restrictions.eq( "messageType", messageType ));
		
		crit.add(Restrictions.eq("messageRead", false));
		crit.addOrder(Order.asc("seqNo"));
		crit.setMaxResults(1);
		
		List<MessageLog> resultList = crit.list();
		if( resultList != null && resultList.size() > 0 )
			return resultList.get(0);
		
		return null;
	}

}
