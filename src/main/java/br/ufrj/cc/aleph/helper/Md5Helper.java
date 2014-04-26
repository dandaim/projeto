package br.ufrj.cc.aleph.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Helper {
	
	public static String md5( final String text ){
		
		MessageDigest md = null;
		
		try {
			
			md = MessageDigest.getInstance("MD5");
			
		} catch ( NoSuchAlgorithmException e ) {
			
			e.printStackTrace();			
		}
		
		BigInteger hash = new BigInteger( 1, md.digest( text.getBytes() ) );
		
		return hash.toString( 16 );
	}
	
}
