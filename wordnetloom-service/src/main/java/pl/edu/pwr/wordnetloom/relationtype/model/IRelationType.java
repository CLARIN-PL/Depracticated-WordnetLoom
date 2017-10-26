package pl.edu.pwr.wordnetloom.relationtype.model;

import pl.edu.pwr.wordnetloom.common.model.GraphPosition;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.util.Set;


public interface IRelationType {
    String getName(String locale);
    void setName(String locale, String name);
    String getDescription(String locale);
    void setDescription(String locale, String description);
    String getDisplayText(String locale);
    void setDisplayText(String locale, String display);
    String getShortDisplayText(String locale);
    void setShortDisplayText(String locale, String shortDisplay);
    Long getId();
    void setId(Long id);
    Set<Lexicon> getLexicons();
    void setLexicons(Set<Lexicon> lexicons);
    // TODO dodać List<RelationTest>
    Boolean getAutoReverse();
    void setAutoReverse(Boolean autoReverse);
    IRelationType getParent();
    void setParent(IRelationType parent);
    IRelationType getReverse();
    void setReverse(IRelationType reverse);
    GraphPosition getGraphPosition();
    void setGraphPosition(GraphPosition graphPosition);
    String getColor();
    void setColor(String color);
    boolean isMultilingual();
    void setMultilingual(boolean multilingual);
}
