package com.pramati.imaginea.webCrawler.queue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerProperties;
import org.apache.log4j.Logger;

public class MailArchivesReaderQueueManager {
	private static final Logger LOGGER = Logger.getLogger(MailArchivesReaderQueueManager.class);
	private static MailArchivesReaderQueueManager me;
	private volatile Queue<MailArchiveDTO> queue = null;
	private volatile boolean isQueueStarted = false;
	private volatile boolean isDone = false;
	private volatile int inQueueCtr = 0;
	private volatile int outQueueCtr = 0;
	private volatile int outSkipCount = 0;
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

	public boolean addQueueEntry(final Collection<MailArchiveDTO> mailArchiveDTOs) {
		if (mailArchiveDTOs == null || mailArchiveDTOs.isEmpty()) {
			return false;
		} else {
			for (MailArchiveDTO dto: mailArchiveDTOs ) {
				addQueueEntry(dto);
			}
			return true;
		}
	}
	public boolean addQueueEntry(final MailArchiveDTO mailArchiveDTO) {
		inQueueCtr++;
		return queue.add(mailArchiveDTO);
	}
	
	public MailArchiveDTO pollEntryFromQueue() {
		if (!queue.isEmpty()) {
			return queue.poll();
		}
		
		return null;
	}
	
	public void increseOutQueueCtr(final boolean isSkipp) {
		if (isSkipp)
			outSkipCount++;
		else
			outQueueCtr++;
	}
	public void initQueue() {
		(new Thread() {
			public void run() {
				while (!isDone) {
					System.out.print("\r Queue Status - In [" + inQueueCtr + "] / Processed [" + outQueueCtr + "] / Skipped [" + outSkipCount + "]  ");
				}
				
			}
		}).start();
		if (!isQueueStarted) {
			LOGGER.info("Queue Init done.");
			isQueueStarted = true;
			ExecutorService service = Executors.newFixedThreadPool(WebCrawlerProperties.getThreadPoolSize());
			int emptyQeueRunTimes = 0;
			while (true) {
//				System.out.print("\r Queue Status - In [" + inQueueCtr + "] / Processed [" + outQueueCtr + "] / Skipped [" + outSkipCount + "]  ");
				MailArchiveDTO archiveDTO = null;
				if ( (archiveDTO = pollEntryFromQueue()) != null ) {
					WorkerForMailArchiveReader worker = new WorkerForMailArchiveReader(archiveDTO);
					service.submit(worker);
					emptyQeueRunTimes = 0;
				} /*else if (WebCrawlerQueueManager.getInstance().isServiceShutDown()
						&& inQueueCtr <= (outQueueCtr + outSkipCount)) {
					LOGGER.warn(" inQueueCtr <= (outQueueCtr + outSkipCount) :: " + inQueueCtr + "<= ("+ outQueueCtr + " + " + outSkipCount + ")");
					LOGGER.warn("Meet condition WebCrawlerQueueManager.getInstance().isServiceShutDown() && inQueueCtr <= (outQueueCtr + outSkipCount");
					break;
				} */else {
					if (emptyQeueRunTimes > 3) {
						break;  // to stop pool run
					}
					emptyQeueRunTimes++;
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			LOGGER.info("Before shutdown Queue");
			service.shutdown();
			isDone = true;
		}
	}
}
