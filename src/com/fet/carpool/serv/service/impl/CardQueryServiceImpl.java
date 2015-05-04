package com.fet.carpool.serv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dao.CardInfoDao;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.CardQueryService;
import com.fet.carpool.serv.util.DesUtil;
import com.fet.carpool.serv.util.StringUtil;

@Transactional
@Service("cardQueryService")
public class CardQueryServiceImpl extends BaseService implements
		CardQueryService {

	@Autowired
	private CardInfoDao cardInfoDao;
	
	@Override
	public List<CardInfoDto> getCardInfoList(String accCode, String status, int pageNo,
			int pageSize) {
		
		List<CardInfoDto> cardList = cardInfoDao.getCardInfoList(accCode, status, pageNo, pageSize);
		calcCardNoOriginal( cardList );
		return cardList;
	}

	@Override
	public int getCardInfoCount(String accCode, String status) {
		
		return cardInfoDao.getCardInfoCount(accCode, status);
	}

	@Override
	public int getRowNumByCardNoOut( String accCode, String cardNoOut ) {
		return cardInfoDao.getRowNumByCardNoOut(accCode, cardNoOut);
	}

	
	
	@Override
	public int getCardInfoCount(CardInfoDto cardInfo) {
		return cardInfoDao.getCardInfoCount(cardInfo);
	}

	@Override
	public List<CardInfoDto> getCardInfoList(CardInfoDto cardInfo, int pageNo,
			int pageSize) {
		List<CardInfoDto> cardList = cardInfoDao.getCardInfoList(cardInfo, pageNo, pageSize);
		calcCardNoOriginal( cardList );
		return cardList;
	}
	
	protected void calcCardNoOriginal( List<CardInfoDto> cardList ) throws Exception {
		
		for( CardInfoDto cardInfo : cardList ) {
			if( cardInfo.getCardNoIn() != null ) {
				try {
					byte[] inData = StringUtil.toBytes(cardInfo.getCardNoIn());
					byte[] oriData = DesUtil.Des3DecodeCBC(SERIALNO_IN_KEY, SERIALNO_IN_IV, inData);
					cardInfo.setCardNoOriginal( StringUtil.toHexString(oriData));
				} catch (Exception e) {
					throw new Exception("calc cardNooriginal failed", e);
				}
			}
		}
	}
}
