package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import java.util.ArrayList;
import java.util.Comparator;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;

public class ViwnNodeAlphabeticComparator implements Comparator<ViwnNode> {

    public static ArrayList<RelationTypes> order = null;

    private ArrayList<RelationTypes> rel_order = null;

    public ViwnNodeAlphabeticComparator() {
        if (order == null) {
            rel_order = new ArrayList<>();

//            rel_order.add(RelationTypes.getByName("hiperonimia"));
//            rel_order.add(RelationTypes.getByName("hiponimia"));
//            rel_order.add(RelationTypes.getByName("mieszkaniec"));
//            rel_order.add(RelationTypes.getByName("bliskoznaczność"));
        } else {
            rel_order = order;
        }
    }

    @Override
    public int compare(ViwnNode arg0, ViwnNode arg1) {
        if (!(arg1 instanceof ViwnNodeSynset)) {
            return -1;
        }
        if (!(arg0 instanceof ViwnNodeSynset)) {
            return 1;
        }

        if (!(arg0.getSpawner() instanceof ViwnNodeSynset)) {
            return arg0.getLabel().compareTo(arg1.getLabel());
        }

        ViwnNodeSynset s1 = (ViwnNodeSynset) arg0;
        ViwnNodeSynset s2 = (ViwnNodeSynset) arg1;
        ViwnNodeSynset spawner = (ViwnNodeSynset) (arg0.getSpawner());

        ViwnEdgeSynset ee1 = null;
        ViwnEdgeSynset ee2 = null;

        for (ViwnEdgeSynset e : spawner.getRelation(arg0.getSpawnDir())) {
            if (e.getChild().equals(s1.getSynset().getId())
                    || e.getParent().equals(s1.getSynset().getId())) {
                ee1 = e;
                break;
            }
        }

        if (ee1 == null) {
            return -1;
        }

        for (ViwnEdgeSynset e : spawner.getRelation(arg1.getSpawnDir())) {
            if (e.getChild().equals(s2.getSynset().getId())
                    || e.getParent().equals(s2.getSynset().getId())) {
                ee2 = e;
                break;
            }
        }

        if (ee2 == null) {
            return -1;
        }

        int idx1 = rel_order.indexOf(ee1.getRelationType());
        int idx2 = rel_order.indexOf(ee2.getRelationType());

        if (idx1 == idx2) {
            return arg0.getLabel().compareTo(arg1.getLabel());
        } else {
            return new Integer(idx1).compareTo(idx2);
        }
    }

}
