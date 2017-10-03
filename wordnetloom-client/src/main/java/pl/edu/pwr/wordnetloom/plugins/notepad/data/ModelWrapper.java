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
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Vector;
//
//import Sense;
//import SenseRelation;
//import Synset;
//import SynsetRelation;
//import GenericListModel;
//import AbstractProgressThread;
//import RemoteUtils;
//
///**
// * A wrapper for generic model used by notepad
// * @author Max
// * 
// */
package pl.edu.pwr.wordnetloom.plugins.notepad.data;
public class ModelWrapper{
	
}
//public class ModelWrapper {
//	private static final int	CACHE_ENABLE_LEVEL	= 20;
//	private static final String				DATA_READING		= "Odczyt danych";
//	private static final String				DATA_FOR_NOTEPAD	= "Dane dla notesu";
//	final GenericListModel<EntryWrapper>	model;
//	final Map<String, String>				prefixes;
//	final IEntryWrapperPostCreation			postCreation;
//	private static final String				SYNSET				= "Synset";
//	private static final String				LEXICAL_UNIT		= "LexicalUnit";
//	private static final String				LEXICAL_REALTION	= "LexicalRelation";
//	private static final String				SYNSET_RELATION		= "SynsetRelation";
//
//	/**
//	 * Create new model wrapper for notepad
//	 */
//	public ModelWrapper() {
//		this.model = new GenericListModel<EntryWrapper>();
//
//		// Build prefixes map
//		prefixes = new HashMap<String, String>();
//		prefixes.put(LEXICAL_UNIT, "<font color=\"red\">JN</font>");
//		prefixes.put(LEXICAL_REALTION, "<font color=\"#AA0000\">RJN</font>");
//		prefixes.put(SYNSET, "<font color=\"green\">S</font>");
//		prefixes.put(SYNSET_RELATION, "<font color=\"#0AA000\">RS</font>");
//
//		postCreation = new IEntryWrapperPostCreation() {
//
//			/*
//			 * (non-Javadoc)
//			 * @see IEntryWrapperPostCreation#postCreate(java.lang.Object)
//			 */
//			public void postCreate(Object object) {
//				if (object instanceof SenseRelation) {
////					SenseRelation rel = (SenseRelation) object;
////					rel = RemoteUtils.lexicalRelationRemote.dbGetDetails(rel);
////					rel.setRelationType(RemoteUtils.relationTypeRemote.dbGetFullName(rel.getRelationType()));
//				} else if (object instanceof SynsetRelation) {
////					SynsetRelation rel = (SynsetRelation) object;
////					rel = RemoteUtils.synsetRelationRemote.dbGetDetails(rel);
////					rel.setRelationType(RemoteUtils.relationTypeRemote.dbGetFullName(rel.getRelation()));
//				}
//			}
//		};
//	}
//
//	/**
//	 * Add list of object to the model
//	 * @param list
//	 */
//	private void add(Collection<EntryWrapper> list) {
//		synchronized (model) {
//			Collection<EntryWrapper> news = new Vector<EntryWrapper>();
//			news.addAll(model.getCollection());
//			for (EntryWrapper ew : list) {
//				if (!news.contains(ew)) {
//					news.add(ew);
//				}
//			}
//			model.setCollection(news, null);
//		}
//		save();
//	}
//
//	/**
//	 * @return entries in the model
//	 */
//	private Collection<Entry> getEntries() {
//		Collection<Entry> list = new Vector<Entry>();
//		synchronized (model) {
//			Collection<EntryWrapper> modelList = model.getCollection();
//			for (EntryWrapper entryWrapper : modelList) {
//				list.add(entryWrapper.getEntry());
//			}
//		}
//		return list;
//	}
//
//	/**
//	 * @param relations
//	 * @param comments
//	 */
//	public void addLexicalRelations(Collection<SenseRelation> relations,
//			String comments) {
//		Collection<EntryWrapper> list = new Vector<EntryWrapper>();
//		for (SenseRelation rel : relations) {
//			Long[] ids = { rel.getSenseFrom().getId(), rel.getSenseTo().getId(), rel.getRelation().getId() };
//			Entry entry = new Entry(LEXICAL_REALTION, ids, comments);
//			list.add(new EntryWrapper(entry, prefixes, postCreation, rel));
//		}
//		add(list);
//	}
//
//	/**
//	 * @param relations
//	 * @param comments
//	 */
//	public void addSynsetRelations(Collection<SynsetRelation> relations,
//			String comments) {
//		Collection<EntryWrapper> list = new Vector<EntryWrapper>();
//		for (SynsetRelation rel : relations) {
//			Long[] ids = { rel.getSynsetFrom().getId(), rel.getSynsetTo().getId(), rel.getRelation().getId() };
//			Entry entry = new Entry(SYNSET_RELATION, ids, comments);
//			list.add(new EntryWrapper(entry, prefixes, postCreation, rel));
//		}
//		add(list);
//	}
//
//	/**
//	 * @param units
//	 * @param comments
//	 */
//	public void addUnits(Collection<Sense> units, String comments) {
//		Collection<EntryWrapper> list = new Vector<EntryWrapper>();
//		for (Sense unit : units) {
//			Long[] ids = { unit.getId() };
//			Entry entry = new Entry(LEXICAL_UNIT, ids, comments);
//			list.add(new EntryWrapper(entry, prefixes, postCreation, unit));
//		}
//		add(list);
//	}
//
//	/**
//	 * @param synsets
//	 * @param comments
//	 */
//	public void addSynsets(Collection<Synset> synsets, String comments) {
//		Collection<EntryWrapper> list = new Vector<EntryWrapper>();
//		for (Synset synset : synsets) {
//			Long[] ids = { synset.getId() };
//			Entry entry = new Entry(SYNSET, ids, comments);
//			list.add(new EntryWrapper(entry, prefixes, postCreation, synset));
//		}
//		add(list);
//	}
//
//	/**
//	 * Load data form the storage
//	 */
//	public void loadData() {
//
//		// Load data
//		final Collection<Entry> entries = Storage.loadEntires();
//		final Collection<EntryWrapper> wrappers = new Vector<EntryWrapper>();
//
//		// Start main thread
//		new AbstractProgressThread(null, DATA_FOR_NOTEPAD, null) {
//			@Override
//			protected void mainProcess() {
//				progress.setGlobalProgressParams(1, 2);
//				progress.setProgressParams(1, entries.size(), DATA_READING);
//				int index = 0;
//				
//				Map<Class<?>, Map<String,?>> cache = new HashMap<Class<?>, Map<String,?>>();
//				if (entries.size() > CACHE_ENABLE_LEVEL) {
//					
//					Collection<Sense> units = RemoteUtils.lexicalUnitRemote.dbFastGetUnits("");
//					Map<String,Sense> unitsCache = new HashMap<String, Sense>();
//					for (Sense i : units) {
//						unitsCache.put(i.getId().toString(), i);
//					}
//					cache.put(Sense.class, unitsCache);
//					
//					Collection<Synset> synsets = RemoteUtils.synsetRemote.dbFastGetSynsets("");
//					Map<String,Synset> synsetsCache = new HashMap<String, Synset>();
//					for (Synset i : synsets) {
//						synsetsCache.put(i.getId().toString(), i);
//					}
//					cache.put(Synset.class, synsetsCache);
//				
//					Collection<SenseRelation> lexRels = RemoteUtils.lexicalRelationRemote.dbFastGetRelations(null);
//					Map<String,SenseRelation> lexRelsCache = new HashMap<String, SenseRelation>();
//					for (SenseRelation i : lexRels) {
//						StringBuilder key = new StringBuilder();
//						key.append(i.getSenseFrom());
//						key.append("|");
//						key.append(i.getSenseTo());
//						key.append("|");
//						key.append(i.getRelation());
//						lexRelsCache.put(key.toString(), i);
//					}
//					cache.put(SenseRelation.class, lexRelsCache);
//					
//					Collection<SynsetRelation> synRels = RemoteUtils.synsetRelationRemote.dbFastGetRelations(null);
//					Map<String,SynsetRelation> synRelsCache = new HashMap<String, SynsetRelation>();
//					for (SynsetRelation i : synRels) {
//						StringBuilder key = new StringBuilder();
//						key.append(i.getSynsetFrom());
//						key.append("|");
//						key.append(i.getSynsetTo());
//						key.append("|");
//						key.append(i.getRelation());
//						synRelsCache.put(key.toString(), i);
//					}
//					cache.put(SynsetRelation.class, synRelsCache);
//				}
//				
//				for (Entry entry : entries) {
//					if (index % 10 == 0) {
//						progress.setProgressValue(index);
//					}
//					index++;
//					EntryWrapper ew = new EntryWrapper(entry, prefixes, postCreation, cache);
//					if (ew.getObject() != null) {
//						wrappers.add(ew);
//					}
//				}
//			}
//		};
//
//		model.setCollection(wrappers);
//	}
//
//	/**
//	 * Remove items from the model
//	 * @param indices
//	 */
//	public void remove(int[] indices) {
//		synchronized (model) {
//			// Convert indices to objects
//			ArrayList<EntryWrapper> news = new ArrayList<EntryWrapper>();
//			news.addAll(model.getCollection());
//			ArrayList<EntryWrapper> toRemove = new ArrayList<EntryWrapper>();
//			for (int i : indices) {
//				toRemove.add(news.get(i));
//			}
//
//			// Remove objects
//			news.removeAll(toRemove);
//			model.setCollection(news, null);
//		}
//		save();
//	}
//
//	/**
//	 * @return model
//	 */
//	public GenericListModel<EntryWrapper> getModel() {
//		return model;
//	}
//
//	/**
//	 * Save changes into storage
//	 */
//	public void save() {
//		Storage.saveEntries(getEntries());
//	}
//}
