/**
 * 
 */
package de.gumulka.jabref.online;

import java.io.IOException;
import java.util.Map.Entry;

import net.sf.jabref.BibtexEntry;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.gumulka.jabref.model.Result;

/**
 * @author Fabian Pflug
 *
 */
public class ACM extends Search {

	public ACM(BibtexEntry e) {
		super(e);
	}
	
	public void run() {
		try {

			Response res = Jsoup.connect("http://dl.acm.org/advsearch.cfm").referrer("about:blank").timeout(10000)
					.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0").execute();
			Document doc = res.parse();
			Connection con =  Jsoup.connect("http://dl.acm.org/results.cfm");
			// TODO copy Cookies 
			for(Entry<String, String> cookie : res.cookies().entrySet()) {
				con.cookie(cookie.getKey(), cookie.getValue());
			}
			con.referrer("http://dl.acm.org/advsearch.cfm");
			con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
			for(Element e : doc.select("input[type=Hidden]")) {
				con.data(e.attr("name"), e.attr("value"));
			}
			con.data("termzone", "Title");
			String tmp = entry.getField("title");
			if(tmp == null)
				tmp = "";
			con.data("allofem", tmp);
			con.data("anyofem", "");
			con.data("noneofem", "");
			con.data("peoplezone", "Author");
			tmp = entry.getField("author");
			if(tmp == null)
				tmp = "";
			con.data("people", tmp);
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
			if(doc.select("p:contains(Found)").first().text().contains("Found 1 within")) {
				String url = "http://dl.acm.org/" + doc.select("a[href*=citation.cfm").first().attr("href") + "&preflayout=flat";
				con =  Jsoup.connect(url);
				con.referrer("http://dl.acm.org/results.cfm");
				con.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0");
				for(Entry<String, String> cookie : res.cookies().entrySet()) {
					con.cookie(cookie.getKey(), cookie.getValue());
				}
				result = new Result(entry);
				result.setField("url", url.substring(0, url.indexOf('&')));
				extractCitation(con);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void extractCitation (Connection con) throws IOException {
		con.timeout(10000);
		Document doc = con.get();
		
		Element titleel = doc.select("img[src=images/apdf.jpg]").first();
		if(titleel != null) {
			String title = titleel.attr("title");
			result.setField("title",title);
		} else {
			titleel = doc.select("img[src*=portalparts.acm.org]").first();
			if(titleel != null) {
				String title = titleel.attr("title");
				result.setField("title",title);
			}
		}
			
		String authors = "";
		Element AuthorTable = doc.select("table:contains(Authors:)").last();
		for(Element e : AuthorTable.select("a[title*=Author]")) {
			authors += e.text() + ", ";
		}
		int index = authors.lastIndexOf(',');
		if(index >0)
			authors = authors.substring(0, index);
		result.setField("author", authors);
		
		Element abstractel = doc.select("div[class=flatbody]").select("div[style=display:inline]").first();
		if(abstractel!=null) {
			String abs = abstractel.text();
			result.setField("abstract",abs);
		}
		
//		System.out.println("Year");
//		System.out.println(doc.select("table:contains(Bibliometrics)").last().text());
		
		Element doiel = doc.select("a[href*=dx.doi.org]").first();
		if(doiel!=null) {
			String doi = doiel.text();
			doi = doi.trim();
			index = doi.indexOf(' ');
			if(index >0)
				doi = doi.substring(0, index);
			result.setField("doi",doi);
		}	
		
		Element noteel = doc.select("table:contains(Published in)").last();
		if(noteel!= null) {
			String note = noteel.text();
			index = note.indexOf("table of contents");
			if(index >0)
				note = note.substring(0, index);
			index = note.indexOf(" doi>");
			if(index >0)
				note = note.substring(0, index);
			index = note.indexOf(" Full text:");
			if(index >0)
				note = note.substring(0, index);
			result.setField("note", note);
		}
	}
	
	
}
