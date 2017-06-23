package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.models;

import java.util.ArrayList;
import java.util.Collection;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

/**
 * klasa dostarczajaca danych z uwzglednieniem linii podzialu
 *
 * @author Max
 */
public class UnitsInSynsetListModel extends GenericListModel<Sense> {

    // stałe
    private static final String VALUE_SPLIT_LINE = "=============================  ";

    private int splitPosition = 0;

    /**
     * konstruktor
     *
     */
    public UnitsInSynsetListModel() {
        super();
    }

    /**
     * ustawienie kolekcji z danymi
     *
     * @param collection - kolekcja danych
     * @param splitPosition - polozenie linii podzialu
     */
    public void setCollection(Collection<Sense> collection, int splitPosition) {
        this.splitPosition = splitPosition;
        if (collection == null) {
            itemsCollection = new ArrayList<>();
        } else {
            itemsCollection = collection;
        }
        notifyAllListeners();
    }

    /**
     * odczytanie wielkosci z uwzglednieniem linii podzialu (+1)
     *
     * @return
     */
    @Override
    public int getSize() {
        return itemsCollection.size() + 1;
    }

    /**
     * odczytanie elemtu o podanym indeksie z uwzglednieniem linii podzialu
     *
     * @param index
     * @return
     */
    @Override
    public String getElementAt(int index) {
        Sense unit;
        if (index != splitPosition) {
            unit = getObjectAt(index);
        } else {
            return VALUE_SPLIT_LINE;
        }
        if (unit != null) {
            return unit.toString();
        }
        return ".";
    }

    /**
     * odczytanie obiektu o podanym indeksie z uwzglednieniem lini podzialu
     *
     * @return
     */
    @Override
    public Sense getObjectAt(int index) {
        if (index < splitPosition) {
            return super.getObjectAt(index); // bez zmian
        }
        return super.getObjectAt(index > 0 ? index - 1 : 0); // bez zmian
    }

    /**
     * odczytanie polozenia linii podzialu
     *
     * @return indeks linii podzialu
     */
    public int getSplitPosition() {
        return splitPosition;
    }

    /**
     * odczytanie indeksow zaznaczonych elementow
     *
     * @param list - lista zaznaczonych elementow
     * @return lista indeksow
     */
    public Collection<Integer> getIndices(Collection<Sense> list) {
        Collection<Integer> result = new ArrayList<>();
        int size = getSize();
        for (int i = 0; i < size; i++) {
            if (i == splitPosition) {
                continue; // nie ma sensu dla podzialu
            }
            Sense elem = getObjectAt(i);
            if (elem != null) {
                for (Sense t : list) {
                    if (t != null && t.equals(elem)) {
                        result.add(i);
                        break;
                    }
                }
            }
        }
        return result;
    }
}
