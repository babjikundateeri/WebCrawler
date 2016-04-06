package com.pramati.imaginea.webCrawler.queue;

import java.util.LinkedList;
import java.util.Queue;

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;

public class MailArchivesReaderQueueManager {
	private static MailArchivesReaderQueueManager me;
	private volatile Queue<MailArchiveDTO> queue = null;
	private volatile boolean isQueueStarted = false;
	private MailArchivesReaderQueueManager() {
		queue = new LinkedList<MailArchiveDTO>();
	}
	
	public static MailArchivesReaderQueueManager getInstance() {
		if (me == null) {
			synchronized (MailArchivesReaderQueueManager.class) {
				if (me == null) {
					me = new MailArchivesReaderQueueManager();
				}
			}
		}
		return me;
	}
	
	public boolean addQueueEntry(final MailArchiveDTO mailArchiveDTO) {
		return queue.add(mailArchiveDTO);
	}
	
	public MailArchiveDTO pollEntryFromQueue() {
		if (!queue.isEmpty()) {
			return queue.poll();
		}
		
		return null;
	}
	
	public void initQueue() {
		if (!isQueueStarted) {
			isQueueStarted = true;
		}
	}
}
