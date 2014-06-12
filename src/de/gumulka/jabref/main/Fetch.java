package de.gumulka.jabref.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import de.gumulka.jabref.controller.Settingscontroller;
import de.gumulka.jabref.model.Result;
import de.gumulka.jabref.model.Settings;
import de.gumulka.jabref.online.Search;
import de.gumulka.jabref.view.MyBox;
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
	private static boolean init = false;

	private List<Result> results = new LinkedList<Result>();

	public Fetch() {
		sp = new Settingspanel();
		sc = new Settingscontroller(sp);
		if (!init) {
			try {
				if (Settings.getInstance().isCheckVersion()) {
					new VersionCheck().start();
				}
			} catch (Exception e) {
				Log.error(e);
			}
			try {
				initclasses("de.gumulka.jabref.websites");
			} catch (Exception e) {
				Log.error(e);
			}
		}
		init = true;
	}

	public String getName() {
		return "Push to Autocomplete";
	}

	public String getApplicationName() {
		return "Autocomplete";
	}

	public String getTooltip() {
		return "Starts updating information with online sites.";
	}

	public Icon getIcon() {
		return new ImageIcon(getClass().getResource(
				"/resources/jabRef_icon_16q.png"));
	}

	public String getKeyStrokeName() {
		return "Push to Autocomplete";
	}

	public JPanel getSettingsPanel() {
		sp.refresh();
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
			Log.debug("Searching for: \nAuthor: " + e.getField("author")
					+ "\nTitle: " + e.getField("title") + "\nDoi: "
					+ e.getField("doi"));
			Search s = new Search();
			s.search(e);
			BibtexEntry res = s.getResult();
			if (res == null && Settings.getInstance().isSendDebug()) {
				try {
					Connection con = Jsoup.connect(
							"http://www.jabref.gummu.de/debug.php").method(
							Method.POST);
					con.data("author", "" + e.getField("author"));
					con.data("title", "" + e.getField("title"));
					con.data("doi", "" + e.getField("doi"));
					con.execute();
				} catch (Exception ex) {
					// we don't want to handle this.
				}
			}
			Result tmp = new Result(e, res);
			if (tmp.hasNewInformation())
				results.add(new Result(e, res));
		}
	}

	public void operationCompleted(BasePanel panel) {
		result = new JFrame("Results");
		result.setLocationRelativeTo(panel);
		if (results.size() > 0) {
			result.setSize(1000, 500);
			MyBox temp = new MyBox(BoxLayout.Y_AXIS, result);
			temp.add(new JLabel("Results:"));
			for (Result r : results) {
				temp.add(new Resultview(r, temp));
			}
			JScrollPane sp = new JScrollPane(temp);
			result.add(sp);
		} else {
			result.setSize(500, 200);
			result.add(new JLabel("There are no results."));
			JButton button = new JButton("OK");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result.dispose();
				}
			});
			result.add(button);
		}
		waiting.setVisible(false);
		waiting.dispose();
		result.setVisible(true);
		results.clear();
	}

	public boolean requiresBibtexKeys() {
		return false;
	}

	private void initclasses(String packagename) throws Exception {

		String resPath = this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar")
				.replaceFirst("file:", "");
		JarFile jarFile;
		try {
			jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> e = jarFile.entries();

			URL[] urls = { new URL("jar:file:" + jarPath + "!/") };
			@SuppressWarnings("resource")
			URLClassLoader cl = new URLClassLoader(urls, this.getClass()
					.getClassLoader());
			Class<?> c;
			while (e.hasMoreElements()) {
				JarEntry je = (JarEntry) e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				// -6 because of .class
				String className = je.getName().substring(0,
						je.getName().length() - 6);
				className = className.replace('/', '.');
				if (!className.startsWith(packagename))
					continue;
				c = cl.loadClass(className);
				c.newInstance();
			}
		} catch (IOException e) {
			Log.error(e);
		}
	}

}
