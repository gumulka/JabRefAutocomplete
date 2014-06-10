/**
 * 
 */
package de.gumulka.jabref.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.jabref.BibtexEntry;

/**
 * @author Fabian Pflug
 *
 */
public class Result {
	
		private BibtexEntry entry;
		private Map<String,String> entrys = new HashMap<String,String>();
		
		
		public Result(BibtexEntry entry){
			this.setEntry(entry);
		}


		public String getField(String name) {
			return entrys.get(name);
		}
		
		public void setField(String name, String value) {
			entrys.put(name, value);
		}
		
		public Set<String> getAllFields() {
			return entrys.keySet();
		}
		
		public BibtexEntry getEntry() {
			return entry;
		}


		private void setEntry(BibtexEntry entry) {
			this.entry = entry;
		}
		
}
