package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringSearchable implements Searchable<String>{
    private Collection<String> items = new ArrayList<>();

    public StringSearchable(Collection<String> items){
        this.items = items;
    }

    public Collection<String> search(String value){
        List<String> founds = new ArrayList<>();

        for(String s: items){
            if(s.indexOf(value) == 0){
                founds.add(s);
            }
        }
        return founds;
    }
}
