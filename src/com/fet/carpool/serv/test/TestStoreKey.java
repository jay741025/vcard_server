package com.fet.carpool.serv.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.fet.carpool.serv.util.DesUtil;
import com.fet.carpool.serv.util.StringUtil;

public class TestStoreKey {

	protected static byte[] SERIALNO_IN_KEY = "616161615252525243434343".getBytes();
	protected static byte[] SERIALNO_OUT_KEY = "111122223333444455556666".getBytes();
	protected static byte[] SERIALNO_MAC_KEY = "AAAABBBBCCCCDDDDEEEEFFFF".getBytes();
	
	protected static byte[] SERIALNO_IN_IV = new byte[] { 6, 1, 3, 7, 8, 2, 4, 5 };
	protected static byte[] SERIALNO_OUT_IV = new byte[] { 9, 9, 0, 0, 8, 8, 7, 7 };
	protected static byte[] SERIALNO_MAC_IV = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
	
	private static final String key_file = "d:/temp/keyfile";
	
	public static void main(String[] args) throws  Exception {
		runEncrypt(args);
	}
	
	public static void runEncrypt(String[] args) throws  Exception {
		
		ObjectInputStream input = new ObjectInputStream( new FileInputStream( key_file ) );
		Key key = (Key) input.readObject();
		byte[] data = "abcdefgh".getBytes();
		input.close();
		
		// 
		byte[] c1 = DesUtil.Des3EncodeCBC(SERIALNO_IN_KEY, SERIALNO_IN_IV, data);
		
		// 
		IvParameterSpec ivSpec = new IvParameterSpec(SERIALNO_IN_IV);
		Cipher cipher = Cipher.getInstance("DESEDE/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] c2 = cipher.doFinal(data);
		
		System.out.println( "c1=" + StringUtil.toHexString(c1) );
		System.out.println( "c2=" + StringUtil.toHexString(c2) );
	}
	
	public static void outputKey(String[] args) throws  Exception {
		
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESEDE");
		KeySpec keySpec = new DESedeKeySpec( SERIALNO_IN_KEY );
		Key secretKey = keyFactory.generateSecret(keySpec);
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(key_file));
		output.writeObject(secretKey);
		
		output.close();
	}

}
