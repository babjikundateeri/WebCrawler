package com.pramati.imaginea.webCrawler.queue;

import java.util.concurrent.Callable;

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;

public class WorkerForMailArchiveReader implements Callable<MailArchiveDTO> {
	private MailArchiveDTO mailArchiveDTO;
	
	public WorkerForMailArchiveReader (final MailArchiveDTO mailArchiveDTO) {
		this.mailArchiveDTO = mailArchiveDTO;
	}
	
	public MailArchiveDTO call() throws Exception {
		return null;
	}

}
