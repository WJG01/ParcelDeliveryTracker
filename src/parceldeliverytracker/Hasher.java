package parceldeliverytracker;

import java.security.MessageDigest;
import java.util.Base64;

import org.apache.commons.codec.binary.Hex;



public class Hasher {
	
	//+sha256(String): String
	public static String sha256(String input)
	{
		return hash(input,"SHA-256");
	}
	//+sha384(String): String
	public static String sha384(String input)
	{
		return hash(input,"SHA-384");
	}
	//+sha512(String): String
	public static String sha512(String input)
	{
		return hash(input,"SHA-512");
	}

	
	//+md5(String) :String -->public method
	public static String md5(String input)
	{
		return hash(input,"MD5");
	}
	
	//overload MD5 method with salt
	public static String md5(String input,byte[] salt)
	{
		return hash(input,"MD5",salt);
	}
	
	//-hash(String): String --> private method
	private static String hash(String input,String algorithm)
	{
		var hashCode = "";
		try {
			
			var md = MessageDigest.getInstance(algorithm);
			
			//fetch the input byte arr into MessageDigest instance
			md.update(input.getBytes());
			
			
			//digest the input byte arr
			byte[] hashBytes = md.digest();

			//convert hashBytes to String(Base64)
			hashCode = Base64.getEncoder().encodeToString(hashBytes);
			
			//convert to Hex format 
			//SOURCE: https://commons.apache.org/proper/commons-codec/
			hashCode= Hex.encodeHexString(hashBytes);
			
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return hashCode;
	}
	
	
	//overload md5 method with salt
	private static String hash(String input,String algorithm,byte[] salt)
	{
		var hashCode = "";
		try {
			
			var md = MessageDigest.getInstance(algorithm);
			
			//fetch the input byte arr into MessageDigest instance
			md.update(input.getBytes());
			md.update(salt);
			
			
			//digest the input byte arr
			byte[] hashBytes = md.digest();
			//convert hashBytes to String(Base64)
			hashCode = Base64.getEncoder().encodeToString(hashBytes);
			
			//convert to Hex format 
			//SOURCE: https://commons.apache.org/proper/commons-codec/
			hashCode= Hex.encodeHexString(hashBytes);
			
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return hashCode;
	}

}
