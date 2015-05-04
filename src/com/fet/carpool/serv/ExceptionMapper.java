package com.fet.carpool.serv;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.fet.carpool.serv.dto.ServiceHeader;

@Provider
public class ExceptionMapper implements ExceptionMapper<Exception> {

	protected Logger logger;
	
		
	public ExceptionMapper() {
		super();
		logger = Logger.getLogger(getClass());
	}


	@Override
	public Response toResponse(Exception e) {
		
		logger.warn( "VCardExceptionMapper...", e );
		
		ServiceHeader header = new ServiceHeader();
		header.resetResponseDatetime();
		
		String exceptionMessage = e.getMessage();
		ResponseBuilder rb;
		if( e instanceof WebApplicationException ) {
			// replace default response
			WebApplicationException webex = (WebApplicationException)e;
			Response originalResponse = webex.getResponse();
			
			rb = Response.status(originalResponse.getStatus());
			header.setResponseStatusCode( String.valueOf( originalResponse.getStatus() ) );
			header.setResponseStatusDesc(originalResponse.getStatusInfo().getReasonPhrase() );
		} else {
			// the other exception
			rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			header.setResponseStatusCode("exception");
		
			if( e instanceof Exception ) {
				header.setResponseStatusDesc(exceptionMessage);
			} else {
				if( exceptionMessage == null || exceptionMessage.length()  <= 40 )
					header.setResponseStatusDesc(exceptionMessage);
				else
					header.setResponseStatusDesc( e.getMessage().substring(0,40) + "..." );	// no more detail
			}
		}
		
		rb.entity(header);
		rb.type("application/json;charset=UTF-8");  
		return rb.build();
	}

}
