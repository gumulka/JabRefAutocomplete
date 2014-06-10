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
		if (field.equals("copyall")) {
			for(String s : result.getAllFields())
				result.getEntry().setField(s, result.getField(s));
		} else {
			result.getEntry().setField(field, result.getField(field));
		}
	}

}
