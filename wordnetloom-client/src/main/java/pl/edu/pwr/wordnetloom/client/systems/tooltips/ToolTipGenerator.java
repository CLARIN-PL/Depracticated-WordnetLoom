package pl.edu.pwr.wordnetloom.client.systems.tooltips;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.*;
import pl.edu.pwr.wordnetloom.client.systems.misc.IObjectWrapper;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.user.model.User;

import java.util.*;

/**
 * klasa dostarcza metod zwracajacych tooltipy dla jednostki leksyklanej i
 * synsetu
 *
 * @author Max
 */
public class ToolTipGenerator implements ToolTipGeneratorInterface, Loggable {
    // elementy wspolne

    protected final static String HTML_HEADER = "<html><body style=\"font-weight:normal\">";
    protected final static String HTML_FOOTER = "</body></html>";
    protected final static String HTML_TAB = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    protected final static String HTML_SPA = "&nbsp;";
    protected final static String HTML_BR = "<br>";
    protected final static String DESC_STR = "<div style=\"text-align:left; margin-left:10px; width:250px;\">%s</div>";
    protected final static String RED_FORMAT = "<font color=\"red\">%s</font>";
    static private volatile ToolTipGenerator generator = null;

    // szablony uzywane do wyswietlania danych
    static String SYNSET_TEMPLATE
            = HTML_SPA + "<font size=\"4\"><b><i>%s</i></b></font>" + HTML_BR
            + HTML_SPA + "<b>" + Labels.DEFINITION_COLON + "</b> " + DESC_STR + HTML_BR
            + HTML_SPA + "<b>" + Labels.ARTIFICIAL_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.STATUS_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.DOMAIN_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.SYNSET_COMMENT_COLON + "</b> " + DESC_STR + HTML_BR
            + HTML_SPA + "<b>" + Labels.UNIT_COMMENT_COLON + "</b> " + DESC_STR + HTML_BR;

    static String LEXICALUNIT_TEMPLATE
            = HTML_SPA + "<font size=\"4\"><b><i>%s</i></b></font>" + HTML_BR
            + HTML_SPA + "<b>" + Labels.LEXICON_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.DOMAIN_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.PARTS_OF_SPEECH_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.REGISTER_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.DEFINITION_COLON + "</b>" + DESC_STR
            + HTML_SPA + "<b>" + Labels.USE_CASE_COLON + "</b>" + DESC_STR;

    static String SYNSET_RELATION_TEMPLATE
            = HTML_SPA + "<b>" + Labels.RELATION_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.CORRECT_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.SOURCE_SYNSET_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.TARGET_SYNSET_COLON + "</b> %s" + HTML_BR;

    static String LEXICAL_RELATION_TEMPLATE
            = HTML_SPA + "<b>" + Labels.RELATION_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.CORRECT_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.SOURCE_UNIT_COLON + "</b> %s" + HTML_BR
            + HTML_SPA + "<b>" + Labels.TARGET_UNIT_COLON + "</b> %s" + HTML_BR;

    static String RELATIONS_HEADER
            = HTML_SPA + "<font size=\"4\"><b><i>" + Labels.RELATIONS_COLON + "</i></b></font>" + HTML_BR;

    final static String RELATIONS_TYPE = HTML_TAB + "<b>%s:</b>" + HTML_BR;
    final static String RELATIONS_ITEM = HTML_TAB + HTML_TAB + "%s" + HTML_BR;

    static String SYNSETS_HEADER
            = HTML_SPA + "<font size=\"4\"><b><i>" + Labels.SYNSETS_COLON + "</i></b></font>" + HTML_BR;

    final static String SYNSET_ITEM = DESC_STR;
    private boolean isEnabled = true;
    private TooltipTextCache cache;

    //TODO cache can store outdated data
    private class TooltipTextCache {
        private final int CAPACITY_LIMIT = 10;
        private Map<Long, String> cache;
        private Queue<Long> objectsIds;

        public TooltipTextCache() {
            cache = new HashMap<>(CAPACITY_LIMIT);
            objectsIds = new ArrayDeque<>(CAPACITY_LIMIT);
        }

        public String find(Long id) {
            return cache.get(id);
        }

        public void put(Long id, String text) {
            if (cache.size() == CAPACITY_LIMIT && !cache.containsKey(id)) { //if reach limit size, search earliest inserted id and replace them
                Long textId = objectsIds.poll();
                cache.remove(textId);
            }
            cache.put(id, text);
            objectsIds.add(id);
        }
    }

    private class PopupTextBuilder {

        private final String DEFINITION_LABEL = HTML_SPA + "<b>" + Labels.DEFINITION_COLON + "</b> " + DESC_STR + HTML_BR;
        private final String ARTIFICIAL_LABEL = HTML_SPA + "<b>" + Labels.ARTIFICIAL_COLON + "</b> %s" + HTML_BR;
        private final String STATUS_LABEL = HTML_SPA + "<b>" + Labels.STATUS_COLON + "</b> %s" + HTML_BR;
        private final String DOMAIN_LABEL = HTML_SPA + "<b>" + Labels.DOMAIN_COLON + "</b> %s" + HTML_BR;
        private final String OWNER_LABEL = HTML_SPA + "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR;
        private final String SYNSET_COMMENT_LABEL = HTML_SPA + "<b>" + Labels.SYNSET_COMMENT_COLON + "</b> " + DESC_STR + HTML_BR;
        private final String UNIT_COMMENT_LABEL = HTML_SPA + "<b>" + Labels.UNIT_COMMENT_COLON + "</b> " + DESC_STR + HTML_BR;
        private final String LEXICON_LABEL = HTML_SPA + "<b>" + Labels.LEXICON_COLON + "</b> %s" + HTML_BR;
        private final String PART_OF_SPEECH_LABEL = HTML_SPA + "<b>" + Labels.PARTS_OF_SPEECH_COLON + "</b> %s" + HTML_BR;
        private final String REGISTER_LABEL = HTML_SPA + "<b>" + Labels.REGISTER_COLON + "</b> %s" + HTML_BR;
        private final String USE_CASE_LABEL = HTML_SPA + "<b>" + Labels.USE_CASE_COLON + "</b>" + HTML_BR;
        private final String RELATIONS_HEADER = HTML_SPA + "<font size=\"4\"><b><i>" + Labels.RELATIONS_COLON + "</i></b></font>" + HTML_BR;
        private final static String DESC_STR = "<div style=\"text-align:left; margin-left:10px; width:250px;\">%s</div>";

        private StringBuilder textBuilder = new StringBuilder();

        public String createSenseText(Sense sense, List<SenseRelation> relations) {
            textBuilder.setLength(0);
            SenseAttributes attributes = sense.getSenseAttributes();
            addLexicon(sense.getLexicon())
                    .addDomain(sense.getDomain())
                    .addPartOfSpeech(sense.getPartOfSpeech());
            if (attributes != null) {
                addRegister(attributes.getRegister())
                        .addDefinition(attributes.getDefinition());
            }
            addExamples(sense.getExamples());
            if (relations != null && !relations.isEmpty()) {
                addSenseRelations(relations);
            }
            return buildText();
        }

        public String createSynsetText(Synset synset) {
            textBuilder.setLength(0);
            SynsetAttributes attributes = synset.getSynsetAttributes();
            if(attributes != null) {
                addDefinition(attributes.getDefinition())
                        .addArtificial(attributes.getIsAbstract());
            } else {
                addArtificial(false);
            }
            addStatus(synset.getStatus());

            Sense headSense = null;
            if(!synset.getSenses().isEmpty()) {
                headSense = synset.getSenses().get(0);
                addDomain(headSense.getDomain());
            }
            if(attributes != null){
                addOwner(attributes.getOwner())
                        .addSynsetComment(attributes.getComment());
            }
            if(headSense != null){
                SenseAttributes senseAttributes = headSense.getSenseAttributes();
                addSenseComment(senseAttributes.getComment());
            }

            return buildText();
        }

        public String buildText() {
            return HTML_HEADER + textBuilder.toString() + HTML_FOOTER;
        }

        public void clear() {
            textBuilder.setLength(0);
        }

        private PopupTextBuilder addSenseComment(String comment) {
            if(comment != null) {
                addString(UNIT_COMMENT_LABEL, comment);
            }
            return this;
        }

        private PopupTextBuilder addSynsetComment(String comment) {
            if(comment != null){
                addString(SYNSET_COMMENT_LABEL, comment);
            }
            return this;
        }

        private PopupTextBuilder addOwner(User user){
            if(user != null) {
                addString(OWNER_LABEL, user.getFullname());
            }
            return this;
        }

        private PopupTextBuilder addArtificial(Boolean artificial) {
            if(artificial != null) {
                addString(ARTIFICIAL_LABEL,artificial ? Labels.YES : Labels.NO);
            }
            return this;
        }

        private PopupTextBuilder addStatus(Integer status) {
            if(status != null) {

                addString(STATUS_LABEL, String.valueOf(status)); //TODO zobaczyć jakie wartości ma status
            }
            return this;
        }

        private PopupTextBuilder addDefinition(String definition) {
            if (definition != null) {
                addString(DEFINITION_LABEL, definition);
            }
            return this;
        }

        private PopupTextBuilder addDomain(String domain) {
            return addString(DOMAIN_LABEL, domain);
        }

        private PopupTextBuilder addDomain(Domain domain) {
            Domain normalizedDomain = DomainManager.getInstance().getNormalized(domain);
            String domainText = LocalisationManager.getInstance().getLocalisedString(normalizedDomain.getName()) + "=>" + normalizedDomain.getDescription().toString();
            return addDomain(domainText);
        }

        private PopupTextBuilder addLexicon(Lexicon lexicon) {
            return addLexicon(lexicon.getName());
        }

        private PopupTextBuilder addLexicon(String lexicon) {
            return addString(LEXICON_LABEL, lexicon);
        }

        private PopupTextBuilder addPartOfSpeech(PartOfSpeech partOfSpeech) {
            String partOfSpeechText = LocalisationManager.getInstance().getLocalisedString(partOfSpeech.getName());
            return addString(PART_OF_SPEECH_LABEL, partOfSpeechText);
        }

        private PopupTextBuilder addRegister(Long register) {
            String registerText = RegisterManager.getInstance().getName(register);
            return addString(REGISTER_LABEL, registerText);
        }

        private PopupTextBuilder addExamples(Collection<SenseExample> examples) {
            if (examples != null && !examples.isEmpty()) {
                textBuilder.append(USE_CASE_LABEL);
                for (SenseExample example : examples) {
                    textBuilder.append(String.format(DESC_STR, example.getExample()));
                }
            }
            return this;
        }

        private PopupTextBuilder addSenseRelations(List<SenseRelation> relations) {
            textBuilder.append(getSenseRelationsText(relations));
            return this;
        }

        private String getSenseRelationsText(List<SenseRelation> relations) {
            Map<Long, List<String>> relationsMap = new HashMap<>();
            Long relationTypeNameId;
            for (SenseRelation relation : relations) {
                relationTypeNameId = relation.getRelationType().getName();
                if (!relationsMap.containsKey(relationTypeNameId)) {
                    relationsMap.put(relationTypeNameId, new ArrayList<>());
                }
                relationsMap.get(relationTypeNameId).add(relation.getChild().toString());

            }
            String relationTypeText;
            StringBuilder relationBuilder = new StringBuilder(RELATIONS_HEADER);
            for (Map.Entry<Long, List<String>> entry : relationsMap.entrySet()) {
                relationTypeText = LocalisationManager.getInstance().getLocalisedString(entry.getKey());
                relationBuilder.append(String.format(RELATIONS_TYPE, relationTypeText));
                for (String s : entry.getValue()) {
                    relationBuilder.append(String.format(RELATIONS_ITEM, s));
                }
            }

            return relationBuilder.toString();
        }

        private PopupTextBuilder addString(String label, String value) {
            textBuilder.append(String.format(label, value));
            return this;
        }
    }

    /**
     * konstrukor prywatny
     */
    private ToolTipGenerator() {
        /**
         *
         */
        cache = new TooltipTextCache();
    }

    /**
     * odczytanie instancji generatora
     *
     * @return generator
     */
    static public ToolTipGenerator getGenerator() {
        ToolTipGenerator gen = ToolTipGenerator.generator;
        if (gen == null) {
            synchronized (ToolTipGenerator.class) {
                gen = ToolTipGenerator.generator;
                if (gen == null) {
                    ToolTipGenerator.generator = gen = new ToolTipGenerator();
                }
            }
        }
        return gen;
    }

    /**
     * formatowanie wartosci
     *
     * @param value - wartosc
     * @return sformatowana wartosc
     */
    static private String format(String value) {
        if (value == null || value.equals("")) {
            return "...";
        }
        return value;
    }

    /**
     * odczytanie tooltipu dla jednostki leksykalnej
     *
     * @param object jednostka leksykalna
     * @return hint do wyswietlenia
     */
    private String getToolTipText(Sense object) {
        if (!isEnabled) {
            return null;
        }
        String text = cache.find(object.getId());
        if (text != null) {
            return text;
        }
        Sense sense = RemoteService.senseRemote.fetchSense(object.getId());
        // we must get relations from database, because collections are lazy
        List<SenseRelation> relations = RemoteService.senseRelationRemote.findRelations(sense, null, true, false);
        text = new PopupTextBuilder().createSenseText(sense, relations);
        cache.put(object.getId(), text);
        return text;
    }

    /**
     * odczytanie tooltipu dla synsetu
     *
     * @param object synsetu
     * @return hint do wyswietlenia
     */
    private String getToolTipText(Synset object) {
        if (!isEnabled) {
            return null;
        }

        String text = cache.find(object.getId());
        if(text != null){
            return text;
        }
        Synset synset = RemoteService.synsetRemote.fetchSynset(object.getId()); //TODO można wziąć synset z listy i pobrać tylko atrybuty, chyba będzie działać
        //TODO dorobić relację dla jednostki
        text = new PopupTextBuilder().createSynsetText(synset);
        cache.put(object.getId(), text);
        return text;

//        // odczytanie relacji
//        Collection<SynsetsRelationsDC> relations = SynsetsRelationsDC.dbFastGetRelations(object, SynsetsRelationsDC.IS_PARENT, false);
//        StringBuilder relString = new StringBuilder();
//        if (relations.size() > 0) {
//            relString.append(RELATIONS_HEADER);
//            // stworzenie opisu relacji
//            for (SynsetsRelationsDC synsetsRelations : relations) {
//                relString.append(String.format( // dodanie typu relacji
//                        RELATIONS_TYPE, synsetsRelations.getRelationType().toString()));
//
//                int size = synsetsRelations.getSize();
//                for (int i = 0; i < size; i++) {
//                    // zabezpieczenie przed nullami
//                    SynsetRelation relTmp = synsetsRelations.getRelation(i);
//                    String text = null;
//                    if (relTmp != null && relTmp.getSynsetTo() != null) {
//                        text = relTmp.getSynsetTo().toString();
//                    }
//
//                    // dodanie konkretnej relacji
//                    relString.append(String.format(
//                            RELATIONS_ITEM, text != null ? text : ""));
//                }
//            }
//        }
//        return HTML_HEADER; //+ global + relString.toString() + HTML_FOOTER;
    }

    /**
     * Get tooltip for synset relation
     *
     * @param object - synset relation
     * @return hint to show
     */
    private String getToolTipText(SynsetRelation object) {
        if (!isEnabled) {
            return null;
        }

//        object = RemoteUtils.synsetRelationRemote.dbGet(object.getId());
//
//        String global = String.format(SYNSET_RELATION_TEMPLATE,
//                object, "", "tak", object.getSynsetFrom(), object.getSynsetTo());
        return HTML_HEADER;// + global + HTML_FOOTER;
    }

    /**
     * Get tooltip for lexical relation
     *
     * @param object - lexical relation
     * @return hint to show
     */
    private String getToolTipText(SenseRelation object) {
        if (!isEnabled) {
            return null;
        }

//        object = RemoteUtils.lexicalRelationRemote.dbGet(object.getId());
//
//        String global = String.format(LEXICAL_RELATION_TEMPLATE,
//                object, "", "tak", object.getSenseFrom(), object.getSenseTo());
        return HTML_HEADER;// + global + HTML_FOOTER;
    }

    /**
     * wygenerowanie tooltipu da obiektu
     *
     * @param object - obiekt dla ktorego ma zostać wygenerowany opis
     * @return
     */
    @Override
    public String getToolTipText(Object object) {
        if (!isEnabled) {
            return null;
        }

        try {
            if (object instanceof IObjectWrapper) {
                return getToolTipText(((IObjectWrapper) object).getObject());
            } else if (object instanceof Synset) {
                return getToolTipText((Synset) object);
            } else if (object instanceof Sense) {
                return getToolTipText((Sense) object);
            } else if (object instanceof SynsetRelation) {
                return getToolTipText((SynsetRelation) object);
            } else if (object instanceof SenseRelation) {
                return getToolTipText((SenseRelation) object);
            }
        } catch (Exception e) {
            logger().error("While getting tooltip ", e);
        }
        return "";
    }

    /**
     * wlacza/wylacza pokazywanie rozszerzonych tooltipow
     *
     * @param isEnabled - TRUE wlacza pokazywanie, FALSE wylacza
     */
    public void setEnabledTooltips(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * odczytuje czy pokazywanie podpowiedzi jest aktualnie wlaczone lub
     * wylaczone
     *
     * @return TRUE jesli podpowiedzi sa pokazywane
     */
    @Override
    public boolean hasEnabledTooltips() {
        return isEnabled;
    }

}
