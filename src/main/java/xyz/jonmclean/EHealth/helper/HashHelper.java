package xyz.jonmclean.EHealth.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class HashHelper {
	
	public static String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			
			for(int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			
			return hexString.toString();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String byteArrayToString(byte[] array) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i< array.length; i++) {
			builder.append((char) array[i]);
		}
		
		return builder.toString();
	}
	
}
