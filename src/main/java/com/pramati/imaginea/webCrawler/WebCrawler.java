package com.pramati.imaginea.webCrawler;

import com.pramati.imaginea.webCrawler.queue.MailArchivesReaderQueueManager;
import com.pramati.imaginea.webCrawler.queue.WebCrawlerQueueManager;
import com.pramati.imaginea.webCrawler.utils.FileWriter;
import com.pramati.imaginea.webCrawler.utils.URLConnectionReader;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerParser;
import org.apache.log4j.Logger;

import com.pramati.imaginea.webCrawler.exceptions.WebCrawlerRunnerException;

public class WebCrawler {
	private static Logger LOGGER = Logger.getLogger(WebCrawler.class);
	public static void main (String[] args) {
		LOGGER.info("Starting Crawler..");
		try {
			URLConnectionReader urlConnectionReader = new URLConnectionReader();
			WebCrawlerParser parser = new WebCrawlerParser(urlConnectionReader);
			MailArchivesReaderQueueManager.getInstance().setUrlConnectionReader(urlConnectionReader);
			WebCrawlerQueueManager.getInstance().setParser(parser);

			new WebCrawlerRunner(args, parser).runWebCrawler();
		} catch (WebCrawlerRunnerException e) {
			LOGGER.error(e.getMessage());
		}
		
		LOGGER.info("Crawler run finished..");
	}
}
