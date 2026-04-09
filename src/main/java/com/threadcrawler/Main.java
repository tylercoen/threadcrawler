package com.threadcrawler;

import com.threadcrawler.core.CrawlManager;
import com.threadcrawler.parser.HtmlParser;

public class Main {

	public static void main(String[] args) {
		String startUrl = "https://news.ycombinator.com";
		int maxPages = 20;
		int maxDepth = 2;

		HtmlParser parser = new HtmlParser();

		CrawlManager manager = new CrawlManager();
		manager.startCrawl(startUrl, maxPages, maxDepth);

	}
}
