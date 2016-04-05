package com.pramati.imaginea.webCrawler.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class URLConnectionReader {
	private static Logger LOGGER = Logger.getLogger(URLConnectionReader.class);
	private boolean isOnline = false;
	URL url = null;
	URLConnection urlConnection = null;
	public URLConnectionReader (final String urlStr) {
		if (isOnline) {
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
			LOGGER.info("Connection done for " + urlStr);
		} else {
			LOGGER.warn("URL is OFFLINE, Reading Data FROM stored file");
		}
		
	}
	
	public InputStream getInputStream() throws IOException {
		if (isOnline) {
			return urlConnection.getInputStream();
		} else {
			File f = new File ("/home/babjik/Downloads/maven-users");
			return new FileInputStream(f);
		}
		
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
