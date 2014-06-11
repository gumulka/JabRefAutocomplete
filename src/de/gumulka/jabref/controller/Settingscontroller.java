/**
 * 
 */
package de.gumulka.jabref.controller;

import de.gumulka.jabref.model.Settings;
import de.gumulka.jabref.online.Search;
import de.gumulka.jabref.view.Settingspanel;

/**
 * @author Fabian Pflug
 *
 */
public class Settingscontroller {
	
	private Settingspanel sp;
	private Settings set = Settings.getInstance();
	
	public Settingscontroller(Settingspanel sp){
		this.sp = sp;
	}
	
	public void store(){
		set.setAutocopy(sp.isAutocopy());
		set.setSendDebug(sp.isSendDebug());
		for(Search s: Search.getAllSites()) {
			set.setSite(s.getSearchName(), sp.isSet(s.getSearchName()));
		}
		set.save();
	}
	
}
