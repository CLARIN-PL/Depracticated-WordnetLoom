package pl.edu.pwr.wordnetloom.client.systems.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;

// TODO: make a singleton
public class RelationTypes {

    private static final HashMap<Long, RelationTypes> all_rels
            = new HashMap<>();

    private static final HashMap<Long, List<RelationTypes>> top_child
            = new HashMap<>();

    static private boolean loaded = false;

    static public void refresh() {
        loaded = false;
        loadRels();
    }

    static public void loadRels() {
        if (loaded) {
            return;
        }
        loaded = true;

        all_rels.clear();
        top_child.clear();

        List<RelationType> full = RemoteUtils.relationTypeRemote.dbFullGetRelationTypes(LexiconManager.getInstance().getLexicons());
        List<RelationTypes> highest = new ArrayList<>();

        for (RelationType r : full) {

            if (r.getParent() == null) {
                RelationTypes rts = new RelationTypes();
                rts.me = r;
                if (r.getReverse() != null) {
                    rts.rev_id = r.getReverse().getId();
                }
                rts.updated = true;
                all_rels.put(r.getId(), rts);
                highest.add(rts);
            }
        }

        for (RelationType r : full) {
            if (r.getParent() == null) {
                continue;
            }

            RelationTypes parent = null;

            loop:
            for (RelationTypes p : highest) {
                if (p.me.equals(r.getParent())) {
                    parent = p;
                    break loop;
                }
            }

            if (parent == null) {
                continue;
            }

            List<RelationTypes> children = top_child.get(parent.me.getId());
            if (children == null) {
                children = new ArrayList<>();
                top_child.put(parent.me.getId(), children);
            }

            RelationTypes newChild = new RelationTypes();
            newChild.me = r;
            if (r.getReverse() != null) {
                newChild.rev_id = r.getReverse().getId();
            }
            newChild.updated = true;
            children.add(newChild);
            all_rels.put(r.getId(), newChild);
        }

    }

    static public RelationTypes getByName(String name) {
        RelationTypes rt = null;
        for (RelationTypes r : all_rels.values()) {
            if (r.me.getName().getText().equals(name)) {
                rt = r;
                break;
            }
        }
        return rt;
    }

    static public RelationTypes get(Long id) {
        return all_rels.get(id);
    }

    public static String getFullNameFor(long id) {
        StringBuilder buf = new StringBuilder();
        RelationType rel = get(id).getRelationType();
        RelationType parent = rel.getParent();
        if (parent != null) {
            buf.append(parent.getName().getText());
            buf.append(":");
        }
        buf.append(rel.getName().getText());
        return buf.toString();
    }

    private RelationType me;

    public RelationType getRelationType() {
        return me;
    }

    // TODO: remove me (cache)
    private Long rev_id;
    private boolean updated = false;

    public Collection<RelationTypes> getChildren() {
        if (top_child.get(me.getId()) != null) {
            return Collections.unmodifiableCollection(
                    top_child.get(me.getId()));
        }
        return null;
    }

    public String name() {
        return me.getName().getText();
    }

    public String Sname() {
        return me.getShortDisplayText().getText();
    }

    // TODO: remove me (cache)
    public Long rev_id() {
        if (rev_id == null && !updated) {
            rev_id = RemoteUtils.relationTypeRemote.getReverseID(me);
            updated = true;
        }
        return rev_id;
    }

    public Long Id() {
        return me.getId();
    }

    @Override
    public int hashCode() {
        return me.getId().intValue();
    }

    @Override
    public boolean equals(Object o) {
        RelationTypes rt = (RelationTypes) o;
        return me.getId().equals(rt.me.getId());
    }
}
