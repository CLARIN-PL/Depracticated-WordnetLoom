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
package pl.edu.pwr.wordnetloom.plugins.relations;
//
//import java.util.Collection;
//
//import Sense;
//import Synset;
//import SynsetRelation;
//import pl.wroc.pwr.ci.plwordnet.plugins.lexeditor.LexicalService;
//import SynsetPropertiesView;
//import SynsetStructureView;
//import SynsetStructureViewUI;
//import RelationsDA;
//import RelationsTableView;
//import RelationsTableViewUI;
//import RelationsTreeView;
//import SynsetsView;
//import ToolbarView;
//import ToolbarViewUI;
//import SimpleListenerInterface;
//import ActionWrapper;
//import SimpleListenerWrapper;
//import RemoteUtils;
//import AbstractService;
//import GlobalEventListener;
//import Workbench;
//
///**
// * serwis zajmujący się dostarczaniem interfejsu użytkownika
// * do edytcji relacji pomiedzy synsetami
// * @author Max
// */
public class RelationsService{
}
//
//	private static String TITLE_TOOLBAR = "Pasek narzędzi";
//	private static String TITLE_RELATION_TRE = "Drzewo relacji";
//	private static String TITLE_RELATION_TABLE = "Tabela relacji";
//	private static String TITLE_PROPERTIES = "Właściwości";
//	private static String MAIN_SYNSETS_LABEL = "Wszystkie";
//	private static String DESC_SYNSETS_LABEL = "Wybrane";
//	private static String MAIN_SYNSET_LABEL = "Źródłowy";
//	private static String DESC_SYNSET_LABEL = "Docelowy";
//
//	// nazwa perspektywy
//	private String perspectiveName = null;
//
//	// widoki
//	private SynsetStructureView unitsInMainSynset;
//	private SynsetStructureView unitsInDescSynset;
//	private SynsetsView synsetsMain;
//	private SynsetsView synsetsDesc;
//	private RelationsTableView relationsTable;
//	private RelationsTreeView relationsTree;
//	private ToolbarView toolbar;
//	private SynsetPropertiesView mainSynsetProperties;
//	private SynsetPropertiesView secondSynsetProperties;
//	private LexicalService lservice=null;
//	
//	/**
//	 * konstruktor serwisu
//	 * @param workbench - srodowisko dla usługi
//	 * @param perspectiveName - nazwa perskeptywy do użycia
//	 */
//	public RelationsService(Workbench workbench,String perspectiveName) {
//		super(workbench);
//		this.perspectiveName=perspectiveName;
//	}
//
//	/**
//	 * instalacja akcji nowa jednostka w menu edycja
//	 */
//	public void installMenuItems() {/***/}
//
//	/**
//	 * instalacja widoków
//	 */
//	public void installViews() {
//		this.synsetsMain=new SynsetsView(this.workbench,MAIN_SYNSETS_LABEL,1);
//		this.workbench.installView(this.synsetsMain, 0,perspectiveName);
//
//		this.unitsInMainSynset=new SynsetStructureView(this.workbench,MAIN_SYNSET_LABEL,false,true,true,1);
//		this.workbench.installView(this.unitsInMainSynset, 1,perspectiveName);
//
//		this.mainSynsetProperties=new SynsetPropertiesView(this.workbench,TITLE_PROPERTIES,1);
//		this.workbench.installView(this.mainSynsetProperties, 1,perspectiveName);
//
//		this.synsetsDesc=new SynsetsView(this.workbench,DESC_SYNSETS_LABEL,2);
//		this.workbench.installView(this.synsetsDesc, 2,perspectiveName);
//
//		this.unitsInDescSynset=new SynsetStructureView(this.workbench,DESC_SYNSET_LABEL,false,true,true,2);
//		this.workbench.installView(this.unitsInDescSynset, 3,perspectiveName);
//
//		this.secondSynsetProperties=new SynsetPropertiesView(this.workbench,TITLE_PROPERTIES,2);
//		this.workbench.installView(this.secondSynsetProperties, 3,perspectiveName);
//
//		this.relationsTable=new RelationsTableView(this.workbench,TITLE_RELATION_TABLE, false);
//		this.workbench.installView(this.relationsTable, 4,perspectiveName);
//
//		if (this.workbench.getParam(TITLE_RELATION_TRE)==null || this.workbench.getParam(TITLE_RELATION_TRE).equals("1")){
//			this.relationsTree=new RelationsTreeView(this.workbench,TITLE_RELATION_TRE);
//			this.workbench.installView(this.relationsTree, 4,perspectiveName);
//		}
//
//		this.toolbar=new ToolbarView(this.workbench,TITLE_TOOLBAR);
//		this.workbench.installView(this.toolbar, 5,perspectiveName);
//
//		// zdarzenia
//		this.synsetsMain.addSelectionChangeListener(unitsInMainSynset);
//		this.synsetsMain.addSelectionChangeListener(mainSynsetProperties);
//		this.synsetsMain.addSelectionChangeListener(relationsTable);
//		if ( relationsTree != null )
//			this.synsetsMain.addSelectionChangeListener(relationsTree);
//		this.synsetsMain.addSelectionChangeListener(new SimpleListenerWrapper(this,"event_refreshToolbar"));		
//
//		this.unitsInMainSynset.addSelectionListener(new SimpleListenerWrapper(this,"event_refreshToolbar"));
//
//		this.synsetsDesc.addSelectionChangeListener(unitsInDescSynset);
//		this.synsetsDesc.addSelectionChangeListener(secondSynsetProperties);
//		this.synsetsDesc.addSelectionChangeListener(new SimpleListenerWrapper(this,"event_refreshToolbar"));
//		this.synsetsDesc.addSelectionChangeListener(new SimpleListenerWrapper(this,"descSynset_selectionChanged"));
//
//		this.unitsInDescSynset.addSelectionListener(new SimpleListenerWrapper(this,"event_refreshToolbar"));
//
//		this.relationsTable.addActionListener(new SimpleListenerWrapper(this,"table_onClicked"));
//
//		this.toolbar.addRefreshRelationListener(new SimpleListenerWrapper(this,"toolbar_refreshRelation"));
//		this.toolbar.addShowSynsetListener(new SimpleListenerWrapper(this,"toolbar_showSynset"));
//		this.toolbar.addRefreshUnitsInSynsetListener(new SimpleListenerWrapper(this,"toolbar_refreshUnits"));
//		this.toolbar.addSynsetChangedListener(new SimpleListenerWrapper(this,"toolbar_synsetChanged"));
//
//		// przelacanie perspektyw
//		this.unitsInMainSynset.addClickListener(new SimpleListenerWrapper(this,"event_buttonInMainOnClick"));
//		this.unitsInMainSynset.addUnitChangeListener(new SimpleListenerWrapper(GlobalEventListener.getInstance(), "notifyUnitSelectionListener"));
//		this.unitsInDescSynset.addClickListener(new SimpleListenerWrapper(this,"event_buttonInDescOnClick"));
//		this.unitsInDescSynset.addUnitChangeListener(new SimpleListenerWrapper(GlobalEventListener.getInstance(), "notifyUnitSelectionListener"));
//
//		// szybkie reagowanie na zmiany we wlasciwosciach synsetu
//		this.mainSynsetProperties.addChangeListener(new ActionWrapper(synsetsMain,"redrawList"));
//		this.secondSynsetProperties.addChangeListener(new ActionWrapper(synsetsDesc,"redrawList"));
//		
//		mainSynsetProperties.addChangeListener(unitsInMainSynset);
//		unitsInMainSynset.addSynsetUpdateListener(mainSynsetProperties);
//		
//		secondSynsetProperties.addChangeListener(unitsInDescSynset);
//		unitsInDescSynset.addSynsetUpdateListener(secondSynsetProperties);
//	}
//
//
//	/**
//	 * wcisnieto przycisk switch
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void table_onClicked(Object object, Integer tag) {
//		switch (tag.intValue()) {
//			case RelationsTableViewUI.RELATION_WAS_DELETED:
//				refreshSynsetsView(1);
//				break;
//			case RelationsTableViewUI.RELATION_WAS_CHANGED:
//				if ( relationsTree != null )
//					this.relationsTree.doAction(object, 0);
//				break;
//			case RelationsTableViewUI.SHOW_SYNSET_IN_FIRST_VIEW:
//				showSynset(object,1);
//				break;
//			case RelationsTableViewUI.SHOW_SYNSET_IN_SECOND_VIEW:
//				showSynset(object,2);
//				break;
//		}
//	}
//
//	/**
//	 * wymuszono odswiezenie relacji
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void toolbar_refreshRelation(Object object, Integer tag) {
//		switch (tag.intValue()) {
//			case ToolbarViewUI.REFRESH_RELATION:
//				relationsTable.doAction(object,0);
//				if ( relationsTree != null )
//					relationsTree.doAction(object,0);
//				break;
//			case ToolbarViewUI.REFRESH_MARKS:
//				relationsTable.refreshFocusObject((Synset)object);
//				if ( relationsTree != null )
//					relationsTree.refreshFocusObject((Synset)object);
//				break;
//		}
//	}
//
//	/**
//	 * nalezy odswiezy toolbar
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void event_refreshToolbar(Object object, Integer tag) {
//		// zaznaczony głowny synset
//		Synset mainSynset=unitsInMainSynset.getLastSynset();
//		// zaznaczone jednostki z synsetu
//		Collection <Sense> selectedUnits=unitsInMainSynset.getSelectedUnits();
//		// zaznaczony docelowy synset
//		Synset descSynset=unitsInDescSynset.getLastSynset();
//
//		// odswiżenei przyciskow
//		toolbar.refreshButtons(mainSynset,selectedUnits,descSynset);
//	}
//
//
//	/**
//	 * pokazanie konkretnego synsetu
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void toolbar_showSynset(Object object, Integer tag) {
//		showSynset(object,tag.intValue());
//	}
//	
//	/**
//	 * synset został usunięty, trzeba usunąć go z list
//	 * @param object - zmieniony synset
//	 * @param tag - parametr określający jaka zmiana nastąpiła
//	 */
//	public void toolbar_synsetChanged(Object object, Integer tag) {
//		refreshSynsetsView(1);
//		refreshSynsetsView(2);
//	}
//
//	/**
//	 * odswiezenie jednostek w synsetach
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void toolbar_refreshUnits(Object object, Integer tag) {
//		switch (tag.intValue()) {
//			case 1: unitsInMainSynset.refreshData(); break;
//			case 2: unitsInDescSynset.refreshData(); break;
//		}
//	}
//
//	/**
//	 * zmienilo sie zanzaczeniw docelowym synsecie
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void descSynset_selectionChanged(Object object, Integer tag) {
//		relationsTable.refreshFocusObject((Synset)object);
//		if ( relationsTree != null )
//			relationsTree.refreshFocusObject((Synset)object);
//	}
//
//	/**
//	 * wywolanie akcji przelaczenia perspektywy lub usuniecia jednostki
//	 * z synsetu
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void event_buttonInMainOnClick(Object object, Integer tag) {
//		if (object!=null && tag.intValue()==SynsetStructureViewUI.UNIT_REMOVED) {
//			Synset synset=(Synset)object;
//			if (synset.getUnits()==null || synset.getUnits().size()==0) {
//				refreshSynsetsView(1);
//			}
//		} else {
//			event_changePerspective(object,tag);
//		}
//	}
//
//	/**
//	 * wywolanie akcji przelaczenia perspektywy lub usuniecia jednostki
//	 * z synsetu
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	public void event_buttonInDescOnClick(Object object, Integer tag) {
//		if (object!=null && tag.intValue()==SynsetStructureViewUI.UNIT_REMOVED) {
//			Synset synset=(Synset)object;
//			if (synset.getUnits()==null || synset.getUnits().size()==0) {
//				refreshSynsetsView(2);
//			}
//		} else {
//			event_changePerspective(object,tag);
//		}
//	}
//
//	/**
//	 * wywolanie akcji przelaczenia perspektywy
//	 * z synsetu
//	 * @param object - obiekt
//	 * @param tag - parametr
//	 */
//	private void event_changePerspective(Object object, Integer tag) {
//		if (object!=null) {
//			switch (tag.intValue()) {
//			// przejscie do relacji
//			case SynsetStructureViewUI.SYNSET_RELATIONS:
//					Object[] array=(Object[])object;
//					selectAndShow(array[0],array[1]);
//					break;
//			// przejscie do jednostek
//			case SynsetStructureViewUI.LEXICAL_PERSPECTIVE:
//				if (lservice==null)
//					lservice=(LexicalService)workbench.getService(LexicalService.class.getName());
//				if (lservice!=null) {
//					lservice.selectAndShow(object);
//				}
//				break;
//			}
//		}
//	}
//
//	public boolean onClose() { return true; }
//
//	/**
//	 * odświeżenei list sysnetów
//	 * @param viewNumber - numer listy do odświeżenia
//	 */
//	public void refreshSynsetsView(int viewNumber) {
//		switch (viewNumber) {
//			case 1: synsetsMain.refreshData(); break;
//			case 2: synsetsDesc.refreshData(); break;
//		}
//	}
//
//
//	/**
//	 * ustawienie wyszukiwan i wyswietlenie perspektywy
//	 * @param unitsObjects - kolekcja jednostek
//	 * @param synsetObject - synset
//	 */
//	@SuppressWarnings("unchecked")
//	public void selectAndShow(Object unitsObjects, Object synsetObject) {
//		Collection<Sense> units=(Collection<Sense>)unitsObjects;
//		Synset synset=(Synset)synsetObject;
//
//		String filter="";
//		if (units!=null && units.size()>0) // ustawienie na szukanie jednostek
//			for (Sense unit : units) {
//				filter=""+unit.getLemma();
//				break;
//			}
//		synsetsMain.setLastSynset(synset,filter);
//		synsetsMain.setStatus(0);
//		synsetsMain.setDomain(null);
//		unitsInMainSynset.setLastUnits(units);
//
//		if (!workbench.getActivePerspective().getName().equals(RelationsPlugin.RELATIONS_PERSPECTIVE_NAME))
//			workbench.choosePerspective(RelationsPlugin.RELATIONS_PERSPECTIVE_NAME);
//		else {
//			refreshSynsetsView(1);
//			refreshSynsetsView(2);
//		}
//	}
//
//	/**
//	 * ustawienie jako glowny okreslonego synsetu
//	 * @param synsetObject - synset
//	 * @param viewNumber - numer widoku
//	 */
//	public void showSynset(Object synsetObject,int viewNumber) {
//		Synset synset=(Synset)synsetObject;
//		String filter="";
//		// ustawienie filtra, aby nie wyszukiwać wszystkich
//		Sense unit=RelationsDA.getFirstUnit(synset);
//		if (unit!=null) {
//			filter=unit.getLemma().getWord();
//		}
//		// ustawienie synsetow
//		switch (viewNumber) {
//			case 1:synsetsMain.setLastSynset(synset,filter);break;
//			case 2:synsetsDesc.setLastSynset(synset,filter);break;
//		}
//		refreshSynsetsView(viewNumber);
//	}
//
//	public void onStart() {/***/}
//	
//	/**
//	 * Set new state in the service
//	 * @param state
//	 * @return true if succeeded
//	 */
//	public boolean setState(Object state) {
//		if (state instanceof Synset) {
//			Synset synset = (Synset)state;
//			String filter = null;
//			Collection<Sense> units = RemoteUtils.synsetRemote.dbFastGetUnits(synset);
//			for (Sense unit : units) {
//				filter = unit.getLemma().getWord();
//				break;
//			}
//			synsetsMain.setLastSynset(synset, filter);
//			synsetsMain.setDomain(RemoteUtils.synsetRemote.dbGetDomain(synset));
//			synsetsMain.setStatus(0);
//			
//			synsetsMain.refreshData();
//			return true;
//		} else if (state instanceof SynsetRelation) {
//			SynsetRelation r = (SynsetRelation)state;
////			r = RemoteUtils.synsetRelationRemote.dbGetDetails(r);
//			
//			String filter = null;
//			Collection<Sense> units = RemoteUtils.synsetRemote.dbFastGetUnits(r.getSynsetFrom());
//			for (Sense unit : units) {
//				filter = unit.getLemma().getWord();
//				break;
//			}
//			synsetsMain.setLastSynset(r.getSynsetFrom(), filter);
//			synsetsMain.setDomain(RemoteUtils.synsetRemote.dbGetDomain(r.getSynsetFrom()));
//			synsetsMain.setStatus(0);
//			
//			synsetsMain.refreshData();
//			return true;
//		}
//		return false;
//	}
//	
//	public void addSynsetChangedListener(SimpleListenerInterface newListener) {
//		toolbar.addSynsetChangedListener(newListener);
//	}
//}
//
