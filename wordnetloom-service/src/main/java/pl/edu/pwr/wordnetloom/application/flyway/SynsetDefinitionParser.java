package pl.edu.pwr.wordnetloom.application.flyway;

import java.sql.Connection;
import java.util.List;

public class SynsetDefinitionParser {

    public void run(Connection connection) {

    }

    private List<Attribute> getAttributesList (Connection connection){
        return null;
    }

    private class Attribute {

        private long id;

        private String definition;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }
    }

    private class Example {
        private long id;

        private String example;

        private String type;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
}
