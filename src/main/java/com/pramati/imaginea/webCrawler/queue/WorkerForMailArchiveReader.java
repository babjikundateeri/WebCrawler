package com.pramati.imaginea.webCrawler.queue;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;
import com.pramati.imaginea.webCrawler.utils.FileWriter;
import com.pramati.imaginea.webCrawler.utils.URLConnectionReader;

public class WorkerForMailArchiveReader implements Callable<MailArchiveDTO> {
	private static final Logger LOGGER = Logger.getLogger(WorkerForMailArchiveReader.class);
	private MailArchiveDTO mailArchiveDTO;
	private URLConnectionReader urlConnectionReader;

	public WorkerForMailArchiveReader (URLConnectionReader urlConnectionReader, final MailArchiveDTO mailArchiveDTO) {
		this.urlConnectionReader = urlConnectionReader;
		this.mailArchiveDTO = mailArchiveDTO;
	}
	
	public MailArchiveDTO call() throws Exception {
		LOGGER.debug("Starting processing for " + mailArchiveDTO.getFileName());
		FileWriter fileWriter = new FileWriter();
		fileWriter.setFileDir(mailArchiveDTO.getDir());
		fileWriter.setFileName(mailArchiveDTO.getFileName());
		fileWriter.initFile();
		if (mailArchiveDTO.isCheckForPreExistance() && fileWriter.isFileExists()) {
			// no need to loading this mail again
			MailArchivesReaderQueueManager.getInstance().increseOutQueueCtr(true);
			throw new Exception("File already exists " + mailArchiveDTO.getDir() + File.separator + mailArchiveDTO.getFileName());
		}
		
		fileWriter.loadFileContent(urlConnectionReader.getInputStream(mailArchiveDTO.getURL()));
		MailArchivesReaderQueueManager.getInstance().increseOutQueueCtr(false);
		return mailArchiveDTO;
	}

}
