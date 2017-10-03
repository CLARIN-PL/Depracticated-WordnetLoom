///*
//    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
//                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
//                       Radosław Ramocki, Michał Stanek
//    Part of the WordnetLoom
//
//    This program is free software; you can redistribute it and/or modify it
//under the terms of the GNU General Public License as published by the Free
//Software Foundation; either version 3 of the License, or (at your option)
//any later version.
//
//    This program is distributed in the hope that it will be useful, but
//WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//or FITNESS FOR A PARTICULAR PURPOSE. 
//
//    See the LICENSE and COPYING files for more details.
//*/
//
//package pl.wroc.pwr.ci.plwordnet.plugins.notepad.data;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Vector;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.TransformerFactoryConfigurationError;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.apache.xerces.parsers.SAXParser;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
///**
// * Storage for notepad's entries.
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.notepad.data;
final public class Storage {
	
}
//final public class Storage {
//	private static final String	ENTRIES	= "entries";
//	private static final String	CONFIG_NOTEPAD_XML	= "config/notepad.xml";
//
//	/**
//	 * Load data from storage
//	 * @return collection of entries
//	 */
//	static public Collection<Entry> loadEntires() {
//		NotepadXMLHandler handler = new NotepadXMLHandler();
//		if (new File(CONFIG_NOTEPAD_XML).exists()){
//			SAXParser parser = new SAXParser();
//			parser.setContentHandler(handler);
//			parser.setErrorHandler(handler);
//			try {
//				parser.parse(CONFIG_NOTEPAD_XML);
//			} catch (SAXException e) {
//				// This is a bug in XML file probably
//				Main.logException(e);
//			} catch (IOException e) {
//				// No such file is acceptable exception
//				Main.logException(e);
//			}
//		}
//		return handler.getEntries();
//	}
//
//	/**
//	 * Save entries into storage
//	 * @param entries - entries to save
//	 */
//	static public void saveEntries(Collection<Entry> entries) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		try {
//			// Create document
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.newDocument();
//			Element root = document.createElement(ENTRIES);
//			document.appendChild(root);
//			for (Entry entry : entries) {
//				Element element = document.createElement(NotepadXMLHandler.ENTRY);
//				element.setAttribute(NotepadXMLHandler.CLASS, entry.getEntryClass());
//				element.setAttribute(NotepadXMLHandler.COMMENTS, entry.getEntryComments());
//				
//				// Add ids
//				for (Long id : entry.getEntryIds()) {
//					Element idElem = document.createElement(NotepadXMLHandler.ID);
//					idElem.setAttribute(NotepadXMLHandler.VALUE, id.toString());
//					element.appendChild(idElem);
//				}
//				root.appendChild(element);
//				
//			}
//			document.getDocumentElement().normalize();
//			
//			// Save into XML
//			Transformer transformer  = TransformerFactory.newInstance().newTransformer();
//            DOMSource source = new DOMSource(document);
//            transformer.transform(source, new StreamResult(new File(CONFIG_NOTEPAD_XML)));			
//		} catch (ParserConfigurationException e) {
//			Main.logException(e);
//		} catch (TransformerConfigurationException e) {
//			Main.logException(e);
//		} catch (TransformerFactoryConfigurationError e) {
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			Main.logException(e);
//		}
//	}
//}
//
//final class NotepadXMLHandler extends DefaultHandler {
//
//	static final String	VALUE	= "value";
//	static final String	COMMENTS	= "comments";
//	static final String	ENTRY	= "entry";
//	static final String	ID	= "id";
//	static final String	CLASS	= "class";
//
//	private String			entryClass;
//	private Collection<Long> entryIds;
//	private String	entryComments;
//
//	private Collection<Entry> entries = new Vector<Entry>();
//
//	/*
//	 * (non-Javadoc)
//	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
//	 */
//	@Override
//	public void startElement(String uri, String localName, String rawName,
//			Attributes attributes) {
//		if (rawName.equals(ENTRY)) {
//			entryClass = attributes.getValue(CLASS);
//			entryIds = new Vector<Long>();
//			entryComments = attributes.getValue(COMMENTS);
//		} else if (rawName.equals(ID)) {
//			entryIds.add(new Long(attributes.getValue(VALUE)));
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void endElement(String namespaceURL, String localName, String rawName) {
//		if (rawName.equals(ENTRY)) {
//			Long[] ids = new Long[entryIds.size()];
//			Entry item = new Entry(entryClass, entryIds.toArray(ids), entryComments.toString());
//			entries.add(item);
//		}
//	}
//
//	/**
//	 * Get entries from XML file
//	 * @return entries
//	 */
//	public Collection<Entry> getEntries() {
//		return entries;
//	}
//}