/**
 * 
 */
package de.gumulka.jabref.model;

import java.util.Set;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.BibtexEntryType;

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

		public BibtexEntryType getType() {
			return second.getType();
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
