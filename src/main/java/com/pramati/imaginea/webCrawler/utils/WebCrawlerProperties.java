package com.pramati.imaginea.webCrawler.utils;

import java.io.File;

public class WebCrawlerProperties {
	
	private static String YEAR = "2016";
	private static String MONTH = null;
	private static String BASE_URL = "http://mail-archives.apache.org/mod_mbox";
	private static String QUERY_URL = "maven-users";
	private static String CURRENT_DIR = System.getProperty("user.dir");
	private static String FILE_EXTENSION = ".txt";
	private static Integer MAIN_POOL_SIZE = 5;
	private static Integer THREAD_POOL_SIZE = 10;
	private static Integer MAX_ALLOWED_THREADS = 30;
	private static Integer MAILS_PER_PAGE = 100;
	
	public static String getYear() {
		return YEAR;
	}
	
	public static String getQeueryURL() {
		return QUERY_URL;
	}
	
	public static String getBaseURL() {
		return BASE_URL;
	}
	public static String getMailArchiveURL() {
		return BASE_URL + File.separator + QUERY_URL;
	}
	
	public static String getCurrentDirectory() {
		return CURRENT_DIR;
	}
	
	public static String getFileExtension() {
		return FILE_EXTENSION;
	}
	
	public static void setYear(final String year) {
		YEAR = year;
	}
	
	public static void setYear(final int year) {
		setYear(""+year);
	}
	
	public static Integer getMainPoolSize() {
		return MAIN_POOL_SIZE;
	}

	public static String getMonth() {
		return MONTH;
	}

	public static void setMonth(String month) {
		MONTH = month;
	}
	
	public static Integer getMailsPerPage() {
		return MAILS_PER_PAGE;
	}
	
	public static Integer getThreadPoolSize() {
		return THREAD_POOL_SIZE;
	}
	
	public static Integer getMaxAllowedThreadCount() {
		return MAX_ALLOWED_THREADS;
	}
}
