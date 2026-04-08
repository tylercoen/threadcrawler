package com.threadcrawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

	public List<String> extractLinks(String url) throws IOException {
		List<String> links = new ArrayList<>();

		// Connect to the URL and fetch the HTML
		Document document = Jsoup.connect(url).get();

		// Select all anchor tags with href attribute
		Elements anchorTags = document.select("a[href]");

		for (Element element : anchorTags) {
			String link = element.absUrl("href"); // Convert relative → absolute
			if (!link.isEmpty()) {
				links.add(link);
			}
		}

		return links;
	}
}