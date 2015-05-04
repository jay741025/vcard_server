package com.fet.carpool.serv.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.FriendDao;
import com.fet.carpool.serv.dto.FriendChatDto;
import com.fet.carpool.serv.persistence.Friend;
import com.fet.carpool.serv.persistence.Group;

@Repository("friendDao")
public class FriendDaoImpl extends BaseDao implements FriendDao {

    @Autowired
    private SessionFactory sessionFactory;
    
	@Override
	public boolean addFriend(Friend friend) {
		
		if( !friendExists( friend.getAccountId(), friend.getFriendId() ) ) {
			sessionFactory.getCurrentSession().save(friend);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void deletefriend( String accountId ,String friendId ){
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Friend.class);
		crit.add(Restrictions.eq("accountId", accountId));
		crit.add(Restrictions.eq("friendId", friendId));
		List<Group> list = crit.list();
		if( list == null || list.size() == 0 )
			throw new Exception( "account id " + accountId + ", friend id " + friendId + " not found" );
		
		sessionFactory.getCurrentSession().delete(list.get(0));
		
	}

	
	@SuppressWarnings("rawtypes")
	public boolean friendExists( String accountId, String friendId ) {
		
		if( StringUtils.isEmpty(accountId) || StringUtils.isEmpty(friendId) )
			return false;
			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Friend.class);
		crit.add( Restrictions.eq( "accountId", accountId ) );
		crit.add( Restrictions.eq( "friendId", friendId ) );
		List list = crit.list();
		return list != null && list.size() > 0 ;
	}
	
	@SuppressWarnings("unchecked")
	public List<FriendChatDto> listFriend( String accountId ) {
		
		String sql = "SELECT a.FRIEND_ID as friendId, c.ACCOUNT_NAME as friendName , c.ACCOUNT_PIC as friendPic , a.TIMESTAMP as timestamp " 
				+ "FROM VCARD_FRIEND a " 
				+ "LEFT JOIN VCARD_ACCOUNT c ON c.ACCOUNT_ID = a.friend_id " 
				+ "WHERE a.account_id = :accountId";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("friendId", StandardBasicTypes.STRING);
		query.addScalar("friendName", StandardBasicTypes.STRING);
		query.addScalar("friendPic", StandardBasicTypes.STRING);
		query.addScalar("timestamp", StandardBasicTypes.LONG);
		query.setString("accountId", accountId);
		query.setResultTransformer(Transformers.aliasToBean(FriendChatDto.class));
		query.setMaxResults(MAX_PAGE_SIZE);
		
		return query.list();
	}
}
