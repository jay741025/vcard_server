package com.fet.carpool.serv.test;

import sun.misc.BASE64Encoder;

public class Test3to4 {

	public static final char[] ARRAY1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	
	public static void main(String[] args) {
		
		String str = "A1v";
		byte[] data = str.getBytes();
		
		for( byte b : data ) {
			System.out.println( Integer.toBinaryString(b) );
		}
		
		System.out.println( base64(data) );
		System.out.println( base65(data) );
//		System.out.println( "------");
//		
//		int b1 = ( oldValue[0] >>> 2 ) & 0x3f;
//		int b2 = ( oldValue[0] << 4 ) & 0x30 | ( oldValue[1] >>> 4 ) & 0x0f;
//		int b3 = ( oldValue[1] << 2 ) & 0x3C | ( oldValue[2] >>> 6 ) & 0x03;
//		int b4 = 0x3f & oldValue[2];
//
//		System.out.println( Integer.toBinaryString(b1) );
//		System.out.println( Integer.toBinaryString(b2) );
//		System.out.println( Integer.toBinaryString(b3) );
//		System.out.println( Integer.toBinaryString(b4) );
	}
	
	public static String base64( byte[] data ) {
		
		return new BASE64Encoder().encode(data);
	}
	public static String base65( byte[] data ) {
		
		if( data == null )
			return null;
		if( data.length == 0 )
			return "";
		if( data.length % 3 != 0 )
			throw new IllegalArgumentException();
		
		int pos;
		int len = data.length;
		int b1, b2, b3, b4;
		StringBuilder s = new StringBuilder( len * 4 / 3 + 4 );
		for ( pos = 0 ; pos < len ; pos ++ ) {
			b1 = b2 = b3 = b4 = -1;
//			int b1 = ( data[0] >>> 2 ) & 0x3f;
//			int b2 = ( data[0] << 4 ) & 0x30 | ( data[1] >>> 4 ) & 0x0f;
//			int b3 = ( data[1] << 2 ) & 0x3C | ( data[2] >>> 6 ) & 0x03;
//			int b4 = 0x3f & data[2];
		}
		return null;
		
//		char c1 = ARRAY1[b1];
//		char c2 = ARRAY1[b2];
//		char c3 = ARRAY1[b3];
//		char c4 = ARRAY1[b4];
//		return new String( new char[] {c1,c2,c3,c4} );
	}

}
