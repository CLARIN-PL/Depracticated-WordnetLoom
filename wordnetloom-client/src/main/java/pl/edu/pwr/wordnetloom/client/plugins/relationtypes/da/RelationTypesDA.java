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
package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.da;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetPlugin;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationArgument;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationTest;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

/**
 * klasa pośrednicząca w wymianie danych
 *
 * @author Max
 */
public class RelationTypesDA {

    private RelationTypesDA() {
    }

    /**
     * usuniecie podanego testu
     *
     * @param test -test
     */
    public static void delete(RelationTest test) {
        RemoteUtils.testRemote.removeRelationTest(test);
    }

    /**
     * pobranie relacji najwyzszego typu
     *
     * @param type - typ relacji (czy leksylane czy synsetow)
     * @param pos - czesc mowy
     * @return lista relacji
     */
    public static Collection<RelationType> getHighestRelations(RelationArgument type, PartOfSpeech pos) {
        return RemoteUtils.relationTypeRemote.dbGetHighest(type, pos, LexiconManager.getInstance().getLexicons());
    }

    /**
     * odczytanie dzieci podanej relacji
     *
     * @param rel - relacja
     * @return dzieci relacji
     */
    public static Collection<RelationType> getChildren(RelationType rel) {
        return RemoteUtils.relationTypeRemote.dbGetChildren(rel, LexiconManager.getInstance().getLexicons());
    }

    /**
     * odczytanie testow dla danej relacji
     *
     * @param rel - relacja
     * @return dzieci relacji
     */
    public static List<RelationTest> getTests(RelationType rel) {
        return new ArrayList<>(); //RemoteUtils.relationTypeRemote.dbGetTests(rel);
    }

    /**
     * uaktualnienie danych o relacji
     *
     * @param rel - relacja
     * @param name - nazwa
     * @param display - forma wyswietlana
     * @param shortcut
     * @param desc - opis relacji
     * @param pos - czesci mowy
     * @param lexicon
     * @param objectType - typ relacji (leksykalna czy synsetow)
     * @param multilingual
     */
    public static void update(RelationType rel, String name, String display, String shortcut, String desc, String pos,
            Lexicon lexicon, RelationArgument objectType, boolean multilingual) {
        rel.getName().setText(name);
        rel.getDisplayText().setText(display);
        rel.getShortDisplayText().setText(shortcut);
        rel.getDescription().setText(desc);
        rel.setArgumentType(objectType);
        rel.setLexicon(lexicon);
        rel.setMultilingual(multilingual);
        RemoteUtils.relationTypeRemote.mergeObject(rel);
    }

    /**
     * uaktualnienie danych o relacji
     *
     * @param rel - relacja
     * @param reverseRelation - relacja odwrotna
     * @param autoReverse - automatycznie tworz odwrtona
     */
    public static void update(RelationType rel, RelationType reverseRelation, Boolean autoReverse) {
        if (reverseRelation != null) {
            rel.setReverse(reverseRelation);
        } else {
            rel.setReverse(null);
        }
        rel.setAutoReverse(autoReverse);
        RemoteUtils.relationTypeRemote.mergeObject(rel);
    }

    /**
     * uaktualnienie danych o relacji
     *
     * @param rel - relacja
     * @param pos - czesci mowy
     */
    public static void update(RelationType rel, String pos) {
        RemoteUtils.relationTypeRemote.mergeObject(rel);
    }

    /**
     * utworzenie nowej relacji
     *
     * @param name - nazwa relacji
     * @param parent - relacja nadrzedna
     * @param order
     * @param moveTestsAndConnections - czy przeniesc testy i powiazania
     */
    public static void newRelation(String name, RelationType parent, int order, boolean moveTestsAndConnections) {
        RelationType newRelation = new RelationType();
        newRelation.setName(new Text(name));
        newRelation.setLexicon(LexiconManager.getInstance().getFullLexicons().get(0));
        newRelation.setArgumentType(RelationArgument.LEXICAL);
        newRelation.setShortDisplayText(new Text(""));
        newRelation.setDisplayText(new Text(""));
        newRelation.setDescription(new Text(""));
        newRelation.setAutoReverse(false);

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("config/disp_relations.cfg", true)));
            out.print("," + name);
            out.close();

            out = new PrintWriter(new BufferedWriter(new FileWriter("config/relations_color.cfg", true)));
            out.println(name + "=#BFBFBF");
            out.close();
        } catch (IOException e) {
            Logger.getLogger(ViWordNetPlugin.class)
                    .log(Level.WARN,
                            "relations config file: config/disp_relations.cfg not found");
        }

        if (parent != null) {
            newRelation.setParent(parent);
        }

        newRelation = RemoteUtils.relationTypeRemote.save(newRelation);
        if (moveTestsAndConnections) {
            RemoteUtils.testRemote.switchTestsIntoNewRelation(parent, newRelation);
            RemoteUtils.lexicalRelationRemote.dbMove(parent, newRelation);
            RemoteUtils.synsetRelationRemote.dbMove(parent, newRelation);
        }
    }

    /**
     * usuniecie relacji
     *
     * @param rel - relacja
     */
    public static void delete(RelationType rel) {
        RemoteUtils.relationTypeRemote.dbDelete(rel, LexiconManager.getInstance().getLexicons());
    }

    /**
     * utworzenie nowego testu
     *
     * @param text - tresc testu
     * @param pos - czesc mowy
     * @param rel - relacja dla ktorej jest test
     */
    public static void newTest(String text, PartOfSpeech pos, RelationType rel) {

        RelationTest test = new RelationTest();
        test.setRelationType(rel);
        test.setText(new Text(text));
        test.setPos(pos);
        test.setOrder(0);

        RemoteUtils.testRemote.persist(test);
    }

    /**
     * uaktualnienie danych o tescie
     *
     * @param test - test do uaktualnienia
     * @param text - nowa tresc
     * @param pos - nowa czesc mowy
     */
    public static void update(RelationTest test, String text, PartOfSpeech pos) {
        test.setText(new Text(text));
        test.setPos(pos);
        RemoteUtils.testRemote.merge(test);
    }

    public static void update(RelationTest test, Integer order) {
        test.setOrder(order);
        RemoteUtils.testRemote.merge(test);
    }

    /**
     * odczytanie relacji odwrotnej
     *
     * @param rel - relacja dla ktorej ma zostac odczytana odwrotna
     * @return relacja odwrotna albo null jesli takiej nie ma
     */
    public static RelationType getReverseRelation(RelationType rel) {
        if (rel == null) {
            return null;
        }
        Long reverseId = null;
        if (rel.getReverse() != null) {
            reverseId = rel.getReverse().getId();
        }
        return reverseId != null ? RemoteUtils.relationTypeRemote.getEagerRelationTypeByID(rel) : null;
    }

    /**
     * odczytanie opisu relacji odwrotnej
     *
     * @param rel - relacja dla ktorej ma zostac odczytana odwrotna
     * @return nazwa relacji odwrotnej albo null
     */
    public static String getReverseRelationName(RelationType rel) {
        RelationType rev = getReverseRelation(rel);
        if (rev != null) {
            if (rev.getParent() != null) {
                RelationType parent = RemoteUtils.relationTypeRemote.dbGet(rev.getParent().getId());
                if (parent != null) {
                    return parent.getName() + " / " + rev.getName();
                }
            }
            return RelationTypes.getFullNameFor(rev.getId());
        }
        return null;
    }

    /**
     * odczytanie ile razy dany typ relacji został już wykorzystany
     *
     * @param relType - typ relacji
     * @return ilość wykorzystać
     */
    public static int getRelationUseCount(RelationType relType) {
        if (relType.getArgumentType() == RelationArgument.LEXICAL) {
            return RemoteUtils.lexicalRelationRemote.dbGetRelationUseCount(relType);
        } else if (relType.getArgumentType() == RelationArgument.SYNSET) {
            return RemoteUtils.synsetRelationRemote.dbGetRelationUseCount(relType);
        }
        return 0;
    }

    /**
     * @return Returns mapping from a symbol of relation to RelationType object.
     */
    static public HashMap<String, RelationType> getRelationSymbols() {
        // FIXME: hidden!
        return null;
    }

}
