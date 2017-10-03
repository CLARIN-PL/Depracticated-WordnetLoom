/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
*/

package pl.edu.pwr.wordnetloom.plugins.lexeditor.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.systems.ui.TextAreaPlain;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.systems.ui.ButtonExt;

/**
 * okienko do wprowadzania parametrow dla nowej relacji
 * @author Max
 */
public class RelationTypeFrame extends IconDialog implements ActionListener,KeyListener {

	protected static final long serialVersionUID = 1L;

	// elementu interfejsu uzytkownika
	protected ComboBoxPlain relationType;
	protected ComboBoxPlain  relationSubType;
	protected ButtonExt buttonChoose,buttonCancel;
	protected TextAreaPlain description;
	protected JList testsLit;
	protected static ComboBoxPlain  parentItem;
	protected ComboBoxPlain  middleItem;
	protected static ComboBoxPlain  childItem;
	protected RelationType fixedRelationType;

	//private RelationArgument type=null;
	protected RelationType chosenType=null;
	protected ArrayList<RelationType> mainRelations=null;
	protected Collection<RelationType> subRelations=null;
	protected static PartOfSpeech pos;

	/**
	 * konstruktor
	 * @param frame - srodowisko
	 * @param type - typ relacji
	 * @param pos - czesc mowy
	 * @param fixedRelationType - typ relacji ustawiony na sztywno
	 * @param parentUnits - jednostki podzedne
	 * @param middleUnits - jednostki pośrednie
	 * @param childUnits - jednostki nadrzedne
	 */
	private RelationTypeFrame(JFrame frame,
							  RelationArgument type,
							  PartOfSpeech pos,
							  RelationType fixedRelationType,
							  RelationType suggestedRelationType,
							  Sense suggestedUnit,
							  Collection<Sense> parentUnits,
							  Collection<Sense> middleUnits,
							  Collection<Sense> childUnits) {
		super(frame, Labels.RELATION_PARAMS,650,500);
		RelationTypeFrame.pos = pos;
		this.fixedRelationType = fixedRelationType;

		
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//this.setAlwaysOnTop(true);

        // element nadrzedny
        parentItem=new ComboBoxPlain();
        for (Sense parent : parentUnits) {
        	parentItem.addItem(parent.getLemma().getWord());
		}

        // element podrzedny
        childItem=new ComboBoxPlain();
        for (Sense child : childUnits) {
        	childItem.addItem(child.getLemma().getWord());
		}
        //Wybranie zaproponowanej jednostki
        if(suggestedUnit!=null){
        	
        	childItem.setSelectedItem(suggestedUnit.getLemma().getWord());
        	parentItem.setSelectedItem(suggestedUnit.getLemma().getWord());
        }

        // element posredni
        middleItem=new ComboBoxPlain();
        if (middleUnits != null && middleUnits.size() != 0) {
        	for (Sense middle : middleUnits) {
        		middleItem.addItem(middle.getLemma());
        	}
		} else {
			middleItem.setEnabled(false);
		}

        // opis relacji
        description=new TextAreaPlain("");
        description.setRows(6);
        description.setEditable(false);

        // lista testow
        testsLit=new JList();

    	// podtyp relacji
        relationSubType=new ComboBoxPlain();
        relationSubType.addKeyListener(this);
        relationSubType.setEnabled(false);

        // typ relacji
        relationType=new ComboBoxPlain();
        relationType.addKeyListener(this);

        // wyswietlenie relacji
        mainRelations = new ArrayList<RelationType>();
        Collection<RelationType> readRelations=LexicalDA.getHighestRelations(type,pos);
        for (RelationType relType : readRelations) {
        	relType = LexicalDA.getEagerRelationTypeByID(relType);
        	if (fixedRelationType == null
        		|| relType.getId().longValue() == fixedRelationType.getId().longValue()
        		|| (fixedRelationType.getParent() != null &&
        			relType.getId().longValue() == fixedRelationType.getParent().getId())) {
        		relationType.addItem(RelationTypes.getFullNameFor(relType.getId()));
        		mainRelations.add(relType);
        	}
		}
        
//        System.out.println("POS: "+PosManager.getInstance().getNormalized(pos).getName());
//        for(RelationType rt : mainRelations){
//        	System.out.println("ID: "+rt.getId()+"\t"+RelationTypes.getFullNameFor(rt.getId()));
//        }

        // przycisk wybierz
        buttonChoose=new ButtonExt(Labels.SELECT,this,KeyEvent.VK_W);
        buttonChoose.addKeyListener(this);
        buttonCancel=new ButtonExt(Labels.CANCEL,this,KeyEvent.VK_A);
        buttonCancel.addKeyListener(this);

        relationSubType.addActionListener(this);
        relationType.addActionListener(this);

        // czy sa jakieś relacje
        if (mainRelations.size()>0) {
            //Ustawienie na sugestię, jeśli istnieje
            if(suggestedRelationType!=null){
            	if(suggestedRelationType.getParent()==null){
            		relationType.setSelectedItem(RelationTypes.getFullNameFor(suggestedRelationType.getId()));
            	}else{
            		relationType.setSelectedItem(RelationTypes.getFullNameFor(suggestedRelationType.getParent().getId()));
            		relationSubType.setSelectedItem(RelationTypes.getFullNameFor(suggestedRelationType.getId()));
            	}
            }
            else
            	relationType.setSelectedIndex(0);
    		buttonChoose.setEnabled(true);
        } else {
        	buttonChoose.setEnabled(false);
        }

        // dopisanie zdarzen
        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        middleItem.addKeyListener(this);
        middleItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);


        // dodanie elemetow UI do okna
		this.add("",new LabelExt(Labels.RELATION_TYPE_COLON,'t',relationType));
		this.add("tab hfill",relationType);
		this.add("br",new LabelExt(Labels.RELATION_SUBTYPE_COLON,'y',relationType));
		this.add("tab hfill",relationSubType);
		this.add("br",new LabelExt(Labels.RELATION_DESC_COLON,'\0',description));
		this.add("br hfill",new JScrollPane(description));

		this.add("br",new LabelExt(Labels.SOURCE_UNIT_COLON,'r',parentItem));
		this.add("tab hfill",parentItem);
		if (middleItem.isEnabled()) { // dodanie tylko gdy dostarczono jednostki posrednie
			this.add("br",new LabelExt(Labels.INTERMEDIATE_UNIT_COLON,'p',parentItem));
			this.add("tab hfill",middleItem);
		}
		this.add("br",new LabelExt(Labels.TARGET_UNIT_COLON,'d',childItem));
		this.add("tab hfill",childItem);
		this.add("br",new LabelExt(Labels.TESTS_COLON,'\0',testsLit));
		this.add("br hfill vfill",new JScrollPane(testsLit));
		this.add("br center",this.buttonChoose);
		this.add("",this.buttonCancel);
	}

	/**
	 * @author amusial
	 * Constructor for derived classes
	 * @param frame parent component
	 * @param type relation type
	 * @param pos part of speech
	 * @param fixedRelationType fixed relation type
	 * */
	protected RelationTypeFrame(JFrame frame,
			  RelationArgument type,
			  PartOfSpeech pos,
			  RelationType fixedRelationType) {
		super(frame, Labels.RELATION_PARAMS,650,500);
		RelationTypeFrame.pos = pos;
		this.fixedRelationType = fixedRelationType;

		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	/**
	 * wyswietlenie okienka dialogowego
	 * @param workbench - srodowisko
	 * @param type - typ relacji
	 * @param pos - czesc mowy dla ktorej ma byc brana relacja
	 * @param parentUnits - jednostki nadrzedne
	 * @param childUnits - jednostki podrzedne
	 * @return typ relacji albo null
	 */
	public static RelationType showModal(Workbench workbench,
											RelationArgument type,
											PartOfSpeech pos,
											Collection<Sense> parentUnits,
											Collection<Sense> childUnits) {
		return showModal(workbench.getFrame(),type,pos,null,null,null,parentUnits,null,childUnits);
	}

	/**
	 * @param workbench
	 * @param type
	 * @param pos
	 * @param fixedRelationType
	 * @param parentUnits
	 * @param middleUnits
	 * @param childUnits
	 * @return typ relacji albo null
	 */
	public static RelationType showModal(Workbench workbench,
			  RelationArgument type,
			  PartOfSpeech pos,
			  RelationType fixedRelationType,
			  Collection<Sense> parentUnits,
			  Collection<Sense> middleUnits,
			  Collection<Sense> childUnits) {
		return showModal(workbench.getFrame(), type, pos, fixedRelationType,null,null, parentUnits, middleUnits, childUnits);
	}

	/**
	 * konwersja jednostki na liste
	 * @param unit - jednostka
	 * @return lista zawierajaca jednostek
	 */
	public static Collection<Sense> unitToList(Sense unit) {
		Collection<Sense> list = new ArrayList<Sense>();
		list.add(unit);
		return list;
	}
	
	/**
	 * wyswietlenie okienka dialogowego
	 * @param frame - srodowisko
	 * @param type - typ relacji
	 * @param pos - czesc mowy dla ktorej ma byc brana relacja
	 * @param relationType - typ relacji jesli jest ustawiona na sztywno
	 * @param suggestedRelationType - typ sugerowanej relacji, o ile istnieje
	 * @param suggestedUnit 
	 * @param parentUnits - jednostki nadrzedne
	 * @param middleUnits - jednostki posrednie
	 * @param childUnits - jednostki podrzedne
	 * @return typ relacji albo null
	 */
	public static RelationType showModal(JFrame frame,
			 								RelationArgument type,
			 								PartOfSpeech pos,
											RelationType relationType,
											RelationType suggestedRelationType,
											Sense suggestedUnit,
											Collection<Sense> parentUnits,
											Collection<Sense> middleUnits,
											Collection<Sense> childUnits) {
		RelationTypeFrame framew=new RelationTypeFrame(frame,type,pos,relationType,suggestedRelationType,suggestedUnit,parentUnits,middleUnits,childUnits);
		framew.setVisible(true);
		return framew.chosenType;
	}

	/**
	 * uzyto klawiatury
	 */
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_ESCAPE: // gdy wcisnieto esc
				arg0.consume();
				setVisible(false);
				break;
		}
	}

	/**
	 * odczytanie zaznaczonej relacji
	 * @return zaznaczona relacja
	 */
	protected RelationType getSelectedRelation() {
		if (subRelations!=null && subRelations.size()>0) {
			// jest pod typ
			int index=relationSubType.getSelectedIndex();
			for (RelationType type : subRelations) {
				if (index--==0) {
					return type;
				}
			}
		} else {
			// brak podtypu
			int index=relationType.getSelectedIndex();
			for (RelationType type : mainRelations) {
				if (index--==0) {
					return type;
				}
			}
		}
		return null;
	}

	/**
	 * wczytanie testow dla podanej relacji
	 * @param type - typ relacji
	 */
	protected void loadTests(RelationType type) {
		if (middleItem.getItemCount() == 0) {
			int a = parentItem.getSelectedIndex();
			int b = childItem.getSelectedIndex();
			
			if(a < 0 || b < 0)
				return;
			
			List<String> tests=LexicalDA.getTests(type,
					(parentItem.getItemAt(parentItem.getSelectedIndex())).toString(),
					(childItem.getItemAt(childItem.getSelectedIndex())).toString(),
					pos);
			testsLit.setListData(tests.toArray(new String[]{}));
		} else {
			List<String> tests=LexicalDA.getTests(type,
					(parentItem.getItemAt(parentItem.getSelectedIndex())).toString(),
					middleItem.getItemAt(middleItem.getSelectedIndex()).toString(),
					pos);
			tests.addAll(LexicalDA.getTests(type,
					(parentItem.getItemAt(parentItem.getSelectedIndex())).toString(),
					(childItem.getItemAt(childItem.getSelectedIndex())).toString(),
					pos));
			
			testsLit.setListData(tests.toArray(new String[]{}));
		}
	}

	/**
	 * nacisnieto ktorys z przyciskow
	 */
	public void actionPerformed(ActionEvent arg0) {
		// nacisnieto wybierz
		if (arg0.getSource()==buttonChoose) {
			chosenType=getSelectedRelation();
			this.setVisible(false);
			
		// nacisnieto anuluj
		} else if (arg0.getSource()==buttonCancel) {
			this.setVisible(false);

		// zmienil sie typ relacji
		} else if (arg0.getSource()==relationType) {
			relationSubType.removeAllItems();
			description.setText("");
			testsLit.setListData(new String[]{});

			// odczytanie indeksu wybranej relacji
			int index=relationType.getSelectedIndex();
			for (RelationType type : mainRelations) {
				
				if (index--==0) {
					// odswiezenenie podrelacji
					subRelations = new ArrayList<RelationType>();
					Collection<RelationType> readRelations=LexicalDA.getChildren(type);
			        for (RelationType relType : readRelations) {
			        	if (fixedRelationType == null || // gdy nie zdefiniowana relacji
			        		fixedRelationType.getId().longValue() == relType.getId().longValue()) { // lub gdy zgadza sie ze zdefiniowana relacja
			        		relationSubType.addItem(RelationTypes.getFullNameFor(relType.getId()));
			        		subRelations.add(relType);
			        	}
					}
			    	if (subRelations.size()>0)
			    		relationSubType.setSelectedIndex(0);
			    	else {
						loadTests(type);
			    	}
			    	description.setText(type.getDescription().getText());
					break;
				}
			}
			relationSubType.setEnabled(subRelations!=null && subRelations.size()>0);

		// zmienil sie podtyp lub zmienila sie nadzedna lub podrzedna jednostka
		} else if (arg0.getSource()==relationSubType || arg0.getSource()==parentItem || arg0.getSource()==childItem || arg0.getSource()==middleItem) {
			
			testsLit.setListData(new String[]{});
			RelationType relation=getSelectedRelation();
			if (relation!=null) {
				loadTests(relation);
			}
		}
	}

	public void keyReleased(KeyEvent arg0) {/***/}
	public void keyTyped(KeyEvent arg0) {/***/}
}
