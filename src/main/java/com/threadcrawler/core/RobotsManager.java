package com.threadcrawler.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RobotsManager {

	private final Map<String, List<String>> disallowRules = new ConcurrentHashMap<>();

	public void loadRobots(String host) {
		if (disallowRules.containsKey(host)) {
			return;
		}

		List<String> rules = new ArrayList<>();

		try {
			String robotsUrl = "https://" + host + "/robots.txt";

			System.out.println("Loading robots.txt from " + robotsUrl);

			URI uri = URI.create(robotsUrl);

			BufferedReader reader = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

			boolean useRules = false;

			String line;

			while ((line = reader.readLine()) != null) {

				line = line.trim();

				if (line.startsWith("#") || line.isEmpty()) {
					continue;
				}

				if (line.equalsIgnoreCase("User-agent: *")) {
					useRules = true;
					continue;
				}

				if (line.toLowerCase().startsWith("user-agent:")) {
					useRules = false;
					continue;
				}

				if (useRules && line.toLowerCase().startsWith("disallow:")) {
					String path = line.substring(9).trim();

					if (!path.isEmpty()) {
						rules.add(path);
					}
				}
			}
			reader.close();

		} catch (IOException e) {
			System.out.println("No robots.txt found for " + host);
		}
		disallowRules.put(host, rules);

		System.out.println("Loaded " + rules.size() + " robots rules.");
	}

	public List<String> getRules(String host) {
		return disallowRules.get(host);
	}

	public boolean isAllowed(String url) {
		try {
			URI uri = URI.create(url);

			String host = uri.getHost();

			List<String> rules = disallowRules.get(host);

			// No robots.txt loaded or no restriction
			if (rules == null) {
				return true;
			}

			String path = uri.getPath();

			for (String rule : rules) {
				if (path.startsWith(rule)) {
					return false;
				}
			}
			return true;

		} catch (Exception e) {
			return false;
		}

	}
}
