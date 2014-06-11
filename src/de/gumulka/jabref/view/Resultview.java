/**
 * 
 */
package de.gumulka.jabref.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.gumulka.jabref.controller.CopyButton;
import de.gumulka.jabref.model.Result;

/**
 * @author gumulka
 * 
 */
public class Resultview extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5787180668883470799L;

	public Resultview(Result result) {

		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());
		Component orign, copy, title;
		Set<String> keys = result.getEntry().getAllFields();
		keys.addAll(result.getAllFields());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridwidth = 3;
		this.add(new JLabel(result.getEntry().getCiteKey()), c);
		c.gridx = 3;
		JButton confirmall = new JButton("import dataset");
		confirmall.setActionCommand("COPYALL");
		confirmall.addActionListener(new CopyButton(result));
		this.add(confirmall, c);
		c.gridwidth = 1;
		c.gridy++;
		if(!result.getEntry().getType().equals(result.getType())) {
			c.gridx = 0;
			this.add(new JLabel("TYPE"),c);
			c.gridx = 1;
			this.add(new JTextArea(result.getType().getName()),c);
			c.gridx = 2;
			this.add(new JTextArea(result.getType().getName()),c);
			c.gridx = 3;
			JButton confirm = new JButton("change Type");
			confirm.setActionCommand("TYPECHANGE");
			confirm.addActionListener(new CopyButton(result));
			this.add(confirm, c);
			c.gridy++;
		}
			
		for (String s : keys) {
			if (result.getField(s) == null)
				continue;
			if (result.getField(s).equalsIgnoreCase(
					result.getEntry().getField(s)))
				continue;
			title = new JLabel(s);
			orign = new JTextArea(result.getEntry().getField(s));
			((JTextArea) orign).setLineWrap(true);
			copy = new JTextArea(result.getField(s));
			((JTextArea) copy).setLineWrap(true);
			c.gridx = 0;
			this.add(title, c);
			c.gridx = 1;
			this.add(orign, c);
			c.gridx = 2;
			this.add(copy, c);
			c.gridx = 3;
			JButton confirm = new JButton("import new");
			confirm.setActionCommand(s);
			confirm.addActionListener(new CopyButton(result));
			this.add(confirm, c);
			c.gridy++;
		}
	}
}
