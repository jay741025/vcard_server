package com.fet.carpool.serv.service;

import com.fet.carpool.serv.dto.CardInfoDto;

public interface CardProcessService {

	public CardInfoDto getActivedCardInfoAndReserve( String accCode );
	public CardInfoDto getActivedCardInfoAndReserve( String accCode, int amount );
	
	//public CardInfoDto getCardInfo( String accCode, String seqNo, String cardNo );
}
