package com.pramati.imaginea.webCrawler.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;
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

		Document document;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		LOGGER.debug("After factory loading");
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			LOGGER.debug("After builder loading..");
			document = builder.parse(URLConnectionReader.getInputStream(url));
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

				if (gridTableChilds != null) {
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

	public static Collection<MailArchiveDTO> parseMonthlyData(final MailArchivesMonthlyDTO monthlyDTO) {
		Collection<MailArchiveDTO> mailArchiveDTOs = new ArrayList<MailArchiveDTO>();
		final  String dirName = getDirectoryName(monthlyDTO);
		boolean isDirExists = checkForDirectoryExistance(dirName) ;
		LOGGER.debug(monthlyDTO.getId() + " isDirExists " +  dirName + " / " +isDirExists);
		int noOfPriviousFiles = 0;
		File[] filesInDir = null;
		if (isDirExists) {
			File dir = new File(dirName);
			filesInDir = dir.listFiles();
			noOfPriviousFiles = filesInDir.length;
		}

		LOGGER.info(monthlyDTO.getId() + "  Mails in local / Mails in Server  :: " + noOfPriviousFiles + " / " + monthlyDTO.getMsgCount());
		if (noOfPriviousFiles < monthlyDTO.getMsgCount()) {
			// have different pages
			LOGGER.info(monthlyDTO.getId() + " is going forward");
			String primaryURL =  WebCrawlerProperties.getMailArchiveURL() + File.separator + monthlyDTO.getLink();

			int numberOfPages = (monthlyDTO.getMsgCount() / WebCrawlerProperties.getMailsPerPage() );

			int pageNumber = 0;
			while (pageNumber <= numberOfPages) {

				final String url = primaryURL + "?" + pageNumber++;

				// need to collect page numbers and links to the mails
				Document document = null;
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder builder = factory.newDocumentBuilder();
					document = builder.parse(URLConnectionReader.getInputStream(url));

					Element msgListTable = document.getElementById(WebCrawlerConstants.MSGLIST);

					if (msgListTable != null && msgListTable.hasChildNodes()) {
						NodeList tableChildsList = msgListTable.getChildNodes();

						for (int tblChildctr = 0; tblChildctr < tableChildsList.getLength(); tblChildctr++) {
							Node tableChildNode = tableChildsList.item(tblChildctr);
							if (tableChildNode.getNodeType() == Node.ELEMENT_NODE  && tableChildNode.hasChildNodes()
									&& tableChildNode.getNodeName().equalsIgnoreCase(WebCrawlerConstants.TBODY) ) {

								NodeList theadChildNodeList = tableChildNode.getChildNodes();

								for (int trctr = 0; trctr < theadChildNodeList.getLength(); trctr++) {

									Node trNode = theadChildNodeList.item(trctr);
									NodeList tdNodeList = null;
									if (trNode.getNodeType() != Node.ELEMENT_NODE || !trNode.hasChildNodes()) {
										continue;
									}

									MailArchiveDTO dto = new MailArchiveDTO();

									tdNodeList = trNode.getChildNodes();

									for (int tdctr = 0; tdctr < tdNodeList.getLength(); tdctr++) {
										Node tdNode = tdNodeList.item(tdctr);
										if (tdNode.getNodeType() != Node.ELEMENT_NODE) continue;

										NamedNodeMap tdAttributes = tdNode.getAttributes();
										Node tdAttribute = tdAttributes.getNamedItem(WebCrawlerConstants.CLASS);
										LOGGER.debug(monthlyDTO.getId() + " " + tdAttribute.getNodeValue() );
										if (tdAttribute.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.AUTHOR)) { // author
											dto.setAuthor(tdNode.getTextContent());
										} else if (tdAttribute.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.SUBJECT)
												&& tdNode.hasChildNodes()) {  // subject
											NodeList aNodesList = tdNode.getChildNodes();

											for (int aNodeCtr = 0; aNodeCtr < aNodesList.getLength(); aNodeCtr++) {
												Node aNode = aNodesList.item(aNodeCtr);

												if (aNode.getNodeType() != Node.ELEMENT_NODE || !aNode.getNodeName().equalsIgnoreCase(WebCrawlerConstants.A)) continue;

												NamedNodeMap aAttributes = aNode.getAttributes();
												Node hrefNode = aAttributes.getNamedItem(WebCrawlerConstants.HREF);

												if (hrefNode.getNodeType() != Node.ATTRIBUTE_NODE) continue;
												dto.setLink(hrefNode.getNodeValue());
												dto.setURL(WebCrawlerProperties.getMailArchiveURL()
														+ WebCrawlerConstants.SLASH + monthlyDTO.getId() + WebCrawlerConstants.MBOX
														+ WebCrawlerConstants.SLASH + WebCrawlerConstants.AJAX
														+ WebCrawlerConstants.SLASH + hrefNode.getNodeValue());
											}
										} else if (tdAttribute.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.DATE)) { // date
											dto.setDate(tdNode.getTextContent());
											dto.setFileName(tdNode.getTextContent() + WebCrawlerProperties.getFileExtension());
										}

									}
									dto.setDir(dirName);
									dto.setCheckForPreExistance(isDirExists);
									LOGGER.debug(monthlyDTO.getId() + " " + dto);
									// add dto to collection
									mailArchiveDTOs.add(dto);
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
		} else {
			LOGGER.info(monthlyDTO.getId() + " is skipping");
		}
		return mailArchiveDTOs;
	}

	private static boolean checkForDirectoryExistance(final String dirPath) {
		boolean isExists = true;

		File dir = new File(dirPath);
		if (!dir.exists()) {
			isExists = false;
			dir.mkdirs();
		}
		return isExists;
	}

	private static String getDirectoryName (final MailArchivesMonthlyDTO monthlyDTO) {
		return WebCrawlerProperties.getCurrentDirectory() + File.separator + WebCrawlerProperties.getQeueryURL()
				+ File.separator + monthlyDTO.getYear()
				+ File.separator + monthlyDTO.getId() + WebCrawlerConstants.MBOX;
	}
}
