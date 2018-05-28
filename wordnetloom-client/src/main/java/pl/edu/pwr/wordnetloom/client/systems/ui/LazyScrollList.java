package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LazyScrollList<T> extends WebScrollPane {

    public interface ScrollListener<T>
    {
        /**
         * Metoda wykonywana w momencie przesunięcia listy do samego końca.
         * W metodzie powiny być pobierane kolejne elementy i dodawane do komponentu
         * @param offset obecne przesunięcie
         * @param limit ilość elementów która ma zostać pobrana
         */
        java.util.List<T> load(int offset, int limit);
    }

    private WebList list;
    private DefaultListModel model;
    private int limit;
    private int offset;
    private boolean end;
    private T blankItem;

    private ScrollListener scrollListener;

    private java.util.List<Boolean> downloadedItems;

    public LazyScrollList(WebList list, DefaultListModel model,T blankItem, int limit){
        super(list);
        this.model = model;
        this.blankItem = blankItem;
        WebPanel panel = new WebPanel(new BorderLayout());
        panel.add(list, BorderLayout.NORTH);

        setViewportView(panel);

        this.list = list;
        this.limit = limit;
        /*getVerticalScrollBar().addAdjustmentListener(e -> {
            if(e.getValue() != 0
                    && e.getValue() == e.getAdjustable().getMaximum() - e.getAdjustable().getVisibleAmount()
                    && !e.getValueIsAdjusting()){
                if(!end){
                    onBottomScroll();
                }
            }
        });*/
        int height = list.getHeight();
        System.out.println("Wysokość elementu : " + height);
        getVerticalScrollBar().addAdjustmentListener(e->{
            System.out.println(list.getFirstVisibleIndex());

        });


    }

    private void onBottomScroll(){
        if(scrollListener != null && !end) {
            offset = model.getSize();
            java.util.List<T> elements = scrollListener.load(offset, limit);
            for(T element : elements) {
                model.addElement(element);
            }
            //TODO przechwycić maksymalną wartość
            for(int i= elements.size(); i < 155; i++)
            {
                model.addElement(blankItem);
            }
            if(elements.size() < limit){
                end = true;
            }
        }
    }

    public void setCollection(java.util.List<T> collection, int allElementsCount)
    {
        for(T element: collection)
        {
            model.addElement(element);
        }
        for(int i=collection.size(); i < allElementsCount; i++)
        {
            model.addElement(blankItem);

        }
    }

    public void setScrollListener(ScrollListener listener){
        scrollListener = listener;
    }

    public int getLimit() {return limit;}

    public void reset(){
//        loadMoreButton.setVisible(false);
        list.clearSelection();
        end = false;
        offset = 0;
    }

    public int getModelSize() {
        return model.getSize();
    }

    public void setHorizontalScrolling(boolean scrolling) {
        setHorizontalScrollBarPolicy(scrolling ? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED : JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
}
