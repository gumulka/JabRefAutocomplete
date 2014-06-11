/**
 * 
 */
package de.gumulka.jabref.online;

import java.io.IOException;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.imports.BibtexParser;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.gumulka.jabref.model.Result;

/**
 * @author Fabian Pflug
 * 
 */
public class SpringerLink extends Search {

	public SpringerLink(BibtexEntry e) {
		super(e);
	}
	
	
	public void run() {
		try {

			Connection con = Jsoup.connect("http://link.springer.com/search");
			
			String tmp = entry.getField("title");
			if (tmp == null)
				tmp = "";
			tmp = tmp.replace(":", "");
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
				result = extract(entry, "http://link.springer.com" + links.first().attr("href"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Result extract(BibtexEntry entry, String url) {
		try {
			String biburl = url.replace("http://link.springer.com", "http://link.springer.com/export-citation");
			biburl += ".bib";
			Connection con = Jsoup.connect(biburl);
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			con.timeout(10000);
			Response res = con.execute();

			
			BibtexEntry second = BibtexParser.singleFromString(res.body());
			if(second == null) return null;
			Result result = new Result(entry, second);
			extractWebInfo(url, result);
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	private static void extractWebInfo(String url, Result res)
			throws IOException {

		Connection con = Jsoup.connect(url);
		con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
		con.timeout(10000);
		Document doc = con.get();
		
		
		Element abstractel = doc.select("div[class=abstract-content formatted]").last();
		if (abstractel != null) {
			String abs = abstractel.text();
			res.setField("abstract", abs);
		}

		Element noteel = doc.select("div[class=article-note").last();
		if (noteel != null) {
			res.setField("note", noteel.text());
		}
	}

}
