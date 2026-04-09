package com.threadcrawler.model;

public class UrlNode {
	private String url;
	private int depth;

	public UrlNode(String url, int depth) {
		this.url = url;
		this.depth = depth;
	}

	public String getUrl() {
		return url;
	}

	public int getDepth() {
		return depth;
	}
}