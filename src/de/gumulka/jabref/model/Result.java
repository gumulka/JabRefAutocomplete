/**
 * 
 */
package de.gumulka.jabref.model;

import java.util.LinkedList;
import java.util.List;

import net.sf.jabref.BibtexEntry;
import de.gumulka.jabref.online.Provider;

/**
 * @author Fabian Pflug
 * 
 */
public class Result {

	private BibtexEntry entry;
	private List<BibtexEntry> found = new LinkedList<BibtexEntry>();
	

	public Result(BibtexEntry original) {
		this.entry = original;
	}

	public void add(BibtexEntry online) {
		if (online == null) {
			return;
		}
		if (entry.getField("bibtexkey") == null)
			online.setField("bibtexkey", "");
		else
			online.setField("bibtexkey", entry.getField("bibtexkey"));

		for(BibtexEntry e : found) {
			if(isMergable(e, online)) {
				merge(e,online);
				return;
			}
		}
		found.add(online);
	}
	
	private void merge(BibtexEntry to, BibtexEntry from) {
		for(String s : from.getAllFields()) {
			if(to.getField(s) == null)
				to.setField(s, from.getField(s));
		}
	}
	
	private boolean isMergable(BibtexEntry first, BibtexEntry second) {
		String firstField, secondField;
		for(String s : first.getAllFields()) {
			secondField = second.getField(s);
			firstField = first.getField(s);
			if(secondField == null) 
				continue;
			if(firstField == null) 
				continue;
			secondField = Provider.clean(secondField);
			firstField = Provider.clean(firstField);
			if(firstField.equalsIgnoreCase(secondField))
				continue;
			if(s.equalsIgnoreCase("author")) {
				firstField = Provider.formatAuthors(firstField);
				secondField = Provider.formatAuthors(secondField);
				if(firstField.equalsIgnoreCase(secondField))
					continue;
			}
			return false;
		}
		return first.getType().equals(second.getType());
	}

	public boolean hasEntrys() {
		return found.size()>0;
	}

	public boolean hasNewInformation() {
		boolean conflict = false;
		if(found.size()==1) { // if there is just one possible new Entry, we can automatically merge the new Information in.
			BibtexEntry online = found.get(0);
			for (String s : online.getAllFields()) {
				String first = entry.getField(s);
				if (first == null || first.length() == 0) {
					if (Settings.getInstance().isAutocopy())
						entry.setField(s, online.getField(s));
					else {
						conflict = true;
					}
				} else if (!first.equalsIgnoreCase(online.getField(s))) {
					conflict = true;
				}
			}
			conflict |= !entry.getType().equals(online.getType());
		}
		if(found.size()>1) // else: better safe then sorry
			return true;
		return conflict;
	}

	public List<BibtexEntry> getAllNew() {
		return found;
	}
	
	public BibtexEntry getEntry() {
		return entry;
	}
}
