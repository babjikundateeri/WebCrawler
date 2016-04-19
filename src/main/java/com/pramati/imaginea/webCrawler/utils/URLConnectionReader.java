package com.pramati.imaginea.webCrawler.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class URLConnectionReader {
	private static Logger LOGGER = Logger.getLogger(URLConnectionReader.class);


	public static InputStream getInputStream(final String urlStr) throws IOException {
		URL url = null;
		URLConnection urlConnection = null;
		try {
			url = new URL(urlStr);
			urlConnection = url.openConnection();
		} catch (MalformedURLException e) {
			LOGGER.warn("Unable to read from URL " + urlStr, e);
			return null;
		} catch (IOException e) {
			LOGGER.warn("Unable to Connect to " + urlStr, e);
			return null;
		}
		LOGGER.debug("Connection done for " + urlStr);
		return  urlConnection.getInputStream();
	}

	public static String getContent(final String urlStr) {

		BufferedReader reader = null;
		StringBuilder urlContent = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getInputStream(urlStr)));
			urlContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				urlContent.append(line);
			}
			
		} catch (IOException e) {
			LOGGER.warn("Unable to reade data from " + urlStr, e);
		}	
		
		return urlContent.toString();
	}
	
}
