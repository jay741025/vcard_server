package com.fet.carpool.serv.managedBean;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

public class BaseManagedBean {

	protected Logger logger;

	public BaseManagedBean() {
		super();
		logger = Logger.getLogger(getClass());
	}

	protected Object getBeanById(String beanId) {
		ApplicationContext ctx = FacesContextUtils
				.getWebApplicationContext(FacesContext.getCurrentInstance());
		return ctx.getBean(beanId);
	}

	protected void addMessage(String msg) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				msg, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	protected void addMessage(Severity severity, String msg) {
		FacesMessage message = new FacesMessage(severity, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	protected void addInvalidInputValueMessage( String msg) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
