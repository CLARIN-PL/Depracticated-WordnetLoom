package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "yiddish_particles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "dtype",
        discriminatorType = DiscriminatorType.STRING)
public class Particle implements Serializable {

    private static final long serialVersionUID = 2535893050404613251L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    protected Long id;

    private int position;

    @ManyToOne
    @JoinColumn(name = "extension_id", referencedColumnName = "id")
    private YiddishSenseExtension extension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public YiddishSenseExtension getExtension() {
        return extension;
    }

    public void setExtension(YiddishSenseExtension extension) {
        this.extension = extension;
    }


}
