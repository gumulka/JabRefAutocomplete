/**
 * 
 */
package de.gumulka.jabref.model;

import net.sf.jabref.BibtexEntry;

/**
 * @author Fabian Pflug
 *
 */
public class Result {
	
		private BibtexEntry entry;
		private String author, Abstract, year, doi, note, title;
		
		
		public Result(BibtexEntry entry){
			this.entry = entry;
		}


		public String getAuthor() {
			return author;
		}


		public void setAuthor(String author) {
			this.author = author;
		}


		public String getYear() {
			return year;
		}


		public void setYear(String year) {
			this.year = year;
		}


		public String getDoi() {
			return doi;
		}


		public void setDoi(String doi) {
			this.doi = doi;
		}


		public String getAbstract() {
			return Abstract;
		}


		public void setAbstract(String abstract1) {
			Abstract = abstract1;
		}


		public String getNote() {
			return note;
		}


		public void setNote(String note) {
			this.note = note;
		}


		public String getTitle() {
			return title;
		}


		public void setTitle(String title) {
			this.title = title;
		}
		
}
