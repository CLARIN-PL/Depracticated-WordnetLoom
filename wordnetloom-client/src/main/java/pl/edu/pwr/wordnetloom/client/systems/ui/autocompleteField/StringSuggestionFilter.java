package pl.edu.pwr.wordnetloom.client.systems.ui.autocompleteField;

import java.util.ArrayList;
import java.util.Collection;

public class StringSuggestionFilter implements SuggestionFilter<String> {

    @Override
    public Collection<String> filter(Collection<String> items, String text) {
        Collection<String> result = new ArrayList<>();
        for (String item : items) {
            if (item.contains(text)){
                result.add(item);
            }
        }
        return result;
    }
}
