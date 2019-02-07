package pl.edu.pwr.wordnetloom.client.systems.ui.autocompleteField;

import java.util.Collection;

public interface SuggestionFilter<T> {
    Collection<String> filter(Collection<T> items, String text);
}
