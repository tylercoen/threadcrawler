package com.threadcrawler.core;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.threadcrawler.model.UrlNode;
import com.threadcrawler.worker.CrawlTask;

public class CrawlManager {

	private Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
	private ExecutorService executor;
	private java.util.concurrent.atomic.AtomicInteger activeTasks = new java.util.concurrent.atomic.AtomicInteger(0);
	private String baseDomain;
	private long crawlDelayMs = 200;

	public void startCrawl(String startUrl, int maxPages, int maxDepth) {
		executor = Executors.newVirtualThreadPerTaskExecutor();
		this.baseDomain = extractDomain(startUrl);

		activeTasks.incrementAndGet();

		// Submit initial task

		executor.submit(new CrawlTask(new UrlNode(startUrl, 0), this, maxPages, maxDepth));

		while (activeTasks.get() > 0) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		executor.shutdown();

		System.out.println("Crawl complete. Pages visited: " + visitedUrls.size());
	}

	public boolean shouldVisit(String url, int depth, int maxPages, int maxDepth) {
		return !visitedUrls.contains(url) && visitedUrls.size() < maxPages && depth <= maxDepth;
	}

	public void markVisited(String url) {
		visitedUrls.add(url);
	}

	public void submitTask(CrawlTask task) {
		executor.submit(task);
	}

	public void incrementTasks() {
		activeTasks.incrementAndGet();
	}

	public void decrementTasks() {
		activeTasks.decrementAndGet();
	}

	private String extractDomain(String url) {
		try {
			java.net.URI uri = new java.net.URI(url);
			return uri.getHost();
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isSameDomain(String url) {
		try {
			java.net.URI uri = new java.net.URI(url);
			String host = uri.getHost();
			return host != null && host.equals(baseDomain);
		} catch (Exception e) {
			return false;
		}
	}

	public void applyRateLimit() {
		try {
			Thread.sleep(crawlDelayMs);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}