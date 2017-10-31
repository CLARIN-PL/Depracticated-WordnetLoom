package pl.edu.pwr.wordnetloom.commontests.relationtest;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;

import java.util.Arrays;
import java.util.List;

@Ignore
public class RelationTestForTestsRepository {

    public static RelationTest relationTestA() {
        RelationTest t = new RelationTest();
        return t;
    }

    public static RelationTest relationTestB() {
        RelationTest t = new RelationTest();

        return t;
    }

    public static List<RelationTest> allRelationTests() {
        return Arrays.asList(relationTestA(), relationTestB());
    }
}
