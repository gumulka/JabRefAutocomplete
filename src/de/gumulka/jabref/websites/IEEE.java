/**
 * 
 */
package de.gumulka.jabref.websites;

import java.io.IOException;
import java.net.URL;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.imports.BibtexParser;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import de.gumulka.jabref.main.Log;
import de.gumulka.jabref.online.Search;

/**
 * @author Fabian Pflug
 * 
 */
public class IEEE extends Search {

	public IEEE(){
		super("IEEE");
	}
	
	public void run() {
		try {

			Connection con = Jsoup.connect("http://ieeexplore.ieee.org/search/searchresult.jsp");
			con.data("action", "search");
			con.data("sortType", "");
			con.data("rowsPerPage", "");
			con.data("searchField", "Search_all");
			con.data("matchBoolean", "true");

			String query = "(";
			String tmp = entry.getField("author");
			if (tmp != null) {
			//	tmp = formatAuthors(tmp);
				query += "(\"Authors\":" + tmp + ") ";
			}
			if(tmp!=null && entry.getField("title") != null)
				query += "AND ";
			
			tmp = entry.getField("title");
			tmp = formatTitle(tmp);
			if (tmp != null) {
				query += "\"Document Title\":" + tmp;
			}
			query += ")";
			con.data("queryText", query);
			
			con.timeout(10000);
			
			Document doc = con.get();
			
			Elements links = doc.select("a[href*=articleDetails.jsp]");
			if(links.size()==2)
				result = extract("http://ieeexplore.ieee.org" + links.first().attr("href"));

		} catch (IOException e) {
			Log.error(e);
		}

	}

	public BibtexEntry extract(String url) {
		try {
			Connection con = Jsoup.connect("http://ieeexplore.ieee.org/xpl/downloadCitations");
			String id = url.substring(url.indexOf("arnumber=")+9);
			int index = id.indexOf('&');
			if(index>0)
				id = id.substring(0, index);
			con.data("recordIds", id);
			con.data("citations-format","citation-abstract");
			con.data("download-format", "download-bibtex");
			con.method(Method.POST);
			Response res = con.execute();
			return BibtexParser.singleFromString(res.body().replace("<br>", ""));
		} catch (Exception e) {
			Log.error(e, "Error extracting the url: " + url);
		}
		return null;
	}
	
	
	public boolean isParseable(URL site) {
		return site.getHost().contains("ieeexplore.ieee.org");
	}

}
