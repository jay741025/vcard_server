package com.fet.carpool.serv.rws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.dto.TransactionDto;
import com.fet.carpool.serv.dto.TransactionListBean;
import com.fet.carpool.serv.service.TransactionService;

@Component("transWs")
@Path("trans")
public class TransactionWs {

    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TransactionService transServ;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionListBean list(@QueryParam("accountId") String accountId) {
        List<TransactionDto> transList = transServ.listByAccountId(accountId);
        TransactionListBean response = new TransactionListBean();
        response.setAccountId(accountId);
        response.setCount(transList.size());
        response.setTransList(transList);
        return response;
    }
}
