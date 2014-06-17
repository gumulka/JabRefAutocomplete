/**
 * 
 */
package de.gumulka.jabref.online;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import net.sf.jabref.BibtexEntry;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import de.gumulka.jabref.main.Log;
import de.gumulka.jabref.model.Result;
import de.gumulka.jabref.model.Settings;

/**
 * @author Fabian Pflug
 * 
 */
public class Search extends Thread {

	private BibtexEntry entry;
	private Result result = null;
	
	public Search(BibtexEntry entry) {
		this.entry = entry;
		result = new Result(entry);
		this.start();
	}

	public void run() {
		String doi = entry.getField("doi");
		if (doi != null) {
			if (doi.startsWith("http://"))
				doi = doi.substring(7);
			if (doi.startsWith("https://"))
				doi = doi.substring(8);
			if (doi.startsWith("dx.doi.org/"))
				doi = doi.substring(11);
			Response res;
			try {
				res = Jsoup
						.connect("http://dx.doi.org/" + doi)
						.userAgent(
								"Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0")
						.followRedirects(true).execute();
				URL url = res.url();
				for (Provider s : Provider.getAllSites()) {
					if (s.isParseable(url)) {
						BibtexEntry tmp = s.extract(url.toString());
						if (tmp != null) {
							result.add(tmp);
							return;							
						}
					}
				}
			} catch (Exception e) {
				Log.error(e);
			}
		}
		List<Provider> second = new LinkedList<Provider>();
		for (Provider s : Provider.getAllSites()) {
			if (Settings.getInstance().isSet(s.getSearchName()))
				try {
					second.add(s.getClass().newInstance());
				} catch (Exception e) {
					Log.error(e);
				}
		}
		for (Provider s : second) {
			s.search(entry);
		}
		for (Provider s : second) {
			result.add(s.getResult());
		}
	}

	public Result getResult() {
		try {
			join();
		} catch (InterruptedException e) {
			Log.error(e);
		}
		return result;
	}

}
