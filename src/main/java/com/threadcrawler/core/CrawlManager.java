package com.threadcrawler.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.threadcrawler.model.UrlNode;
import com.threadcrawler.parser.HtmlParser;

public class CrawlManager {

	private Queue<UrlNode> urlQueue = new LinkedList<>();
	private Set<String> visitedUrls = new HashSet<>();
	private HtmlParser parser = new HtmlParser();

	public void startCrawl(String startUrl, int maxPages, int maxDepth) {
		urlQueue.add(new UrlNode(startUrl, 0));

		while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
			UrlNode currentNode = urlQueue.poll();
			String currentUrl = currentNode.getUrl();
			int currentDepth = currentNode.getDepth();

			if (visitedUrls.contains(currentUrl) || currentDepth > maxDepth) {
				continue;
			}
			System.out.println("Crawling (Depth " + currentDepth + "): " + currentUrl);
			visitedUrls.add(currentUrl);

			try {
				List<String> links = parser.extractLinks(currentUrl);

				for (String link : links) {
					urlQueue.add(new UrlNode(link, currentDepth + 1));
				}
			} catch (IOException e) {
				System.err.println("Failed to crawl: " + currentUrl);
			}
		}

		System.out.println("Crawl complete. Pages visited: " + visitedUrls.size());
	}
}