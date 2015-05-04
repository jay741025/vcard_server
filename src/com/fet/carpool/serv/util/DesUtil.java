package com.fet.carpool.serv.util;

import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesUtil {

	public static byte[]  Des3EncodeCBC( byte[] key, byte[] iv, byte[] data ) throws Exception {
		
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESEDE");
		KeySpec keySpec = new DESedeKeySpec( key );
		Key secretKey = keyFactory.generateSecret(keySpec);
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		
		Cipher cipher = Cipher.getInstance("DESEDE/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		
		return cipher.doFinal(data);
	}
	
	public static byte[]  Des3DecodeCBC( byte[] key, byte[] iv, byte[] data ) throws Exception {
		
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESEDE");
		KeySpec keySpec = new DESedeKeySpec( key );
		Key secretKey = keyFactory.generateSecret(keySpec);
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		
		Cipher cipher = Cipher.getInstance("DESEDE/CBC/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
		
		return cipher.doFinal(data);
	}
	
	public static void oddParity( byte[] b ) {
		
		if( b == null )
			return;
		
		for( int i = 0 ; i < b.length ; i++ ) {
			int bitCount = Integer.bitCount(b[i]);
			if( ( bitCount & 0x01 ) == 0 )
				b[i] = (byte)( b[i] ^ 0x01 );
		}
	}
}
