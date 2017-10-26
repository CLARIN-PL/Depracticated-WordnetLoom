package pl.edu.pwr.wordnetloom.common.model;

import javax.persistence.*;
import java.io.Serializable;


@MappedSuperclass
public abstract class GenericEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    protected Long id;

    @Version
    private int version;
    @Column(name = "entity_status")
    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus = EntityStatus.ACTIVE;

    public boolean isPersistent() {
        return id != null;
    }

    public void markAsRemoved() {
        entityStatus = EntityStatus.DELETED;
    }

    public boolean isRemoved() {
        return entityStatus == EntityStatus.DELETED;
    }

    public EntityStatus getEntityStatus() {
        return entityStatus;
    }

    public enum EntityStatus {
        ACTIVE,
        DELETED
    }
}
