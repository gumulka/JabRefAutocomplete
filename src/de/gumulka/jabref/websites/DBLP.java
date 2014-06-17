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
public class DBLP extends Provider {

	public DBLP(){
		super(DBLP.class.getSimpleName());
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
			
			Connection con = Jsoup.connect("http://www.dblp.org/search/api/");
			con.data("q", query);
			con.data("h", "1000");
			con.data("c", "4");
			con.data("f","0");
			con.data("format", "xml");
			con.timeout(10000);
			
			Document doc = con.get();
			
			Elements hits = doc.select("hit");
			if(hits.size()==1) {
				String url = hits.select("url").first().text();
				result = extract(url);
			}
		} catch (IOException e) {
			Log.error(e);
		}

	}

	public BibtexEntry extract(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			String Bibtex = doc.select("pre").first().text();
			return BibtexParser.singleFromString(Bibtex);
		} catch (Exception e) {
			Log.error(e, "Error extracting the url: " + url);
		}
		return null;
	}
	
	
	public boolean isParseable(URL site) {
		return site.getHost().contains("dblp.org");
	}

}
