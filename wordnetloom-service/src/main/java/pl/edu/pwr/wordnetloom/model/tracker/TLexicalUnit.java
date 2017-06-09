package pl.edu.pwr.wordnetloom.model.tracker;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tracker_lexicalunit")
public class TLexicalUnit implements Serializable {

    private static final long serialVersionUID = -1857510468064042475L;

    private Long ID;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;
    private String lemma;
    private Integer domain;
    private Integer pos;
    private Integer tagcount;
    private Integer source;
    private Integer status;
    private String comment;
    private Integer variant;
    private String owner;
    private Integer project;

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Integer getDomain() {
        return domain;
    }

    public void setDomain(Integer domain) {
        this.domain = domain;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Integer getTagcount() {
        return tagcount;
    }

    public void setTagcount(Integer tagcount) {
        this.tagcount = tagcount;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getVariant() {
        return variant;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }
}
