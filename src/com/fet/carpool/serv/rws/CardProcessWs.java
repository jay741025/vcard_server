package com.fet.carpool.serv.rws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.Exception;
import com.fet.carpool.serv.dto.CardInfoDto;
import com.fet.carpool.serv.service.CardProcessService;

@Component("cardProcessWs")
@Path("cardProcess")
public class CardProcessWs extends MyVcardWs {

    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CardProcessService cardProcessService;
    
    @GET
    @Path("getAvailableCardNo")
    @Produces(MediaType.TEXT_PLAIN)
    /**
     * 
     * @param accCode
     * @param amount	指定要尋找的儲值卡面額, 0 為不限制
     * @return
     */
    public String getAvailableCardNo( @QueryParam("accCode") @DefaultValue("000002") String accCode,
    		@QueryParam("amount") @DefaultValue("0") int amount ) {
    	
		CardInfoDto cardInfo = cardProcessService.getActivedCardInfoAndReserve(accCode, amount );
		if( cardInfo != null )
			return cardInfo.getNotes();
		
		throw new Exception("No available card" );
    }
}
