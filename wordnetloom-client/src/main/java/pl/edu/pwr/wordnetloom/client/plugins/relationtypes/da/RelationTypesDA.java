package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.da;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.ERelationType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.management.relation.RelationService;
import java.util.List;

public class RelationTypesDA {

    public static List<Lexicon> getLexicon(ERelationType type) {
        switch (type){
            case SENSE:
//                return RemoteService.senseRelationTypeRemote.
                throw new NotImplementedException(); //TODO stworzyć metodę pobierającą dozwolone leksykony
            case SYNSET:
                throw new NotImplementedException(); //TODO stworzyć metodę pobierającą dozwolone leksykony
            default:
                throw new IllegalArgumentException("Incorrect relation type");
        }
    }
}
