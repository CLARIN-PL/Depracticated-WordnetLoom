package pl.edu.pwr.wordnetloom.client.systems.models;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.apache.commons.collections15.map.HashedMap;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

/**
 * klasa dostarczajaca danych do JList
 *
 * @author Max
 * @param <T> - klasa bedaca skladnikiem kolekcji
 */
public class GenericListModel<T> implements ListModel {

    protected Collection<T> itemsCollection = Collections.synchronizedList(new ArrayList<>());
    protected Iterator<T> iter = null;
    protected int lastIndex = -1;
    protected String markName = null;
    protected Object tag = null;

    // lista sluchaczy
    private EventListenerList listeners = new EventListenerList();

    /**
     * konstruktor
     *
     * @param collection - kolekcja do zapamietania
     */
    public GenericListModel(Collection<T> collection) {
        setCollection(collection);
    }

    public GenericListModel(boolean synsetMode) {
        boolean synsetMode1 = synsetMode;
        setCollection(null);
    }

    /**
     * konstruktor
     */
    public GenericListModel() {
        setCollection(null);
    }

    /**
     * ustawienie kolekcji z danymi
     *
     * @param collection - kolekcja danych
     */
    public void setCollection(Collection<T> collection) {
        setCollection(collection, null);
    }

    /**
     * ustawienie kolekcji z danymi
     *
     * @param collection - kolekcja danych
     * @param markName - nazwa do oznaczenia
     */
    public void setCollection(Collection<T> collection, String markName) {
        this.markName = markName;
        iter = null;
        lastIndex = -1;
        if (collection == null) {
            itemsCollection = new ArrayList<>();
        } else {
            itemsCollection = collection;
        }
        notifyAllListeners(); // powiadomienie słuchaczy o zmianie
    }

    /**
     * odczytanie kolekcji danych
     *
     * @return kolekcja
     */
    public Collection<T> getCollection() {
        return itemsCollection;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return itemsCollection.size();
    }

    /**
     * zamienienie tekstu tak aby mial dodatkowe znaczniki
     *
     * @param text - tekst oryginalny
     * @return tekst po zmianie
     */
    private String formatString(String text) {
        if (markName != null) {
            return text.replace(markName, "#" + markName + "#");
        }
        StringBuilder formatedString = new StringBuilder();
        formatedString.append("<html><body style=\"font-weight:normal\">")
                .append("<div>").append(text).append("</div>")
                .append("</body></html>");
        return formatedString.toString();
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.ListModel#getElementAt(int)
     */
    public String getElementAt(int index) {
        return ((ArrayList<T>)itemsCollection).get(index).toString();
//        if (synsetMode) {
//            Long id = ((Sense) element).getSenseToSynset().getIdSynset();
//            return element == null ? Labels.NO_VALUE
//                    : formatString(buildNameTagForSynset(synsetTags.get(id)));
//        }
//        if (element instanceof RelationTest) {
//            return formatString(buildTagForRelationTest((RelationTest) element));
//        }
//        return element == null ? Labels.NO_VALUE : formatString(element
//                .toString() + " " + ((Sense) element).getLexicon().getLexiconIdentifier().getText());
    }

    public String buildTagForRelationTest(RelationTest relationTest) {

        StringBuilder tag = new StringBuilder();
        tag.append(htmlTagBuilder("3", "green", "[" + relationTest.getSenseApartOfSpeech() + "]"));
        tag.append(" ");
        tag.append(formatRelationTest(relationTest));
        return tag.toString();
    }

    public String formatRelationTest(RelationTest relationTest) {
        String relation = relationTest.toString();
        relation = relation.replace(">", "&gt;");
        relation = relation.replace("<", "<font color=\"blue\">&lt;");
        relation = relation.replace("&gt;", "&gt;</font>");
        return relation;
    }

    public String buildNameTagForSynset(String names) {
        StringBuilder synsetString = new StringBuilder();
        names = names.substring(1, names.length() - 1);
        String[] unitText = names.split(",");
        String first = unitText[0] + htmlTagBuilder("3", "black", "| ");
        synsetString.append(htmlTagBuilder("3", "black", "("));
        synsetString.append(htmlTagBuilder("3", "blue", first));
        if (unitText.length > 1) {
            synsetString
                    .append("<br><div style=\"text-align:left; margin-left:8px; width:150px;\">");
            for (int i = 1; i < unitText.length; i++) {
                synsetString.append(htmlTagBuilder("3", "black", unitText[i]));
                if (i < unitText.length - 1) {
                    synsetString.append(htmlTagBuilder("3", "black", ", "));
                }
            }
            synsetString.append(htmlTagBuilder("3", "black", ")"));
            synsetString.append("</div>");
        } else {
            synsetString.append(htmlTagBuilder("3", "black", ")"));
        }
        return synsetString.toString();
    }

    private String htmlTagBuilder(String fontSize, String color, String text) {
        StringBuilder tagText = new StringBuilder();
        tagText.append("<font size=\"").append(fontSize).append("\"")
                .append("color=\"").append(color).append("\">")
                .append("<b>").append(text).append("</b></font>");
        return tagText.toString();
    }

    /**
     * odczytanie obiektu o podanym indeksie
     *
     * @param index - numer obiektu w kolekcji
     * @return obiekt z kolekcji
     */
    public T getObjectAt(int index) {
        // teoretycznie jest to szybsza wersja wyszukiwania danych
        // gdyz przy kolejnych pobraniach nie zaczyna od nowa

        // / Dodano synchronizacje na 'iter', ponieważ zdażało się,
        // / że 'hasNext()' zwracał true, ale 'next()' już nie znajdywał
        // elementu.
        T element_to_return = null;
        synchronized (itemsCollection) {
            if (iter == null || index <= lastIndex) {
                iter = itemsCollection.iterator();
                lastIndex = -1;
            }

            for (; iter.hasNext() && element_to_return == null;) {
                T element = iter.next();
                if (index == ++lastIndex) {
                    element_to_return = element;
                }
            }
        }
        if (element_to_return != null) {
            return element_to_return;
        }

        iter = null;

        // wersja normalna jeśli nie znajdzie szybszą
        for (Iterator<T> iterLocal = itemsCollection.iterator(); iterLocal
                .hasNext();) {
            T element = iterLocal.next();
            if (index-- == 0) {
                return element;
            }
        }

        return null;
    }

    /**
     * powiadomienie wszystkich słuchaczy o zmianie
     */
    protected void notifyAllListeners() {
        for (ListDataListener dataListener : listeners
                .getListeners(ListDataListener.class)) {
            dataListener.contentsChanged(new ListDataEvent(this,
                    ListDataEvent.CONTENTS_CHANGED, 0, 0));
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener
	 * )
     */
    public void addListDataListener(ListDataListener dataListener) {
        listeners.add(ListDataListener.class, dataListener);
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.
	 * ListDataListener)
     */
    public void removeListDataListener(ListDataListener dataListener) {
        listeners.remove(ListDataListener.class, dataListener);
    }

    /**
     * odczyta taga
     *
     * @return tag
     */
    public Object getTag() {
        return tag;
    }

    /**
     * ustawienie taga
     *
     * @param tag - tag
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * odczytanie indeksu elementu na liscie
     *
     * @param object - obiekt do odszukania
     * @return indeks na liscie lub -1 gdy nie znaleziony
     */
    public int getIndex(T object) {
        int index = 0;
        for (T elem : itemsCollection) {
            if (object.equals(elem)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Collection<Sense> buildSenseCollectionAsSynstes(List<Sense> sense, String sortFrase) {
        Collection<Sense> synsets = new ArrayList<Sense>();
        if (!sense.isEmpty()) {
            for (Sense senseItem : sense) {
//                if (senseItem.getSenseToSynset().getSenseIndex() == 0) {
//                    synsets.add(senseItem);
//                }
//                if (senseItem.getLexicon().getId() == 2
//                        && senseItem.getSenseToSynset().getSenseIndex() == 1) {
//                    synsets.add(senseItem);
//                }
            }
        }

        Collator collator = Collator.getInstance(Locale.US);
        String rules = ((RuleBasedCollator) collator).getRules();
        try {
            RuleBasedCollator correctedCollator
                    = new RuleBasedCollator(rules.replaceAll("<'\u005f'", "<' '<'\u005f'"));
            collator = correctedCollator;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.NO_DECOMPOSITION);

        final Collator myFavouriteCollator = collator;

        Comparator<Sense> senseComparator = (a, b) -> {
            String aa = a.getWord().getWord().toLowerCase();
            String bb = b.getWord().getWord().toLowerCase();

            int c = myFavouriteCollator.compare(aa, bb);
            if (c == 0) {
                aa = a.getPartOfSpeech().getId().toString();
                bb = b.getPartOfSpeech().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            if (c == 0) {
                if (a.getVariant() == b.getVariant()) {
                    c = 0;
                }
                if (a.getVariant() > b.getVariant()) {
                    c = 1;
                }
                if (a.getVariant() < b.getVariant()) {
                    c = -1;
                }
            }
            if (c == 0) {
                aa = a.getLexicon().getId().toString();
                bb = b.getLexicon().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            return c;
        };

        //Items starting with frase
        List<Sense> withFraseOnBegining = new ArrayList<>();
        //Other items
        List<Sense> other = new ArrayList<>();

        for (Sense se : synsets) {
            if (se.getWord().toString().startsWith(sortFrase.toLowerCase())) {
                withFraseOnBegining.add(se);
            } else {
                other.add(se);
            }
        }
        withFraseOnBegining.sort(senseComparator);
        other.sort(senseComparator);
        withFraseOnBegining.addAll(other);
        return withFraseOnBegining;
    }

    public Map<Long, String> buildSynsetNameWithContainedUnits(List<Sense> sense) {
        Map<Long, String> names = new HashedMap<>();
        if (!sense.isEmpty()) {
            int i = 0;
            while (i < sense.size()) {
                Long id = sense.get(i).getSynset().getId();
                List<String> senseName = new ArrayList<>();
                while (sense.get(i).getSynset().getId().equals(id)) {
                    senseName.add(sense.get(i).toString() + " " + sense.get(i).getLexicon().getIdentifier());
                    i++;
                    if (i > sense.size() - 1) {
                        break;
                    }
                }
                names.put(id, senseName.toString());
                if (i < sense.size()) {
                    id = sense.get(i).getSynset().getId();
                }
            }
        }
        return names;
    }

    @SuppressWarnings("unchecked")
    public void setCollectionToSynsets(List<Sense> sense, String sortFrase) {
        Map<Long, String> synsetTags = buildSynsetNameWithContainedUnits(sense);
        setCollection((Collection<T>) buildSenseCollectionAsSynstes(sense, sortFrase), null);
    }

}
