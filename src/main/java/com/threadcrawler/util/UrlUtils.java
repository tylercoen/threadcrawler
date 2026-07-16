package com.threadcrawler.util;

import java.net.URI;
import java.net.URISyntaxException;

public final class UrlUtils {

	private UrlUtils() {
		// Utility class
	}

	public static String normalize(String url) {
		try {
			URI uri = new URI(url);

			String scheme = uri.getScheme();
			String host = uri.getHost();

			if (scheme == null || host == null) {
				return null;
			}

			scheme = scheme.toLowerCase();
			host = host.toLowerCase();

			int port = uri.getPort();

			// Remove default ports
			if ((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443)) {
				port = -1;
			}

			String path = uri.getPath();

			if (path == null) {
				path = "";
			}

			if (path.endsWith("/") && path.length() > 1) {
				path = path.substring(0, path.length() - 1);
			}

			URI normalized = new URI(scheme, null, host, port, path, uri.getQuery(), null); // removes fragments(#..)

			return normalized.toString();
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public static boolean isCrawlable(String url) {
		if (url == null) {
			return false;
		}

		String lower = url.toLowerCase();

		// Skip non-html resources
		if (lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif")
				|| lower.endsWith(".svg") || lower.endsWith(".ico") || lower.endsWith(".pdf") || lower.endsWith(".zip")
				|| lower.endsWith(".css") || lower.endsWith(".js")) {
			return false;
		}

		if (lower.contains("/special:")) {
			return false;
		}

		if (lower.contains("/talk:")) {
			return false;
		}

		if (lower.contains("action=edit")) {
			return false;
		}

		if (lower.contains("action=history")) {
			return false;
		}
		return true;
	}

}
