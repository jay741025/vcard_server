package com.fet.carpool.serv.dao.impl;

import java.util.List;



import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.AccountDao;
import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.persistence.Account;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDao implements AccountDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public Account getAccountById(String accountId) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(
                Account.class);
        crit.add(Restrictions.eq("accountId", accountId));
        List<Account> result = crit.list();
        if( result.size() > 0 )
            return result.get(0);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Account> getAccountAll() {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(
                Account.class);        
        List<Account> result = crit.list();
        if( result.size() > 0 )
            return result;
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public List<Account> queryAccount( Account condition ) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(Account.class);
        if( !StringUtils.isEmpty( condition.getAccountId() ) )
        	crit.add(Restrictions.like("accountId", "%" + condition.getAccountId() + "%") );
        if( !StringUtils.isEmpty( condition.getAccountName() ) )
        	crit.add(Restrictions.like("accountName", "%" + condition.getAccountName() + "%") );
        if( !StringUtils.isEmpty( condition.getAccountType() ) )
        	crit.add(Restrictions.like("accountType", "%" + condition.getAccountType() + "%") );
        
        crit.setMaxResults(MAX_PAGE_SIZE);
        return crit.list();
    }

    public void updateAccount(Account account) {
        sessionFactory.getCurrentSession().update(account);
    }

    public void addNewAccount(Account account) {
    	sessionFactory.getCurrentSession().save(account);
    }
}
