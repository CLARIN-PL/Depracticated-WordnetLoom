package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "relation_test")
@NamedQueries({
	@NamedQuery(name = "RelationTest.removeRelationTestsFor",
			query = "DELETE FROM RelationTest r WHERE r.relationType = :relationType"),

	@NamedQuery(name = "RelationTest.removeAllRelationTests",
			query = "DELETE FROM RelationTest r"),
			
	@NamedQuery(name = "RelationTest.getRelationTestsFor",
			query = "SELECT r FROM RelationTest r WHERE r.relationType = :relationType"),
			
	@NamedQuery(name = "RelationTest.getAllRelationTests",
			query = "SELECT r FROM RelationTest r"),
})
public class RelationTest implements Serializable{

	private static final long serialVersionUID = 2710746394598283537L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name = "text", referencedColumnName = "id", nullable = false)
	private Text text;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "relation_type", referencedColumnName = "id", nullable = false)
	private RelationType relationType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pos", referencedColumnName = "id", nullable = false)
	private PartOfSpeech pos;

	@Column(name = "position" , nullable = false, columnDefinition="int default 0")
	private Integer order;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	public PartOfSpeech getPos() {
		return pos;
	}

	public void setPos(PartOfSpeech pos) {
		this.pos = pos;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String toString(){
		return text.toString();
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof RelationTest))
			return false;
		RelationTest e = (RelationTest) o;

		if(id == null) return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode(){
		int hashCode = (id.hashCode());
		if(hashCode == 0)
			return super.hashCode();
		return hashCode;
	}
}
