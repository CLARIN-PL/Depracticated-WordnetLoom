package pl.edu.pwr.wordnetloom.model.tracker;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tracker_unitandsynset")
public class TUnitAndSynset implements Serializable {

    private static final long serialVersionUID = -5043473625336716074L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;
    private Long LEX_ID;
    private Long SYN_ID;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getLEX_ID() {
        return LEX_ID;
    }

    public void setLEX_ID(Long lEX_ID) {
        LEX_ID = lEX_ID;
    }

    public Long getSYN_ID() {
        return SYN_ID;
    }

    public void setSYN_ID(Long sYN_ID) {
        SYN_ID = sYN_ID;
    }
}
