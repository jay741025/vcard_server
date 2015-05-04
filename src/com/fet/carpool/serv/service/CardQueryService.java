package com.fet.carpool.serv.service;

import java.util.List;

import com.fet.carpool.serv.dto.CardInfoDto;

public interface CardQueryService {

	public List<CardInfoDto> getCardInfoList( String accCode, String status, int pageNo, int pageSize );
	public List<CardInfoDto> getCardInfoList( CardInfoDto cardInfo, int pageNo, int pageSize );
	
	public int getCardInfoCount( String accCode, String status );
	public int getCardInfoCount( CardInfoDto cardInfo );
	
	public int getRowNumByCardNoOut( String accCode, String cardNo );
}
