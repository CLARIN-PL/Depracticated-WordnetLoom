package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

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
@Table(name = "sense_attribute")
@NamedQueries({
		@NamedQuery(name = "SenseAttribute.getForName", query = "SELECT sa FROM SenseAttribute sa, AttributeType at WHERE at.typeName = :typeName AND at.tableName = 'sense' AND sa.type = at"),
		@NamedQuery(name = "SenseAttribute.getForSense", query = "SELECT sa FROM SenseAttribute sa WHERE sa.sense = :sense"),
		@NamedQuery(name = "SenseAttribute.getSenseAttributeForName", query = "SELECT sa FROM SenseAttribute sa, AttributeType at WHERE "
				+ "at.typeName.text = :typeName AND at.tableName = 'sense' AND sa.type = at AND sa.sense.id = :sense"),
		@NamedQuery(name = "SenseAttribute.deleteBySense", query = "DELETE FROM SenseAttribute sa WHERE sa.sense.id = :senseID"), })
public class SenseAttribute implements Serializable {

	private static final long serialVersionUID = -6738496417082820449L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type", referencedColumnName = "id", nullable = false)
	private AttributeType type;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "value", referencedColumnName = "id", nullable = false)
	private Text value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sense", referencedColumnName = "id", nullable = false)
	private Sense sense;

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public Text getValue() {
		return value;
	}

	public void setValue(Text value) {
		this.value = value;
	}

	public Sense getSense() {
		return sense;
	}

	public void setSense(Sense sense) {
		this.sense = sense;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof SenseAttribute))
			return false;
		SenseAttribute e = (SenseAttribute) o;

		if (id == null)
			return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode() {
		int hashCode = (id.hashCode());
		if (hashCode == 0)
			return super.hashCode();
		return hashCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
