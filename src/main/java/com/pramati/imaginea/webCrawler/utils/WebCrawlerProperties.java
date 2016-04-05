package com.pramati.imaginea.webCrawler.utils;

import java.io.File;

public class WebCrawlerProperties {
	
	private static String YEAR = "2016";
	private static String BASE_URL = "http://mail-archives.apache.org/mod_mbox";
	private static String QUERY_URL = "maven-users";
	private static String currentDirectory = System.getProperty("user.dir");
	private static String fileExtension = ".txt";
	
	public static String getYear() {
		return YEAR;
	}
	
	public static String getMailArchiveURL() {
		return BASE_URL + File.separator + QUERY_URL;
	}
	
	public static String getCurrentDirectory() {
		return currentDirectory;
	}
	
	public static String getFileExtension() {
		return fileExtension;
	}
	
	public static void setYear(final String year) {
		YEAR = year;
	}
	
	public static void setYear(final int year) {
		setYear(""+year);
	}
	
}
