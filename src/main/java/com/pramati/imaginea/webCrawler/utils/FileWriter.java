package com.pramati.imaginea.webCrawler.utils;

import java.io.File;

import org.apache.log4j.Logger;

public class FileWriter {
	private String year;
	private String month;
	private String fileName;
	
	private static Logger LOGGER = Logger.getLogger(FileWriter.class);
	
	
	public FileWriter (final String year,
			final String month,
			final String fileName) {
		LOGGER.info("Initializ");
		this.year = year;
		this.month = month;
		this.fileName = fileName;
		checkDirectory();
	}
	
	private boolean checkDirectory () {
		final String dirPath = getDirPath();
		File dir = new File (dirPath) ;
		
		if (!dir.exists()) {
			LOGGER.debug("Dir " + dir + " does not exists, creating the dir's");
			dir.mkdirs();
		}
		
		return true;
	}
	
	private String getDirPath() {
		//return WebCrawlerProperties.getCurrentDirectory() + File.separator + year + File.separator + month + File.pathSeparator + fileName + WebCrawlerProperties.getFileExtension();
		return WebCrawlerProperties.getCurrentDirectory() + File.separator + year + File.separator + month ;
	}
}
