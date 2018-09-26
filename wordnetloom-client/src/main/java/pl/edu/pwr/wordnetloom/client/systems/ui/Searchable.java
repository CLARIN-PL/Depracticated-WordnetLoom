package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.util.Collection;

public interface Searchable<T> {
    public Collection<T> search(String value);
}
