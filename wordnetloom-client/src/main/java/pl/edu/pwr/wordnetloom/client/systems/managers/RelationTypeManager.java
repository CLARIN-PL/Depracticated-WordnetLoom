package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RelationTypeManager implements Loggable {

    private static RelationTypeManager instance;
    private List<RelationType> relationTypes;

    private RelationTypeManager() {
        relationTypes = new ArrayList<>();
    }

    public static RelationTypeManager getInstance() {
        if (instance == null) {
            instance = new RelationTypeManager();
        }
        return instance;
    }

    public void load(List<RelationType> relationTypes) {

        this.relationTypes.clear();
        this.relationTypes.addAll(relationTypes);

    }

    public RelationType get(Long id, RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> id.equals(r.getId()))
                .findAny()
                .orElse(null);
    }

    public RelationType get(Long id, RelationArgument relationArgument, Lexicon lexicon) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> id.equals(r.getId()))
                .filter(r -> r.getLexicons().contains(lexicon))
                .findAny()
                .orElse(null);
    }

    public RelationType get(Long id) {
        return relationTypes
                .stream()
                .filter(r -> id.equals(r.getId()))
                .findAny()
                .orElse(null);
    }

    public List<RelationType> getParents(RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> r.getParent() == null)
                .sorted(Comparator.comparingInt(RelationType::getOrder))
                .collect(Collectors.toList());
    }

    public List<RelationType> getMultilingualParents(RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> r.getParent() == null)
                .filter(RelationType::getMultilingual)
                .sorted(Comparator.comparingInt(RelationType::getOrder))
                .collect(Collectors.toList());
    }

    public List<RelationType> getRelationsWithoutProxyParent(RelationArgument relationArgument) {
        return relationTypes
                .stream()
                .filter(r -> relationArgument.equals(r.getRelationArgument()))
                .filter(r -> !hasChildren(r.getId()))
                .collect(Collectors.toList());
    }

    public List<RelationType> getRelationsWithoutProxyParent(RelationArgument relationArgument, Lexicon lexicon) {
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

    public List<RelationType> getChildren(Long parentId, RelationArgument relationArgument) {
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
                .sorted(Comparator.comparingLong(RelationType::getOrder))
                .collect(Collectors.toList());
    }


    public String getFullName(Long id) {
        RelationType relationType = get(id);
        return LocalisationManager.getInstance().getLocalisedString(relationType.getName());
    }

    public HashMap<Long, Color> getRelationsColors() {

        HashMap<Long, Color> relationColors = new HashMap<>();

        relationTypes.forEach(rt -> {
            Color c;
            try {
                c = Color.decode(rt.getColor());
            } catch (Exception e) {
                c = Color.decode("#000000");
                logger().warn(rt.getColor() + " is not a valid color falling back to #000000");
            }

            relationColors.put(rt.getId(), c);
        });

        return relationColors;
    }
}
