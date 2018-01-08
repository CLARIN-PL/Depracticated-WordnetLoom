package pl.edu.pwr.wordnetloom.sense.model;

import org.hibernate.annotations.GenericGenerator;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sense_attributes")
public class SenseAttributes /*extends GenericEntity */ implements Serializable, Cloneable{

    @Id
    @Column(name = "sense_id", unique = true, nullable = false)
    @GenericGenerator(name = "foreigngen", strategy = "foreign", parameters = @org.hibernate.annotations.Parameter(name = "property", value = "sense"))
    @GeneratedValue(generator = "foreigngen")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sense_id")
    @MapsId
    @PrimaryKeyJoinColumn
    private Sense sense;

    @Lob
    private String definition;

    @Lob
    private String comment;

//    private String register;

    private Long register;

    private String link;

    @Column(name = "error_comment")
    private String errorComment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public SenseAttributes() {
        super();
    }

    public SenseAttributes(String definition, String comment, Long register, String link) {
        this.definition = definition;
        this.comment = comment;
        this.register = register;
        this.link = link;
    }

    public SenseAttributes(SenseAttributes sa) {
        definition = sa.definition;
        comment = sa.comment;
        register = sa.register;
        link = sa.link;
        owner = sa.owner;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public String getRegister() {
//        return register;
//    }
//
//    public void setRegister(String register) {
//        this.register = register;
//    }

    public Long getRegister(){
        return register;
    }

    public void setRegister(Long register){
        this.register = register;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Sense getSense() {
        return sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getErrorComment() {
        return errorComment;
    }

    public void setErrorComment(String errorComment) {
        this.errorComment = errorComment;
    }
}
