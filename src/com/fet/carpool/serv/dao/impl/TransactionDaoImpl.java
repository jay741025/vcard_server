package com.fet.carpool.serv.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.TransactionDao;
import com.fet.carpool.serv.persistence.Transaction;

@Repository("transactionDao")
public class TransactionDaoImpl extends BaseDao implements TransactionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public List<Transaction> listByAccountId(String accountId) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(
                Transaction.class);
        crit.add(Restrictions.eq("accountId", accountId));
        return crit.list();
    }

    @Override
    public void saveTransaction(Transaction trans) {
        sessionFactory.getCurrentSession().saveOrUpdate(trans);
    }

}
