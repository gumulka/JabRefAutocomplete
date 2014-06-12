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
	
	public static String clean(String toClean) {
		String s = toClean.replaceAll("é", "e");
		s = s.replaceAll("ô", "o");
		s = s.replaceAll("ó", "o");
		s = s.replaceAll("á", "a");
		s = s.replaceAll("ä", "a");
		s = s.replaceAll("ö", "o");
		s = s.replaceAll("ü", "u");
		s = s.replaceAll(":", " ");
		s = s.replaceAll("\\?", " ");
		s = s.replaceAll("\\.", " ");
		s = s.replaceAll("-", " ");
		s = s.replaceAll(",", " ");
		s = s.replaceAll("'", " ");
		s = s.replaceAll("\"", " ");

		return s.trim();
	}
	
	private static String formatAuthorsRec(String authors) {
		String s= authors.trim();
		
		if(s.contains(" and ")) {
			String ret = "";
			for(String a : s.split(" and "))  {
				ret += formatAuthorsRec(a);
			}
			return ret;
		}
		if(s.contains(";")) {
			String ret = "";
			for(String a : s.split(";"))  {
				ret += formatAuthorsRec(a);
			}
			return ret;
		}
		
		if(s.equalsIgnoreCase("et al"))
			return "";
		if(s.length()<2)
			return "";
		int indexK = s.indexOf(',');
		int indexS = s.indexOf(' ');
		if (indexK >0 && indexK< indexS+2) {
			String Vorname = s.substring(indexK + 1).trim();				
			String Nachname = s.substring(0, indexK).trim();
			if(Vorname.length()<2)
				return '"' + Nachname + "\" ";
			return '"' + Vorname + " " + Nachname + "\" ";
		} else if(indexK>0 && indexK>indexS+2 ){
			String ret = "";
			for(String a : s.split(","))  {
				ret += formatAuthorsRec(a);
			}
			return ret;			
		} else
			return '"' + s + "\" ";
	}

	public static String formatAuthors(String authors) {
		authors = authors.replaceAll("AND", "and");
		authors = formatAuthorsRec(authors);
		return clean(authors);
	}

	public static String formatTitle(String title) {
		return clean(title);
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
