package com.pramati.imaginea.webCrawler.exceptions;

public class WebCrawlerRunnerException extends Exception{

	private static final long serialVersionUID = 1L;

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
