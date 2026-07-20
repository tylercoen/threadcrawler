package com.threadcrawler;

import com.threadcrawler.core.CrawlManager;
import com.threadcrawler.core.RobotsManager;
import com.threadcrawler.parser.HtmlParser;

public class Main {

	public static void main(String[] args) {
		String startUrl = "https://news.ycombinator.com/";
		int maxPages = 100;
		int maxDepth = 3;

		RobotsManager robots = new RobotsManager();

		// System.out.println(robots.isAllowed("https://cnn.com/search?q=test"));
		// System.out.println(robots.isAllowed("https://cnn.com/news"));

		HtmlParser parser = new HtmlParser();

		CrawlManager manager = new CrawlManager();
		manager.startCrawl(startUrl, maxPages, maxDepth);

	}
}
