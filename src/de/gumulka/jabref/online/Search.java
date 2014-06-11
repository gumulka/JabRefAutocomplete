/**
 * 
 */
package de.gumulka.jabref.online;

import net.sf.jabref.BibtexEntry;
import de.gumulka.jabref.model.Result;

/**
 * @author Fabian Pflug
 *
 */
public class Search extends Thread {

	protected BibtexEntry entry;
	protected Result result = null;
	
	public Search(BibtexEntry e) {
		entry = e;
	}
	
	public void search()  {
		start();
	}


	protected static String formatAuthors(String authors) {
		authors.replace("AND", "and");
		String[] splittet = authors.split(" and ");
		String ret = "";
		int index;
		for(String s : splittet) {
			index = s.indexOf(',');
			if(index>0) {
				String Vorname = s.substring(index+1).trim();
				String Nachname = s.substring(0, index).trim();
				ret += '"' + Vorname + " " + Nachname + "\" ";
			}
			else
				ret += '"' + s + "\" ";
		}
		return ret.trim();
	}
	
	public Result getResult() {
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
