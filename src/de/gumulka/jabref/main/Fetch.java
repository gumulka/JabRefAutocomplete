package de.gumulka.jabref.main;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.sf.jabref.BasePanel;
import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.MetaData;
import net.sf.jabref.external.PushToApplication;
import net.sf.jabref.plugin.core.JabRefPlugin;
import de.gumulka.jabref.view.Settingspanel;

/**
 * The main class to communicate between JabRef and Autocomplete.
 * 
 * @author Fabian Pflug
 */
public class Fetch extends JabRefPlugin implements PushToApplication {

	
	private Settingspanel sp;
	
	public Fetch() {
		sp = new Settingspanel();
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
	}

	public void pushEntries(BibtexDatabase database, BibtexEntry[] entrys,
			String keyString, MetaData metaData) {

	}

	public void operationCompleted(BasePanel panel) {
	}

	public boolean requiresBibtexKeys() {
		return false;
	}

}
