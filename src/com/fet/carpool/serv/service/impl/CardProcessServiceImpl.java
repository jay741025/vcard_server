package com.fet.carpool.serv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.CardInfoDao;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.service.BaseService;
import com.fet.carpool.serv.service.CardProcessService;

@Transactional
@Service("cardProcessService")
public class CardProcessServiceImpl extends BaseService implements
		CardProcessService {

	@Autowired
	private CardInfoDao cardInfoDao;
	
	@Override
	public CardInfoDto getActivedCardInfoAndReserve(String accCode) {
		
		return getActivedCardInfoAndReserve( accCode, 0 );
	}

	@Override
	public CardInfoDto getActivedCardInfoAndReserve(String accCode, int amount) {
		
		CardInfoDto cardInfo = cardInfoDao.getActivedCardInfo(accCode, amount );
		// reserve data (update status)
		if( cardInfo != null ) {
			cardInfo.setStatusToActiveReserved();
			cardInfoDao.updateCardStatus(cardInfo);
		}
		
		return cardInfo;
	}

//	@Override
//	public CardInfoDto getCardInfo(String accCode, String seqNo, String cardNo) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
