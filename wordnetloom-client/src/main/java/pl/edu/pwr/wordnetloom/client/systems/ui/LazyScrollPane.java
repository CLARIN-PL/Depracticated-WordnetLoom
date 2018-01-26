package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.button.WebButton;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.awt.*;

public class LazyScrollPane extends JScrollPane{

    public interface ScrollListener
    {
        /**
         * Metoda wykonywana w momencie przesunięcia listy do samego końca.
         * W metodzie powiny być pobierane kolejne elementy i dodawane do komponentu
         * @param offset obecne przesunięcie
         * @param limit ilość elementów która ma zostać pobrana
         */
        void onBottomScroll(int offset, int limit);
    }
    //TODO sprawdzić to
    private WebList list;
    private int limit;
    private int offset;
    private boolean end;
    private ScrollListener scrollListener;
    private WebPanel panel;
    private WebButton loadMoreButton;

    public LazyScrollPane(WebList list, int limit){

        panel = new WebPanel(new BorderLayout());
        panel.add(list, BorderLayout.NORTH);

        loadMoreButton = new WebButton("Ładuj"); //TODO dorobić etykietę
        loadMoreButton.addActionListener(e -> onBottomScroll());
        loadMoreButton.setVisible(false);
        loadMoreButton.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(loadMoreButton);
        setViewportView(panel);

        this.list = list;
        this.limit = limit;
//        getVerticalScrollBar().addAdjustmentListener(e -> {
//            if(e.getValue() != 0
//                    && e.getValue() == e.getAdjustable().getMaximum() - e.getAdjustable().getVisibleAmount()
//                    && !e.getValueIsAdjusting()){
//                if(!end){
//                    onBottomScroll();
//                }
//            }
//        });
    }

    private void onBottomScroll(){
        if(scrollListener != null && !end) {
            offset = list.getModel().getSize();
            scrollListener.onBottomScroll(offset, limit);
        }
    }

    public void setScrollListener(ScrollListener listener){
        scrollListener = listener;
    }

    public int getLimit() {return limit;}

    public void setEnd(boolean end){
        this.end = end;
        loadMoreButton.setVisible(!end);
    }

    public void reset(){
        list.clearSelection();
        end = false;
        offset = 0;
        loadMoreButton.setVisible(false);
    }
}
