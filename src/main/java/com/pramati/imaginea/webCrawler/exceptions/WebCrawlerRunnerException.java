package com.pramati.imaginea.webCrawler.exceptions;

public class WebCrawlerRunnerException extends Exception{
	public WebCrawlerRunnerException (String message) {
		super(message);
	}
	
	public WebCrawlerRunnerException (Throwable e) {
		super(e);
	}
	
	public WebCrawlerRunnerException () {
		super();
	}
	
}
