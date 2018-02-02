package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.models;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Represents list of lexical units with head split by line
 *
 */
public class UnitsInSynsetListModel extends GenericListModel<Sense> {

    private static final String VALUE_SPLIT_LINE = "─────────────────────────────────";

    private int splitPosition = 0;

    public UnitsInSynsetListModel() {
        super();
    }

    public void setCollection(Collection<Sense> collection, int splitPosition) {

        this.splitPosition = splitPosition;
        if (collection == null) {
            itemsCollection = new ArrayList<>();
        } else {
            setCollection(collection);
        }
        notifyAllListeners();
    }

    @Override
    public void setCollection(Collection<Sense> collection) {
        if (collection != null) {
            List<Sense> list = new ArrayList<>(collection);
            list.sort(new UnitInSynsetComparator());
            itemsCollection = list;
        }
    }

    @Override
    public int getSize() {
        return itemsCollection.size() + 1;
    }


    @Override
    public String getElementAt(int index) {
        Sense unit;
        if (index != splitPosition) {
            unit = getObjectAt(index);
        } else {
            return VALUE_SPLIT_LINE;
        }
        if (unit != null) {
            return toString(unit);
        }
        return ".";
    }

    public String toString(Sense sense) {
        String word = sense.getWord().getWord();
        String variant = String.valueOf(sense.getVariant());
        String domain = LocalisationManager.getInstance().getLocalisedString(sense.getDomain().getName());
        return word + " " + variant + " (" + domain + ")";
    }


    @Override
    public Sense getObjectAt(int index) {
        if (index < splitPosition) {
            return super.getObjectAt(index);
        }
        return super.getObjectAt(index > 0 ? index - 1 : 0);
    }

    public int getLineSplitPosition() {
        return splitPosition;
    }

    private class UnitInSynsetComparator implements Comparator<Sense> {

        @Override
        public int compare(Sense o1, Sense o2) {
            return o1.getSynsetPosition().compareTo(o2.getSynsetPosition());
        }
    }

    public void setSplitPosition(int splitPosition) {
        this.splitPosition = splitPosition;
    }


    public Collection<Integer> getIndexesOfSelectedElements(Collection<Sense> list) {
        Collection<Integer> result = new ArrayList<>();
        int size = getSize();
        for (int i = 0; i < size; i++) {
            if (i == splitPosition) {
                continue;
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
