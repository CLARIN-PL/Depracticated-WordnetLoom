package pl.edu.pwr.wordnetloom.client.systems.ui;

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
    private JList list;
    private int limit;
    private int offset;
    private boolean end;
    private ScrollListener scrollListener;
    private JPanel panel;
    private JButton loadMoreButton;

    public LazyScrollPane(JList list,int limit){
        panel = new JPanel(new BorderLayout());
        panel.add(list, BorderLayout.NORTH);
        loadMoreButton = new JButton("Ładuj"); //TODO dorobić etykietę
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
        if(scrollListener != null && !end){
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
