package pl.edu.pwr.wordnetloom.common.dto;

import java.io.Serializable;

public class CountInfo implements Serializable {

    private static final long serialVersionUID = 7618071065940106736L;

    private Long synsetID;
    private Long count;

    public CountInfo() {
    }

    public CountInfo(Long synsetID, Long count) {
        this.synsetID = synsetID;
        this.count = count;
    }

    public Long getSynsetID() {
        return synsetID;
    }

    public void setSynsetID(Long synsetID) {
        this.synsetID = synsetID;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
