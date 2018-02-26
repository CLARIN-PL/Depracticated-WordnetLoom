package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.model.SynsetExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

import java.util.*;

public class LexicalDA {

    private LexicalDA() {
    }

    static public List<Sense> getLexicalUnits(String filterText, Domain domain, PartOfSpeech pos, RelationType relationType, String register, String comment, String example, int limitSize, List<Long> lexicons) {
        if (filterText == null) {
            filterText = "";
        }
//        return null;
        Long domainId = null;
        Long posId = null;
        if (domain != null) {
            domainId = domain.getId();
        }
        if (pos != null) {
            posId = pos.getId();
        }
        SenseCriteriaDTO criteria = new SenseCriteriaDTO(posId, domainId, filterText, lexicons);
        return RemoteService.senseRemote.findByCriteria(criteria);
        //RemoteUtils.lexicalUnitRemote.dbFastGetUnits(filterText, pos, domain, relationType, register, comment, example, limitSize, lexicons);
    }

    /**
     * odczytane listy lexemow wyszukanych po synsecie spelnijacych filtr
     *
     * @param filter
     * @param domain
     * @param relationType
     * @param artificial
     * @param comment
     * @param definition
     * @param limitSize
     * @param pos
     * @param lexicons
     * @return list of lexical units
     */
    static public List<Sense> getSenseBySynsets(String filter, Domain domain, RelationType relationType,
                                                String definition, String comment, String artificial, int limitSize, PartOfSpeech pos, List<Long> lexicons) {
        if (filter == null) {
            filter = "";
        }
        return null;//RemoteUtils.synsetRemote.dbFastGetSenseBySynsetUbyPose(filter, domain, relationType, definition, comment, artificial, limitSize, pos, lexicons);
    }

    /**
     * odczytane listy lexemow spelnijacych filtr
     *
     * @param filterText - filtr nazw
     * @param pos        - filtr czesci mowy
     * @param lexicons
     * @return kolekcja z danymi
     */
    static public Collection<Sense> getLexicalUnits(String filterText, PartOfSpeech pos, List<Long> lexicons) {
        return new ArrayList<>();//RemoteUtils.lexicalUnitRemote.dbFastGetUnits(filterText, pos, null, lexicons);
    }

    /**
     * zmiana kolejnosci jednostek w sysnecie z uwzglednieniem polozenia punktu
     * waznosci
     *
     * @param synset         - sysnet w ktorym mamy wymieniac kolejnosc
     * @param firstUnit      - pierwsza jednostka
     * @param secondUnit     - druga jednostka
     * @param firstUnitIndex - indeks pierwszej jednostki
     * @param splitLineIndex - indeks linii podzialu
     * @return Synset
     */
    static public Synset exchangeUnitsInSynset(Synset synset, Sense firstUnit, Sense secondUnit, int firstUnitIndex, int splitLineIndex) {
//        if (firstUnitIndex == splitLineIndex) {
//            synset.setSplit(splitLineIndex + 1);
//            RemoteUtils.synsetRemote.updateSynset(synset);
//        } else if (firstUnitIndex + 1 == splitLineIndex) {
//            synset.setSplit(splitLineIndex - 1);
//            RemoteUtils.synsetRemote.updateSynset(synset);
//        } else {
//            // zwykla zamiana jednostek na liscie
//            // exchange firstUnit,secondUnit
//            RemoteUtils.unitAndSynsetRemote.dbExchangeUnits(synset, firstUnit, secondUnit);
//            RemoteUtils.synsetRemote.updateSynset(synset);
//        }
        return refresh(synset);
    }

    /**
     * uktualnienie jednostki
     *
     * @param lexicalUnit - jednostka leksykalna
     * @param lemma       - lemat
     * @param domain      - domena
     * @param pos         - czesc mowy
     * @param tagCount    - tagcount
     * @param status      - status
     * @param comment     - komentarz
     * @return true jesli mozna bylo ustawic, false jesli sa bledne relacji
     */
    public static boolean updateUnit(Sense lexicalUnit, String lemma, Domain domain, PartOfSpeech pos, int tagCount, String comment) {
        boolean result = true;
        if (lexicalUnit != null) {

            lexicalUnit.setWord(new Word(lemma));
            lexicalUnit.setDomain(domain);
            lexicalUnit.setPartOfSpeech(pos);

            //RemoteUtils.lexicalUnitRemote.updateSense(lexicalUnit);
            //RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(lexicalUnit, Sense.COMMENT, lemma);
            //RemoteUtils.trackerRemote.updatedLexicalUnit(lexicalUnit, comment, PanelWorkbench.getOwnerFromConfigManager());
        }
        return result;
    }

    /**
     * uktualnienie jednostki
     *
     * @param lexicalUnit - jednostka leksykalna
     * @param lemma       - lemat
     * @param lexicon
     * @param variant     - numer porządkowy jednostki dla danego lematu
     * @param domain      - domena
     * @param pos         - czesc mowy
     * @param tagCount    - tagcount
     * @param status      - status
     * @param comment     - komentarz
     * @param register
     * @param useCase
     * @param link
     * @param definition
     * @return true jesli mozna bylo ustawic, false jesli sa bledne relacji
     */
    public static boolean updateUnit(
            Sense lexicalUnit, String lemma, Lexicon lexicon,
            int variant, Domain domain, PartOfSpeech pos, int tagCount, String comment, String register, String useCase, String link, String definition) {
        boolean result = true;
        if (lexicalUnit != null) {

            Word w = new Word(lemma);
            //       w = RemoteUtils.lexicalUnitRemote.saveWord(w);

            lexicalUnit.setWord(w);
            lexicalUnit.setLexicon(lexicon);
            lexicalUnit.setDomain(domain);
            lexicalUnit.setVariant(variant);
            lexicalUnit.setPartOfSpeech(pos);



//            RemoteUtils.lexicalUnitRemote.updateSense(lexicalUnit);
//            if (definition != null) {
//                RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(lexicalUnit, Sense.DEFINITION, definition);
//            }
//            if (comment != null) {
//                RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(lexicalUnit, Sense.COMMENT, comment);
//            }
//            if (register != null) {
//                RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(lexicalUnit, Sense.REGISTER, register);
//            }
//            if (useCase != null) {
//                RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(lexicalUnit, Sense.USE_CASES, useCase);
//            }
//            if (link != null) {
//                RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(lexicalUnit, Sense.LINK, link);
//            }
//            if (comment != null) {
//                RemoteUtils.trackerRemote.updatedLexicalUnit(lexicalUnit, comment, PanelWorkbench.getOwnerFromConfigManager());
//            }
        }
        return result;
    }

    /**
     * uktualnienie jednostki
     *
     * @param lexicalUnit - jednostka leksykalna
     * @param status      - status
     * @return true jesli mozna bylo ustawic, false jesli sa bledne relacji
     */
    public static boolean updateUnit(Sense lexicalUnit) {
        if (lexicalUnit != null) {
            //      RemoteUtils.lexicalUnitRemote.updateSense(lexicalUnit);
        }
        return true;
    }


    public static boolean updateSynset(Synset synset, String definition, String comment, boolean isAbstract, Set<SynsetExample> examples) {
        boolean result = true;
        if (synset != null) {

            if(isAbstract != synset.getAbstract()) {
                synset.setAbstract(isAbstract);
                RemoteService.synsetRemote.save(synset);
            }

            SynsetAttributes sa = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());
            sa.setDefinition(definition);
            sa.setComment(comment);
            examples.forEach( e-> e.setSynsetAttributes(sa));
            //sa.setExamples(examples);
            RemoteService.synsetRemote.save(sa);
        }
        return result;
    }

    /**
     * ustawienie poprawnosci relacji
     *
     * @param relation - relacja
     * @param valid    - poprawnosc
     */
    public static void setValid(SenseRelation relation, boolean valid) {
        // zapisanie stanu relacji
        //   RemoteUtils.lexicalRelationRemote.mergeObject(relation);

        if (!valid) { // nie jest poprawna, wiec zmieniamy satus
//            Sense unit = relation.getSenseFrom();
//            unit = RemoteUtils.lexicalUnitRemote.dbGet(unit.getId());
//            RemoteUtils.lexicalUnitRemote.updateSense(unit);
        }
    }

    /**
     * usuniecie podanych jednostek z synsetu
     *
     * @param units  - jednostki do usuniecia
     * @param synset - synset
     * @return Synset
     */
    public static Synset deleteConnections(Collection<Sense> units, Synset synset) {
        for (Sense unitDTO : units) {
            // synset = RemoteUtils.unitAndSynsetRemote.dbDeleteConnection(unitDTO, synset);
        }
        // NOWY WLASCICICEL
        if (synset != null) {
            // RemoteUtils.synsetRemote.updateSynset(synset);
        }
        // KONIEC NOWY WLASCICICEL
        return synset;
    }

    /**
     * usuniecie podanych jednostek z synsetu
     *
     * @param units       - jednostki do usuniecia
     * @param synset      - synset
     * @param updateOwner - czy uaktualnić właściciela synsetu na aktualnie
     *                    zalogowanego użytkownika
     */
    public static void deleteConnections(Collection<Sense> units, Synset synset, boolean updateOwner) {
        for (Sense unitDTO : units) {
            // RemoteUtils.unitAndSynsetRemote.dbDeleteConnection(unitDTO, synset);
        }
        if (updateOwner) {
            // RemoteUtils.synsetRemote.updateSynset(synset);
        }
    }

    /**
     * Usuniecie podanej jednostki z synsetu. Automatycznie uaktualnie
     * właściciela synsetu na aktualnie zalogowanego użytkownika.
     *
     * @param unit   - jednostka do usuniecia
     * @param synset - synset
     */
    public static void deleteConnection(Sense unit, Synset synset) {
        Collection<Sense> units = new ArrayList<>();
        units.add(unit);
        deleteConnections(units, synset);
    }

    /**
     * usuniecie podanej jednostki z synsetu
     *
     * @param unit        - jednostka do usuniecia
     * @param synset      - synset
     * @param updateOwner - czy uaktualnić właściciela synsetu na aktualnie
     *                    zalogowanego użytkownika
     */
    public static void deleteConnection(Sense unit, Synset synset, boolean updateOwner) {
        Collection<Sense> units = new ArrayList<>();
        units.add(unit);
        deleteConnections(units, synset, updateOwner);
    }

    /**
     * Dodanie jednostki do synsetu. Automatycznie uaktualnia właściciela
     * synsetu na aktualnie zalogowanego użytkownika
     *
     * @param unit   - jednostka
     * @param synset - synset
     * @return Synset
     */
    public static Synset addConnection(Sense unit, Synset synset) {
        //synset = RemoteUtils.unitAndSynsetRemote.dbAddConnection(unit, synset);
        return synset;
    }

    /**
     * Dodanie jednostki do synsetu. Automatycznie uaktualnia właściciela
     * synsetu na aktualnie zalogowanego użytkownika
     *
     * @param unit        - jednostka
     * @param synset      - synset
     * @param updateOwner - czy uaktualnić właściciela synsetu na aktualnie
     *                    zalogowanego użytkownika
     */
    public static void addConnection(Sense unit, Synset synset, boolean updateOwner) {
        // RemoteUtils.unitAndSynsetRemote.dbAddConnection(unit, synset, true);

        // NOWY WLASCICIEL
        if (updateOwner) {
            //   RemoteUtils.synsetRemote.updateSynset(synset);
        }
        // KONIEC NOWY WLASCICIEL
    }

    /**
     * dodanie jednostki do nowego synsetu
     *
     * @param unit - jednostka
     * @return synset, który zotał utworzony
     */
    public static Synset addToNewSynset(Sense unit) {
        Synset newSynset = new Synset();
        newSynset.setSplit(1);
        // newSynset = RemoteUtils.synsetRemote.updateSynset(newSynset);
        //RemoteUtils.dynamicAttributesRemote.saveOrUpdateSynsetAttribute(newSynset, Synset.OWNER, PanelWorkbench.getOwnerFromConfigManager());
        // RemoteUtils.unitAndSynsetRemote.dbAddConnection(unit, newSynset, true);
        return newSynset;
    }

    /**
     * usuniecie relacji
     *
     * @param relation - relacja do usuniecia
     */
    public static void delete(SenseRelation relation) {
        //  RemoteUtils.lexicalRelationRemote.dbDelete(relation);
    }

    /**
     * usuniecie jednostki
     *
     * @param unit - jednostka do usuniecia
     */
    public static void delete(Sense unit) {
        // RemoteUtils.lexicalUnitRemote.dbDelete(unit, PanelWorkbench.getOwnerFromConfigManager());
    }

    /**
     * Utworzenie relacji, której właścicielem będzie aktualnie zalogowany
     * użytkownik.
     *
     * @param parent - element nadrzedny
     * @param child  - element podrzedny
     * @param rel    - typ relacji
     */
    public static void makeRelation(Sense parent, Sense child, RelationType rel) {
        //RemoteUtils.lexicalRelationRemote.dbMakeRelation(parent, child, rel);
    }

    /**
     * Utworzenie relacji, które właścicielem będzie dany użytkownik.
     *
     * @param parent - element nadrzedny
     * @param child  - element podrzedny
     * @param rel    - typ relacji
     * @param owner  - użytkownik relacji
     */
    public static void makeRelation(Sense parent, Sense child, RelationType rel, String owner) {
        //  RemoteUtils.lexicalRelationRemote.dbMakeRelation(parent, child, rel);
    }

    /**
     * pobranie relacji najwyzszego typu
     *
     * @param type - typ relacji (czy leksylane czy synsetow)
     * @param pos  - czesc mowy
     * @return lista relacji
     */
    public static Collection<RelationType> getHighestRelations(PartOfSpeech pos) {
        return new ArrayList(); //RemoteUtils.relationTypeRemote.dbGetHighest(type, pos, LexiconManager.getInstance().getLexicons());
    }

    /**
     * odczytanie dzieci podanej relacji
     *
     * @param rel - relacja
     * @return dzieci relacji
     */
    public static Collection<RelationType> getChildren(RelationType rel) {
        return new ArrayList(); //RemoteUtils.relationTypeRemote.dbGetChildren(rel, LexiconManager.getInstance().getLexicons());
    }

    /**
     * odczytanie jednostek z bazy danych w trybie pelnym
     *
     * @param testLemma - filtr
     * @param lexicons
     * @return lita jednostek
     */
    public static List<Sense> getFullLexicalUnits(String testLemma, List<Long> lexicons) {
        return null; //RemoteUtils.lexicalUnitRemote.dbFullGetUnits(testLemma, lexicons);
    }

    /**
     * odczytanie czesci mowy synsetu
     *
     * @param synset   - synset
     * @param lexicons
     * @return czesc mowy
     */
    public static PartOfSpeech getPos(Synset synset, List<Long> lexicons) {
        return null;//RemoteUtils.synsetRemote.dbGetPos(synset, lexicons);
    }

    /**
     * odczytanie jednostek synsetu
     *
     * @param synset   - sysnet
     * @param lexicons
     * @return jednostki synsetu
     */
    public static ArrayList<Sense> getLexicalUnits(Synset synset, List<Long> lexicons) {
        return new ArrayList<>(); //RemoteUtils.synsetRemote.dbFastGetUnits(synset, lexicons)
    }

    /**
     * odczytanie synsetow podanej jednostki
     *
     * @param lexicalUnit - jednostka
     * @return lista synsetow
     */
    public static Collection<Synset> getSynsets(Sense lexicalUnit) {
        return new ArrayList<>(); //RemoteUtils.lexicalUnitRemote.dbFastGetSynsets(lexicalUnit, LexiconManager.getInstance().getLexicons());
    }

    /**
     * odczytanie synsetow podanego lematu
     *
     * @param lemma    - lemat
     * @param lexicons
     * @return lista synsetow
     */
    public static Collection<Synset> getSynsets(String lemma, List<Long> lexicons) {
        return new ArrayList<>(); //RemoteUtils.lexicalUnitRemote.dbFastGetSynsets(lemma, lexicons);
    }

    /**
     * skonowanie synsetu
     *
     * @param synset - synset
     */
    public static void clone(Synset synset) {
//		RemoteUtils.synsetRemote.dbClone(synset,Synset.getDefaultOwner());
    }

    /**
     * odswiezenie synsetu
     *
     * @param synset - synset
     * @return Synset
     */
    public static Synset refresh(Synset synset) {
        return null;// RemoteUtils.synsetRemote.dbGet(synset.getId());
    }

    /**
     * odswiezenie jednostki
     *
     * @param unit - unit
     * @return Sense
     */
    public static Sense refresh(Sense unit) {
        return null; //RemoteUtils.lexicalUnitRemote.dbGet(unit.getId());
    }

    /**
     * wyciaganie danych o wyrazach do wstawienia
     *
     * @param text    - tekst oryginalny
     * @param code    - kod do znalezienia, np <a:
     * @param replace - kod do zmianany <a>
     * @return pierwszy elementy o informacja o formie wyrazu do znalezienia, a
     * drugi element to poprawiony tekst
     */
    static private String[] extractUnitDefinition(String text, String code, String replace) {
        String[] result = new String[2];
        int pos = text.indexOf(code);
        if (pos != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(text.substring(0, pos));
            int endPos = text.indexOf(">", pos);

            result[0] = text.substring(pos + code.length(), endPos);

            sb.append(replace).append(text.substring(endPos + 1));
            result[1] = sb.toString();
        } else {
            result[0] = null;
            result[1] = text;
        }

        return result;
    }

    /**
     * odczytanie form jednostki o podanych definicjach
     *
     * @param defs - definicje form
     * @param unit - jednostka do odmiany
     * @return odmienione formy
     */
    static private Collection<String> getForms(Collection<String> defs, String unit) {
        Collection<String> forms = new ArrayList<>();
        // ustawieni suffixu
        String suffix = "";
        if (unit.endsWith("się")) { // konczy sie z się, a ma to zostać odcięte
            unit = unit.substring(0, unit.length() - 4);
            suffix = " się"; // ustawienie sufixu
        }
        for (String def : defs) {
            List<String> splits = Arrays.asList(def.split("\\|"));
            //forms.add(RemoteUtils.wordFormsRemote.getFormFor(new Word(unit), splits) + suffix);
        }
        return forms;
    }

    /**
     * odczytanie wlasciwych testow
     *
     * @param rel        - relacja
     * @param parentUnit - elementy nadrzedny
     * @param childUnit  - element podrzedny
     * @param pos        - czesc mowy
     * @return lista dostepnych testow
     */
    public static List<String> getTests(RelationType rel, String parentUnit, String childUnit, PartOfSpeech pos) {

        List<String> result = new ArrayList<>();
//        List<RelationTest> tests = RemoteUtils.testRemote.getRelationTestsFor(rel);
//
//        int testIndex = 1;
//        // przejscie po testach
//        for (RelationTest test : tests) {
//            if (test.getPos().equals(pos)) { // wybranie tego co ma dobra czesc mowy
//                String text = test.getText().getText();
//                Collection<String> defOfUnitA = new ArrayList<>();
//                Collection<String> defOfUnitB = new ArrayList<>();
//                String[] parseResult;
//                boolean found = true;F
//                while (found) {
//                    parseResult = extractUnitDefinition(text, "<x#", "<x" + defOfUnitA.size() + ">");
//                    found = parseResult[0] != null;
//                    if (found) {
//                        defOfUnitA.add(parseResult[0]);
//                    }
//                    text = parseResult[1];
//                }
//                found = true;
//                while (found) {
//                    parseResult = extractUnitDefinition(text, "<y#", "<y" + defOfUnitB.size() + ">");
//                    found = parseResult[0] != null;
//                    if (found) {
//                        defOfUnitB.add(parseResult[0]);
//                    }
//                    text = parseResult[1];
//                }
//
//                Collection<String> formsOfUnitA = getForms(defOfUnitA, parentUnit);
//                Collection<String> formsOfUnitB = getForms(defOfUnitB, childUnit);
//
//                int index = 0;
//                for (String f : formsOfUnitA) {
//                    text = text.replace("<x" + index + ">", "<font color=\"blue\">" + (f == null || "null".equals(f) || "null się".equals(f) ? parentUnit : f) + "</font>");
//                    index++;
//                }
//                index = 0;
//                for (String f : formsOfUnitB) {
//                    text = text.replace("<y" + index + ">", "<font color=\"blue\">" + (f == null || "null".equals(f) || "null się".equals(f) ? childUnit : f) + "</font>");
//                    index++;
//                }
//
//                result.add("<html>" + (testIndex++) + ". " + text + "</html>");
//            }
//        }
        return result;
    }

    /**
     * odczytanie relacji odwrotnej
     *
     * @param rel - relacja
     * @return relacja odwrotna
     */
    public static RelationType getReverseRelation(RelationType rel) {
        return null; //RemoteUtils.relationTypeRemote.dbGet(rel.getReverse().getId());
    }

    /**
     * odczytanie opisu relacji
     *
     * @param rel - relacja dla ktorej ma zostac odczytany opis
     * @return pelna nazwa relacji
     */
    public static String getRelationName(RelationType rel) {
        if (rel != null) {
            if (rel.getParent() != null) { // ma rodzica
//                IRelationType parent = RelationTypeManager.get(rel.getId()).getRelationType().getParent();
//                if (parent != null) {
//                    return RelationTypeManager.get(parent.getId()).name() + " / " + RelationTypeManager.get(rel.getId()).name();
//                }
            }
            return RelationTypeManager.getInstance().getFullName(rel.getId());
        }
        return null;
    }

    /**
     * zapisanie jednostki w bazie danych
     *
     * @param unit - jednostka do zapisania
     */
    public static void saveUnit(Sense unit) {
        // RemoteUtils.lexicalUnitRemote.updateSense(unit);
    }

    /**
     * zapisanie jednostki w bazie danych z odświeżeniem ID obiektu
     *
     * @param unit - jednostka do zapisania
     * @return Sense
     */
    public static Sense saveUnitRefreshID(Sense unit) {
        return null;//RemoteUtils.lexicalUnitRemote.updateSense(unit);
    }

    /**
     * zapisanie jednostki w bazie danych z odświeżeniem ID obiektu
     *
     * @param unit - jednostka do zapisania
     * @return unit - jednostka zapisana
     */
    public static Sense saveUnitWithReturn(Sense unit) {
        //unit = RemoteUtils.lexicalUnitRemote.updateSense(unit);
        return unit;
    }

    /**
     * zatwierdzenie jednostki uzytniwka i przeksztalcenie jej w systemowa
     *
     * @param unit - jednostka do zatwierdzenia
     */
    public static void approveUnit(Sense unit) {
        // RemoteUtils.lexicalUnitRemote.updateSense(unit);
    }

    /**
     * czy dostepne sa relacja danego typu
     *
     * @param relType - typ relacji
     * @return TRUE jesli sa dostepne relacje danego typu
     */
    public static boolean areRelations() {
        return true;//RemoteUtils.relationTypeRemote.dbGetHighest(relType, null, LexiconManager.getInstance().getLexicons()).size() > 0;
    }

    /**
     * sprawdzenie czy podana relacja już istnieje
     *
     * @param parent - element nadrzedny
     * @param child  - element podrzedny
     * @param rel    - typ relacji
     * @return TRUE jesli relacja istnieje
     */
    public static boolean checkIfRelationExists(Sense parent, Sense child, RelationType rel) {
        return true;//RemoteUtils.lexicalRelationRemote.dbRelationExists(parent, child, rel);
    }

    /**
     * odczytanie domeny jednostki z bazy danych
     *
     * @param unit - jednostka
     * @return domena
     */
    public static Domain getDomain(Sense unit) {
        // unit = RemoteUtils.lexicalUnitRemote.dbGet(unit.getId());
        return unit.getDomain();
    }

    /**
     * Sprawdza, czy jednostka należy do jakiegokolwiek synsetu
     *
     * @param unit - jednostka
     * @return TRUE jeśli należy do synsetu
     */
    public static boolean checkIfInAnySynset(Sense unit) {
        return false;// RemoteUtils.lexicalUnitRemote.dbInAnySynset(unit);
    }

    /**
     * odczytanie jednostek nie należących do żadnego synsetu
     *
     * @param filter - filtr dla lematu
     * @param pos
     * @return lista jednostek
     */
    public static Collection<Sense> getLexicalUnitsNotInAnySynset(String filter, PartOfSpeech pos) {
        return null; //RemoteUtils.lexicalUnitRemote.dbGetUnitsNotInAnySynset(filter, pos);
    }

    /**
     * Zwraca wolny numer wariantu dla danego lematu
     *
     * @param lemma    -- lemat jednostki
     * @param pos      -- część mowy
     * @param lexicons -- leksykon
     * @return numer wolnego wariantu
     */
    public static int getAvaibleVariantNumber(String lemma, PartOfSpeech pos, List<Long> lexicons) {
        int maxVariant = 0;

        Collection<Sense> units = LexicalDA.getFullLexicalUnits(lemma, lexicons);
        if (units != null && units.size() > 0) {
            for (Sense unit : units) {
                if (unit.getPartOfSpeech().equals(pos)) {
                    maxVariant = Math.max(maxVariant, unit.getVariant());
                }
            }
        }

        return maxVariant + 1;
    }

    public static RelationType getEagerRelationTypeByID(RelationType rt) {
        return null;
        //RemoteUtils.relationTypeRemote.getEagerRelationTypeByID(rt);
    }

}
