package pl.edu.pwr.wordnetloom.client.systems.common;

import java.util.ArrayList;
import java.util.Collection;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;

@Deprecated
// TODO: remove me
abstract class AbstractRelationsDC<T> {

    private RelationType relationType;
    private Collection<T> relations;
    private int indexOffset = 0;
    private int lastSize;

    /**
     * * synset jest nadrzednym elementem
     */
    public final static int IS_PARENT = 0;
    /**
     * * synset jest podrzednym elementem
     */
    public final static int IS_CHILD = 1;

    /**
     * konstruktor
     *
     * @param relationType - typ relacji
     * @param relations - relacje
     */
    public AbstractRelationsDC(RelationType relationType,
            Collection<T> relations) {
        this.relationType = relationType;
        this.relations = relations;
        if (this.relations == null) { // aby nie bylo nulla
            this.relations = new ArrayList<T>();
        }
        lastSize = this.relations.size();
        indexOffset = 0;
    }

    /**
     * odczytanie relacji o podanym indeksie
     *
     * @param index - indeks relacji
     * @return relacja albo null
     */
    public T getRelation(int index) {
        index -= indexOffset; // uwzglednienie przesuniecia
        for (T rel : relations) {
            if (index == 0) {
                return rel;
            }
            index--;
        }
        return null;
    }

    /**
     * odczytaj typ relacji
     *
     * @return typ relacji
     */
    public RelationType getRelationType() {
        return relationType;
    }

    /**
     * ustawienie przesuniecia indeksu
     *
     * @param indexOffset - przesuniecie indeksu
     */
    public void setIndexOffset(int indexOffset) {
        this.indexOffset = indexOffset;
    }

    /**
     * odczytanie wielkosc
     *
     * @return wielkosc
     */
    public int getSize() {
        return lastSize;
    }
}
