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
	URL url = null;
	URLConnection urlConnection = null;
	public URLConnectionReader (final String urlStr) {
		try {
			url = new URL(urlStr);
			urlConnection = url.openConnection();
		} catch (MalformedURLException e) {
			LOGGER.warn("Unable to read from URL " + urlStr, e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.warn("Unable to Connect to " + urlStr, e);
			e.printStackTrace();
		}
		LOGGER.debug("Connection done for " + urlStr);
		
	}
	
	public InputStream getInputStream() throws IOException {
		return urlConnection.getInputStream();
	}
	
	public String getContent() {
		BufferedReader reader = null;
		StringBuilder urlContent = null;
		try {
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			urlContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				urlContent.append(line);
			}
			
		} catch (IOException e) {
			LOGGER.warn("Unable to reade data from " + url.toString(), e);
		}	
		
		return urlContent.toString();
	}
	
}
