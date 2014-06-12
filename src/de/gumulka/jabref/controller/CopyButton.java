package de.gumulka.jabref.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.gumulka.jabref.model.Result;
import de.gumulka.jabref.view.Resultview;

public class CopyButton implements ActionListener {

	private Result result;
	private Resultview view;

	public CopyButton(Result result, Resultview res) {
		this.result = result;
		this.view = res;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String field = arg0.getActionCommand();
		if (field.equals("COPYALL")) { // copy all the fields
			for(String s : result.getAllFields()) {
				String tmp = view.getField(s);
				if(tmp!=null)
					result.getEntry().setField(s, view.getField(s));
			}
			result.getEntry().setType(result.getType());
			view.remove();
		} else if(field.equals("TYPECHANGE")) { // copy just the type
			result.getEntry().setType(result.getType());
			view.check(field);
		} else { // copy one field
			result.getEntry().setField(field, view.getField(field));
			view.check(field);
		}
	}

}
