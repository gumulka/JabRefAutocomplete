/**
 * 
 */
package de.gumulka.jabref.controller;

import de.gumulka.jabref.view.Settingspanel;

/**
 * @author Fabian Pflug
 *
 */
public class Settingscontroller {
	
	private Settingspanel sp;
	
	public Settingscontroller(Settingspanel sp){
		this.sp = sp;
	}
	
	public void store(){
		sp.setVisible(false);
	}
	
}
