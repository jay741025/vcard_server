package com.fet.carpool.serv.rws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fet.carpool.serv.dto.EventDto;
import com.fet.carpool.serv.dto.SendingMessageDto;
import com.fet.carpool.serv.persistence.Event;
import com.fet.carpool.serv.service.EventService;




@Component("eventWs")
@Path("event")
public class EventWs {

	@Context
	private HttpServletResponse httpServletResponse;
	@SuppressWarnings("unused")
	@Context
	private HttpServletRequest httpServletRequest;

	@Autowired
	private EventService eventServ;

	protected Logger logger;

	@POST
	@Path("lineRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String lineRequest(EventDto event)
			throws UnsupportedEncodingException {

		logger = Logger.getLogger(getClass());
		logger.debug("lineRequest");
		logger.debug("Result:" + event.getResult().toString());
		String data =event.getResult().toString();
		eventServ.saveEvent(data);
		String[] split = data.split(",");
		String to ="";
		String text = "已收到您的訊息，客服人員儘快回覆您!";
		for(int i=0 ; i< split.length;i++){
			if(split[i].indexOf("from=")>-1 && split[i].split("=").length>1 && "".equals(to) ){				
				to =split[i].split("=")[1];
				
			}
			if(split[i].indexOf("STKVER")>-1){
				text = "已收到您的貼圖，客服人員儘快回覆您!";
			}
			
		}
		
		SendingMessageDto sendingMessage = new SendingMessageDto();
		sendingMessage.setTo(to);
		sendingMessage.setText(text);
		@SuppressWarnings("unused")
		HashMap<String, Object> result =eventServ.sendingMessage(sendingMessage);
		return "OK";
	}
	
	@GET
	@Path("getData")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getData( @QueryParam("id") String id ) {		
		List<Event> result = eventServ.list(id);
		return result;
		
	}
	
	@GET
	@Path("getUser")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUser( @QueryParam("mids") String mids ) {
		
		logger = Logger.getLogger(getClass());
		logger.debug("getUser");
		logger.debug("mids:" + mids);
		
		String https_url = "https://channel-apis.line.naver.jp/v1/profiles?mids="+mids;
		URL url;
		String result = "";
		
		try {

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			
			logger.debug("setRequestMethod:" + "GET");
			((HttpURLConnection) con).setRequestMethod("GET");
			
			con.setRequestProperty(
					"X-Line-ChannelToken",
					"i9xDJOOD5Uhz8mwB2vMKLfHYd80XCFDl8nchkZdIuaE4IWMcQTd7QnY+Px61tlFvF/6+viTa86o3EzVr3KLXL7PoCzbpAnZXq+akUHHoOyfPxwO4xHt4EVvODR+UtQPHAs0rV9/akxVJIi94k6agpeorKn3IckxDqhiLNhDQMVk=");
			
			logger.debug("setRequestProperty:"
					+ "X-Line-ChannelToken-i9xDJOOD5Uhz8mwB2vMKLfHYd80XCFDl8nchkZdIuaE4IWMcQTd7QnY+Px61tlFvF/6+viTa86o3EzVr3KLXL7PoCzbpAnZXq+akUHHoOyfPxwO4xHt4EVvODR+UtQPHAs0rV9/akxVJIi94k6agpeorKn3IckxDqhiLNhDQMVk=");
			
			httpServletResponse.setStatus(con.getResponseCode());
			result = print_content(con);

		} catch (MalformedURLException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}

		logger.debug(result);
		return result;
		
	}
	
	@POST
	@Path("sendingMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String sendingMessage(SendingMessageDto sendingMessage)
			throws UnsupportedEncodingException {

		HashMap<String, Object> result =eventServ.sendingMessage(sendingMessage);
		httpServletResponse.setStatus((int) result.get("status"));
		return (String) result.get("streamReader");

	}

	private String print_content(HttpsURLConnection con) {

		String result = "";
		if (con != null) {

			try {

				System.out.println("****** Content of the URL ********");
				BufferedReader br ;
				if (con.getResponseCode() == 200) {					
					
					br = new BufferedReader(new InputStreamReader(
							con.getInputStream(), "UTF-8"));
		        } else {
		        	br = new BufferedReader(new InputStreamReader(
							con.getErrorStream(), "UTF-8"));
		        }
				

				String input;

				while ((input = br.readLine()) != null) {
					System.out.println(input);
					result += input;
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;

	}
}
