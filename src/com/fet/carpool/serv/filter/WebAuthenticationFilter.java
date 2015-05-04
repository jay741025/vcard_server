package com.fet.carpool.serv.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class WebAuthenticationFilter
 */
@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }
					, urlPatterns = { "/main/func/*" })
public class WebAuthenticationFilter implements Filter {

	protected static final String COOKIE_DOMAIN = ".seed.net.tw";
	protected static final String COOKIE_NAME = "SEEDNET_AUTH";
	
	protected Logger logger;
	
    /**
     * Default constructor. 
     */
    public WebAuthenticationFilter() {
    	logger = Logger.getLogger(getClass());
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		
		
		HttpServletRequest req = (HttpServletRequest) request;
		Cookie[] cookies = req.getCookies();
		logger.debug( "param=" + req.getParameter("param"));
		
		// find cookie about authentication
		Cookie targetCookie = null;
		for( int i = 0 ; cookies != null && i < cookies.length ; i++ ) {
			Cookie c = cookies[i];
			if( c.getName().equals( COOKIE_NAME) ) {
				targetCookie = c;
				break;
			}
		}
		
		if( targetCookie == null ) {
			logger.debug( "No Authentication Cookie" );
			chain.doFilter(request, response);
			return;
		}
		

		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
