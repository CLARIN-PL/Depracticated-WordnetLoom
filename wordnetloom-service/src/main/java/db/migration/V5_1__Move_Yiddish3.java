package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.*;

public class V5_1__Move_Yiddish3 implements JdbcMigration{

    final String DIRECTIONS = "LEFT=Syn_PWN-plWN,Hipo_PWN-plWN,Hiper_PWN-plWN,mczęść_PWN-plWN,melement_PWN-plWN,mmateriał_PWN-plWN,hczęść_PWN-plWN,helement_PWN-plWN,hmateriał_PWN-plWN,pot_odp_PWN-plWN,międzyjęzykowa_synonimia_częściowa_PWN-plWN,międzyjęzykowa_synonimia_międzyrejestrowa_PWN-plWN,pot_odp_PWN-plWN,holonimia,holonimia_czasownikowa,wielokrotność,uprzedniość,presupozycja,gradacyjność,Part_holonym,Member_holonym,Substance_holonym,Domain_of_synset_-_TOPIC,Domain_of_synset_-_REGION,Domain_of_synset_-_USAGE,Verb_Group,Attribute\n" +
            "RIGHT=Syn_plWN-PWN,Hipo_plWN-PWN,Hiper_plWN-PWN,mczęść_plWN-PWN,melement_plWN-PWN,mmateriał_plWN-PWN,hczęść_plWN-PWN,helement_plWN-PWN,hmateriał_plWN-PWN,pot_odp_plWN-PWN,międzyjęzykowa_synonimia_częściowa_plWN-PWN,międzyjęzykowa_synonimia_międzyrejestrowa_plWN-PWN,bliskoznaczność,meronimia,meronimia_czasownikowa,kauzacja,procesywność,stanowość,inchoatywność,fuzzynimia_synsetów,określnik,Part_meronym,Member_meronym,Substance_meronym,Member_of_this_domain_-_TOPIC,Member_of_this_domain_-_REGION,Member_of_this_domain_-_USAGE,Similar_to,Entailment,Cause,has_hypernymy,has_hyponym,is_related_to,entails,causes,has_component_meronym,has_member_meronym,has_portion_meronym,has_substance_meronym,synonimia_Y-Pol,synonimia_Pol-Y\n" +
            "BOTTOM=hiperonimia,egzemplarz,Hypernym,Instance_Hypernym\n" +
            "TOP=hiponimia,typ,mieszkaniec,wartość_cechy,Hyponym,Instance_Hyponym,sumo_instance";

    final String COLORS = "meronimia=#0000ff\n" +
            "Part_meronym=#0000ff\n" +
            "Member_meronym=#0000ff\n" +
            "Substance_meronym=#0000ff\n" +
            "meronimia_czasownikowa=#0000ff\n" +
            "holonimia=#0000ff\n" +
            "Part_holonym=#0000ff\n" +
            "Member_holonym=#0000ff\n" +
            "Substance_holonym=#0000ff\n" +
            "holonimia_czasownikowa=#0000ff\n" +
            "egzemplarz=#008080\n" +
            "Instance_Hyponym=#008080\n" +
            "typ=#008080\n" +
            "Instance_Hypernym=#008080\n" +
            "bliskoznaczność=#ff0000\n" +
            "Domain_of_synset_-_TOPIC=#ff0000\n" +
            "Domain_of_synset_-_REGION=#ff0000\n" +
            "Domain_of_synset_-_USAGE=#ff0000\n" +
            "Member_of_this_domain_-_TOPIC=#ff0000\n" +
            "Member_of_this_domain_-_REGION=#ff0000\n" +
            "Member_of_this_domain_-_USAGE=#ff0000\n" +
            "mieszkaniec=#0000ff\n" +
            "fuzzynimia_synsetów=#808080\n" +
            "stanowość=#008b8b\n" +
            "Verb_Group=#008b8b\n" +
            "procesywność=#8a2be2\n" +
            "Entailment=#0000ff\n" +
            "kauzacja=#800000\n" +
            "Cause=#800000\n" +
            "wielokrotność=#8b4513\n" +
            "presupozycja=#800000\n" +
            "uprzedniość=#800000\n" +
            "inchoatywność=#800000\n" +
            "gradacyjność=#00a5a7\n" +
            "określnik=#00BFFF\n" +
            "Similar_to=#00BFFF\n" +
            "Syn_plWN-PWN=#C71585\n" +
            "Hipo_plWN-PWN=#C71585\n" +
            "Hiper_plWN-PWN=#C71585\n" +
            "mczęść_plWN-PWN=#C71585\n" +
            "melement_plWN-PWN=#C71585\n" +
            "mmateriał_plWN-PWN=#C71585\n" +
            "hczęść_plWN-PWN=#C71585\n" +
            "helement_plWN-PWN=#C71585\n" +
            "hmateriał_plWN-PWN=#C71585\n" +
            "Syn_PWN-plWN=#C71585\n" +
            "Hipo_PWN-plWN=#C71585\n" +
            "Hiper_PWN-plWN=#C71585\n" +
            "mczęść_PWN-plWN=#C71585\n" +
            "melement_PWN-plWN=#C71585\n" +
            "mmateriał_PWN-plWN=#C71585\n" +
            "hczęść_PWN-plWN=#C71585\n" +
            "helement_PWN-plWN=#C71585\n" +
            "hmateriał_PWN-plWN=#C71585\n" +
            "pot_odp_PWN-plWN=#DC143C\n" +
            "międzyjęzykowa_synonimia_częściowa_PWN-plWN=#DC143C\n" +
            "międzyjęzykowa_synonimia_międzyrejestrowa_PWN-plWN=#DC143C\n" +
            "pot_odp_plWN-PWN=#DC143C\n" +
            "międzyjęzykowa_synonimia_częściowa_plWN-PWN=#C71585\n" +
            "międzyjęzykowa_synonimia_międzyrejestrowa_plWN-PWN=#C71585\n" +
            "sumo_instance=#808080\n" +
            "has_participle=#BFBFBF\n" +
            "has_antonymy=#BFBFBF\n" +
            "has_pertainym=#BFBFBF\n" +
            "has_hypernymy=#BFBFBF\n" +
            "has_hyponym=#BFBFBF\n" +
            "is_related_to=#BFBFBF\n" +
            "entails=#BFBFBF\n" +
            "causes=#BFBFBF\n" +
            "has_component_meronym=#BFBFBF\n" +
            "has_member_meronym=#BFBFBF\n" +
            "has_portion_meronym=#BFBFBF\n" +
            "has_substance_meronym=#BFBFBF\n" +
            "synonimia_Y-Pol=#BFBFBF\n" +
            "synonimia_Pol-Y=#BFBFBF\n";

    @Override
    public void migrate(Connection connection) throws SQLException {
        setSafeUpdate(connection, 0);
        moveSenseAttributes(connection);
        moveSynsetAttributes(connection);
        updateRelationTypes(connection);
        setSafeUpdate(connection, 1);
    }

    private void setSafeUpdate(Connection connection, int safeUpdate) throws SQLException {
        final String SAFE_UPDATE_STATEMENT = "SET SQL_SAFE_UPDATES = ?";
        PreparedStatement statement = connection.prepareStatement(SAFE_UPDATE_STATEMENT);
        statement.setInt(1, safeUpdate);
        statement.execute();
    }

    private void updateRelationTypes(Connection connection) throws SQLException {
        Map<String, RelationType> relationsTypes = findIncompleteRelationTypes(connection);
        relationsTypes = setRelationsDirections(relationsTypes);
        relationsTypes = setRelationsColors(relationsTypes);
        List<RelationType> relationTypesToUpdate = convertRelationTypesToList(relationsTypes);
        updateRelationTypesInDb(relationTypesToUpdate, connection);
        System.out.println();
    }

    private void updateRelationTypesInDb(List<RelationType> relationTypes, Connection connection) throws SQLException {
        final String UPDATE_STATEMENT = "UPDATE relation_type " +
                "SET color = ?," +
                " node_position = ? " +
                "WHERE uuid = ?";
        PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
        for(RelationType relationType : relationTypes){
            if(relationType.getColor() != null){
                statement.setString(1, relationType.getColor());
            } else {
                statement.setNull(1, Types.VARCHAR);
            }
            if(relationType.getNodePosition() != null){
                statement.setString(2, relationType.getNodePosition());
            } else {
                statement.setNull(2, Types.VARCHAR);
            }
            statement.setObject(3, relationType.getUuid());

            statement.execute();
        }
    }

    private List<RelationType> convertRelationTypesToList(Map<String, RelationType> relations){
        Set<RelationType> relationTypesSet = new HashSet<>();
        relations.forEach((name, type)->{
            if(!relationTypesSet.contains(type)){
                if(type.getColor() != null || type.getNodePosition() != null){
                    relationTypesSet.add(type);
                }
            }
        });
        return new ArrayList<>(relationTypesSet);
    }

    private Map<String, RelationType> setRelationsColors(Map<String, RelationType> relations){
        String[] lines = COLORS.split(System.getProperty("line.separator"));
        String relation;
        String color;
        for(String line : lines){
            String[] split = line.split("=");
            relation = split[0];
            color = split[1];
            RelationType relationType = relations.get(relation);
            if(relationType != null){
                relationType.setColor(color);
            } else {
                System.out.println("Ojoj, zepsuło się");
            }
        }
        return relations;
    }

    private Map<String, RelationType> setRelationsDirections(Map<String, RelationType> relations) {
        String[] lines = DIRECTIONS.split(System.getProperty("line.separator"));
        String direction;
        for(String line : lines){
            String[] split = line.split("=");
            direction = split[0];
            String[] values = split[1].split(",");
            for(String value : values){
                RelationType relationType = relations.get(value);
                if(relationType != null){
                    relationType.setNodePosition(direction);
                } else {
                    System.out.println("Coś poszło nie tak");
                }
            }

        }
        return relations;
    }

    private Map<String, RelationType> findIncompleteRelationTypes(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT R.id, R.uuid, A1.id, A1.value, A2.id, A2.value FROM relation_type R " +
                "JOIN application_localised_string A1 ON R.name_id = A1.id AND A1.language = 'pl' " +
                "JOIN application_localised_string A2 ON R.short_display_text_id = A2.id AND A2.language = 'pl' " +
                "WHERE color IS NULL OR node_position = 'IGNORE'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        Map<String, RelationType> relations = new HashMap<>();
        RelationType relationType;
        while(resultSet.next()) {
            relationType = new RelationType();
            relationType.setId(resultSet.getLong(1));
            relationType.setUuid((byte[]) resultSet.getObject(2));
            relationType.setNameId(resultSet.getLong(3));
            relationType.setName(resultSet.getString(4));
            relationType.setShortNameId(resultSet.getLong(5));
            relationType.setShortName(resultSet.getString(6));

            relations.put(relationType.getName(), relationType);
            relations.put(relationType.getShortName(), relationType);
        }
        return relations;
    }

    private void moveSynsetAttributes(Connection connection) throws SQLException {
        // TODO sprawdzić, czy wszystkie atrybuty są ładowane
        List<Attribute> attributes = getSynsetAttributes(connection);
        List<Attribute> filteredAttributes = filterAttributes(attributes);
        updateSynsetAttributes(filteredAttributes, connection);
    }

    private void moveSenseAttributes(Connection connection) throws SQLException {
        List<Attribute> attributes = getSenseAttributes(connection);
        List<Attribute> filteredAttributes = filterAttributes(attributes);
        updateSenseAttributes(filteredAttributes, connection);

    }

    private Map<String, Long> getRegisters(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT D.id, S.value FROM dictionaries D JOIN application_localised_string S ON D.name_id = S.id AND S.language = 'pl' " +
                "WHERE dtype = 'Register'";
        Map<String, Long> result = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        while(resultSet.next()){
            Long id = resultSet.getLong(1);
            String value = resultSet.getString(2);
            result.put(value, id);
        }
        return result;
    }

    private PreparedStatement getUpdateSenseAttributeStatement(String columnName, Connection connection) throws SQLException {
        String UPDATE_QUERY = "UPDATE sense_attributes SET COL=? WHERE sense_id = ?".replaceAll("COL", columnName);
        return connection.prepareStatement(UPDATE_QUERY);
    }

    private PreparedStatement getUpdateSynsetAttributeStatement(String columnName, Connection connection) throws SQLException {
        String UPDATE_QUERY = "UPDATE synset_attributes SET COL=? WHERE synset_id = ?".replaceAll("COL", columnName);
        return connection.prepareStatement(UPDATE_QUERY);
    }

    // TODO zrobić refaktor, połączyć z updateSenseAttributes
    private void updateSynsetAttributes(List<Attribute> attributes, Connection connection) throws SQLException{
        System.out.print("updateSynsetAttributes");
        Map<String, PreparedStatement> updateStatements = new HashMap<>();
        Map<String, Long> registers = getRegisters(connection);
        PreparedStatement statement;
        for(Attribute attribute : attributes){
            String column = getColumn(attribute.getType());

            if(updateStatements.containsKey(column)){
                statement = updateStatements.get(column);
            } else {
                statement = getUpdateSynsetAttributeStatement(column, connection);
                updateStatements.put(column, statement);
            }

            if(attribute.getType().equals("register")){
                Long registerId = registers.get(attribute.getValue());
                if(registerId != null){
                    statement.setLong(1, registerId);
                } else {
                    System.out.println("Niepoprawny rejestr");
                }
            } else {
                statement.setString(1, attribute.getValue());
            }
            statement.setLong(2, attribute.getId());
            statement.executeUpdate();
        }

        for(Map.Entry<String, PreparedStatement> entry:updateStatements.entrySet()){
            entry.getValue().close();
        }
    }

    private void updateSenseAttributes(List<Attribute> attributes, Connection connection) throws SQLException {
        System.out.print("updateSenseAttributes");
        Map<String, PreparedStatement> updateStatements = new HashMap<>();
        Map<String, Long> registers = getRegisters(connection);
        PreparedStatement statement;
        for(Attribute attribute : attributes){
            System.out.println(attribute.getId());

            String column = getColumn(attribute.getType());

            if(updateStatements.containsKey(column)){
                statement = updateStatements.get(column);
            } else {
                statement = getUpdateSenseAttributeStatement(column, connection);
                updateStatements.put(column, statement);
            }

            if(attribute.getType().equals("register")){
                Long registerId = registers.get(attribute.getValue());
                if(registerId != null){
                    statement.setLong(1, registerId);
                } else {
                    System.out.println("Niepoprawny rejestr");
                }
            } else {
                statement.setString(1, attribute.getValue());
            }
            statement.setLong(2, attribute.getId());
            statement.executeUpdate();
        }

        for(Map.Entry<String, PreparedStatement> entry:updateStatements.entrySet()){
            entry.getValue().close();
        }
    }

    private String getColumn(String type){
        switch (type){
            case "register":
                return "register_id";
            // TODO posprawdzać te wartości
            case "source":
            case "project":
            case "use_cases":
            case "a1_markedness":
            case "a1_emotional_markedness":
            case "a2_markedness":
            case "a2_emotional_markedness":
                return null;
            default:
                return type;
        }
    }

    private List<Attribute> filterAttributes(List<Attribute> attributes){
        System.out.println("filterAttributes");
        final Set<String> emptyValues = new HashSet<>(Arrays.asList("", "brak rejestru", "n/d", "|", "Yiddish"));
        List<Attribute> result = new ArrayList<>();
        for(Attribute attribute : attributes){
            if(!emptyValues.contains(attribute.getValue())){
                result.add(attribute);
            }
        }
        return result;
    }

    //TODO zrobić refaktor, połączyć z getSenseAttributes
    private List<Attribute> getSynsetAttributes(Connection connection) throws SQLException{
        System.out.print("getSynsetAttributes");
        final String SELECT_QUERY = "SELECT A.synset, T1.text, T2.text " +
                "FROM `plwordnet3-prod`.synset_attribute A\n" +
                "JOIN `plwordnet3-prod`.sense_to_synset SS ON A.synset = SS.id_synset " +
                "JOIN `plwordnet3-prod`.sense SE ON SS.id_sense = SE.id AND SE.id_lexicon > 2 " +
                "JOIN `plwordnet3-prod`.attribute_type AT ON A.type = AT.id " +
                "JOIN `plwordnet3-prod`.text T1 ON AT.type_name = T1.id " +
                "JOIN `plwordnet3-prod`.text T2 ON A.value = T2.id";
        List<Attribute> resultList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);

        while(resultSet.next()){
            Long id = resultSet.getLong(1);
            String type = resultSet.getString(2);
            String value = resultSet.getString(3);
            Attribute attribute = new Attribute(id, type, value);
            resultList.add(attribute);
        }
        statement.close();
        return resultList;
    }

    private List<Attribute> getSenseAttributes(Connection connection) throws SQLException {
        System.out.println("getSenseAttributes");
        final String SELECT_QUERY = "SELECT A.sense, TE.text, TE2.text FROM `plwordnet3-prod`.sense_attribute A " +
                "JOIN `plwordnet3-prod`.sense S ON A.sense = S.id AND S.id_lexicon > 2 " +
                "JOIN `plwordnet3-prod`.attribute_type T ON A.type = T.id " +
                "JOIN `plwordnet3-prod`.text TE ON T.type_name = TE.id " +
                "JOIN `plwordnet3-prod`.text TE2 ON A.value = TE2.id " +
                "WHERE TE.text != 'source' ;";
        List<Attribute> resultList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        while(resultSet.next()){
            Long id = resultSet.getLong(1);
            String type = resultSet.getString(2);
            String value = resultSet.getString(3);
            Attribute attribute = new Attribute(id, type, value);
            resultList.add(attribute);
        }
        statement.close();
        return resultList;
    }

    private class Attribute{
        private Long id;
        private String type;
        private String value;

        public Attribute(Long id, String type, String value) {
            this.id = id;
            this.type = type;
            this.value = value;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private class RelationType{
        private Long id;
        private byte[] uuid;
        private Long nameId;
        private String name;
        private Long shortNameId;
        private String shortName;
        private String nodePosition;
        private String color;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public byte[] getUuid() {
            return uuid;
        }

        public void setUuid(byte[] uuid) {
            this.uuid = uuid;
        }

        public Long getNameId() {
            return nameId;
        }

        public void setNameId(Long nameId) {
            this.nameId = nameId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getShortNameId() {
            return shortNameId;
        }

        public void setShortNameId(Long shortNameId) {
            this.shortNameId = shortNameId;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getNodePosition() {
            return nodePosition;
        }

        public void setNodePosition(String nodePosition) {
            this.nodePosition = nodePosition;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
