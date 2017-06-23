package pl.edu.pwr.wordnetloom.tracker;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tracker_synset")
public class TSynset implements Serializable {

    private static final long serialVersionUID = 3030191293044997754L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private Long tid;
    private Integer split;
    private String definition;
    @Column(columnDefinition = "BIT")
    private Integer isabstract;
    private Integer status;
    private String comment;
    private String owner;
    private String unitsstr;

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

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getIsabstract() {
        return isabstract;
    }

    public void setIsabstract(Integer isabstract) {
        this.isabstract = isabstract;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUnitsstr() {
        return unitsstr;
    }

    public void setUnitsstr(String unitsstr) {
        this.unitsstr = unitsstr;
    }
}
