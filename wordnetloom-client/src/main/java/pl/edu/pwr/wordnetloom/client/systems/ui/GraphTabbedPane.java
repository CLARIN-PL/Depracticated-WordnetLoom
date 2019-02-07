package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;

import java.util.ArrayList;
import java.util.List;

public class GraphTabbedPane extends CloseableTabbedPane {

    private List<TabInfo> tabInfo;

    public GraphTabbedPane(boolean showTabButton, boolean allowTabsClosing){
        super(showTabButton, allowTabsClosing);
        tabInfo = new ArrayList<>();
    }

    public void addTab(View view){

        addTab(view.getTitle(), view.getPanel());
        TabInfo info = new TabInfo();
        tabInfo.add(info);
    }

    public TabInfo getTabInfo(int tabIndex){
        return tabInfo.get(tabIndex-1);
    }

    public class TabInfo {
        private CriteriaDTO senseCriteriaDTO;
        private CriteriaDTO synsetCriteriaDTO;
        private LazyScrollList senseList;
        private LazyScrollList synsetList;
        // TODO dodać listy

        public CriteriaDTO getSenseCriteriaDTO() {
            return senseCriteriaDTO;
        }

        public void setSenseCriteriaDTO(CriteriaDTO criteriaDTO) {
            senseCriteriaDTO = criteriaDTO;
        }

        public CriteriaDTO getSynsetCriteriaDTO() {
            return synsetCriteriaDTO;
        }

        public void setSynsetCriteriaDTO(CriteriaDTO criteriaDTO){
            this.synsetCriteriaDTO = criteriaDTO;
        }

        public LazyScrollList getSenseList() {
            return senseList;
        }

        public void setSenseList(LazyScrollList list){
            this.senseList = list;
        }

        public LazyScrollList getSynsetList(){
            return synsetList;
        }

        public void setSynsetList(LazyScrollList list){
            this.synsetList = list;
        }
    }
}
