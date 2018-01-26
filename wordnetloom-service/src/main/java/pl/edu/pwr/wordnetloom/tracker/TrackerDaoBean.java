package pl.edu.pwr.wordnetloom.tracker;

import java.util.Date;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.tracker.model.Tracker;

@Stateless
public class TrackerDaoBean implements TrackerDaoLocal {

    @Override
    public Tracker insert(Tracker.TABLE table, Long tid, String user) {
        Tracker tracker = makeTracker(table, tid, user);
        tracker.setInserted(1);

        //    local.persistObject(tracker);
        return tracker;
    }

    @Override
    public Tracker remove(Tracker.TABLE table, Long tid, String user) {
        Tracker tracker = makeTracker(table, tid, user);
        tracker.setDeleted(1);

        //      local.persistObject(tracker);
        return tracker;
    }

    @Override
    public Tracker update(Tracker.TABLE table, Long tid, String user) {
        Tracker tracker = makeTracker(table, tid, user);
        //     local.persistObject(tracker);

        Tracker tracker2 = makeTracker(table, tid, user);
        tracker2.setData_before_change(tracker.getId());
        //     local.persistObject(tracker2);

        return tracker2;
    }

    private Tracker makeTracker(Tracker.TABLE table, Long tid, String user) {
        Tracker tracker = new Tracker();

        tracker.setDatetime(new Date());
        tracker.setDeleted(0);
        tracker.setInserted(0);
        tracker.setData_before_change(null);
        tracker.setTable(table.getName());
        tracker.setTid(tid);
        tracker.setUser(user);

        return tracker;
    }

    private TLexicalUnit makeTLexicalUnit(Sense sense, String comment, String owner) {
        TLexicalUnit tunit = new TLexicalUnit();
        tunit.setComment(comment);
        tunit.setDomain(sense.getDomain().getId().intValue());
        //  tunit.setLemma(sense.getLemma().getWord());
        tunit.setOwner(owner);
        tunit.setPos(sense.getPartOfSpeech().getId().intValue());
        tunit.setProject(1);//TODO atrybut dynamiczny
        tunit.setSource(null);//TODO atrybut dynamiczny
        tunit.setID(sense.getId());
        //    tunit.setVariant(sense.getSenseNumber());
        return tunit;
    }

    @Override
    public void insertedLexicalUnit(Sense sense, String comment, String owner) {
        TLexicalUnit tunit = makeTLexicalUnit(sense, comment, "");
        //     local.persistObject(tunit);

        insert(Tracker.TABLE.LEXICALUNIT, tunit.getTid(), owner);
    }

    @Override
    public void deletedLexicalUnit(Sense sense, String comment, String owner) {
        TLexicalUnit tunit = makeTLexicalUnit(sense, comment, "");
        //   local.persistObject(tunit);

        remove(Tracker.TABLE.LEXICALUNIT, tunit.getTid(), owner);
    }

    @Override
    public void updatedLexicalUnit(Sense sense, String comment, String owner) {
        TLexicalUnit tunit = makeTLexicalUnit(sense, comment, "");
        //   local.persistObject(tunit);

        update(Tracker.TABLE.LEXICALUNIT, tunit.getTid(), owner);
    }
}
