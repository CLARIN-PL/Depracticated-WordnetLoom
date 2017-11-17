package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RelationTypeManager {

    private List<RelationType> relationTypes;

    private static RelationTypeManager instance;

    private RelationTypeManager() {
        relationTypes = new ArrayList<>();
    }

    public static RelationTypeManager getInstance() {
        if (instance == null) {
            instance = new RelationTypeManager();
        }
        return instance;
    }

    public void loadRelationTypes(final List<RelationType> relationTypes) {

        this.relationTypes.clear();
        this.relationTypes.addAll(relationTypes);

    }

    public RelationType get(final Long id, final RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> id.equals(r.getId()))
                .findAny()
                .orElse(null);
    }

    public RelationType get(final Long id, final RelationArgument relationArgument, final Lexicon lexicon) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> id.equals(r.getId()))
                .filter(r -> r.getLexicons().contains(lexicon))
                .findAny()
                .orElse(null);
    }

    public RelationType get(final Long id) {
        return relationTypes
                .stream()
                .filter(r -> id.equals(r.getId()))
                .findAny()
                .orElse(null);
    }

    public List<RelationType> getParents(final RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> r.getParent() == null)
                .collect(Collectors.toList());
    }

    public List<RelationType> getRealtionsWithoutProxyParent(final RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> !hasChildren(r.getId()))
                .collect(Collectors.toList());
    }

    public List<RelationType> getRealtionsWithoutProxyParent(final RelationArgument relationArgument, final Lexicon lexicon) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> !hasChildren(r.getId()))
                .filter(r -> r.getLexicons().contains(lexicon))
                .collect(Collectors.toList());
    }

    protected boolean hasChildren(Long id) {
        return relationTypes
                .stream()
                .filter(r -> r.getParent() != null && id.equals(r.getParent().getId()))
                .findAny().orElse(null) != null;
    }

    public List<RelationType> getChildren(final Long parentId, final RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> r.getParent() != null)
                .filter(r -> parentId.equals(r.getParent().getId()))
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .collect(Collectors.toList());
    }

    public List<RelationType> getChildren(Long parentId) {
        return relationTypes
                .stream()
                .filter(r -> r.getParent() != null && parentId.equals(r.getParent().getId()))
                .collect(Collectors.toList());
    }

    public String getFullName(Long id, RelationArgument relationArgument) {
        RelationType relationType = get(id, relationArgument);
        return LocalisationManager.getInstance().getLocalisedString(relationType.getName());
    }

    public String getFullName(Long id) {
        RelationType relationType = get(id);
        return LocalisationManager.getInstance().getLocalisedString(relationType.getName());
    }

}
