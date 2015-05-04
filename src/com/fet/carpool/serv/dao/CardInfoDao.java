package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.CardInfoDto;

public interface CardInfoDao {

	public CardInfoDto queryBySeqNo( String accCode, String seqNo );
	public CardInfoDto queryByCardNoIn( String accCode, String cardNoIn );
	
	public void updateCardStatus( CardInfoDto cardInfo );
	
	public CardInfoDto getActivedCardInfo( String accCode );
	public CardInfoDto getActivedCardInfo( String accCode, int amount );
	
	public List<CardInfoDto> getCardInfoList( String accCode, String status, int pageNo, int pageSize );
	public List<CardInfoDto> getCardInfoList( CardInfoDto cardInfo, int pageNo, int pageSize );
	
	public int getCardInfoCount( String accCode, String status );
	public int getCardInfoCount( CardInfoDto cardInfo );
	
	public int getRowNumByCardNoOut( String accCode, String cardNoOut );
}
