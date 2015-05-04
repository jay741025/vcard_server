package com.fet.carpool.serv.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fet.carpool.serv.dao.EventDao;
import com.fet.carpool.serv.dto.SendingMessageDto;
import com.fet.carpool.serv.persistence.Event;
import com.fet.carpool.serv.service.EventService;


@Transactional
@Service("eventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventDao;

    protected Logger logger;
    
    @Override
    public List<Event> list(String id ) {

        List<Event> event = eventDao.list(id);        
        return event;
    }

    @Override
    public void saveEvent(String result) {
        
    	Event event = new Event();
    	event.setResult(result);
    	event.setDatetime(new Date());
    	eventDao.saveEvent(event);
    }
    
    public HashMap<String, Object> sendingMessage(SendingMessageDto sendingMessage) {
        
    	logger = Logger.getLogger(getClass());
		logger.debug("sendingMessage");
		logger.debug("to:" + sendingMessage.getTo());
		logger.debug("text:" + sendingMessage.getText());
		String https_url = "https://channel-apis.line.naver.jp/v1/events";
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		HttpClient client= new DefaultHttpClient();
		int status =500;
		String streamReader ="";
		try {

			HttpPost post = new HttpPost(https_url);

			post.addHeader("Content-Type", "application/json; charser=UTF-8");
			post.addHeader("X-Line-ChannelToken", "i9xDJOOD5Uhz8mwB2vMKLfHYd80XCFDl8nchkZdIuaE4IWMcQTd7QnY+Px61tlFvF/6+viTa86o3EzVr3KLXL7PoCzbpAnZXq+akUHHoOyfPxwO4xHt4EVvODR+UtQPHAs0rV9/akxVJIi94k6agpeorKn3IckxDqhiLNhDQMVk=");
			HttpEntity postEntity = null;
			try {
				String postData="{" + "\"to\":[\"" + sendingMessage.getTo() + "\"],"
						+ "\"toChannel\":1383378250,"
						+ "\"eventType\":\"138311608800106203\"," + "\"content\":{"
						+ "  \"contentType\":1," + "  \"toType\":1,"
						+ "  \"text\":\"" + URLDecoder.decode(sendingMessage.getText(),"UTF-8") + "\"" + "}"
						+ "}" ;
				postEntity = new StringEntity(postData, "UTF-8");
				logger.debug(postData);
				
			} catch (UnsupportedEncodingException e) {
				
			}
			post.setEntity(postEntity);
			StringBuffer sb = new StringBuffer();
			HttpResponse resp = client.execute(post);
			status =resp.getStatusLine().getStatusCode();
			//httpServletResponse.setStatus(status);
			
			String line = null;
			BufferedReader r = new BufferedReader(new InputStreamReader(resp
					.getEntity().getContent()));

			while ((line = r.readLine()) != null) {
				sb.append(line + "\n");
			}
			streamReader = new String(sb.toString().getBytes("UTF-8"));
			

		} catch (MalformedURLException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}

		logger.debug(result);
		client.getConnectionManager().shutdown();
		result.put("status", status);
		result.put("streamReader", streamReader);
		
		return result;
    }

}
