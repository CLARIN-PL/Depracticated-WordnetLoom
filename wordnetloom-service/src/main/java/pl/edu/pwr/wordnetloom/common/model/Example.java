package pl.edu.pwr.wordnetloom.common.model;

public interface Example {
    String getExample();
    String getType();
    void setExample(String example);
    void setType(String type);

    Example copy();
}
