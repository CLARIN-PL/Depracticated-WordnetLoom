package pl.edu.pwr.wordnetloom.common.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


@MappedSuperclass
public abstract class GenericEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    protected Long id;

    @Version
    private int version;

    public boolean isPersistent() {
        return id != null;
    }

    @Column(name = "entity_status")
    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus = EntityStatus.ACTIVE;

    public void markAsRemoved() {
         entityStatus = EntityStatus.DELETED;
    }

    public boolean isRemoved() {
        return  entityStatus == EntityStatus.DELETED;
    }

    public EntityStatus getEntityStatus() {
        return  entityStatus;
    }

    public enum EntityStatus {

        ACTIVE,
        DELETED;

    }    
}
