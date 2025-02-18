package com.shq.oper.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AES加密，解密工具类
 * 
 * @author tjun
 * 
 */
public final class AES {
	private static Logger log = LoggerFactory.getLogger(AES.class);
	
	private static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
		try {
			final byte[] finalKey = new byte[16];
			int i = 0;
			for(byte b : key.getBytes(encoding)) {
				finalKey[i++%16] ^= b;
			}			
			return new SecretKeySpec(finalKey, "AES");
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 对应mysql加密，select HEX(AES_ENCRYPT(str, aesKey))
	 * 
	 * @param str 加密字符串
	 * @param aesKey aes密钥
	 * @return
	 */
	public static String encryptMysql(String str, String aesKey){
		try {
			final Cipher cipher = Cipher.getInstance("AES");// AES/ECB/PKCS5Padding
			cipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(aesKey, "UTF-8"));
			return Hex.encodeHexString(cipher.doFinal(str.getBytes("UTF-8"))).toUpperCase();
		} catch (Exception e) {
			log.error("encryptMysql error", e);
		}
		return null;
	}
	
	/**
	 * 对应mysql解密，select AES_DECRYPT(UNHEX(str), aesKey)
	 * @param str 解密字符串
	 * @param aesKey aes密钥
	 * @return
	 */
	public static String decryptMysql(String str, String aesKey){
		try {
			final Cipher cipher = Cipher.getInstance("AES");// AES/ECB/PKCS5Padding
			cipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(aesKey, "UTF-8"));
			return new String(cipher.doFinal(Hex.decodeHex(str.toCharArray())), "UTF-8");
		} catch (Exception e) {
			log.error("decryptMysql error", e);
		}
		return null;
	}

}
