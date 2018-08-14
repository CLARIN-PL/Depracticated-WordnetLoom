package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class ComboCheckBox extends JComboBox{

    public interface CheckComboStore {
        boolean isSelected();
        void setSelected(boolean selected);
        String getName();
        Long getId();
    }

    // TODO usunać tę listę
    private List<JCheckBox> checkBoxList;
    private List<? extends CheckComboStore> items;
    private DefaultComboBoxModel model;

    private boolean isReady = false;
    public ComboCheckBox() {
        checkBoxList = new ArrayList<>();
        model = new DefaultComboBoxModel();

        setModel(model);
        setRenderer(new CheckComboRenderer());

        addActionListener(e -> {
            if(isReady){
                ComboCheckBox source = (ComboCheckBox) e.getSource();
                JCheckBox checkBox = (JCheckBox) source.getSelectedItem();
                checkBox.setSelected(!checkBox.isSelected());
                checkBox.updateUI();
            }
        });
    }

    @Override
    public void setPopupVisible(boolean visible){
        if(visible){
            super.setPopupVisible(visible);
        }
        // else preventing closing popup
    }

    public void setElements(List<? extends CheckComboStore> elements){
        isReady = false;
        model.removeAllElements();
        items = elements;
        for(CheckComboStore checkComboStore : elements){
            JCheckBox checkBox = new JCheckBox();
            checkBox.setText(checkComboStore.getName());
            checkBox.setSelected(checkComboStore.isSelected());
            checkBoxList.add(checkBox);
            model.addElement(checkBox);
        }
        isReady = true;
    }

    public void setSelectedIds(List<Long> ids){
        items.forEach(item->{
            if(ids.contains(item.getId())){
                item.setSelected(true);
            }
        });
    }

    public List<Long> getSelectedItemsIds() {
        List<Long> result = new ArrayList<>();
        for(int i =0; i<checkBoxList.size(); i++) {
            if(checkBoxList.get(i).isSelected()){
                result.add(items.get(i).getId());
            }
        }
        return result;
    }

    private class CheckComboRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return (JCheckBox) value;
        }
    }
}
