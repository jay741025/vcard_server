package com.fet.carpool.serv.dto;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceHeader {

	public static final DateFormat HEADER_DATE_FORMAT = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss SSS");
	public static final NumberFormat HEADER_SEQ_FORMAT = new DecimalFormat(
			"00000");

	private Date responseDatetime;

	private String responseStatusCode;
	private String responseStatusDesc;

	public ServiceHeader() {
		super();
		responseStatusCode = "OK";
	}

	public void resetResponseDatetime() {
		responseDatetime = new Date();
	}

	public String getResponseDatetimeString() {
		return responseDatetime == null ? null : HEADER_DATE_FORMAT
				.format(responseDatetime);
	}

	public String getResponseStatusCode() {
		return responseStatusCode;
	}

	public void setResponseStatusCode(String responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}

	public String getResponseStatusDesc() {
		return responseStatusDesc;
	}

	public void setResponseStatusDesc(String responseStatusDesc) {
		this.responseStatusDesc = responseStatusDesc;
	}

}
