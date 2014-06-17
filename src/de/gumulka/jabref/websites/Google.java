/**
 * 
 */
package de.gumulka.jabref.websites;

import java.io.IOException;
import java.net.URL;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.imports.BibtexParser;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.gumulka.jabref.main.Log;
import de.gumulka.jabref.online.Provider;

/**
 * @author Fabian Pflug
 * 
 */
public class Google extends Provider {

	public Google() {
		super(Google.class.getSimpleName());
	}

	public void run() {
		try {
			String query = "";

			String tmp = entry.getField("author");
			if (tmp != null) {
				tmp = formatAuthors(tmp).replace("\"", "");
				query += tmp + " ";
			}
			tmp = entry.getField("title");
			tmp = formatTitle(tmp);
			if (tmp != null) {
				query += tmp;
			}
			Connection con = Jsoup.connect("http://scholar.google.de/scholar");
			con.referrer("about:blank");
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			con.data("q", query);
			con.data("hl", "de");
			con.data("btnG", "");
			con.data("lr", "");
			con.timeout(10000);

			Document doc = con.get();

			Element first = doc.select("a[onclick*=gs_ocit]").first();
			String blub = first.attr("onclick");
			String id = blub
					.substring(blub.indexOf('\'', blub.indexOf("event")) + 1);
			String cites = id;
			id = id.substring(0, id.indexOf('\''));
			cites = cites
					.substring(cites.indexOf('\'', cites.indexOf(',')) + 1);
			cites = id.substring(0, cites.indexOf('\''));
			query = "info:" + id + ":scholar.google.com/";
			con = Jsoup.connect("http://scholar.google.com/scholar");
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			con.data("output", "cite");
			con.data("hl", "de");
			con.data("scirp", cites);
			con.data("q", query);
			doc = con.get();
			Element link = doc.select("a[href*=scholar.bib]").first();

			result = extract("http://scholar.google.com" + link.attr("href"));

		} catch (IOException e) {
			Log.error(e);
		}

	}

	public BibtexEntry extract(String url) {
		try {
			Response res = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.execute();
			return BibtexParser.singleFromString(res.body());
		} catch (Exception e) {
			Log.error(e, "Error extracting the url: " + url);
		}
		return null;
	}

	public boolean isParseable(URL site) {
		return false; // there is no doi pointing to google scholar
	}

}
