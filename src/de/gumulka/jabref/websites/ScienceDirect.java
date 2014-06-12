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
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.gumulka.jabref.main.Log;
import de.gumulka.jabref.online.Search;
import de.gumulka.jabref.test.htmlwriter;

/**
 * @author Fabian Pflug
 * 
 */
public class ScienceDirect extends Search {

	public ScienceDirect(){
		super("sciencedirect");
	}
	
	public void run() {
		try {
			Response res = Jsoup.connect("http://www.sciencedirect.com/").execute();
			Document doc = res.parse(); 
			Element form = doc.select("form[id=quickSearch]").first();
			Connection con = Jsoup.connect("http://www.sciencedirect.com/science");
			for (Entry<String, String> cookie : res.cookies().entrySet()) {
				con.cookie(cookie.getKey(), cookie.getValue());
			}
			for(Element hidden : form.select("input[type=hidden]"))
				con.data(hidden.attr("name"), hidden.attr("value"));
			
			String tmp = entry.getField("author");
			if (tmp != null) {
				tmp = formatAuthors(tmp).replace("\"", "");
				con.data("qs_author", tmp);
			}
			tmp = entry.getField("title");
			if (tmp == null) {
				tmp = "";
			}
			tmp = formatTitle(tmp);
			
			con.data("qs_all", tmp);
			con.data("qs_title", "");
			con.data("qs_vol", "");
			con.data("qs_issue", "");
			con.data("qs_pages", "");
			con.data("sdSearch", "");
			con.referrer("http://www.sciencedirect.com");
			con.method(Method.GET);
			res = con.execute();
			htmlwriter.write(res.body());
			doc = res.parse();
			Elements hits = doc.select("a[href*=science/article/]");
			
			if(hits.size()==1) {
				String url = hits.first().attr("href");
				result = extract(url);
			}
		} catch (IOException e) {
			Log.error(e);
		}

	}

	public BibtexEntry extract(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Element form = doc.select("form[name=exportCite]").first();
			Connection con = Jsoup.connect("http://www.sciencedirect.com" + form.attr("action"));
			for(Element hidden : form.select("input[type=hidden]"))
				con.data(hidden.attr("name"), hidden.attr("value"));
			con.data("zone", "exportDropDown");
			con.data("citation-type", "BIBTEX");
			con.data("format", "cite-abs");
			con.data("export", "Export");
			con.method(Method.POST);
			Response res = con.execute();
			return BibtexParser.singleFromString(res.body());
		} catch (Exception e) {
			Log.error(e, "Error extracting the url: " + url);
		}
		return null;
	}
	
	
	public boolean isParseable(URL site) {
		return site.getHost().contains("sciencedirect.com");
	}

}
