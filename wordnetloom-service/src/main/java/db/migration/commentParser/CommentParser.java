package db.migration.commentParser;

import java.util.ArrayList;
import java.util.List;

public class CommentParser {

    private final int NORMAL_MARKER = 0;
    private final int EXAMPLE_MARKER = 1;
    private final int LINK_MARKER = 2;
    private final int UNKNOWN_MARKER = 3;

    private int currentIndex;
    private int secondIndex;

    public List<ParserResult> parse(String text){
        List<ParserResult> parserResults = new ArrayList<>();
        int markerType = 0;
        int startMarker = 0;
        String marker;
        currentIndex = 0;
        text = text.replace("\n", " ");
        ParserResult parserResult;
        while((currentIndex = getNext(text, currentIndex)) != -1) {
            if(startMarker == 0 && currentIndex != 0) {
                String unmarkedText = text.substring(0, currentIndex);
                parserResults.add(new ParserResult(ResultType.UNKNOWN, unmarkedText));
            }
            startMarker = currentIndex;
            markerType = getMarkerType(text, currentIndex);
            marker = getMarker(text, currentIndex + 1);

            if(marker == null){
                parserResult = serveUnknownMarker(text, currentIndex, startMarker);
                secondIndex = text.length() -1;
            } else {
                switch (markerType) {
                    case NORMAL_MARKER:
                        parserResult = serveNormalMarker(marker, text, currentIndex, startMarker);
                        break;
                    case EXAMPLE_MARKER:
                        parserResult = serveExampleMarker(marker, text);
                        break;
                    case LINK_MARKER:
                        parserResult = serveLinkMarker(marker, text);
                        break;
                    default:
                        parserResult = serveUnknownMarker(text, currentIndex, startMarker);

                }
            }
            if(parserResult != null){
                parserResults.add(parserResult);
            }
            startMarker = secondIndex;
            currentIndex = secondIndex;
        }
        if(secondIndex < text.length() -1){
            String value = text.substring(secondIndex, text.length());
            parserResults.add(new ParserResult(ResultType.UNKNOWN, value));
        }

        return parserResults;
    }

    private ParserResult serveUnknownMarker(String text, int currentIndex, int startMarker) {
        String value;
        value = getUnknown(text, currentIndex, startMarker);
        return new ParserResult(ResultType.UNKNOWN, value);
    }

    private ParserResult serveLinkMarker(final String marker, final String text) {
        String value;
        secondIndex = getIndex("}", text, currentIndex);
        if(secondIndex > currentIndex) {
            value = text.substring(currentIndex, secondIndex).replaceAll("^\\s+", "");
            if(!value.isEmpty() && !value.equals(" ")){
                if(value.startsWith("http") || value.startsWith("www") || value.startsWith("pl")) {
                    secondIndex++;
                    return new ParserResult(ResultType.LINK, value.trim());
                } else {
                    return serveExampleMarker(marker, text);
                }
            } else {
                secondIndex++;
            }
        }
        return null;
    }

    private ParserResult serveExampleMarker(final String marker, final String text) {
        String value = getExample(text, currentIndex);
        if(!value.isEmpty() && !value.equals(" ")){
            return new ParserResult(ResultType.EXAMPLE, value, marker);
        }
        return null;
    }

    private ParserResult serveNormalMarker(String marker, String text, int currentIndex, int startMarker) {
        final String REGISTER = "K";
        final String DEFINITION = "D";
        final String EXAMPLE = "P";

        String value;
        ResultType type;
        if(marker.contains("A") || marker.equals("3")) {
            secondIndex = getIndex("#", text, currentIndex) -1;
            return null;
        } else {
            switch (marker) {
                case REGISTER:
                    value = getRegister(text, currentIndex);
                    type = ResultType.REGISTER;
                    break;
                case DEFINITION:
                    value = getDefinition(text, currentIndex);
                    type = ResultType.DEFINITION;
                    break;
                case EXAMPLE:
                    return serveExampleMarker(marker, text);
                default:
                    value = getUnknown(text, currentIndex, startMarker);
                    type = ResultType.UNKNOWN;
            }
        }
        return new ParserResult(type, value);
    }


    private String getUnknown(final String text, int startIndex, int startMarkerIndex) {
        secondIndex = getNext(text, startIndex);
        if(secondIndex == -1) {
            secondIndex = text.length();
        }
        return text.substring(startMarkerIndex, secondIndex);
    }

    private String getExample(final String text, int startExampleIndex) {
        secondIndex = getIndex("]", text, startExampleIndex);
        if(startExampleIndex == text.length()){
            return "";
        }
        if(text.charAt(startExampleIndex) == ' '){
            startExampleIndex++;
        }
        String value = text.substring(startExampleIndex, secondIndex);
        secondIndex++;
        return value;
    }

    private String getRegister(String text, int startIndex){
        secondIndex = getNext(text, startIndex);
        if(secondIndex == -1){
            secondIndex = text.length();
        }
        String register = text.substring(startIndex, secondIndex).replaceAll("\\s+", "");
        secondIndex--;
        return register;
    }

    private String getDefinition(String text, int startIndex){
        secondIndex = getNext(text, startIndex);
        if(secondIndex == -1){
            secondIndex = text.length();
        }
        if(text.charAt(startIndex) == ' '){
            startIndex++;
        }
        return text.substring(startIndex, secondIndex);
    }

    private int getIndex(String pattern, String text, int startIndex){
        int index = text.indexOf(pattern, startIndex);
        if (index == -1) {
            index = text.length();
        }
        return index;
    }

    private int getNext(String text, int startIndex) {
        int index = startIndex;
        char currentChar = ' ';
        boolean foundFirst = false;
        while (index < text.length()) {
            currentChar = text.charAt(index);

            switch (currentChar) {
                case '#':
                    if (foundFirst) {
                        return index - 1; // zwracamy indeks do początku znacznika, czyli do pierwszego znalezionego znaku
                    }
                case '{':
                case '[':
                case '<':
                    foundFirst = true;
                    break;
                default:
                    foundFirst = false;
            }
            index++;
        }
        return -1;
    }

    private int getMarkerType(String text, int markerPosition) {
        switch (text.charAt(markerPosition)) {
            case '#':
                return NORMAL_MARKER;
            case '[':
                return EXAMPLE_MARKER;
            case '{':
                return LINK_MARKER;
            case '<':
                return UNKNOWN_MARKER;
            default:
                return -1;
        }
    }

    private String getMarker(String text, int startIndex) {
        char[] delimiters = {':', ';', ' ', '>'};
        int index = startIndex;
        StringBuilder markerBuilder = new StringBuilder();
        char currentChar;
        while (index < text.length()) {
            currentChar = text.charAt(index);

            if (currentChar != '#' && currentChar != '[') {
                for (char delimiter : delimiters) {
                    if (delimiter == currentChar) {
                        if (delimiter != ' ' || markerBuilder.length() != 0) { // sprawdzamy czy spacja poprzedza znacznik
                            currentIndex = index + 1;
                            return markerBuilder.toString();
                        }
                    }
                }
                if (currentChar != ' ')
                    markerBuilder.append(currentChar);
            }
            index++;
        }
        return null;
    }


}
