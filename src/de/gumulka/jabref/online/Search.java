/**
 * 
 */
package de.gumulka.jabref.online;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import net.sf.jabref.BibtexEntry;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import de.gumulka.jabref.main.Log;
import de.gumulka.jabref.model.Settings;

/**
 * @author Fabian Pflug
 * 
 */
public class Search extends Thread {

	protected BibtexEntry entry;
	protected BibtexEntry result = null;
	protected String name;

	private static List<Search> all = new LinkedList<Search>();

	protected Search(String name) {
		this.name = name;
		if (!all.contains(this))
			all.add(this);
	}

	public Search() {
		this.name = "Search";
	}
	
	public static List<Search> getAllSites() {
		return all;
	}

	public String getSearchName() {
		return name;
	}
	
	public void search(BibtexEntry bib) {
		this.entry = bib;
		start();
	}

	public void run() {
		String doi = entry.getField("doi");
		if (doi == null) {
			List<Search> second = new LinkedList<Search>();
			for (Search s : all) {
				if(Settings.getInstance().isSet(s.getSearchName()))
				try {
					second.add(s.getClass().newInstance());
				} catch (Exception e) {
					Log.error(e);
				}
			}
			for (Search s : second) {
				s.search(entry);
			}
			for (Search s : second) {
				BibtexEntry tmp = s.getResult();
				if (tmp != null)
					result = tmp;
			}
		} else {
			if(doi.startsWith("http://"))
				doi = doi.substring(7);
			if(doi.startsWith("https://"))
				doi = doi.substring(8);
			if(doi.startsWith("dx.doi.org/"))
				doi = doi.substring(11);
			Response res;
			try {
				res = Jsoup.connect("http://dx.doi.org/" + doi).userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0").followRedirects(true).execute();
				URL url = res.url();
				for (Search s : all) {
					if(s.isParseable(url)) {
						result = s.extract(url.toString());
						if(result != null) 
							break;
					}
				}
			} catch (IOException e) {
				Log.error(e);
			}
		}
	}
	
	public BibtexEntry extract(String url) {
		return null;
	}
	
	protected static String clean(String toClean) {
		String s = toClean.replaceAll("é", "e");
		s = s.replaceAll("ô", "o");
		s = s.replaceAll(":", " ");
		s = s.replaceAll("\\?", " ");
		s = s.replaceAll("\\.", " ");
		s = s.replaceAll("-", " ");

		return s;
	}

	protected static String formatAuthors(String authors) {
		authors = clean(authors);
		authors = authors.replaceAll("AND", "and");
		String[] splittet = authors.split(" and ");
		List<String> ultimateSplit = new LinkedList<String>();
		for(String s : splittet) {
			if(s.lastIndexOf(',') != s.indexOf(',')) { // splittet using ',' because there is more then one.
				for(String n : s.split(","))
					ultimateSplit.add(n);
			} else { // now comes the scary part.
				s = s.trim();
				if(s.lastIndexOf(' ') != s.indexOf(' ')) // if there is more then one space, assume it is two names.
					for(String n : s.split(" "))
						ultimateSplit.add(n);
				else
					ultimateSplit.add(s);
			}
		}
		String ret = "";
		int index;
		for (String s : ultimateSplit) {
			s=s.trim();
			if(s.equalsIgnoreCase("et al"))
				continue;
			if(s.length()<2)
				continue;
			index = s.indexOf(',');
			if (index > 0) {
				String Vorname = s.substring(index + 1).trim();
				String Nachname = s.substring(0, index).trim();
				ret += '"' + Vorname + " " + Nachname + "\" ";
			} else
				ret += '"' + s + "\" ";
		}
		return ret.trim();
	}

	protected static String formatTitle(String title) {
		String ret = clean(title);

		return ret.trim();
	}
	
	public boolean isParseable(URL site) {
		return false;
	}

	public BibtexEntry getResult() {
		try {
			join();
		} catch (InterruptedException e) {
			Log.error(e);
		}
		return result;
	}

	public boolean equals(Object o) {
		if (o instanceof Search)
			return ((Search) o).name.equalsIgnoreCase(this.name);
		return false;
	}

}
