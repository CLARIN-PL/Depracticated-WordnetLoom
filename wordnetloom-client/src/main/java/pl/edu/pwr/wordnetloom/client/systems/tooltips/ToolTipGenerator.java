package pl.edu.pwr.wordnetloom.client.systems.tooltips;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.*;
import pl.edu.pwr.wordnetloom.client.systems.misc.IObjectWrapper;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * konstrukor prywatny
     */
    private ToolTipGenerator() {
        /**
         *
         */
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

        Sense sense = RemoteService.senseRemote.fetchSense(object.getId());
        String global = getGlobalSenseInfo(sense);
        // odczytywanie relacji
        List<SenseRelation> relations = RemoteService.senseRelationRemote.findRelations(sense,null, true, false);
        String relationsText = getSenseRelationsText(relations);

        //TODO wcześniej było jeszcze ustawianie synsetów
        return HTML_HEADER + global + relationsText + HTML_FOOTER;
    }

    private String getGlobalSenseInfo(Sense sense){
        Domain domain = DomainManager.getInstance().getNormalized(sense.getDomain());
        String register = null;
        String comment = null;

        if(sense.getSenseAttributes() != null){
            register = RegisterManager.getInstance().getName(sense.getSenseAttributes().getRegister());
            comment = sense.getSenseAttributes().getComment();
        }
        StringBuilder exampleBuilder = new StringBuilder();
        for(SenseExample example : sense.getExamples()){
            exampleBuilder.append(String.format(DESC_STR, example.getExample()));
        }
        String global = String.format(LEXICALUNIT_TEMPLATE,
                sense.toString(),
                sense.getLexicon().getName(),
                LocalisationManager.getInstance().getLocalisedString(domain.getName()) + " => " + domain.getDescription().toString(),
                LocalisationManager.getInstance().getLocalisedString(sense.getPartOfSpeech().getName()),
                register == null ? Labels.ND : register,
                format(comment),
                exampleBuilder.length() ==0 ? Labels.ND : exampleBuilder.toString());
        return global;
    }

    private String getSenseRelationsText(List<SenseRelation> relations)
    {
        Map<Long, List<String>> relationsMap = new HashMap<>();
        Long relationTypeNameId;
        for(SenseRelation relation : relations){
            relationTypeNameId = relation.getRelationType().getName();
            if(!relationsMap.containsKey(relationTypeNameId)){
                relationsMap.put(relationTypeNameId, new ArrayList<>());
            }
            relationsMap.get(relationTypeNameId).add(relation.getChild().toString());

        }
        String relationTypeText;
        StringBuilder relationBuilder = new StringBuilder(RELATIONS_HEADER);
        for(Map.Entry<Long, List<String>> entry : relationsMap.entrySet()){
            relationTypeText = LocalisationManager.getInstance().getLocalisedString(entry.getKey());
            relationBuilder.append(String.format(RELATIONS_TYPE, relationTypeText));
            for(String s : entry.getValue()){
                relationBuilder.append(String.format(RELATIONS_ITEM, s));
            }
        }

        return relationBuilder.toString();
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

//        object = RemoteUtils.synsetRemote.dbGet(object.getId());
//        String domain_str = "";
//        Domain domain = RemoteUtils.synsetRemote.dbGetDomain(object, LexiconManager.getInstance().getLexicons());
//        if (domain != null) {
//            domain_str = domain.toString();
//        }
//
//        Iterator<Sense> itr = RemoteUtils.lexicalUnitRemote.dbFullGetUnits(object, 999, LexiconManager.getInstance().getLexicons()).iterator();
//
//        String lu_comment = "brak";
//        if (itr.hasNext()) {
//            lu_comment = Common.getSenseAttribute(itr.next(), Sense.COMMENT);
//        }
//        String global = String.format(SYNSET_TEMPLATE,
//                object.toString(),
//                format(Common.getSynsetAttribute(object, Synset.DEFINITION)),
//                Synset.isAbstract(Common.getSynsetAttribute(object, Synset.ISABSTRACT)) ? String.format(RED_FORMAT, "tak") : "nie",
//                "OK", domain_str, "", format(Common.getSynsetAttribute(object, Synset.COMMENT)), format(lu_comment));
//
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
        return HTML_HEADER; //+ global + relString.toString() + HTML_FOOTER;
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
