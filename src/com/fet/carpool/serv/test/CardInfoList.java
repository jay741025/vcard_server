package com.fet.carpool.serv.test;

import java.util.AbstractList;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.fet.carpool.serv.dao.BaseDao;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.service.CardQueryService;

public class CardInfoList extends AbstractList<CardInfoDto> {

	private int pageSize;
	private CardInfoDto queryParam;
	
	private CardInfoDto[] buffer;
	private int currentPageNo;
	private int currentRecordCount;

	protected Logger logger;
	
	public CardInfoList() {
		this( BaseDao.DEFAULT_PAGE_SIZE );
	}
	
	public CardInfoList( int pageSize ) {
		super();
		logger = Logger.getLogger(getClass());
		setPageSize(pageSize);
		currentPageNo = 0;
		currentRecordCount = 0;
	}


	
	@Override
	public CardInfoDto get(int index) {
		
		int pageNo = ( index + 1 ) / pageSize;
		int remainder = ( index + 1 ) % pageSize;
		if( remainder > 0 )
			pageNo++;
		
		if( pageNo != currentPageNo ) {
			currentPageNo = pageNo;
			reloadBuffer();
		}
		
		int pos = index % pageSize;
		if( buffer[pos] == null ) {
			logger.warn( "supprised reload action, index=" + index + ", currentPageNo=" + currentPageNo );
			reloadBuffer();	// 不該發生
		}
		
		return buffer[pos];
	}
	
	private void resizeBuffer() {
		buffer = new CardInfoDto[ pageSize ];
	}
	
	private void reloadBuffer() {
		logger.debug( "CardInfoList.reloadBuffer() invoked, currentpageNo=" + currentPageNo ); 
		List<CardInfoDto> cardList = getCardQueryService().getCardInfoList(queryParam, currentPageNo, pageSize);
		for( int i = 0 ; cardList != null && i < cardList.size() ; i++ )
			buffer[i] = cardList.get(i);
	}
	
	private CardQueryService getCardQueryService() {
		ApplicationContext applicationContext = (ApplicationContext)ContextLoader.getCurrentWebApplicationContext();
		return (CardQueryService) applicationContext.getBean("cardQueryService");
	}

	@Override
	public int size() {
		if( currentRecordCount == 0 ) {
			currentRecordCount = getCardQueryService().getCardInfoCount(queryParam);
		}
		return currentRecordCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {

		this.pageSize = pageSize;
		resizeBuffer();		
	}

	
	public CardInfoDto getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(CardInfoDto queryParam) {
		if( queryParam == null )
			throw new NullPointerException();
		currentRecordCount = 0;
		this.queryParam = queryParam;
	}
	
}
