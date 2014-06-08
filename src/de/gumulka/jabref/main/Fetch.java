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
<<<<<<< HEAD
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

=======

/**
 * Die Hauptklasse des Programms, wie sie von JabRef aufgerufen wird. Sie dient
 * als Kommunikationsschnittstelle zwischen JabRef und JabRef Importer
 * 
 * @author ahengelhaupt
 * 
 */
public class Fetch extends JabRefPlugin implements PushToApplication {

	/**
	 * Name des Plugins den JabRef intern benutzt
	 * 
	 */
	public String getName() {
		return "Push to JabRef Importer";
	}

	/**
	 * Gibt den Namen des Plugins für das Plugin-Auwahlfenster in JabRef an
	 * 
	 * @return Bezeichner des Plugins
	 * 
	 */
	public String getApplicationName() {
		return "JabRef Importer";
	}

	/**
	 * Lässt Tooltip erscheinen wenn man mit der Maus über dem Plugin für eine
	 * kure Zeit stehen bleibt
	 * 
	 * @return Tooltip
	 */
>>>>>>> 3b33476a13db7132ae4adb9766be3f2d6ff26e63
	public String getTooltip() {
		return "Starts updating information with online sites.";
	}

<<<<<<< HEAD
=======
	/**
	 * Gibt das Icon des Plugins zurück welches in der Plugin-Auswahlliste zu
	 * sehen ist
	 * 
	 * @return Plugin-Icon
	 */

>>>>>>> 3b33476a13db7132ae4adb9766be3f2d6ff26e63
	public Icon getIcon() {
		return new ImageIcon(getClass().getResource(
				"/resources/jabRef_icon_16q.png"));
	}

<<<<<<< HEAD
	public String getKeyStrokeName() {
		return "Push to JabRef Autocomplete";
	}

	public JPanel getSettingsPanel() {
		return sp;
	}

	public void storeSettings() {
	}

=======
	/**
	 * 
	 */
	public String getKeyStrokeName() {
		return "Push to JabRef Importer";
	}

	/**
	 * Erzeugt eine Instanz des SettingsPanels und gibt sie zurück
	 * 
	 * @return Das SettingsPanel
	 */
	public JPanel getSettingsPanel() {
		return null;
	}

	/**
	 * 
	 * Speichert die vom Benutzer gemachten Einstellungen
	 * 
	 */
	public void storeSettings() {
	}

	/**
	 * Hier findet der Datenabgleich statt. Methode läuft unabhängig von Jabref.
	 * Sie fordert die implementierende Klasse auf ein JPanel welches mit dem
	 * Optionen Panel der Implementierung aufgerufen wird falls notwendig.
	 * 
	 * Wenn das JPanel dem Benutzer angezeigt wird und der Benutzer Anstalten
	 * macht seine Einstellungen zu speichern wird die storeSettings() Methode
	 * der Implementierung aufgerufen.
	 * 
	 * Diese Methode muss sicherstellen, dass sich alle Komponenten auf dem
	 * Panel in gültigen Auswahlmodi befinden.
	 * 
	 * @return Ein JPanel welches die Einstellungen zeigt oder null wenn keine
	 *         Einstellungen gebraucht werden.
	 * 
	 * 
	 * @param database
	 *            Datenbank die alle BibTex-Einträge enthält und zudem auch auf
	 *            Änderungen der Einträge reagiert.
	 * 
	 * @param entries
	 *            Ausgewählte Einträge
	 * 
	 * @param metaData
	 * 
	 * 
	 */
>>>>>>> 3b33476a13db7132ae4adb9766be3f2d6ff26e63
	public void pushEntries(BibtexDatabase database, BibtexEntry[] entrys,
			String keyString, MetaData metaData) {

	}

<<<<<<< HEAD
	public void operationCompleted(BasePanel panel) {
	}

=======
	/**
	 * Benachrichtigung etc, diese Methode wird vom event dispatch thread
	 * aufgerufen nachdem puchEntries() fertig ist
	 * 
	 * 
	 */
	public void operationCompleted(BasePanel panel) {
	}

	/**
	 * Prüft ob diese Operation gesetzte BibTex-Keys für die Einträge benötigt.
	 * Wenn true zurückgegeben wird wird eine Fehlermeldung angezeigt, falls
	 * Keys fehlen sollten
	 * 
	 * @return wahr, falls BibTeX-Keys für diese Operation benötigt werden
	 */
>>>>>>> 3b33476a13db7132ae4adb9766be3f2d6ff26e63
	public boolean requiresBibtexKeys() {
		return false;
	}

}
