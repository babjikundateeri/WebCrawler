package com.pramati.imaginea.webCrawler.queue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.pramati.imaginea.webCrawler.utils.WebCrawlerParser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;
import com.pramati.imaginea.webCrawler.dto.MailArchivesMonthlyDTO;
import com.pramati.imaginea.webCrawler.utils.URLConnectionReader;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerConstants;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerProperties;

public class WorkerThreadForMonthlyArchives implements Callable<MailArchivesMonthlyDTO> {
	
	private final static Logger LOGGER = Logger.getLogger(WorkerThreadForMonthlyArchives.class);

	private MailArchivesMonthlyDTO monthlyDTO;
	
	public WorkerThreadForMonthlyArchives (MailArchivesMonthlyDTO monthlyDTO) {
		this.monthlyDTO = monthlyDTO;
	}
	
	/**
	 *  check's if mails of this month already done leaded.
	 *  if yes, do nothing
	 *  
	 *  if some mails are downloaded already, try to download others, which are not downloaded
	 *  
	 *  if completely mails are not downloaded, need to create the directory structure and then download all the mails
	 */
	public MailArchivesMonthlyDTO call() throws Exception {
		LOGGER.debug(monthlyDTO.getId() + " Proecessing for " + monthlyDTO.getMonth());
		Collection<MailArchiveDTO> mailArchiveDTOs = WebCrawlerParser.parseMonthlyData(monthlyDTO);
		LOGGER.info(monthlyDTO.getId() + "  entries " + mailArchiveDTOs.size());
		MailArchivesReaderQueueManager.getInstance().addQueueEntry(mailArchiveDTOs);
		MailArchivesReaderQueueManager.getInstance().initQueue();
		return monthlyDTO;
	}
	

}
