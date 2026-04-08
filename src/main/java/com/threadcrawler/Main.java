package com.threadcrawler;

import java.io.IOException;
import java.util.List;

import com.threadcrawler.parser.HtmlParser;

public class Main {

	public static void main(String[] args) {
		String url = "https://news.ycombinator.com";

		HtmlParser parser = new HtmlParser();

		try {
			List<String> links = parser.extractLinks(url);

			System.out.println("Links found on: " + url);
			for (String link : links) {
				System.out.println(link);
			}

		} catch (IOException e) {
			System.err.println("Error fetching URL: " + e.getMessage());
		}
	}
}