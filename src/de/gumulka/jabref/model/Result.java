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
		
		private boolean conflict;
		
		public Result(BibtexEntry old, BibtexEntry found){
			this.entry = old;
			this.second = found;

			conflict = false;
			
			if(second == null) {
				return;
			}
			
			if(entry.getField("bibtexkey") != null)
				second.setField("bibtexkey", entry.getField("bibtexkey"));

			for(String s : second.getAllFields()) {
				String first = entry.getField(s);
				if(first==null || first.length()==0) {
					if(Settings.getInstance().isAutocopy()) 
						entry.setField(s, second.getField(s));
					else {
						conflict = true;
					}
				}else if(!first.equalsIgnoreCase(second.getField(s))) {
					conflict = true;
				}
			}

			conflict |= !entry.getType().equals(second.getType());
			
		}
		
		public boolean hasNewInformation() {
			return conflict;
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
