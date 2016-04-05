package com.pramati.imaginea.webCrawler;

import org.apache.log4j.Logger;

import com.pramati.imaginea.webCrawler.exceptions.WebCrawlerRunnerException;

public class WebCrawler {
	private static Logger LOGGER = Logger.getLogger(WebCrawler.class);
	public static void main (String[] args) {
		LOGGER.info("Starting Crawler..");
		try {
			new WebCrawlerRunner(args).runWebCrawler();
		} catch (WebCrawlerRunnerException e) {
			LOGGER.error(e.getMessage());
		}
		
		LOGGER.info("Crawler run finished..");
	}
}
