package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LazyScrollList<T> extends WebScrollPane {

    public interface LoadElementsListener<T>{
        ArrayList<T> load(int offset, int limit);
    }

    private WebList list;
    private int limit;
    private int offset;
    private boolean end;

    private ArrayList<T> tempElements;

    private LoadElementsListener loadListener;

    public LazyScrollList(WebList list, int limit) {
        super(list);
        WebPanel panel = new WebPanel(new BorderLayout());
        panel.add(list, BorderLayout.NORTH);
        setViewportView(panel);

        this.list = list;
        this.limit = limit;
        getVerticalScrollBar().addAdjustmentListener(e->{
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
        if(loadListener != null && !end){
            offset = list.getModel().getSize();
            ArrayList<T> elements = loadListener.load(offset, limit);
            offset = offset + elements.size();
            if(elements.size() < limit){
                end = true;
            } else {

            }
        }
    }

    public void setLoadListener(LoadElementsListener listener){
        loadListener = listener;
    }

    public int getLimit(){
        return limit;
    }

    /*public void setEnd(boolean end) {
        this.end = end;
    }*/

    public void reset(){
        list.clearSelection();
        end = false;
        offset = 0;
    }

    public void setHorizontalScrolling(boolean scrolling) {
        setHorizontalScrollBarPolicy(scrolling ? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED : JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
}
