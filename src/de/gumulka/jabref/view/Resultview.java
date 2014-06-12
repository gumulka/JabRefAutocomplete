/**
 * 
 */
package de.gumulka.jabref.view;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.gumulka.jabref.controller.CopyButton;
import de.gumulka.jabref.model.Result;

/**
 * @author Fabian Pflug
 * 
 */
public class Resultview extends JPanel {
	
	private JButton confirmall;

	private Container father;
	
	private List<JButton> buttons;
	private List<JTextArea> fields;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5787180668883470799L;

	public Resultview(Result result, Container father) {
		this.father = father;
		buttons = new LinkedList<JButton>();
		fields = new LinkedList<JTextArea>();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		this.setLayout(new GridBagLayout());
		JLabel title;
		JTextArea orign, copy;
		Set<String> keys = result.getEntry().getAllFields();
		keys.addAll(result.getAllFields());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridwidth = 3;
		this.add(new JLabel(result.getEntry().getCiteKey()), c);
		c.gridx = 3;
		confirmall = new JButton("import dataset");
		confirmall.setActionCommand("COPYALL");
		confirmall.addActionListener(new CopyButton(result, this));
		this.add(confirmall, c);
		c.gridwidth = 1;
		c.gridy++;
		if(!result.getEntry().getType().equals(result.getType())) {
			c.gridx = 0;
			this.add(new JLabel("TYPE"),c);
			c.gridx = 1;
			this.add(new JLabel(result.getType().getName()),c);
			c.gridx = 2;
			this.add(new JLabel(result.getType().getName()),c);
			c.gridx = 3;
			JButton confirm = new JButton("change Type");
			buttons.add(confirm);
			confirm.setActionCommand("TYPECHANGE");
			confirm.addActionListener(new CopyButton(result,this));
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
			orign.setLineWrap(true);
			orign.setEditable(false);
			copy = new JTextArea(result.getField(s));
			copy.setLineWrap(true);
			copy.setEditable(true);
			copy.setName(s);
			fields.add(copy);
			c.gridx = 0;
			this.add(title, c);
			c.gridx = 1;
			this.add(orign, c);
			c.gridx = 2;
			this.add(copy, c);
			c.gridx = 3;
			JButton confirm = new JButton("import new");
			buttons.add(confirm);
			confirm.setActionCommand(s);
			confirm.addActionListener(new CopyButton(result, this));
			this.add(confirm, c);
			c.gridy++;
		}
	}
	
	public void check(String field) {
		JButton removed = null;
		for(JButton but : buttons) {
			if(but.getActionCommand().equalsIgnoreCase(field))
			{
				removed = but;
				break;
			}
		}
		buttons.remove(removed);
		removed.setEnabled(false);
		if(buttons.size()==0)
			remove();
	}
	
	public String getField(String name) {
		for(JTextArea text : fields) {
			if(text.getName().equals(name))
				return text.getText();
		}
		return null;
	}
	
	public void remove() {
		father.remove(this);
		father.invalidate();
		father.repaint();
	}
}
