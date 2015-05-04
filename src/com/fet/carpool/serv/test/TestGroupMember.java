package com.fet.carpool.serv.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TestGroupMember {

	public static void main(String[] args) throws JSONException, UnsupportedEncodingException {
		
		JSONObject json = new JSONObject();
		
		JSONArray members = new JSONArray();
		members.put("12345678BBBB");
		members.put("12345678CCCC");
		members.put("12345678DDDD");
		json.put("Member", members);
		
		System.out.println( json.toString() );
		System.out.println( URLEncoder.encode(json.toString(),"UTF-8") );
	}

}
