/**
 * 
 */
package de.gumulka.jabref.model;

import java.util.Set;

import net.sf.jabref.BibtexEntry;

/**
 * @author Fabian Pflug
 *
 */
public class Result {
	
		private BibtexEntry entry;
		private BibtexEntry second;
		
		
		public Result(BibtexEntry old, BibtexEntry found){
			this.entry = old;
			this.second = found;
			second.setField("bibtexkey", entry.getField("bibtexkey"));
		}


		public String getField(String name) {
			return second.getField(name);
		}
		
		public void setField(String name, String value) {
			second.setField(name, value);
		}
		
		public Set<String> getAllFields() {
			return second.getAllFields();
		}
		
		public BibtexEntry getEntry() {
			return entry;
		}
}
