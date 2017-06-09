package pl.edu.pwr.wordnetloom.model.dto;

import java.io.Serializable;

public class LightweightSense implements Serializable {

    private static final long serialVersionUID = -4507839378721535170L;

    private Long id;
    private String text;

    public LightweightSense() {
    }

    public LightweightSense(Long id, String text) {
        setId(id).setText(text);
    }

    public Long getId() {
        return id;
    }

    public LightweightSense setId(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public LightweightSense setText(String text) {
        this.text = text;
        return this;
    }
}
