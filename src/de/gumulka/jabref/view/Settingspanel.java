/**
 * 
 */
package de.gumulka.jabref.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Fabian Pflug
 *
 */
public class Settingspanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2744344866701301982L;

	public Settingspanel() {

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());

		JLabel none = new JLabel("Sorry, there are no Settings at the Moment.");
		c.gridx = 0;
		c.gridy = 1;
		this.add(none, c);
		
	}
	

}
