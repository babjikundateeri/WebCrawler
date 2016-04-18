package com.pramati.imaginea.webCrawler.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.pramati.imaginea.webCrawler.dto.MailArchivesMonthlyDTO;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerProperties;

public class WebCrawlerQueueManager {
	private static final Logger LOGGER = Logger.getLogger(WebCrawlerQueueManager.class);
	private static WebCrawlerQueueManager me;
	
	private volatile Queue<MailArchivesMonthlyDTO> queueInput = null;
	
	private WebCrawlerQueueManager() {
		queueInput = new LinkedList<MailArchivesMonthlyDTO>();
	}
	
	public static WebCrawlerQueueManager getInstance() {
		if (me == null) {
			synchronized (WebCrawlerQueueManager.class) {
				if (me == null) {
					me = new WebCrawlerQueueManager();
				}
			}
		}
		return me;
	}
	public boolean addMailArchivesMonthlyDTO(final java.util.Collection<MailArchivesMonthlyDTO> monthlyDTOs) {
		if (monthlyDTOs == null || monthlyDTOs.isEmpty()) {
			return false;
		}
		return queueInput.addAll(monthlyDTOs);
	}
	public boolean addMailArchivesMonthlyDTO(final MailArchivesMonthlyDTO dto) {
		if (dto.getLink() != null && !dto.getLink().equalsIgnoreCase(""))
			return queueInput.add(dto);
		else
			return false;
	}
	
	public MailArchivesMonthlyDTO poolEntryFromQueue() {
		if (!queueInput.isEmpty()) {
			return queueInput.poll();
		}
		return null;
	}
	
	public void initQueue() {
		LOGGER.debug("Starting Service");
		ExecutorService service = Executors.newFixedThreadPool(WebCrawlerProperties.getMainPoolSize());
		MailArchivesMonthlyDTO dto;
		while ( (dto = poolEntryFromQueue() ) != null) {
			WorkerThreadForMonthlyArchives worker = new WorkerThreadForMonthlyArchives(dto);
			service.submit(worker);
		}
		LOGGER.debug("Shutting down the service");
		service.shutdown();
	}
	
}
