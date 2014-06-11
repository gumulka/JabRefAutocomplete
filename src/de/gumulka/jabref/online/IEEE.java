/**
 * 
 */
package de.gumulka.jabref.online;

import java.io.IOException;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.imports.BibtexParser;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import de.gumulka.jabref.model.Result;

/**
 * @author Fabian Pflug
 * 
 */
public class IEEE extends Search {

	public IEEE(BibtexEntry e) {
		super(e);
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
			if (tmp != null) {
				query += "\"Document Title\":" + tmp;
			}
			query += ")";
			con.data("queryText", query);
			
			con.timeout(10000);
			
			Document doc = con.get();
			
			Elements links = doc.select("a[href*=articleDetails.jsp]");
			if(links.size()==2)
				result = extract(entry, "http://ieeexplore.ieee.org" + links.first().attr("href"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Result extract(BibtexEntry entry, String url) {
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
			BibtexEntry second = BibtexParser.singleFromString(res.body().replace("<br>", ""));
			if(second == null)
				return null;
			Result result = new Result(entry, second);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
