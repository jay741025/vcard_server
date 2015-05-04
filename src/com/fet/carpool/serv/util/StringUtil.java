package com.fet.carpool.serv.util;

public class StringUtil {

	private static final char[] HEX_CHARS = { '0','1','2','3','4','5','6','7',
		'8','9','A','B','C','D','E','F'
	};
	
	public static String toHexString( byte[] value ) {
		
		if( value == null )
			return "";
		
		StringBuffer s = new StringBuffer();
		for( int i = 0 ; i < value.length ; i++ ) {
			
			int high = ( value[i] & 0xf0 ) >> 4;
			int low = value[i] & 0x0f;
			s.append(HEX_CHARS[high]);
			s.append(HEX_CHARS[low]);
		}
		return s.toString();
	}
	
	public static byte[] toBytes( String s ) {
		
		if( s == null )
			return new byte[] {};
		
		if( s.length() % 2 != 0 )
			throw new IllegalArgumentException( "invalid length of input string" );
		
		char[] c = s.toUpperCase().toCharArray();
		byte[] result = new byte[ c.length / 2 ];
		int pos = 0;
		for( int i = 0 ; i < c.length ; i += 2 ) {
			
			int high, low;
			if( c[i] >= '0' && c[i] <= '9' ) 
				high = ( c[i] - '0' ) << 4;
			else if( c[i] >= 'A' && c[i] <= 'F' )
				high = ( c[i] - 'A' + 10 ) << 4;
			else 
				throw new IllegalArgumentException( "invalid string format" );
			
			if( c[i+1] >= '0' && c[i+1] <= '9' ) 
				low = ( c[i+1] - '0' );
			else if( c[i+1] >= 'A' && c[i+1] <= 'F' )
				low = ( c[i+1] - 'A' + 10 );
			else 
				throw new IllegalArgumentException( "invalid string format" );
			
			result[pos++] = (byte)( high | low );
		}
		return result;
	}
	

}
