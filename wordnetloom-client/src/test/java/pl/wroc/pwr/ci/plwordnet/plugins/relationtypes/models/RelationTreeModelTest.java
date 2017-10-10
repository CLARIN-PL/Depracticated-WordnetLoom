package pl.wroc.pwr.ci.plwordnet.plugins.relationtypes.models;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.client.plugins.login.data.UserSessionData;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTreeModel;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTypeNode;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.relationtype.model.IRelationType;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import pl.edu.pwr.wordnetloom.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RelationTreeModelTest {

    private static final String locale = "pl";

    private static RelationTreeModel relationTreeModel;
    private static List<IRelationType> types;
    private static RelationTypeNode root;

    @BeforeClass
    public static void BeforeClass(){
        relationTreeModel = new RelationTreeModel();
        root = relationTreeModel.getRoot();
        IRelationType type1 = new SynsetRelationType();
        type1.setName(locale, "Jeden" );
        IRelationType type2 = new SynsetRelationType();
        type2.setName(locale, "Dwa");
        IRelationType type3 = new SynsetRelationType();
        type3.setName(locale, "Trzy");
        IRelationType type4 = new SynsetRelationType();
        type4.setName(locale, "Cztery");
        types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
        types.add(type3);
        types.add(type4);

        //inicjalizacja ustawień potrzebnych do pobrania nazw typów relacji
        UserSessionData data = new UserSessionData("testUser", "password", locale, new User());
        RemoteConnectionProvider.getInstance().setUserSessionData(data);
    }

    @Before
    public void BeforeTest(){
        relationTreeModel.clear();
    }

    @Test
    public void AddRelationsFromListTest(){
        relationTreeModel.setRelationTypeNodes(types);
        assertEquals(4, relationTreeModel.getChildCount(root));
        for(int i=0; i < relationTreeModel.getChildCount(root); i++){
            assertEquals(types.get(i), relationTreeModel.getChild(root, i).getValue());
            assertTrue(relationTreeModel.getChild(root, i).getAllowsChildren()); //sprawdzenie, czy do dodanych elementów można wstawić dzieci
        }
    }

    @Test
    public void AddRelationType(){
        relationTreeModel.addNode(types.get(0));
        assertEquals(1, relationTreeModel.getChildCount(root));
        relationTreeModel.addNode(types.get(1));
        assertEquals(types.get(1), relationTreeModel.getChild(root, 1).getValue());
    }

    @Test
    public void AddRelationSubType(){
        final IRelationType expectedChild = types.get(1);
        relationTreeModel.addNode(types.get(0));
        RelationTypeNode typeNode = relationTreeModel.getChild(root, 0); // pobieramy wstawiony element
        relationTreeModel.addChild(typeNode,expectedChild);
        assertEquals(expectedChild,relationTreeModel.getChild(typeNode, 0).getValue());
        //sprawdzenie, czy wstawione dziecko może mieć dzieci
        assertFalse(relationTreeModel.getChild(typeNode, 0).getAllowsChildren());
        assertEquals(1, typeNode.getChildCount());
    }

    @Test
    public void AddChildToSubtype(){
        relationTreeModel.addNode(types.get(0));
        RelationTypeNode typeNode = relationTreeModel.getChild(root, 0);
        typeNode.addChild(types.get(1));
        RelationTypeNode childNode = relationTreeModel.getChild(typeNode, 0);
        childNode.addChild(types.get(2));
        assertEquals(0, childNode.getChildCount());
    }

    @Test
    public void RemoveRelationType(){
        final int DELETED_INDEX = 0;
        final int typesCount = types.size();
        relationTreeModel.setRelationTypeNodes(types);
        RelationTypeNode node = relationTreeModel.getChild(root, DELETED_INDEX);
        relationTreeModel.removeChild(root, node);
        assertEquals(typesCount -1, relationTreeModel.getChildCount(root));
        //sprawdzenie, czy element znajduje się w modelu
        for(int i=0; i< relationTreeModel.getChildCount(root); i++){
            assertFalse(relationTreeModel.getChild(root, i).getValue().equals(types.get(DELETED_INDEX)));
        }
    }

    @Test
    public void RemoveRelationSubType(){
        relationTreeModel.addNode(types.get(0));
        RelationTypeNode parentNode = relationTreeModel.getChild(root, 0);
        relationTreeModel.addChild(parentNode, types.get(1));
        assertEquals(1, parentNode.getChildCount());
        RelationTypeNode childNode = relationTreeModel.getChild(parentNode, 0);
        relationTreeModel.removeChild(childNode.getParent(), childNode);
        assertEquals(0, parentNode.getChildCount());
        assertEquals(null, childNode.getParent());
    }
}