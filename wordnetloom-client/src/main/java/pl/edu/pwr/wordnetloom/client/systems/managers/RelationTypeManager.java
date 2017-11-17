package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.HashMap;
import java.util.List;

public class RelationTypeManager {

    private HashMap<Long, RelationType> senseRelationTypesMap;
    private HashMap<Long, RelationType> synsetRelationTypesMap;

    private static RelationTypeManager instance;

    private RelationTypeManager() {
        senseRelationTypesMap = new HashMap<>();
        synsetRelationTypesMap = new HashMap<>();
        loadRelationTypes();
    }

    public static RelationTypeManager getInstance() {
        if (instance == null) {
            instance = new RelationTypeManager();
        }
        return instance;
    }

    private void loadRelationTypes() {
        senseRelationTypesMap.clear();
        synsetRelationTypesMap.clear();

        //TODO zastanowić sie, czy nie lepiej zrobić jednego zapytania, które pobierze wszystie typy relacji
        List<RelationType> senseRelationTypesList = RemoteService.relationTypeRemote.findHighest(RelationArgument.SENSE_RELATION);
        for (RelationType type : senseRelationTypesList) {
            // type.setChildren(RemoteService.relationTypeRemote.findChildren(type.getId()));
            senseRelationTypesMap.put(type.getId(), type);
        }

        List<RelationType> synsetRelationTypesList = RemoteService.relationTypeRemote.findHighest(RelationArgument.SYNSET_RELATION);
        for (RelationType type : synsetRelationTypesList) {
            //  type.setChildren(RemoteService.relationTypeRemote.findChildren(type.getId()));
            synsetRelationTypesMap.put(type.getId(), type);
        }
    }

    public RelationType get(Long id, RelationArgument relationArgument) {
        if (relationArgument == RelationArgument.SENSE_RELATION) {
            return senseRelationTypesMap.get(id);
        } else {
            return synsetRelationTypesMap.get(id);
        }
    }

    public RelationType get(Long id) {
        RelationType result;
        for (RelationArgument argument : RelationArgument.values()) {
            result = get(id, argument);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public List<RelationType> getChildren(Long parentId, RelationArgument relationArgument) {
        if (relationArgument == RelationArgument.SENSE_RELATION) {
            // return senseRelationTypesMap.get(parentId).getChildren();
        } else {
            // return synsetRelationTypesMap.get(parentId).getChildren();
        }
        return null;
    }

    public List<RelationType> getChildren(Long parentId) {
        List<RelationType> result;
        for (RelationArgument argument : RelationArgument.values()) {
            result = getChildren(parentId, argument);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public String getFullNameFor(Long id, RelationArgument relationArgument) {
        StringBuilder nameBuilder = new StringBuilder();
        RelationType relationType = get(id, relationArgument);
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        if (relationType.getParent() != null) {
            //nameBuilder.append(relationType.getParent().getName().append(":");
        }
        nameBuilder.append(relationType.getName());
        return nameBuilder.toString();
    }

    public String getFullNameFor(Long id) {
        String result;
        for (RelationArgument argument : RelationArgument.values()) {
            result = getFullNameFor(id, argument);
            if (!result.isEmpty()) {
                return result;
            }
        }
        return null;
    }

    public RelationType getByName(String name, RelationArgument relationArgument) {
        HashMap<Long, RelationType> currentMap;
        if (relationArgument == RelationArgument.SENSE_RELATION) {
            currentMap = senseRelationTypesMap;
        } else {
            currentMap = synsetRelationTypesMap;
        }
        
        return null;
    }

    public RelationType getByName(String name) {
        RelationType result;
        for (RelationArgument argument : RelationArgument.values()) {
            result = getByName(name, argument);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    //TODO usupełnić, sprawdzić co tam metoda ma robić
    public void refresh() {
    }
}
