package de.gumulka.jabref.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.gumulka.jabref.model.Result;

public class CopyButton implements ActionListener {

	private Result result;

	public CopyButton(Result result) {
		this.result = result;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String field = arg0.getActionCommand();
		if (field.equals("COPYALL")) {
			for(String s : result.getAllFields())
				result.getEntry().setField(s, result.getField(s));
			result.getEntry().setType(result.getType());
		} else if(field.equals("TYPECHANGE")) {
			result.getEntry().setType(result.getType());
		} else {
			result.getEntry().setField(field, result.getField(field));
		}
	}

}
