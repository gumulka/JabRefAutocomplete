/**
 * 
 */
package de.gumulka.jabref.model;

import net.sf.jabref.BibtexEntry;

/**
 * @author Fabian Pflug
 *
 */
public class Search extends Thread {

	protected BibtexEntry entry;
	
	public Search(BibtexEntry e) {
		entry = e;
	}
	
	public void search(){
		
	}
	
	public void getResult() {
	}
	
}
