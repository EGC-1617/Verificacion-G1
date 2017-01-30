package test.java;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import main.java.Authority;
import main.java.AuthorityImpl;
import main.java.VerificationException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/*En esta clase estarán implementados todos los métodos que ya han sido implementos en la clase VerificacionTest, pero 
en este caso, todas las pruebas se harán con una base de datos local, ya que en la clase VerificacionTest los métodos
hacen uso de una base de datos remota. */
public class VerificacionTestLocal {
	
	private static Authority auth = new AuthorityImpl();
	
	private static List<String> idUtilizados = new ArrayList<String>();
	
	private static final Integer[] tokenAuxList = {234546, 134567, 183745, 123454, 345673, 123456, 
			435343, 678798, 675434, 564354, 120338, 127508, 219240, 231958, 
			264907, 301200, 301415, 318851, 328237, 333555, 366710, 376217, 382413, 
			406463, 409921, 436780, 458841, 461513, 530897, 589116, 590265, 590815, 
			593252, 656720, 746976, 830375, 865247, 869061, 885540, 907197, 909246, 
			961864, 976931, 982612};
	    
	private static Integer calculateToken(Integer votationId){
	
		Integer token = 0;
		
		checkId(votationId);
		
		String binaryInteger = Integer.toBinaryString(votationId);
		char[] numberByNumber = binaryInteger.toCharArray();
		
		int j = 0;
		for(int i=numberByNumber.length-1; 0 <= i; i--){
			String binDigit = Character.toString(numberByNumber[i]);
			Integer digit = new Integer(binDigit);
			if(digit > 0){
				token += digit*tokenAuxList[tokenAuxList.length-1-j];
				
			}
			j++;
		}
	
	return token*17;
	
	}  
	
	
	private static Connection getDatabaseConnection(){
		String USER = "root";
	    String PASS = "admin";  
	    String DB_URL = "jdbc:mysql://127.0.0.1:3306/verificacion";
	    
	    Connection conn = null;
	    
	    try{
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	    	conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    }catch (Exception e){
	    	e.printStackTrace();
	    }
	    
	    return conn;	    
	    
	}
	
	
	private static void checkId(Integer votationId) {
		assert votationId <= 999999998;
		
	}
	
	/*
	 *Cuando hacemos uso del método postKey obtenemos las claves publica y privada según el
	 *id de votación pasado.
	 */
	
	@Test
	public void testPostKeyLocal(){
		String votationId;
		Integer token;
		boolean res;
		
		votationId = (new BigInteger(25, new SecureRandom())).toString();
		
		token = calculateToken(new Integer(votationId));
		
		res = auth.postKeyLocal(votationId, token);
		
		idUtilizados.add(votationId);
		
		Assert.assertTrue(res == true);		
		
	}
	
	//holaaaa
	
}
