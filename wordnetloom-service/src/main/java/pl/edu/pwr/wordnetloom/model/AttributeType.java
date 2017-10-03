package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

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
@Table(name = "attribute_type")
@NamedQueries({
	@NamedQuery(name="AttributeType.AttributeTypeByName", 
		query="SELECT a from AttributeType a WHERE a.typeName.text =:name"),
	@NamedQuery(name = "AttributeType.findByName",
		query = "SELECT a from AttributeType a WHERE a.tableName = :tableName"),
	@NamedQuery(name = "AttributeType.findByTypeName",
		query = "SELECT a from AttributeType a WHERE a.typeName = :typeName"),
	@NamedQuery(name = "AttributeType.AttributeTypeByNameAndType",
		query = "SELECT a from AttributeType a WHERE a.tableName = :name AND a.typeName.text = :type"),
})
public class AttributeType implements Serializable {

	private static final long serialVersionUID = -7332930371458987445L;

	public static final String COLUMN_DEFINITION_SENSE = "sense";
	public static final String COLUMN_DEFINITION_SYNSET = "synset";
	public static final String COLUMN_DEFINITION_PROPOSED_CONNECTION = "proposedconnatr";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_name", referencedColumnName = "id", nullable = false)
	private Text typeName;

	@Column(name= "table_name", columnDefinition= "enum(" +
			"'"+COLUMN_DEFINITION_SENSE+"'," +
			"'"+COLUMN_DEFINITION_SYNSET+"'," +
			"'"+COLUMN_DEFINITION_PROPOSED_CONNECTION+"'" +
			")", nullable = false)
	private String tableName;

	public Text getTypeName() {
		return typeName;
	}

	public void setTypeName(Text typeName) {
		this.typeName = typeName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof AttributeType))
			return false;
		AttributeType e = (AttributeType) o;
		
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
