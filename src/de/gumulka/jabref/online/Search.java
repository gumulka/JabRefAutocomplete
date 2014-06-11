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
	
	public static void printall() {
		for (Search s : all) {
			System.out.println(s.name);
		}
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
				try {
					second.add(s.getClass().newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}
	
	public BibtexEntry extract(String url) {
		return null;
	}

	protected static String formatAuthors(String authors) {
		authors.replaceAll("AND", "and");
		String[] splittet = authors.split(" and ");
		String ret = "";
		int index;
		for (String s : splittet) {
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
		String ret = title.replaceAll(":", "");

		return ret.trim();
	}
	
	public boolean isParseable(URL site) {
		return false;
	}

	public BibtexEntry getResult() {
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean equals(Object o) {
		if (o instanceof Search)
			return ((Search) o).name.equalsIgnoreCase(this.name);
		return false;
	}

}
