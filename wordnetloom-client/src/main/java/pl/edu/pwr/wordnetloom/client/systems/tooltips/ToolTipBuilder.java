package pl.edu.pwr.wordnetloom.client.systems.tooltips;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SenseFormat;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SynsetFormat;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RegisterManager;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.StatusDictionary;
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

public class ToolTipBuilder {

    private final String HTML_HEADER = "<html><body style=\"font-weight:normal\">";
    private final String HTML_FOOTER = "</body></html>";
    private final String HTML_SPA = "&nbsp;";
    private final String HTML_BR = "<br>";
    private final String HTML_TAB = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private final String DESC_STR = "<div style=\"text-align:left; margin-left:10px; width:250px;\">%s</div>";

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

    private final String RELATIONS_TYPE = HTML_TAB + "<b>%s:</b>" + HTML_BR;
    private final String RELATIONS_ITEM = HTML_TAB + HTML_TAB + "%s" + HTML_BR;

    private StringBuilder textBuilder = new StringBuilder();

    public String buildText() {
        return HTML_HEADER + textBuilder.toString() + HTML_FOOTER;
    }

    public void clear() {
        textBuilder.setLength(0);
    }

    public ToolTipBuilder addSenseComment(String comment) {
        if(comment != null) {
            String finalComment = comment.replaceAll("<", "&lt");
            addString(UNIT_COMMENT_LABEL, finalComment);
        }
        return this;
    }

    public ToolTipBuilder addSynsetComment(String comment) {
        if(comment != null){
            String finalComment = comment.replaceAll("<", "&lt");
            addString(SYNSET_COMMENT_LABEL, finalComment);
        }
        return this;
    }

    public ToolTipBuilder addOwner(User user){
        if(user != null) {
            addString(OWNER_LABEL, user.getFullname());
        }
        return this;
    }

    public ToolTipBuilder addArtificial(Boolean artificial) {
        if(artificial != null) {
            addString(ARTIFICIAL_LABEL,artificial ? Labels.YES : Labels.NO);
        }
        return this;
    }

    public ToolTipBuilder addStatus(StatusDictionary status) {
        if(status != null) {
            String statusText = LocalisationManager.getInstance().getLocalisedString(status.getName());
            addString(STATUS_LABEL, String.valueOf(statusText));
        }
        return this;
    }


    public ToolTipBuilder addDefinition(String definition) {
        if (definition != null) {
            addString(DEFINITION_LABEL, definition);
        }
        return this;
    }

    public ToolTipBuilder addDomain(String domain) {
        return addString(DOMAIN_LABEL, domain);
    }

    public ToolTipBuilder addDomain(Domain domain) {
        Domain normalizedDomain = DomainManager.getInstance().getNormalized(domain);
        String domainText = LocalisationManager.getInstance().getLocalisedString(normalizedDomain.getName()) + "=>" + normalizedDomain.getDescription().toString();
        return addDomain(domainText);
    }

    public ToolTipBuilder addLexicon(Lexicon lexicon) {
        return addLexicon(lexicon.getName());
    }

    public ToolTipBuilder addLexicon(String lexicon) {
        return addString(LEXICON_LABEL, lexicon);
    }

    public ToolTipBuilder addPartOfSpeech(PartOfSpeech partOfSpeech) {
        String partOfSpeechText = LocalisationManager.getInstance().getLocalisedString(partOfSpeech.getName());
        return addString(PART_OF_SPEECH_LABEL, partOfSpeechText);
    }

    public ToolTipBuilder addRegister(Long register) {
        String registerText = RegisterManager.getInstance().getName(register);
        return addString(REGISTER_LABEL, registerText);
    }

    public ToolTipBuilder addExamples(Collection<SenseExample> examples) {
        if (examples != null && !examples.isEmpty()) {
            textBuilder.append(USE_CASE_LABEL);
            for (SenseExample example : examples) {
                textBuilder.append(String.format(DESC_STR, example.getExample()));
            }
        }
        return this;
    }

    public ToolTipBuilder addSenseRelations(List<SenseRelation> relations) {
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
            relationsMap.get(relationTypeNameId).add(SenseFormat.getText(relation.getChild()));

        }
        return buildRelationText(relationsMap);
    }

    private String buildRelationText(Map<Long, List<String>> relationsMap) {
        String relationTypeText;
        StringBuilder relationBuilder = new StringBuilder(RELATIONS_HEADER);
        for(Map.Entry<Long, List<String>> entry : relationsMap.entrySet()) {
            relationTypeText = LocalisationManager.getInstance().getLocalisedString(entry.getKey());
            relationBuilder.append(String.format(RELATIONS_TYPE, relationTypeText));
            for(String s : entry.getValue()){
                relationBuilder.append(String.format(RELATIONS_ITEM, s));
            }
        }
        return relationBuilder.toString();
    }

    public ToolTipBuilder addSynsetRelations(List<SynsetRelation> relations) {
        textBuilder.append(getSynsetRelationText(relations));
        return this;
    }

    public String getSynsetRelationText(List<SynsetRelation> relations) {
        Map<Long, List<String>> relationsMap = new HashMap<>();
        Long relationTypeNameId;

        for(SynsetRelation relation : relations) {
            relationTypeNameId = relation.getRelationType().getName();
            if(!relationsMap.containsKey(relationTypeNameId)) {
                relationsMap.put(relationTypeNameId, new ArrayList<>());
            }
            relationsMap.get(relationTypeNameId).add(SynsetFormat.getText(relation.getChild()));
        }

        return buildRelationText(relationsMap);
    }

    public ToolTipBuilder addString(String label, String value) {
        textBuilder.append(String.format(label, value));
        return this;
    }
}
