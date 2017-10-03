package pl.edu.pwr.wordnetloom.model.tracker;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jboss.resteasy.annotations.cache.Cache;

@Entity
@Cache(noStore = true)
@Table(name = "tracker_synsetrelation")
public class TSynsetRelation implements Serializable {

	private static final long serialVersionUID = -6429128969151675338L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tid;
	private Long PARENT_ID;
	private Long CHILD_ID;
	private Long REL_ID;
	private Integer valid;
	private String owner;
	
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Long getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(Long pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}
	public Long getCHILD_ID() {
		return CHILD_ID;
	}
	public void setCHILD_ID(Long cHILD_ID) {
		CHILD_ID = cHILD_ID;
	}
	public Long getREL_ID() {
		return REL_ID;
	}
	public void setREL_ID(Long rEL_ID) {
		REL_ID = rEL_ID;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
