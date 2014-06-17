/**
 * 
 */
package de.gumulka.jabref.main;

import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

import de.gumulka.jabref.model.Settings;

import net.sf.jabref.BibtexEntry;

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
	
	public static void notFound(BibtexEntry e) {
		if (Settings.getInstance().isSendDebug()) {
			try {
				Connection con = Jsoup.connect(
						"http://www.jabref.gummu.de/debug.php").method(
						Method.POST);
				con.data("author", "" + e.getField("author"));
				con.data("title", "" + e.getField("title"));
				con.data("doi", "" + e.getField("doi"));
				con.execute();
			} catch (Exception ex) {
				// we don't want to handle this.
			}
		}
	}
}
