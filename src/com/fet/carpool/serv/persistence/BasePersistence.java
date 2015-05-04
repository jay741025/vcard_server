package com.fet.carpool.serv.persistence;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Table;

public class BasePersistence {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getTableName() {
		
		Annotation table = getClass().getAnnotation(Table.class);
		if( table == null ) 
			return null;
		
		Class annoClass = table.annotationType();
		Method m;
		try {
			m = annoClass.getMethod( "name", null);
			return (String) m.invoke(table, null);
		} catch (Exception e) {
			return null;
		}
		
	}
}
