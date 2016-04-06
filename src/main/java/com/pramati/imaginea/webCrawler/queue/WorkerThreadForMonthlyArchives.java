package com.pramati.imaginea.webCrawler.queue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

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

import com.pramati.imaginea.webCrawler.dto.MailArchiveDTO;
import com.pramati.imaginea.webCrawler.dto.MailArchivesMonthlyDTO;
import com.pramati.imaginea.webCrawler.utils.URLConnectionReader;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerConstants;
import com.pramati.imaginea.webCrawler.utils.WebCrawlerProperties;

public class WorkerThreadForMonthlyArchives implements Callable<MailArchivesMonthlyDTO> {
	
	private final static Logger LOGGER = Logger.getLogger(WorkerThreadForMonthlyArchives.class);

	private MailArchivesMonthlyDTO monthlyDTO;
	
	public WorkerThreadForMonthlyArchives (MailArchivesMonthlyDTO monthlyDTO) {
		Thread.currentThread().setName(monthlyDTO.getId());
		this.monthlyDTO = monthlyDTO;
	}
	
	/**
	 *  check's if mails of this month already done leaded.
	 *  if yes, do nothing
	 *  
	 *  if some mails are downloaded already, try to download others, which are not downloaded
	 *  
	 *  if completely mails are not downloaded, need to create the directory structure and then download all the mails
	 */
	public MailArchivesMonthlyDTO call() throws Exception {
		LOGGER.debug(monthlyDTO.getId() + " Proecessing for " + monthlyDTO.getMonth());
		boolean isDirExists = checkForDirectoryExistance(getDirectoryName()) ;
		LOGGER.debug(monthlyDTO.getId() + " isDirExists " +  getDirectoryName() + " / " +isDirExists);
		int noOfPriviousFiles = 0;
		File[] filesInDir = null;
		if (isDirExists) {
			File dir = new File(getDirectoryName());
			filesInDir = dir.listFiles();
			noOfPriviousFiles = filesInDir.length;
		}

		LOGGER.info(monthlyDTO.getId() + "  Mails in local / Mails in Server  :: " + noOfPriviousFiles + " / " + monthlyDTO.getMsgCount());
		if (noOfPriviousFiles < monthlyDTO.getMsgCount()) {
			// have different pages
			String primaryURL =  WebCrawlerProperties.getMailArchiveURL() + File.separator + monthlyDTO.getLink();
			
			int numberOfPages = (monthlyDTO.getMsgCount() / WebCrawlerProperties.getMailsPerPage() ) + 1;
			
			int pageNumber = 0;
			while (pageNumber <= numberOfPages) {
				
				final String url = primaryURL + "?" + pageNumber++; 
				URLConnectionReader urlReader = new URLConnectionReader(url);
				
				// need to collect page numbers and links to the mails
				Document document = null;
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder builder = factory.newDocumentBuilder();
					document = builder.parse(urlReader.getInputStream());
					
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
										} else if (tdAttribute.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.SUBJECT)) {  // subject
											
										} else if (tdAttribute.getNodeValue().equalsIgnoreCase(WebCrawlerConstants.DATE)) { // date
											dto.setDate(tdNode.getTextContent());
											dto.setFileName(tdNode.getTextContent() + WebCrawlerProperties.getFileExtension());
										}
										
									}
									dto.setDir(getDirectoryName());
									LOGGER.debug(monthlyDTO.getId() + " " + dto);
									// add dto to queue
									MailArchivesReaderQueueManager.getInstance().addQueueEntry(dto);
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
				
		
		return monthlyDTO;
	}
	
	public boolean checkForDirectoryExistance(final String dirPath) {
		boolean isExists = true;
		
		File dir = new File(dirPath);
		if (!dir.exists()) {
			isExists = false;
			dir.mkdirs();
		}
		return isExists;
	}

	private String getDirectoryName () {
		return WebCrawlerProperties.getCurrentDirectory() + File.separator + WebCrawlerProperties.getQeueryURL() 
														  + File.separator + monthlyDTO.getYear()
														  + File.separator + monthlyDTO.getId() + WebCrawlerConstants.MBOX;
	}
}
