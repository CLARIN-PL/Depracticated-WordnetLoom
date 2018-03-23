package pl.edu.pwr.wordnetloom.sense.dto;

public class SenseJson {

    private long id;

    private String word;

    private int variant;

    private long partOfSpeech;

    private long domain;

    private String lexicon;

    public SenseJson() {
    }

    public SenseJson(long id, String word, int variant, long domain, String lexicon) {
        this.id = id;
        this.word = word;
        this.variant = variant;
        this.domain = domain;
        this.lexicon = lexicon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public long getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(long partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public long getDomain() {
        return domain;
    }

    public void setDomain(long domain) {
        this.domain = domain;
    }

    public String getLexicon() {
        return lexicon;
    }

    public void setLexicon(String lexicon) {
        this.lexicon = lexicon;
    }
}
