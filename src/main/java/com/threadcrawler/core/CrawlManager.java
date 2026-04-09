package com.threadcrawler.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.threadcrawler.parser.HtmlParser;

public class CrawlManager {

	private Queue<String> urlQueue = new LinkedList<>();
	private Set<String> visitedUrls = new HashSet<>();
	private HtmlParser parser = new HtmlParser();

	public void startCrawl(String startUrl, int maxPages) {
		urlQueue.add(startUrl);

		while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
			String currentUrl = urlQueue.poll();

			if (visitedUrls.contains(currentUrl)) {
				continue;
			}
			System.out.println("Crawling: " + currentUrl);
			visitedUrls.add(currentUrl);

			try {
				List<String> links = parser.extractLinks(currentUrl);

				for (String link : links) {
					if (!visitedUrls.contains(links)) {
						urlQueue.add(link);
					}
				}
			} catch (IOException e) {
				System.err.println("Failed to crawl: " + currentUrl);
			}
		}

		System.out.println("Crawl complete. Pages visited: " + visitedUrls.size());
	}
}