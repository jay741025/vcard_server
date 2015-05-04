package com.fet.carpool.serv.dto;

public class PageInfo {

	private int totalRecordCount;
	private int totalPageCount;
	private int pageNo;
	private int pageSize;


	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	protected void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
	public void resetTotalPageCount() {
		
		totalPageCount = 0;
		
		if( pageSize < 1 )
			throw new IllegalArgumentException( "Invalid pageSize : " + pageSize );
		if( totalRecordCount < 0 )
			throw new IllegalArgumentException( "Invalid recordCount : " + totalRecordCount );
		
		totalPageCount = totalRecordCount / pageSize;
		if( totalPageCount * pageSize < totalRecordCount )
			totalPageCount += 1;
		
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
