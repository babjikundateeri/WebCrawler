package com.pramati.imaginea.webCrawler.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class WebCrawlerProperties {
	private static final Logger LOGGER = Logger.getLogger(WebCrawlerProperties.class); 
	private static Properties properties = null;
	
	private static String YEAR = "2016";
	private static String MONTH = "all";
	private static String BASE_URL = "http://mail-archives.apache.org/mod_mbox";
	private static String QUERY_URL = "maven-users";
	private static String CURRENT_DIR = System.getProperty("user.dir");
	private static String FILE_EXTENSION = ".txt";
	private static Integer MAIN_POOL_SIZE = 5;
	private static Integer THREAD_POOL_SIZE = 10;
	private static Integer MAX_ALLOWED_THREADS = 30;
	private static Integer MAILS_PER_PAGE = 100;
	
	static {
		initProperties();
	}
	
	public static String getYear() {
		return properties.getProperty(WebCrawlerConstants.YEAR,YEAR);
	}
	
	public static String getQeueryURL() {
		return properties.getProperty(WebCrawlerConstants.QUERY_URL, QUERY_URL);
	}
	
	public static void setQueryURL(final String queryString) {
		properties.setProperty(WebCrawlerConstants.QUERY_URL, queryString);
	}
	
	public static String getBaseURL() {
		return properties.getProperty(WebCrawlerConstants.BASE_URL, BASE_URL);
	}
	public static String getMailArchiveURL() {
		return getBaseURL() + File.separator + getQeueryURL();
	}
	
	public static String getCurrentDirectory() {
		return CURRENT_DIR;
	}
	
	public static String getFileExtension() {
		return FILE_EXTENSION;
	}
	
	public static void setYear(final String year) {
		properties.setProperty(WebCrawlerConstants.YEAR, year);
	}
	
	public static void setYear(final int year) {
		setYear(""+year);
	}
	
	public static Integer getMainPoolSize() {
		return Integer.parseInt(properties.getProperty(WebCrawlerConstants.MAIN_POOL_SIZE, MAIN_POOL_SIZE.toString()));
	}

	public static String getMonth() {
		return properties.getProperty(WebCrawlerConstants.MONTH, MONTH);
	}

	public static void setMonth(String month) {
		properties.setProperty(WebCrawlerConstants.MONTH, month);
	}
	
	public static Integer getMailsPerPage() {
		return Integer.parseInt(properties.getProperty(WebCrawlerConstants.MAILS_PER_PAGE, MAILS_PER_PAGE.toString()));
	}
	
	public static Integer getThreadPoolSize() {
		return Integer.parseInt(properties.getProperty(WebCrawlerConstants.THREAD_POOL_SIZE, THREAD_POOL_SIZE.toString()));
	}
	
	public static Integer getMaxAllowedThreadCount() {
		return Integer.parseInt(properties.getProperty(WebCrawlerConstants.MAX_ALLOWED_THREADS, MAX_ALLOWED_THREADS.toString()));
	}
	
	private static void initProperties() {
		if (properties == null || properties.isEmpty()) {
			LOGGER.debug("Init Properties");
			properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = null;
			try {
				is = classLoader.getResourceAsStream(WebCrawlerConstants.WebCrawlerResourceFile);
				properties.load(is);
			} catch (Exception e) {
				LOGGER.warn("Failed to load properites " + e.getMessage(), e);
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException ignore) {
						LOGGER.warn(ignore.getMessage());
					}
			}
		}
	}
}
