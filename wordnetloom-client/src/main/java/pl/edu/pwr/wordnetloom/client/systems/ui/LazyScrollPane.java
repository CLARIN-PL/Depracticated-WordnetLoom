package pl.edu.pwr.wordnetloom.client.systems.ui;

import javax.swing.*;

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

    private JList list;
    private int limit;
    private int offset;
    private boolean end;
    private ScrollListener scrollListener;

    public LazyScrollPane(JList list,int limit){
        super(list);
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
        if(scrollListener != null){
            offset = list.getModel().getSize();
            scrollListener.onBottomScroll(offset, limit);
        }
    }

    public void setScrollListener(ScrollListener listener){
        scrollListener = listener;
    }

    public int getOffset() {return offset;}
    public void setOffset(int offset){
        this.offset = offset;
    }

    public int getLimit() {return limit;}
    public void setLimit(int limit) {this.limit = limit;}

    public void setEnd(boolean end){
        this.end = end;
    }

    public void reset(){
        end = false;
        offset = 0;
    }
}
