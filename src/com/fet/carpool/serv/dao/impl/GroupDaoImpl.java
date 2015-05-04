package com.fet.carpool.serv.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.GroupDao;
import com.fet.carpool.serv.dto.GroupDto;
import com.fet.carpool.serv.persistence.Group;


@Repository("groupDao")
public class GroupDaoImpl extends BaseDao implements GroupDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void createGroup(GroupDto group) {
		
		Group g = new Group();
		g.setGroupName(group.getGroupName());
		g.setMember(group.getMember());
		g.setCreatorAccountId( group.getCreatorAccountId());
		g.setCreateTime(System.currentTimeMillis());
		sessionFactory.getCurrentSession().save(g);
		group.replaceValue(g);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateGroup(GroupDto group) {
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Group.class);
		crit.add(Restrictions.eq("groupId", group.getGroupId()));
		List<Group> list = crit.list();
		if( list == null || list.size() == 0 )
			throw new Exception( "groupId id " + group.getGroupId() + " not found" );
		
		Group g = list.get(0);
		if( !StringUtils.isEmpty(group.getGroupName()) )
			g.setGroupName(group.getGroupName());
		if( !StringUtils.isEmpty(group.getMember()) )
			g.setMember(group.getMember());
		if( !StringUtils.isEmpty(group.getCreatorAccountId()) )
			g.setCreatorAccountId(group.getCreatorAccountId());
		
		sessionFactory.getCurrentSession().update(g);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupDto> getGroup(int groupId, String accountId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Group.class);
		if( groupId > 0 )
			crit.add(Restrictions.eq("groupId", groupId ));
		if( !StringUtils.isEmpty(accountId) )
			crit.add(Restrictions.like("member", "%" + accountId + "%" ) );
		crit.setMaxResults(MAX_PAGE_SIZE);

		List<Group> groupList = crit.list();
		List<GroupDto> resultList = new ArrayList<GroupDto>();
		for( Group g : groupList ) {
			resultList.add( new GroupDto( g ) );
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteGroup(int groupId) {
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Group.class);
		crit.add(Restrictions.eq("groupId", groupId));
		List<Group> list = crit.list();
		if( list == null || list.size() == 0 )
			throw new Exception( "groupId id " + groupId + " not found" );
		
		sessionFactory.getCurrentSession().delete(list.get(0));
	}
	
	
	
	

}
