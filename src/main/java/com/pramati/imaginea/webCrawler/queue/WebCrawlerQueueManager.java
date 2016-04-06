package com.pramati.imaginea.webCrawler.queue;

import java.util.LinkedList;
import java.util.Queue;

import com.pramati.imaginea.webCrawler.dto.MailArchivesMonthlyDTO;

public class WebCrawlerQueueManager {
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
	
	public boolean addMailArchivesMonthlyDTO(final MailArchivesMonthlyDTO dto) {
		if (dto.getLink() != null && !dto.getLink().equalsIgnoreCase(""))
			return queueInput.add(dto);
		else
			return false;
	}
	
	public MailArchivesMonthlyDTO poolMailArchivesMonthlyDTO() {
		if (!queueInput.isEmpty()) {
			return queueInput.poll();
		}
		return null;
	}
	
}
