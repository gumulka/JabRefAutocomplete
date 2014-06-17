package de.gumulka.jabref.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sf.jabref.BibtexEntry;
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
		String tmp = arg0.getActionCommand();
		int id = Integer.parseInt(tmp.substring(0, tmp.indexOf(';')));
		BibtexEntry online = result.getAllNew().get(id);
		String field = tmp.substring(tmp.indexOf(';')+1);
		if (field.equals("COPYALL")) { // copy all the fields
			for(String s : online.getAllFields()) {
				String tmp2 = view.getField("" + id + ";" + s);
				if(tmp2!=null)
					result.getEntry().setField(s, tmp2);
			}
			result.getEntry().setType(online.getType());
			view.remove();
		} else if(field.equals("TYPECHANGE")) { // copy just the type
			result.getEntry().setType(online.getType());
			view.check(tmp);
		} else { // copy one field
			result.getEntry().setField(field, view.getField(tmp));
			view.check(tmp);
		}
	}

}
