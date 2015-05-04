package com.fet.carpool.serv.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.fet.carpool.serv.persistence.NotificationMapping;

public class BaseService {

	protected static byte[] SERIALNO_IN_KEY = "616161615252525243434343"
			.getBytes();
	protected static byte[] SERIALNO_OUT_KEY = "111122223333444455556666"
			.getBytes();
	protected static byte[] SERIALNO_MAC_KEY = "AAAABBBBCCCCDDDDEEEEFFFF"
			.getBytes();

	protected static byte[] SERIALNO_IN_IV = new byte[] { 6, 1, 3, 7, 8, 2, 4,
			5 };
	protected static byte[] SERIALNO_OUT_IV = new byte[] { 9, 9, 0, 0, 8, 8, 7,
			7 };
	protected static byte[] SERIALNO_MAC_IV = new byte[] { 0, 0, 0, 0, 0, 0, 0,
			0 };

	public static final DateFormat NOTIFICATION_DATE_FORMAT = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	public static final String URL_GCM_HTTPS = "https://android.googleapis.com/gcm/send";
	// public static final String GCM_API_KEY =
	// "AIzaSyC1h5wYzxMbHkeNbqk054A7GzxvphAWhpE"; // damon
	//public static final String GCM_API_KEY = "AIzaSyAL5K9ypS-qBK1X6MYs2fNawsBlCBuROmI"; // Chien-ting
	public static final String GCM_API_KEY = "AIzaSyB5cvdwpPfufetUU33ZnE6t5s92CH6dsuk"; // Jay
	
	public static final String GCM_NOTIFICATION_CHARSET = "UTF-8";
	//private HttpClient client = new DefaultHttpClient();
	protected Logger logger;

	public BaseService() {
		super();
		logger = Logger.getLogger(getClass());
	}

	protected void sendDelayNotificationToGcm(final String jsonString,
			final int seconds) throws IOException {

		new Thread() {

			public void run() {
				try {
					Thread.sleep(seconds * 1000);
				} catch (InterruptedException e) {
				}

				try {
					sendNotificationToGcm(jsonString);
				} catch (IOException e) {
					logger.error("send notification failed", e);

				}
			}
		}.start();
	}

	/**
	 * ?Åø??çHttpClient??Ñ‚?ùSSLPeerUnverifiedException: peer not authenticated?ùÂ?ÇÂ∏∏
	 * ‰∏çÁî®ÂØºÂÖ•SSLËØÅ‰π¶
	 * 
	 * @author shipengzhi(shipengzhi@sogou-inc.com)
	 * 
	 */
	public static class WebClientDevWrapper {
		@SuppressWarnings("deprecation")
		public static HttpClient wrapClient(HttpClient base) {
			try {
				SSLContext ctx = SSLContext.getInstance("TLS");
				X509TrustManager tm = new X509TrustManager() {
					@SuppressWarnings("unused")
					public void checkClientTrusted(X509Certificate[] xcs,
							String string) throws CertificateException {
					}

					@SuppressWarnings("unused")
					public void checkServerTrusted(X509Certificate[] xcs,
							String string) throws CertificateException {
					}

					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] chain,
							String authType)
							throws java.security.cert.CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] chain,
							String authType)
							throws java.security.cert.CertificateException {
						// TODO Auto-generated method stub
						
					}
				};
				X509HostnameVerifier verifier = new X509HostnameVerifier() {
					

					@SuppressWarnings("unused")
					public void verify(String string, X509Certificate xc)
							throws SSLException {
					}

					@Override
					public void verify(String string, String[] strings,
							String[] strings1) throws SSLException {
					}

					
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void verify(String arg0, SSLSocket arg1)
							throws IOException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void verify(String arg0,
							java.security.cert.X509Certificate arg1)
							throws SSLException {
						// TODO Auto-generated method stub
						
					}
				};
				ctx.init(null, new TrustManager[] { tm }, null);
				SSLSocketFactory ssf = new SSLSocketFactory(ctx);
				ssf.setHostnameVerifier(verifier);
				ClientConnectionManager ccm = base.getConnectionManager();
				SchemeRegistry sr = ccm.getSchemeRegistry();
				sr.register(new Scheme("https", ssf, 443));
				return new DefaultHttpClient(ccm, base.getParams());
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	protected void sendNotificationToGcm(String jsonString) throws IOException {

		if (logger.isDebugEnabled())
			logger.debug("gcm request body = " + jsonString);

		String result = "";
		// HttpClient client = WebClientDevWrapper.wrapClient();
		// client = WebClientDevWrapper.wrapClient(client);
		HttpClient client = new DefaultHttpClient();
		client = WebClientDevWrapper.wrapClient(client); 
		try {

			HttpPost post = new HttpPost(URL_GCM_HTTPS);
			post.addHeader("Content-Type", "application/json; charset="
					+ GCM_NOTIFICATION_CHARSET);
			post.addHeader("Authorization", "key=" + "AIzaSyB5cvdwpPfufetUU33ZnE6t5s92CH6dsuk");
			HttpEntity postEntity = null;
			try {

				postEntity = new StringEntity(jsonString, "UTF-8");
				logger.debug(jsonString);

			} catch (UnsupportedEncodingException e) {
				logger.error("stringEntity error", e);
			} finally {
				logger.info("URL_GCM_HTTPS=" + URL_GCM_HTTPS);
				logger.info("key=" + "AIzaSyB5cvdwpPfufetUU33ZnE6t5s92CH6dsuk");
				logger.info("msg=" + jsonString);
			}
			post.setEntity(postEntity);
			StringBuffer sb = new StringBuffer();
			logger.info("client execute");
			HttpResponse resp = client.execute(post);

			String line = null;
			logger.info("BufferedReader");
			BufferedReader r = new BufferedReader(new InputStreamReader(resp
					.getEntity().getContent()));

			while ((line = r.readLine()) != null) {
				sb.append(line + "\n");
			}

			r.close();
			logger.info("readLine");
			result = new String(sb.toString().getBytes("UTF-8"));
			int responseCode = resp.getStatusLine().getStatusCode();

			if (responseCode == 200)
				logger.info("send message OK");
			else
				logger.warn("send message failed, statusCode=" + responseCode
						+ ", message=" + result);

		} catch (MalformedURLException e) {
			logger.debug("MalformedURLException:" + e.getMessage(), e);

		} catch (IOException e) {
			logger.debug("IOException:" + e.getMessage(), e);

		}

		client.getConnectionManager().shutdown();
		//sendNotificationToGcm2(jsonString);

	}
	
	

	protected void sendDelayNotification(final String msg,
			final String msgType, final List<NotificationMapping> regList,
			final int seconds) {

		if(regList.size() == 0){
			logger.error("regList.size() == 0" );
			return ;
		}
		new Thread() {

			public void run() {
				try {
					Thread.sleep(seconds * 1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}

				sendNotification(msg, msgType, regList);
			}
		}.start();
	}

	protected void sendNotification(String msg, String msgType,
			List<NotificationMapping> regList) {

		if (regList == null)
			return;

		JSONArray regIds = new JSONArray();
		String accountId = "";
		for (NotificationMapping regDef : regList) {
			if ("android".equalsIgnoreCase(regDef.getClientType())) {
				regIds.put(regDef.getRegistrationId());

				if (accountId.length() == 0)
					accountId = regDef.getAccountId();
			}
		}

		if (regIds.length() == 0)
			return;

		// set message data
		try {
			JSONObject data = new JSONObject();
			data.put("message",
					URLEncoder.encode(msg, GCM_NOTIFICATION_CHARSET));
			data.put("timestamp", NOTIFICATION_DATE_FORMAT.format(new Date()));
			data.put("messageType", msgType);
			data.put("accountId", accountId);

			JSONObject json = new JSONObject();
			json.put("data", data);
			json.put("registration_ids", regIds);
			String jsonString = json.toString();
			if (logger.isDebugEnabled())
				logger.debug("gcm request body = " + jsonString);

			// send to ...
			logger.info("send notification to " + accountId);
			sendNotificationToGcm(jsonString);

		} catch (Exception e) {
			logger.error("send notification failed", e);
		}
	}
}
