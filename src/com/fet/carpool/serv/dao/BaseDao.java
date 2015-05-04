package com.fet.carpool.serv.dao;

import org.apache.log4j.Logger;

public class BaseDao {

	public static final int MAX_PAGE_SIZE = 500;
	public static final int DEFAULT_PAGE_SIZE = 15;
	
	public static final int QUERY_CONDITION_NO = 2;
	public static final int QUERY_CONDITION_YES = 1;
	public static final int QUERY_CONDITION_BOTH = 0;
	
    protected Logger logger;


    public BaseDao() {
        super();
        logger = Logger.getLogger(getClass());
    }

    protected void checkPageNo( int pageNo ) {
    	if( pageNo < 1 )
    		throw new IllegalArgumentException("Invalid pageNo : " + pageNo );
    }
    protected void checkPageSize( int pageSize ) {
    	if( pageSize < 1|| pageSize > MAX_PAGE_SIZE )
    		throw new IllegalArgumentException("Invalid pageSize : " + pageSize );
    }
}