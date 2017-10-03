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
//import java.util.Map;
//
//import IObjectWrapper;
//import RemoteUtils;
//
///**
// * Wrapper for entry that store object (LexicalUnit, Synset) associated to the entry
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.notepad.data;
public class EntryWrapper{
	
}
//public class EntryWrapper implements IObjectWrapper {
//
//	private static final String	PREFIX_SUFFIX	= ": ";
//	private static final String	CLASS_SUFFIX	= "DTO";
//	private static final String	CLASS_PREFIX	= "pl.wroc.pwr.ci.plwordnet.database.dto.";
//	private final Entry			entry;
//	private String				prefix			= "";
//	private Object object;
//
//	/**
//	 * Create new entry wrapper that store object associated to the entry
//	 * @param entry - entry to be wrapped
//	 * @param prefixes - class name to prefix conversion map
//	 * @param callback - callback for post creation operation
//	 * @param cache - cached object to improve speed of object creation
//	 */
//	public EntryWrapper(Entry entry, Map<String, String> prefixes,
//			IEntryWrapperPostCreation callback, Map<Class<?>, Map<String,?>> cache) {
//		this.entry = entry;
//		try {
//			// Prepare variables
//			Object temp = null;
//			Class<?> cl = Class.forName(CLASS_PREFIX + entry.getEntryClass() + CLASS_SUFFIX);
//			Long[] ids = entry.getEntryIds();
//			
//			// Find object in cache
//			if (cache != null) {
//				Map<String,?> items = cache.get(cl);
//				if (items != null) {
//					StringBuilder key = new StringBuilder();
//					for (int i = 0; i < ids.length; i++) {
//						if (i != 0) {
//							key.append("|");
//						}
//						key.append(ids[i]);
//					}
//					temp = items.get(key.toString());
//				}
//			}
//			
//			// Get object from database if it doesn't exist in cache
//			if (temp == null) {
//				temp = RemoteUtils.commonRemote.dbGet(cl, entry.getEntryIds());
//			}
//			
//			// If there is an object, save it, if not, then it probably doesn't exist anymore
//			if (temp != null) {
////				if (temp instanceof DBComparable) {
//					Object object = temp;
//
//					// Perform post creation operation
//					callback.postCreate(object);
//					String p = prefixes.get(entry.getEntryClass());
//					if (p != null) {
//						prefix = p + PREFIX_SUFFIX;
//					}
////				} else {
////					throw new RuntimeException("Unsupported class " + temp.getClass().toString());
////				}
//			}
//		} catch (ClassNotFoundException e) {
//			Main.logException(e);
//		}
//	}
//
//	/**
//	 * Create new entry wrapper that store object associated to the entry
//	 * @param entry - entry to be wrapped
//	 * @param prefixes - class name to prefix conversion map
//	 * @param callback - callback for post creation operation
//	 * @param cache - object associated to the entry
//	 */
//	public EntryWrapper(Entry entry, Map<String, String> prefixes,
//			IEntryWrapperPostCreation callback, Object cache) {
//		this.entry = entry;
//		this.object = cache;
//		callback.postCreate(object);
//		String p = prefixes.get(entry.getEntryClass());
//		if (p != null) {
//			prefix = p + PREFIX_SUFFIX;
//		}
//	}
//
//	/**
//	 * @return entry
//	 */
//	public Entry getEntry() {
//		return entry;
//	}
//
//	public boolean isEqual(Object second) {
//		if (object != null) {
//			return object.equals(second);
//		}
//		return false;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see pl.wroc.pwr.ci.plwordnet.systems.misc.ObjectWrapper#getObject()
//	 */
//	public Object getObject() {
//		return object;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		if (object != null) {
//			return "<html>" + prefix + object.toString() + "</html>";
//		}
//		return "-";
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object second) {
//		if (second instanceof EntryWrapper) {
//			if (object != null) {
//				EntryWrapper wr = (EntryWrapper) second;
//				return object.equals(wr.getObject());
//			}
//		}
//		return super.equals(second);
//	}
//}
