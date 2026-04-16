package com.threadcrawler;

import com.threadcrawler.core.CrawlManager;
import com.threadcrawler.parser.HtmlParser;

public class Main {

	public static void main(String[] args) {
		String startUrl = "https://en.wikipedia.org/wiki/Web_crawler";
		int maxPages = 100;
		int maxDepth = 3;

		HtmlParser parser = new HtmlParser();

		CrawlManager manager = new CrawlManager();
		manager.startCrawl(startUrl, maxPages, maxDepth);

	}
}
