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

	public Result getResult() {
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
