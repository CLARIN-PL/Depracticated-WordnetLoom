package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;


import javax.swing.*;
import java.awt.*;

public class LazyScrollPane<T> extends WebScrollPane {

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

    private ScrollListener scrollListener;
//    private WebButton loadMoreButton;

    private java.util.List<T> tempItemList;

    public LazyScrollPane(WebList list, DefaultListModel model, int limit){
        super(list);
        this.model = model;
        WebPanel panel = new WebPanel(new BorderLayout());
        panel.add(list, BorderLayout.NORTH);

//        loadMoreButton = new WebButton("Ładuj"); //TODO dorobić etykietę
//        loadMoreButton.addActionListener(e -> onBottomScroll());
//        loadMoreButton.setVisible(false);
//        loadMoreButton.setHorizontalAlignment(SwingConstants.LEFT);
//        panel.add(loadMoreButton);
        setViewportView(panel);

        this.list = list;
        this.limit = limit;
        getVerticalScrollBar().addAdjustmentListener(e -> {
            if(e.getValue() != 0
                    && e.getValue() == e.getAdjustable().getMaximum() - e.getAdjustable().getVisibleAmount()
                    && !e.getValueIsAdjusting()){
                if(!end){
                    onBottomScroll();
                }
            }
        });
    }

    private void onBottomScroll(){
        if(scrollListener != null && !end) {
            offset = model.getSize();
            java.util.List<T> elements = scrollListener.load(offset, limit);
            for(T element : elements) {
                model.addElement(element);
            }
            if(elements.size() < limit){
                end = true;
            }
        }
    }

    public void setScrollListener(ScrollListener listener){
        scrollListener = listener;
    }

    public int getLimit() {return limit;}

   /* public void setEnd(boolean end){
        this.end = end;
//        loadMoreButton.setVisible(!end);
    }*/

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
