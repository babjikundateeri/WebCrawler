package com.pramati.imaginea.webCrawler;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pramati.imaginea.webCrawler.exceptions.WebCrawlerRunnerException;
import com.pramati.imaginea.webCrawler.utils.URLConnectionReader;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerProperties;

public class WebCrawlerRunner {
	private static Logger LOGGER  = Logger.getLogger(WebCrawlerRunner.class);
	private String[] input = null;
	public WebCrawlerRunner(final String[] input) throws WebCrawlerRunnerException {
		this.input = input;
		if (input.length > 0) {
			try  {
				WebCrawlerProperties.setYear(Integer.parseInt(this.input[0]));
			} catch (NumberFormatException ne) {
				throw new WebCrawlerRunnerException("WebCrawler Initialization Exception, Option Year should be integer " + ne.getMessage());
			}
		}
		
		
	}
	public void runWebCrawler() {
		LOGGER.info("Running Crawler for the year " + WebCrawlerProperties.getYear());
		URLConnectionReader urlConnectionReader = new URLConnectionReader(WebCrawlerProperties.getMailArchiveURL());

		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		LOGGER.debug("After factory loading");
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			LOGGER.debug("After builder loading..");
			document = builder.parse(urlConnectionReader.getInputStream());
			LOGGER.debug("Got the Document element");
			
			Element gridElement = document.getElementById("grid");
			
			if (gridElement == null) {
				LOGGER.warn("element with id grid is missing in html");
				return;
			}
						
			NodeList gridTableTbodayList = gridElement.getChildNodes();
			
			for (int gridtbodyctr = 0; gridtbodyctr < gridTableTbodayList.getLength(); gridtbodyctr++) {
				Node gridTableTbody = gridTableTbodayList.item(gridtbodyctr);
				
				if (gridTableTbody.getNodeType() != Node.ELEMENT_NODE) continue;
				
				NodeList gridTableChilds = null;
				if (gridTableTbody.hasChildNodes())
					gridTableChilds = gridTableTbody.getChildNodes();
				
				for (int i = 0; i < gridTableChilds.getLength(); i++) {
					Node gridTableTRNode = gridTableChilds.item(i);
					if (gridTableTRNode.getNodeType() != Node.ELEMENT_NODE && !gridTableTRNode.hasChildNodes()) continue;
					
					NodeList gridTableTDNodes = gridTableTRNode.getChildNodes();
					for (int j = 0; j < gridTableTDNodes.getLength(); j++) {
						Node gridTDNode = gridTableTDNodes.item(j);
						
						if (gridTDNode.getNodeType() == Node.ELEMENT_NODE) {
							LOGGER.debug("Node Name " + gridTDNode.getNodeName());
							//LOGGER.debug("" + gridTDNode.getTextContent());
						}
					}
				}
			}
			
		} catch (ParserConfigurationException e) {
			LOGGER.warn(e.getMessage());
		} catch (SAXException e) {
			LOGGER.warn(e.getMessage());
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
		}
	}
}
