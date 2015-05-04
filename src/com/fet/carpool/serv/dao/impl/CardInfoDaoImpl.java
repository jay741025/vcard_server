package com.fet.carpool.serv.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dao.CardInfoDao;
import com.fet.carpool.serv.dto.CardInfoDto;

@Repository("cardInfoDao")
public class CardInfoDaoImpl extends BaseDao implements CardInfoDao {

	private static final String TABLE_PREFIX = "VCARD_CARDINFO_"; 
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public CardInfoDto queryBySeqNo(String accCode, String seqNo) {
		
		String sql = "SELECT amount, status, notes, card_no_in as cardNoIn FROM " 
				+ TABLE_PREFIX + accCode
				+ " WHERE seqno = :seqNo";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("amount", StandardBasicTypes.INTEGER);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("notes", StandardBasicTypes.STRING);
		query.addScalar("cardNoIn", StandardBasicTypes.STRING);
		query.setString("seqNo", seqNo);
		query.setResultTransformer(Transformers.aliasToBean(CardInfoDto.class));
		@SuppressWarnings("unchecked")
		List<CardInfoDto> list = query.list();
		if( list == null || list.size() == 0 )
			return null;
		
		CardInfoDto cardInfo = list.get(0);
		cardInfo.setAccCode(accCode);
		cardInfo.setSeqNo(seqNo);
		return cardInfo;
	}

	@Override
	public CardInfoDto queryByCardNoIn(String accCode, String cardNoIn ) {
		
		String sql = "SELECT seqNo, amount, status, notes FROM " 
				+ TABLE_PREFIX + accCode
				+ " WHERE card_no_in = :cardNoIn";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("seqNo", StandardBasicTypes.STRING);
		query.addScalar("amount", StandardBasicTypes.INTEGER);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("notes", StandardBasicTypes.STRING);
		query.setString("cardNoIn", cardNoIn);
		query.setResultTransformer(Transformers.aliasToBean(CardInfoDto.class));
		@SuppressWarnings("unchecked")
		List<CardInfoDto> list = query.list();
		if( list == null || list.size() == 0 )
			return null;
		
		CardInfoDto cardInfo = list.get(0);
		cardInfo.setAccCode(accCode);
		cardInfo.setCardNoIn(cardNoIn);
		return cardInfo;
	}
	
	@Override
	public void updateCardStatus(CardInfoDto cardInfo) {
		
		String sql = "UPDATE " + TABLE_PREFIX + cardInfo.getAccCode()
				+ " SET STATUS = :status WHERE SEQNO = :seqCode";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString("status", cardInfo.getStatus());
		query.setString("seqCode", cardInfo.getSeqNo());
		query.executeUpdate();
	}

	@Override
	public CardInfoDto getActivedCardInfo( String accCode ) {
		return getActivedCardInfo( accCode, 0 );
	}
	
	@Override
	public CardInfoDto getActivedCardInfo(String accCode, int amount) {
		
		String sql = "SELECT seqNo, amount, status, notes, card_no_in as cardNoIn FROM " 
				+ TABLE_PREFIX + accCode
				+ " WHERE status = :status";
		if( amount > 0 )
			sql += " AND amount = :amount";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("seqNo", StandardBasicTypes.STRING);
		query.addScalar("amount", StandardBasicTypes.INTEGER);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("notes", StandardBasicTypes.STRING);
		query.addScalar("cardNoIn", StandardBasicTypes.STRING);
		
		query.setString("status", CardInfoDto.STATUS_ACTIVED);
		if( amount > 0 )
			query.setInteger("amount", amount);
		
		query.setResultTransformer(Transformers.aliasToBean(CardInfoDto.class));
		query.setFirstResult(0);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<CardInfoDto> list = query.list();
		if( list == null || list.size() == 0 )
			return null;
		
		CardInfoDto cardInfo = list.get(0);
		cardInfo.setAccCode(accCode);
		
		return cardInfo;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CardInfoDto> getCardInfoList( String accCode, String status, int pageNo, int pageSize ) {
		
		checkPageNo(pageNo);
		checkPageSize(pageSize);
		
		String sql = "SELECT '" + accCode + "' as accCode, seqNo, amount, status, notes, card_no_in as cardNoIn FROM " 
				+ TABLE_PREFIX + accCode;
		if( status != null )
			sql += " WHERE status = :status";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accCode", StandardBasicTypes.STRING);
		query.addScalar("seqNo", StandardBasicTypes.STRING);
		query.addScalar("amount", StandardBasicTypes.INTEGER);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("notes", StandardBasicTypes.STRING);
		query.addScalar("cardNoIn", StandardBasicTypes.STRING);
		if( status != null )
			query.setString("status", status);
		
		query.setResultTransformer(Transformers.aliasToBean(CardInfoDto.class));
		query.setFirstResult( (pageNo-1) * pageSize );
		query.setMaxResults( pageSize );
		
		logger.debug( "getCardInfoList(), sql=" + query.getQueryString() );
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CardInfoDto> getCardInfoList( CardInfoDto cardInfo, int pageNo, int pageSize ) {
		
		checkPageNo(pageNo);
		checkPageSize(pageSize);
		
		String tableName = TABLE_PREFIX + cardInfo.getAccCode();
		String sql = "SELECT '" + cardInfo.getAccCode() + "' as accCode, seqNo, amount, status, notes, card_no_in as cardNoIn FROM "
				+ tableName + " a WHERE 1=1";
		if( !StringUtils.isEmpty(cardInfo.getSeqNo()) )
			sql += " AND seqNo= :seqNo";
		if( !StringUtils.isEmpty(cardInfo.getNotes()) )
			sql += " AND notes= :cardNo";
		if( !StringUtils.isEmpty(cardInfo.getStatus()) )
			sql += " AND status= :status";
		if( cardInfo.getAmount() > 0 )
			sql += " AND amount= :amount";
		if( !StringUtils.isEmpty( cardInfo.getCardNoIn() ) )
			sql += " AND card_no_in = :cardNoIn";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("accCode", StandardBasicTypes.STRING);
		query.addScalar("seqNo", StandardBasicTypes.STRING);
		query.addScalar("amount", StandardBasicTypes.INTEGER);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("notes", StandardBasicTypes.STRING);
		query.addScalar("cardNoIn", StandardBasicTypes.STRING);
		if( !StringUtils.isEmpty(cardInfo.getSeqNo()) )
			query.setString("seqNo", cardInfo.getSeqNo());
		if( !StringUtils.isEmpty(cardInfo.getNotes()) )
			query.setString("cardNo", cardInfo.getNotes());
		if( !StringUtils.isEmpty(cardInfo.getStatus()) )
			query.setString("status", cardInfo.getStatus());
		if( cardInfo.getAmount() > 0 )
			query.setInteger("amount", cardInfo.getAmount());
		if( !StringUtils.isEmpty( cardInfo.getCardNoIn() ) )
			query.setString( "cardNoIn", cardInfo.getCardNoIn() );
		
		query.setResultTransformer(Transformers.aliasToBean(CardInfoDto.class));
		query.setFirstResult( (pageNo-1) * pageSize );
		query.setMaxResults( pageSize );
		
		return query.list();
	}

	@Override
	public int getCardInfoCount(String accCode, String status) {
		
		String sql = "SELECT count(*) as count FROM " 
				+ TABLE_PREFIX + accCode;
		if( status != null )
			sql += " WHERE status = :status";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("count", StandardBasicTypes.INTEGER);
		if( status != null )
			query.setString("status", status);
		
		logger.debug( "getCardInfoCount(), sql=" + query.getQueryString() );
		return (Integer)query.list().get(0);
	}
	

	@Override
	public int getCardInfoCount(CardInfoDto cardInfo) {
		
		String tableName = TABLE_PREFIX + cardInfo.getAccCode();
		String sql = "SELECT count(*) as count FROM "
				+ tableName + " a WHERE 1=1";
		if( !StringUtils.isEmpty(cardInfo.getSeqNo()) )
			sql += " AND seqNo= :seqNo";
		if( !StringUtils.isEmpty(cardInfo.getNotes()) )
			sql += " AND notes= :cardNo";
		if( !StringUtils.isEmpty(cardInfo.getStatus()) )
			sql += " AND status= :status";
		if( cardInfo.getAmount() > 0 )
			sql += " AND amount= :amount";
		if( !StringUtils.isEmpty( cardInfo.getCardNoIn() ) )
			sql += " AND card_no_in = :cardNoIn";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("count", StandardBasicTypes.INTEGER);
		if( !StringUtils.isEmpty(cardInfo.getSeqNo()) )
			query.setString("seqNo", cardInfo.getSeqNo());
		if( !StringUtils.isEmpty(cardInfo.getNotes()) )
			query.setString("cardNo", cardInfo.getNotes());
		if( !StringUtils.isEmpty(cardInfo.getStatus()) )
			query.setString("status", cardInfo.getStatus());
		if( cardInfo.getAmount() > 0 )
			query.setInteger("amount", cardInfo.getAmount());
		if( !StringUtils.isEmpty( cardInfo.getCardNoIn() ) )
			query.setString( "cardNoIn", cardInfo.getCardNoIn() );
		
		logger.debug( "getCardInfoCount(), sql=" + query.getQueryString() );
		return (Integer)query.list().get(0);
	}
	
	
	@Override
	public int getRowNumByCardNoOut( String accCode, String cardNoOut ) {
		
		String sql = "SELECT rownum FROM "
				+ " ( SELECT ROW_NUMBER() OVER ( ORDER BY SEQNO ) as rownum, notes"
				+ "   FROM " + TABLE_PREFIX + accCode
				+ " ) a WHERE notes = :cardNoOut";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addScalar("rownum", StandardBasicTypes.INTEGER);
		query.setString("cardNoOut", cardNoOut);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if( list == null || list.size() == 0 )
			return 0;
		return (Integer)list.get(0);
	}
}
