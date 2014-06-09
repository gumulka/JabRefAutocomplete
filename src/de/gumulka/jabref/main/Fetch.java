package de.gumulka.jabref.main;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jabref.BasePanel;
import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.MetaData;
import net.sf.jabref.external.PushToApplication;
import net.sf.jabref.plugin.core.JabRefPlugin;
import de.gumulka.jabref.controller.Settingscontroller;
import de.gumulka.jabref.model.ACM;
import de.gumulka.jabref.model.Search;
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

	    waiting = new JFrame( "Syncing with Servers" );
//	    waiting.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    waiting.setSize( 250, 100 );
	    waiting.setLocationRelativeTo(null);
	    waiting.add( new JLabel("Syncing " + entrys.length + " publications.\n Please wait.") );
	    waiting.setVisible( true );
	    System.out.println("KeyString: " + keyString);
	    for(BibtexEntry e : entrys) {
	    	System.out.println("Searching for: \nAuthor: " + e.getField("author") + "\nTitle: " + e.getField("title"));
	    	Search acm = new ACM(e);
	    	acm.search();
	    	acm.getResult();
	    	
		    try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
	    }
	}

	public void operationCompleted(BasePanel panel) {
		waiting.setVisible(false);
	}

	public boolean requiresBibtexKeys() {
		return false;
	}

}
