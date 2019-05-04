package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mainmc.folders.Conf;

public class Book {

	private String book;

	public Book(String book) {
		this.book = book.toLowerCase();
	}

	public static List<String> getBooks() {
		Conf config = new Conf();
		List<String> books = new ArrayList<String>();
		Set<String> bookList = config.getConfiguration("Books");
		books.addAll(bookList);
		return books;
	}

	public boolean exists() {
		Conf config = new Conf();
		return config.get().get("Books." + this.book) != null;
	}

	public String getAuthor() {
		Conf config = new Conf();
		return config.getString("Books." + this.book + ".author");
	}

	public String getTitle() {
		Conf config = new Conf();
		return config.getString("Books." + this.book + ".title").replaceAll("&", "§");
	}

	public List<String> getTextPages() {
		Conf config = new Conf();
		return config.getStringList("Books." + this.book + ".pages");
	}

	public List<String> getPages() {

		List<String> pages = new ArrayList<String>();

		for (String page : getTextPages()) {
			pages.add(page.replaceAll("&", "§").replaceAll("/d", "\n"));
		}

		return pages;
	}

}
