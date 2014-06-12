/**
 * 
 */
package de.gumulka.jabref.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.gumulka.jabref.model.Settings;
import de.gumulka.jabref.online.Search;

/**
 * @author Fabian Pflug
 *
 */
public class Settingspanel extends JPanel {

	private Settings set = Settings.getInstance();
	
	private JCheckBox debug, autofill, version;
	
	private List<JCheckBox> sites = new LinkedList<JCheckBox>(); 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2744344866701301982L;

	public Settingspanel() {

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		
		debug = new JCheckBox();
		debug.setText("Send debug information to developer");
		this.add(debug,c);
		
		c.gridy++;
		autofill = new JCheckBox();
		autofill.setText("Automatically fill empty fields");
		this.add(autofill,c);
		
		c.gridy++;
		version = new JCheckBox();
		version.setText("Check for new Versions");
		this.add(version,c);

		c.gridy++;
		this.add(new JLabel("Search this sites:"), c);
		
		JCheckBox tmp;
		for(Search s: Search.getAllSites()) {
			tmp = new JCheckBox();
			tmp.setText(s.getSearchName());
			sites.add(tmp);
			c.gridy++;
			this.add(tmp,c);
		}
		
		c.gridy++;
	}
	
	public boolean isSet(String site) {
		for(JCheckBox b: sites) {
			if(b.getText().equalsIgnoreCase(site))
				return b.isSelected();
		}
		return false;
	}
	
	public boolean isAutocopy() {
		return autofill.isSelected();
	}
	
	public boolean isSendDebug() {
		return debug.isSelected();
	}
	
	public boolean isCheckVersion() {
		return version.isSelected();
	}
	
	public void refresh () {
		debug.setSelected(set.isSendDebug());
		autofill.setSelected(set.isAutocopy());
		version.setSelected(set.isCheckVersion());		
		for(JCheckBox box : sites) {
			box.setSelected(set.isSet(box.getText()));
		}
	}
	

}
