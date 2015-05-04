package com.fet.carpool.serv.rws;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.dto.PageInfo;
import com.fet.carpool.serv.dto.QueryResultBean;
import com.fet.carpool.serv.service.CardQueryService;

@Component("cardQueryWs")
@Path("cardQuery")
public class CardQueryWs extends MyVcardWs {

    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpServletRequest httpServletRequest;
    
    @Autowired
    private CardQueryService cardQueryService;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public QueryResultBean list(@QueryParam("accCode") @DefaultValue("000002") String accCode, 
    		@QueryParam("pageNo") @DefaultValue("1") int pageNo, 
    		@QueryParam("pageSize") @DefaultValue("15") int pageSize ) {
        
    	int totalRecords = cardQueryService.getCardInfoCount(accCode, null);
		List resultList = cardQueryService.getCardInfoList(accCode, null, pageNo, pageSize);
    	
    	PageInfo pageInfo = new PageInfo();
    	pageInfo.setPageNo(pageNo);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setTotalRecordCount(totalRecords);
    	pageInfo.resetTotalPageCount();

    	QueryResultBean result = new QueryResultBean();
    	result.setPageInfo(pageInfo);
    	result.setResultList( resultList);
    	return result;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
    @Path("listByCardNo")
    @Produces(MediaType.APPLICATION_JSON)
    public QueryResultBean listByCardNo(@QueryParam("accCode") @DefaultValue("000002") String accCode, 
    		@QueryParam("cardNo") String cardNoOut, 
    		@QueryParam("pageSize") @DefaultValue("15") int pageSize ) {

    	if( StringUtils.isEmpty(cardNoOut) )
    		throw new IllegalArgumentException("cardNo is empty" );
    	if( pageSize < 1 )
    		throw new IllegalArgumentException( "Invalid pageSize : " + pageSize );

    	int rowNum = cardQueryService.getRowNumByCardNoOut(accCode, cardNoOut);
    	int totalRecords = cardQueryService.getCardInfoCount(accCode, null);
    	int pageNo;
    	List resultList;
    	
    	if( rowNum == 0 ) {	
    		// not found
    		pageNo = 1;
    		resultList = new ArrayList();
    	} else {
    		pageNo = rowNum / pageSize;
    		if( pageNo * pageSize < rowNum )
    			pageNo += 1;
    		resultList = cardQueryService.getCardInfoList(accCode, null, pageNo, pageSize);
    	}
    	
    	PageInfo pageInfo = new PageInfo();
    	pageInfo.setPageNo(pageNo);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setTotalRecordCount(totalRecords);
    	pageInfo.resetTotalPageCount();

    	QueryResultBean result = new QueryResultBean();
    	result.setPageInfo(pageInfo);
    	result.setResultList( resultList);
    	return result;
    }
}
