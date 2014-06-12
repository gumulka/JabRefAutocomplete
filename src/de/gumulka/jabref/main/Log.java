/**
 * 
 */
package de.gumulka.jabref.main;

import java.net.SocketTimeoutException;

/**
 * @author Fabian Pflug
 *
 */
public class Log {

	public static void error(Exception e) {
		if(e instanceof SocketTimeoutException)
			return;
		System.out.println("Logged error:");
		e.printStackTrace();
	}
	
	public static void error(Exception e, String message) {
		System.out.println("Logged error (with message):");
		System.out.println("message: " + message);
		e.printStackTrace();
	}
	
	public static void debug(String message) {
		System.out.println(message);
	}
}
