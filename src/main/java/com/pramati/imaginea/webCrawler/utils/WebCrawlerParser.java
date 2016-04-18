package com.pramati.imaginea.webCrawler.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pramati.imaginea.webCrawler.dto.MailArchivesMonthlyDTO;

public class WebCrawlerParser {
	private static final Logger LOGGER = Logger.getLogger(WebCrawlerParser.class);
	public static Collection<MailArchivesMonthlyDTO> getMailArchivesMonthlyDTOFromURL(final String url) {
		Collection<MailArchivesMonthlyDTO> mailArchivesMonthlyDTOs = new ArrayList<MailArchivesMonthlyDTO>();

		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		LOGGER.debug("After factory loading");
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			LOGGER.debug("After builder loading..");
			document = builder.parse(new URLConnectionReader(url).getInputStream());
			LOGGER.debug("Got the Document element");
			
			Element gridElement = document.getElementById(WebCrawlerConstants.GRID);
			
			if (gridElement == null) {
				LOGGER.warn("element with id grid is missing in html");
				return mailArchivesMonthlyDTOs;
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
						
						if (gridTDNode.getNodeType() == Node.ELEMENT_NODE 
								&& gridTDNode.getTextContent().contains(WebCrawlerProperties.getYear())
								&& gridTDNode.hasChildNodes()) {
							mailArchivesMonthlyDTOs.addAll(WebCrawlerParser.parseMothTable(gridTDNode));
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
		return mailArchivesMonthlyDTOs;
	}
	public static Collection<MailArchivesMonthlyDTO> parseMothTable(Node tableNode) {
		Collection<MailArchivesMonthlyDTO> mailArchivesMonthlyDTOs = new ArrayList<MailArchivesMonthlyDTO>(); 
		NodeList tableChilds = tableNode.getChildNodes();
		for (int i = 0; i < tableChilds.getLength(); i++) {
			Node tableChildNode = tableChilds.item(i);

			if (tableChildNode.getNodeType() == Node.ELEMENT_NODE
					&& tableChildNode.getNodeName().equalsIgnoreCase(WebCrawlerConstants.TBODY)
					&& tableChildNode.hasChildNodes()) {

				NodeList trNodeList = tableChildNode.getChildNodes();

				for (int trctr = 0; trctr < trNodeList.getLength(); trctr++) {
					Node trNode = trNodeList.item(trctr);
					if (trNode.getNodeType() == Node.ELEMENT_NODE && trNode.hasChildNodes()) {
						MailArchivesMonthlyDTO dto = new MailArchivesMonthlyDTO();

						NodeList tdNodeList = trNode.getChildNodes();
						for (int tdctr = 0; tdctr < tdNodeList.getLength(); tdctr++) {
							Node tdNode = tdNodeList.item(tdctr);
							if (tdNode.getNodeType() == Node.ELEMENT_NODE) {
								NamedNodeMap tdNamedNodeMap = tdNode.getAttributes();
								Node classNode = tdNamedNodeMap.getNamedItem(WebCrawlerConstants.CLASS);

								if (classNode.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.DATE)) { // date
									String monthWithYear = tdNode.getTextContent();
									String monthWithYearsplit[] = monthWithYear.split(WebCrawlerConstants.SPACE);
									dto.setMonth(monthWithYearsplit[0]);
									dto.setYear(monthWithYearsplit[1]);
								} else if (classNode.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.LINKS)
										&& tdNode.hasChildNodes()) { // links
									NodeList tdChilds = tdNode.getChildNodes();
									for (int tdChildctr = 0; tdChildctr < tdChilds.getLength(); tdChildctr++) {
										Node tdChildNode = tdChilds.item(tdChildctr);
										if (tdChildNode.getNodeType() == Node.ELEMENT_NODE && tdChildNode.getNodeName()
												.equalsIgnoreCase(WebCrawlerConstants.SPAN)) {
											NamedNodeMap tdAttributes = tdChildNode.getAttributes();
											Node idNode = tdAttributes.getNamedItem(WebCrawlerConstants.ID);
											if (idNode != null && idNode.getNodeType() == Node.ATTRIBUTE_NODE) {
												dto.setId(idNode.getNodeValue());
												dto.setLink(dto.getId() + WebCrawlerConstants.MBOX
														+ WebCrawlerConstants.SLASH + WebCrawlerConstants.DATE);
											}
										}
									}
								} else if (classNode.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.MSG_COUNT)) { // message
																														// count
									try {
										dto.setMsgCount(Integer.parseInt(tdNode.getTextContent()));
									} catch (NumberFormatException e) {
										LOGGER.warn(e.getMessage());
									}
								}
							}
						}
						if (WebCrawlerProperties.getMonth() == null
								|| WebCrawlerProperties.getMonth().equalsIgnoreCase(WebCrawlerConstants.ALL)
								|| WebCrawlerProperties.getMonth().toUpperCase()
										.contains(dto.getMonth().toUpperCase())) {
							LOGGER.debug(dto);
							mailArchivesMonthlyDTOs.add(dto);
						}
					}

				}
			}
		}
		return mailArchivesMonthlyDTOs;
	}
}
