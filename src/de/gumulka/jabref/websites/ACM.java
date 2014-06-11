/**
 * 
 */
package de.gumulka.jabref.websites;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.imports.BibtexParser;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.gumulka.jabref.online.Search;

/**
 * @author Fabian Pflug
 * 
 */
public class ACM extends Search {

	public ACM() {
		super("ACM");
	}

	public void run() {
		try {

			Response res = Jsoup
					.connect("http://dl.acm.org/advsearch.cfm")
					.referrer("about:blank")
					.timeout(10000)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.execute();
			Document doc = res.parse();
			Connection con = Jsoup.connect("http://dl.acm.org/results.cfm");
			// TODO copy Cookies
			for (Entry<String, String> cookie : res.cookies().entrySet()) {
				con.cookie(cookie.getKey(), cookie.getValue());
			}
			con.referrer("http://dl.acm.org/advsearch.cfm");
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			for (Element e : doc.select("input[type=Hidden]")) {
				con.data(e.attr("name"), e.attr("value"));
			}
			con.data("termzone", "Title");
			String tmp = entry.getField("title");
			if (tmp == null)
				tmp = "";
			tmp = formatTitle(tmp);
			con.data("allofem", tmp);
			con.data("anyofem", "");
			con.data("noneofem", "");
			con.data("peoplezone", "Author");
			tmp = entry.getField("author");
			if (tmp == null)
				tmp = "";
			con.data("people", formatAuthors(tmp));
			con.data("peoplehow", "and");
			con.data("keyword", "");
			con.data("keywordhow", "AND");
			con.data("affil", "");
			con.data("affilhow", "AND");
			con.data("pubin", "");
			con.data("pubinhow", "and");
			con.data("pubby", "");
			con.data("pubbyhow", "OR");
			con.data("since_year", "");
			con.data("before_year", "");
			con.data("pubashow", "OR");
			con.data("sponsor", "");
			con.data("sponsorhow", "AND");
			con.data("confdate", "");
			con.data("confdatehow", "OR");
			con.data("confloc", "");
			con.data("conflochow", "OR");
			con.data("isbnhow", "OR");
			con.data("isbn", "");
			con.data("doi", "");
			con.data("ccs", "");
			con.data("subj", "");
			con.data("Go.x", "26");
			con.data("Go.y", "4");
			con.timeout(10000);

			res = con.execute();

			doc = res.parse();
			if (doc.select("p:contains(Found)").first().text()
					.contains("Found 1 within")) {
				String url = "http://dl.acm.org/"
						+ doc.select("a[href*=citation.cfm").first()
								.attr("href") + "&preflayout=flat";
				result = extract(url);
				if(result!=null)
					extractWebInfo(url);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public BibtexEntry extract(String url) {
		try {
			String id = url.substring(url.indexOf("id=") + 3);
			int index = id.indexOf('&');
			if(index >0)
				id = id.substring(0, index);
			index = id.lastIndexOf('.');
			if (index > 0)
				id = id.substring(index + 1);

			String url2 = "http://dl.acm.org/exportformats.cfm?id=" + id
					+ "&expformat=bibtex";
			Connection con = Jsoup.connect(url2).timeout(10000);
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			con.referrer("about:blank");
			Document doc = con.get();
			String bibtex = doc.select("PRE").first().text();
			return BibtexParser.singleFromString(bibtex);
		} catch (IOException e) {
		}
		return null;
	}

	private void extractWebInfo(String url)
			throws IOException {
		if (!url.contains("&preflayout=flat"))
			url += "&preflayout=flat";
		Connection con = Jsoup.connect(url);
		con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
		con.timeout(10000);
		Document doc = con.get();

		Element abstractel = doc.select("div[class=flatbody]")
				.select("div[style=display:inline]").first();
		if (abstractel != null) {
			String abs = abstractel.text();
			result.setField("abstract", abs);
		}

		int index;

		Element noteel = doc.select("table:contains(Published in)").last();
		if (noteel != null) {
			String note = noteel.text();
			index = note.indexOf("table of contents");
			if (index > 0)
				note = note.substring(0, index);
			index = note.indexOf(" doi>");
			if (index > 0)
				note = note.substring(0, index);
			index = note.indexOf(" Full text:");
			if (index > 0)
				note = note.substring(0, index);
			result.setField("note", note);
		}
	}
	
	public boolean isParseable(URL site) {
		return site.getHost().contains("dl.acm.org");
	}

}
