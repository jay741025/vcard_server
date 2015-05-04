package com.fet.carpool.serv.test;

import com.fet.carpool.serv.util.DesUtil;
import com.fet.carpool.serv.util.StringUtil;

public class TestOddParity {

	protected static byte[] SERIALNO_IN_KEY = "616161615252525243434343".getBytes();
	protected static byte[] SERIALNO_OUT_KEY = "111122223333444455556666".getBytes();
	protected static byte[] SERIALNO_MAC_KEY = "AAAABBBBCCCCDDDDEEEEFFFF".getBytes();
	
	protected static byte[] SERIALNO_IN_IV = new byte[] { 6, 1, 3, 7, 8, 2, 4, 5 };
	protected static byte[] SERIALNO_OUT_IV = new byte[] { 9, 9, 0, 0, 8, 8, 7, 7 };
	protected static byte[] SERIALNO_MAC_IV = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

	public static void main(String[] args) throws Exception {

		byte[] data = "abcdefgh".getBytes();
		
		byte[] c1 = DesUtil.Des3EncodeCBC(SERIALNO_IN_KEY, SERIALNO_IN_IV, data);
		System.out.println( StringUtil.toHexString(c1) );
		
		DesUtil.oddParity( SERIALNO_IN_KEY );
		byte[] c2 = DesUtil.Des3EncodeCBC(SERIALNO_IN_KEY, SERIALNO_IN_IV, data);
		System.out.println( StringUtil.toHexString(c2) );

	}

}
