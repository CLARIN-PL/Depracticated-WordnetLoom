package db.migration.commentParser;

import java.util.ArrayList;
import java.util.List;

public class PrincetonDefinitionParser {

    public List<ParserResult> parse(String text){
        List<ParserResult> results = new ArrayList<>();
        int firstExampleIndex = text.indexOf("; \"");
        String definition;
        int endDefinitionIndex = firstExampleIndex > 0 ? firstExampleIndex : text.length();
        definition = text.substring(0, endDefinitionIndex);
        results.add(new ParserResult(ResultType.DEFINITION, definition));
        if(firstExampleIndex > 0){
            int secondSemicolonIndex;
            String example;
            int firstSemicolonIndex = firstExampleIndex;
            while((secondSemicolonIndex = text.indexOf(";", firstExampleIndex+1))  != -1){
                // subtract 1, because we want delete quotes
//                example = text.substring(firstSemicolonIndex+1, secondSemicolonIndex-1).replaceFirst("\"", "");
                example = text.substring(firstSemicolonIndex+1, secondSemicolonIndex).replaceAll("\"", "");
                results.add(new ParserResult(ResultType.EXAMPLE, example));
                firstExampleIndex = secondSemicolonIndex;
            }
            example = text.substring(firstExampleIndex +1, text.length() -1).replaceAll("\"", "");
            results.add(new ParserResult(ResultType.EXAMPLE, example));
        }
        return results;
    }

}
