package com.fet.carpool.serv.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Table;

import com.fet.carpool.serv.persistence.Account;

public class AnnotationTableName {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		
		Class<Account> cla = Account.class;
		Annotation table = cla.getAnnotation(Table.class);
		
		if( table == null ) {
			System.out.println( "no such annotation");
			return;
		}
		
		Class annoClass = table.annotationType();
		Method m = annoClass.getMethod( "name", null);
		System.out.println( m.invoke(table, null) );
//		Field f = annoClass.getDeclaredField("name");
//		System.out.println( f.get(annoClass) );
	}

}
