/**
 * 
 */
package de.gumulka.jabref.view;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JFrame;

/**
 * @author gumulka
 *
 */
public class MyBox extends Box {

	private JFrame top;
	
	public MyBox(int axis, JFrame top) {
		super(axis);
		this.top = top;
	}

	public void repaint() {
		super.repaint();
		top.repaint();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 90903821057276132L;

	public void remove(Component comp)  {
		super.remove(comp);
		if(this.getComponentCount()==1) {
			top.setVisible(false);
			top.dispose();
		}
	}
	
}
