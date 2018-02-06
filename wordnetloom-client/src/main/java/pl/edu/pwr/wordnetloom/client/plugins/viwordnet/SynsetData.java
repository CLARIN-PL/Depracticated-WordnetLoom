package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynsetData {
    private Map<Long, DataEntry> data;

    public SynsetData()  {
        data = new HashMap<>();
    }

    public void setData(Map<Long, DataEntry> data) {
        this.data = data;
    }

    public void addData(DataEntry entry) {
        data.put(entry.getSynset().getId(), entry);
    }

    public void addData(Map<Long, DataEntry> entry){
        data.putAll(entry);
    }

    public DataEntry getById(Long id){
        return data.get(id);
    }

    public boolean contains(Long id) {
        return data.containsKey(id);
    }

    /**
     * Loading synset with his relations. Method load only one synset, not loaded synsets for relations fo main synset.
     * @param synsetId
     * @param lexicons
     */
    public void loadWithSimpleRelation(Long synsetId, List<Long> lexicons) {
        DataEntry dataEntry = RemoteService.synsetRemote.findSynsetDataEntry(synsetId, lexicons);
        data.put(synsetId,dataEntry);
    }


    /**
     * Loading synset and his relations. For all relations of main synset will be loaded synsets with simple relations.
     * Relations will loaded for all directions (LEFT, RIGHT, BOTTOM, TOP)
     * @param synset
     * @param lexicons
     */
    public void load(Synset synset, List<Long> lexicons) {
        load(synset, lexicons, NodeDirection.values());
    }


    public void load(Synset synset, List<Long> lexicons, NodeDirection[] directions)
    {
        //TODO być może będzie można zmienić na synsetId
        Map<Long,DataEntry> entries = RemoteService.synsetRemote.prepareCacheForRootNode(synset, lexicons, directions);
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(boas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = boas.size();
        System.out.println(size);
        data.putAll(entries);
    }

    public Synset getSynsetById(Long id){
        DataEntry entry = data.get(id);
        if(entry != null) {
            return entry.getSynset();
        }
        return null;
    }

    public void changeLabel(Long synsetId, Sense sense){
        DataEntry dataEntry = data.get(synsetId);
        setDataFromSense(dataEntry, sense);
    }

    public void createData(Synset synset, Sense sense){
        DataEntry dataEntry = new DataEntry();
        dataEntry.setSynset(synset);
        setDataFromSense(dataEntry, sense);
    }

    public void createData(Synset synset) {
        Sense headSense = RemoteService.senseRemote.findHeadSenseOfSynset(synset.getId());
        createData(synset, headSense);
    }

    private void setDataFromSense(DataEntry dataEntry, Sense sense) {
        dataEntry.setName(sense.getWord().getWord());
        dataEntry.setDomain(sense.getDomain().getName());
        dataEntry.setVariant(String.valueOf(sense.getVariant()));
    }

    public Long getPartOfSpeechId(Long synsetId){
        DataEntry dataEntry = data.get(synsetId);
        if(dataEntry != null){
            return dataEntry.getPosID();
        }
        return null;
    }

    public void clear() {
        data.clear();
    }

    public int getCount(){
        return data.size();
    }

    public void removeRelation(SynsetRelation relation) {
        DataEntry parentDataEntry = data.get(relation.getParent().getId());
        NodeDirection direction = relation.getRelationType().getNodePosition();
        parentDataEntry.getRelations(direction).remove(relation);
        DataEntry childDataEntry = data.get(relation.getChild().getId());
        NodeDirection oppositeDirection = direction.getOpposite();
        childDataEntry.getRelations(oppositeDirection).remove(relation);
    }

    public void addRelation(SynsetRelation relation){
        DataEntry parentDataEntry = data.get(relation.getParent().getId());
        NodeDirection direction = relation.getRelationType().getNodePosition();
        parentDataEntry.getRelations(direction).add(relation);
        //TODO być może będzie tutaj trzeba dodać dodanie relacji odwrotnej dla dziecka (w usuwaniu byo trzeba)
    }

    //TODO dodać metodę usuwającą synset
}
