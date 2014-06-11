/**
 * 
 */
package de.gumulka.jabref.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fabian Pflug
 * 
 */
public class Settings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -716770652523945244L;

	private static Settings instance;
	private static String filename;

	private Map<String,Boolean> searchSites;
	private boolean autocopy;
	private boolean sendDebug;

	private Settings() {
		setAutocopy(false);
		setSendDebug(false);
		searchSites = new HashMap<String,Boolean>();
	}

	public static Settings getInstance() {
		if(instance==null) {
			String resPath =  Settings.class.getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			filename = resPath.replaceFirst("[.]jar[!].*", ".settings")
							.replaceFirst("file:", "");
			File f = new File(filename);
			if(!f.exists()) {
				instance = new Settings();
			}
			else {
				InputStream fis = null;
				try
				{
					fis = new FileInputStream(filename);
					@SuppressWarnings("resource")
					ObjectInputStream o = new ObjectInputStream( fis );
					instance = (Settings) o.readObject();
				}catch ( IOException e ) { System.err.println( e ); }
				catch ( ClassNotFoundException e ) { System.err.println( e ); }	
				finally { try { fis.close(); } catch ( Exception e ) { } }
			}
		}
		return instance;
	}
	
	public boolean isSet(String site) {
		if(searchSites.containsKey(site))
			return searchSites.get(site);
		else
			return true;
	}
	
	public void setSite(String site, boolean set) {
		searchSites.put(site, set);
	}

	public void save() {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			@SuppressWarnings("resource")
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isAutocopy() {
		return autocopy;
	}

	public void setAutocopy(boolean autocopy) {
		this.autocopy = autocopy;
	}

	public boolean isSendDebug() {
		return sendDebug;
	}

	public void setSendDebug(boolean sendDebug) {
		this.sendDebug = sendDebug;
	}

}
