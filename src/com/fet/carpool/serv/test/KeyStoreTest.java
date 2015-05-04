package com.fet.carpool.serv.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import com.fet.carpool.serv.util.StringUtil;

public class KeyStoreTest {

	protected static byte[] SERIALNO_IN_KEY = "616161615252525243434343".getBytes();
	protected static byte[] SERIALNO_IN_IV = new byte[] { 6, 1, 3, 7, 8, 2, 4, 5 };
	
	private static final String ksFilename = "d:/temp/ks1"; 
	
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, Exception {
		
		KeyStore ks = KeyStore.getInstance( "jceks" );
		InputStream is = new FileInputStream( ksFilename );
		ks.load(is, "1234567".toCharArray());
		
//		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESEDE");
//		KeySpec keySpec = new DESedeKeySpec( SERIALNO_IN_KEY );
//		SecretKey secretKey = keyFactory.generateSecret(keySpec);
//		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(secretKey);
		
//		ks.setEntry("testkey2", skEntry, new KeyStore.PasswordProtection("765432".toCharArray()));
//		ks.store(new FileOutputStream(ksFilename), "1234567".toCharArray());
		System.out.println( ks.isKeyEntry("testkey1") );
		System.out.println( ks.isKeyEntry("testkey2") );
		Key in_key = ks.getKey("testkey2", "765432".toCharArray());
		
		byte[] data = "abcdefgh".getBytes();
		IvParameterSpec ivSpec = new IvParameterSpec(SERIALNO_IN_IV);
		
		Cipher cipher = Cipher.getInstance("DESEDE/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, in_key, ivSpec);
		byte[] result = cipher.doFinal(data);
		System.out.println( StringUtil.toHexString(result));
	}

}
