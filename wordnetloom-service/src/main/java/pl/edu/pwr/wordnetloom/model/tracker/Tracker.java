package pl.edu.pwr.wordnetloom.model.tracker;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tracker")
public class Tracker implements Serializable {

    private static final long serialVersionUID = -1857510468064042475L;

    public enum TABLE {
        LEXICALUNIT("lexicalunit"),
        UNITANDSYNSET("unitandsynset"),
        LEXICALRELATION("lexicalrelation"),
        SYNSETRELATION("synsetrelation");

        TABLE(String s) {
            setName(s);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        private String name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date datetime;
    @Column(columnDefinition = "BIT")
    private Integer inserted;
    @Column(columnDefinition = "BIT")
    private Integer deleted;
    private Long data_before_change;
    @Column(name = "`table`")
    private String table;
    private Long tid;
    private String user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getInserted() {
        return inserted;
    }

    public void setInserted(Integer inserted) {
        this.inserted = inserted;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getData_before_change() {
        return data_before_change;
    }

    public void setData_before_change(Long data_before_change) {
        this.data_before_change = data_before_change;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
