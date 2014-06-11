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
import org.jsoup.select.Elements;

import de.gumulka.jabref.online.Search;

/**
 * @author Fabian Pflug
 * 
 */
public class SpringerLink extends Search {

	public SpringerLink() {
		super("SpringerLink");
	}
	
	public void run() {
		try {

			Connection con = Jsoup.connect("http://link.springer.com/search");
			
			String tmp = entry.getField("title");
			if (tmp == null)
				tmp = "";
			tmp = formatTitle(tmp);
			con.data("dc.title", tmp);
			
			tmp = entry.getField("author");
			if (tmp != null) {
				tmp = formatAuthors(tmp);
				con.data("dc.creator", tmp);
			}
			con.data("date-facet-mode", "between");
			con.data("showAll", "true");
			con.timeout(10000);
			
			Document doc = con.get();
			
			Element list = doc.select("ol[id=results-list]").first();
			if(list == null) return;
			Elements links = list.select("a[class=title]");
			if(links.size()==1)
				result = extract("http://link.springer.com" + links.first().attr("href"));
			if(result != null)
				extractWebInfo("http://link.springer.com" + links.first().attr("href"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public BibtexEntry extract(String url) {
		try {
			String biburl = url.replace("http://link.springer.com", "http://link.springer.com/export-citation");
			biburl += ".bib";
			Connection con = Jsoup.connect(biburl);
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			con.timeout(10000);
			Response res = con.execute();
			return BibtexParser.singleFromString(res.body());
		} catch (Exception e) {
		}
		return null;
	}

	private void extractWebInfo(String url)
			throws IOException {

		Connection con = Jsoup.connect(url);
		con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
		con.timeout(10000);
		Document doc = con.get();
		
		
		Element abstractel = doc.select("div[class=abstract-content formatted]").last();
		if (abstractel != null) {
			String abs = abstractel.text();
			result.setField("abstract", abs);
		}

		Element noteel = doc.select("div[class=article-note").last();
		if (noteel != null) {
			result.setField("note", noteel.text());
		}
	}
	
	public boolean isParseable(URL site) {
		return site.getHost().contains("link.springer.com");
	}

}
