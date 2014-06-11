package de.gumulka.jabref.main;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sf.jabref.BasePanel;
import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.MetaData;
import net.sf.jabref.external.PushToApplication;
import net.sf.jabref.plugin.core.JabRefPlugin;
import de.gumulka.jabref.controller.Settingscontroller;
import de.gumulka.jabref.model.Result;
import de.gumulka.jabref.online.ACM;
import de.gumulka.jabref.online.IEEE;
import de.gumulka.jabref.online.Search;
import de.gumulka.jabref.online.SpringerLink;
import de.gumulka.jabref.view.Resultview;
import de.gumulka.jabref.view.Settingspanel;

/**
 * The main class to communicate between JabRef and Autocomplete.
 * 
 * @author Fabian Pflug
 */
public class Fetch extends JabRefPlugin implements PushToApplication {

	private Settingspanel sp;
	private Settingscontroller sc;
	private JFrame waiting;
	private JFrame result;

	private List<Result> results = new LinkedList<Result>();

	public Fetch() {
		sp = new Settingspanel();
		sc = new Settingscontroller(sp);
	}

	public String getName() {
		return "Push to JabRef Autocomplete";
	}

	public String getApplicationName() {
		return "JabRef Autocomplete";
	}

	public String getTooltip() {
		return "Starts updating information with online sites.";
	}

	public Icon getIcon() {
		return new ImageIcon(getClass().getResource(
				"/resources/jabRef_icon_16q.png"));
	}

	public String getKeyStrokeName() {
		return "Push to JabRef Autocomplete";
	}

	public JPanel getSettingsPanel() {
		return sp;
	}

	public void storeSettings() {
		sc.store();
	}

	public void pushEntries(BibtexDatabase database, BibtexEntry[] entrys,
			String keyString, MetaData metaData) {

		waiting = new JFrame("Syncing with Servers");
		waiting.setSize(250, 100);
		waiting.setLocationRelativeTo(null);
		waiting.add(new JLabel("Syncing " + entrys.length
				+ " publications.\n Please wait."));
		waiting.setVisible(true);
		for (BibtexEntry e : entrys) {
			System.out.println("Searching for: \nAuthor: "
					+ e.getField("author") + "\nTitle: " + e.getField("title"));
			Search acm = new ACM(e);
			acm.search();
			Search springer = new SpringerLink(e);
			springer.search();
			Search ieee = new IEEE(e);
			ieee.search();
			Result res = ieee.getResult();
			if (res != null)
				results.add(res);
			res = acm.getResult();
			if (res != null)
				results.add(res);
			res = springer.getResult();
			if (res != null)
				results.add(res);
		}
	}

	public void operationCompleted(BasePanel panel) {
		waiting.setVisible(false);
		result = new JFrame("Results");
		result.setLocationRelativeTo(panel);
		if (results.size() > 0) {
			result.setSize(1000, 500);
			Box temp = new Box(BoxLayout.Y_AXIS);
			temp.add(new JLabel("Results:"));
			for (Result r : results) {
				temp.add(new Resultview(r));
			}
			JScrollPane sp = new JScrollPane(temp);
			result.add(sp);
		} else {
			result.setSize(500, 200);
			result.add(new JLabel("There are no results."));
		}
		result.setVisible(true);
		results.clear();
	}

	public boolean requiresBibtexKeys() {
		return false;
	}

}
