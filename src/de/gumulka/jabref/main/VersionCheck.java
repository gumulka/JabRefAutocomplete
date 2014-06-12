package de.gumulka.jabref.main;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jsoup.Jsoup;

import de.gumulka.jabref.model.Settings;

public class VersionCheck extends Thread {

	public void run() {
		String actualS;
		try {
			Thread.sleep(2000);
			actualS = Jsoup.connect("http://www.jabref.gummu.de/version.php")
					.execute().body();
			if (Double.parseDouble(actualS) > Settings.version) {
				JFrame version = new JFrame("New Version");
				version.setLocationRelativeTo(null);
				version.setSize(400, 100);
				version.add(new JLabel("A new Version (" + actualS
						+ ") of Autocomplete is available"));
				version.setVisible(true);
			}
		} catch (Exception e) {
		}

	}

}
