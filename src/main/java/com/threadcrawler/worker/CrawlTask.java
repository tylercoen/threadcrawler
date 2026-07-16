package com.threadcrawler.worker;

import java.io.IOException;
import java.util.List;

import com.threadcrawler.core.CrawlManager;
import com.threadcrawler.model.UrlNode;
import com.threadcrawler.parser.HtmlParser;
import com.threadcrawler.util.UrlUtils;

public class CrawlTask implements Runnable {
	private UrlNode node;
	private CrawlManager manager;
	private HtmlParser parser;
	private int maxPages;
	private int maxDepth;

	public CrawlTask(UrlNode node, CrawlManager manager, int maxPages, int maxDepth) {
		this.node = node;
		this.manager = manager;
		this.parser = new HtmlParser();
		this.maxPages = maxPages;
		this.maxDepth = maxDepth;
	}

	@Override
	public void run() {

		try {
			String url = UrlUtils.normalize(node.getUrl());

			if (url == null) {
				return;
			}

			int depth = node.getDepth();

			if (!manager.shouldVisit(url, depth, maxPages, maxDepth)) {
				return;
			}

			if (!manager.markVisited(url)) {
				System.out.println("[SKIPPED] Already visited: " + url);
				return;
			}

			System.out.println("[CRAWLING] Depth " + depth + " -> " + url);

			manager.acquirePermit();

			try {
				List<String> links = parser.extractLinks(url);

				System.out.println("Links found on " + url + ": " + links.size());

				for (String link : links) {
					link = UrlUtils.normalize(link);

					if (!UrlUtils.isCrawlable(link)) {
						continue;
					}

					if (!manager.isSameDomain(link)) {
						continue;
					}

					manager.incrementTasks();

					manager.submitTask(new CrawlTask(new UrlNode(link, depth + 1), manager, maxPages, maxDepth));
				}
			} finally {
				manager.releasePermit();
			}
		} catch (IOException e) {
			System.err.println("Failed: " + node.getUrl());
		} finally {
			manager.decrementTasks();
		}
	}
}
