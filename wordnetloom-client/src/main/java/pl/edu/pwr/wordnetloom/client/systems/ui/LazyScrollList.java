package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LazyScrollList<T> extends WebScrollPane {

    private final int LIST_ITEM_HEIGHT = 20;
    private final int TIME_TO_LOAD = 2000;
    private WebList list;
    private DefaultListModel model;
    private int limit;
    private int offset;
    private boolean end;
    private T blankItem;
    private int listSize;
    private ScrollListener scrollListener;
    private boolean[] loadedItems;
    private boolean startScrolling;

    public LazyScrollList(WebList list, DefaultListModel model, T blankItem, int limit) {
        super(list);

        this.list = list;
        this.limit = limit;
        this.model = model;
        this.blankItem = blankItem;
        initViewport(list);
        setAdjustmentListener(list);
    }

    private void initViewport(WebList list) {
        WebPanel panel = new WebPanel(new BorderLayout());
        panel.add(list, BorderLayout.NORTH);
        setViewportView(panel);
    }

    private void setAdjustmentListener(WebList list) {
        getVerticalScrollBar().addAdjustmentListener(e -> {
            startScrolling = true;
            setTaskTimer(list);
        });
    }

    private void setTaskTimer(WebList list) {
        final int MARGIN = 5;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (!startScrolling) {
                            return;
                        }
                        int startIndex = list.getFirstVisibleIndex() - MARGIN;
                        startIndex = startIndex > 0 ? startIndex : 0;
                        int endIndex = list.getLastVisibleIndex() + MARGIN;
                        endIndex = endIndex < listSize ? endIndex : listSize - 1;
                        // Sometimes not load last element.
                        // When endIndex is second to last index, then load also last element
                        if (endIndex >= listSize - 2) {
                            endIndex = listSize - 1;
                        }
                        startLoad(startIndex, endIndex);
                        startScrolling = false;
                    }
                }, TIME_TO_LOAD
        );
    }

    private void startLoad(int startIndex, int endIndex) {
        if (scrollListener == null) {
            return;
        }
        List<LoadTask> tasks = getLoadTasks(startIndex, endIndex);
        startTasks(tasks);
    }

    private List<LoadTask> getLoadTasks(int startIndex, int endIndex) {
        boolean startTask = false;
        LoadTask currentTask = null;
        List<LoadTask> tasks = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            if (isLoaded(i)) {
                if (startTask) {
                    currentTask.setEndIndex(i - 1);
                    tasks.add(currentTask);
                    startTask = false;
                }
            } else {
                setLoaded(i);
                if (!startTask) {
                    currentTask = new LoadTask(i);
                    startTask = true;
                }
            }
        }
        if (startTask) {
            currentTask.setEndIndex(endIndex);
            tasks.add(currentTask);
            setLoaded(endIndex);
        }
        return tasks;
    }

    private void startTasks(List<LoadTask> tasks) {
        tasks.forEach(task -> {
            new SwingWorker<List<T>, Void>() {

                private List<T> result;

                @Override
                protected List<T> doInBackground() throws Exception {
                    result = scrollListener.load(task.getStartIndex(), task.getLimit());
                    return result;
                }

                @Override
                protected void done() {
                    setItems(result, task.startIndex);
                }
            }.execute();
        });
    }

    public boolean isLoaded(int index) {
        return loadedItems[index];
    }

    public void setLoaded(int index) {
        loadedItems[index] = true;
    }

    public void setCollection(java.util.List<T> collection, int allElementsCount) {
        listSize = allElementsCount;
        SwingUtilities.invokeLater(() -> {
            loadedItems = new boolean[allElementsCount];
            for (int i = 0; i < collection.size(); i++) {
                model.addElement(collection.get(i));
                setLoaded(i);
            }
            addEmptyItems(collection, allElementsCount);
        });
    }

    private void addEmptyItems(List<T> collection, int allElementsCount) {
        for (int i = collection.size(); i < allElementsCount; i++) {
            model.addElement(blankItem);
        }
    }

    public void setItems(java.util.List<T> items, int startIndex) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < items.size(); i++) {
                int index = startIndex + i;
                model.setElementAt(items.get(i), index);
            }
        });
    }

    public T getSelectedItem() {
        int selectedIndex = list.getSelectedIndex();
        return (T) model.getElementAt(selectedIndex);
    }

    public void setScrollListener(ScrollListener listener) {
        scrollListener = listener;
    }

    public int getLimit() {
        return limit;
    }

    public void reset() {
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

    public interface ScrollListener<T> {
        /**
         * Metoda wykonywana w momencie przesunięcia listy do samego końca.
         * W metodzie powiny być pobierane kolejne elementy i dodawane do komponentu
         *
         * @param offset obecne przesunięcie
         * @param limit  ilość elementów która ma zostać pobrana
         */
        java.util.List<T> load(int offset, int limit);
    }

    private class LoadTask {
        private int startIndex;
        private int endIndex;

        public LoadTask(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getLimit() {
            return endIndex - startIndex + 1;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }
    }
}
