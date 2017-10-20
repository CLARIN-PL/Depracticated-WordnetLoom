package pl.edu.pwr.wordnetloom.common.dto;

public class StringMapEntry {

    private String key;
    private String value;

    public StringMapEntry(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void setValue(String value){
        this.value = value;
    }
}
