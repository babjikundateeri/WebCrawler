package com.pramati.imaginea.webCrawler;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.pramati.imaginea.webCrawler.dto.MailArchivesMonthlyDTO;
import com.pramati.imaginea.webCrawler.exceptions.WebCrawlerRunnerException;
import com.pramati.imaginea.webCrawler.queue.WebCrawlerQueueManager;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerParser;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerProperties;

public class WebCrawlerRunner {
	private static Logger LOGGER  = Logger.getLogger(WebCrawlerRunner.class);
	private String[] input = null;
	public WebCrawlerRunner(final String[] input) throws WebCrawlerRunnerException {
		this.input = input;
		if (input.length > 0) {
			try  {
				WebCrawlerProperties.setYear(Integer.parseInt(this.input[0]));
			} catch (NumberFormatException ne) {
				throw new WebCrawlerRunnerException("WebCrawler Initialization Exception, Option Year should be integer " + ne.getMessage());
			}
		}
		
		if (input.length > 1) { //added month in options
			WebCrawlerProperties.setMonth(input[1]);
		}
		
		if (input.length > 2) { // adding archives folder name in options
			WebCrawlerProperties.setQueryURL(input[2]);
		}
	}
	public void runWebCrawler() {
		LOGGER.info("Running Crawler for the year " + WebCrawlerProperties.getYear());
		LOGGER.info("Running Crawler for the Months " + WebCrawlerProperties.getMonth());
		LOGGER.info("Reading Archives for " + WebCrawlerProperties.getQeueryURL());
		Collection<MailArchivesMonthlyDTO> mailArchivesMonthlyDTOs = WebCrawlerParser.getMailArchivesMonthlyDTOFromURL(WebCrawlerProperties.getMailArchiveURL());
		if(WebCrawlerQueueManager.getInstance().addMailArchivesMonthlyDTO(mailArchivesMonthlyDTOs)) {
			WebCrawlerQueueManager.getInstance().initQueue();
		}
	}
	
	
}
