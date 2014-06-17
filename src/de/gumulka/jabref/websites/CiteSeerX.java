/**
 * 
 */
package de.gumulka.jabref.websites;

import java.io.IOException;
import java.net.URL;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.imports.BibtexParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import de.gumulka.jabref.main.Log;
import de.gumulka.jabref.online.Provider;

/**
 * @author Fabian Pflug
 * 
 */
public class CiteSeerX extends Provider {

	public CiteSeerX() {
		super(CiteSeerX.class.getSimpleName());
	}

	public void run() {
		try {

			Connection con = Jsoup
					.connect("http://citeseerx.ist.psu.edu/search");
			String query = "";
			String tmp = entry.getField("author");
			if (tmp != null) {
				tmp = formatAuthors(tmp);
				query += "author:(" + tmp + ")";
			}
			if (tmp != null && entry.getField("title") != null)
				query += " AND ";

			tmp = entry.getField("title");
			tmp = formatTitle(tmp);
			if (tmp != null) {
				query += "title:(" + tmp + ")";
			}
			con.data("q", query);

			con.timeout(10000);

			Document doc = con.get();

			Elements links = doc.select("a[href*=doi]");
			if (links.size() == 1) {
				String doi = links.first().attr("href");
				doi = doi.substring(doi.indexOf("doi") + 4);
				if (doi.contains("&"))
					doi = doi.substring(0, doi.indexOf('&'));
				result = extract("http://citeseerx.ist.psu.edu/viewdoc/summary?doi="
						+ doi);
			}

		} catch (IOException e) {
			Log.error(e);
		}

	}

	public BibtexEntry extract(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			String Bibtex = doc.select("div[id=bibtex]").first().text();
			if (Bibtex.startsWith("BibTeX"))
				Bibtex = Bibtex.substring(7);
			Bibtex = Bibtex.replaceAll(" ", " "); // these are NOT two spaces,
													// they are different
													// representation of a
													// space. if you do not
													// replace it, it can't be
													// parsed.
			return BibtexParser.singleFromString(Bibtex);
		} catch (Exception e) {
			Log.error(e, "Error extracting the url: " + url);
		}
		return null;
	}

	public boolean isParseable(URL site) {
		return site.getHost().contains("citeseerx.ist.psu.edu");
	}

}
