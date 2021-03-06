package com.pramati.imaginea.webCrawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class FileWriter {
	private static Logger LOGGER = Logger.getLogger(FileWriter.class);
	
	private String fileDir;
	private String fileName;
	
	private File file = null;

	public FileWriter() {

	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public FileWriter(final String fileDir,
					  final String fileName) {
		this.fileDir = fileDir;
		this.fileName = fileName;
		initFile();
	}
	
	public void initFile () {
		file = new File (fileDir + File.separator +fileName) ;
	}
	
	public boolean isFileExists() {
		return file != null && file.exists();
	}
	
	public boolean loadFileContent(final InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			int read;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			LOGGER.debug("Writing to file " + fileDir + File.separator + fileName + " is done.");
		} catch (IOException e) {
			LOGGER.warn("Failed to write data to " + fileDir + File.separator +  fileName);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException ignore) {
				}
		}
		return true;
	}
}
