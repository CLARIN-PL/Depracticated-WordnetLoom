package pl.edu.pwr.wordnetloom.common.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;
@TypeDef(defaultForType = UUID.class, typeClass = UUIDType.class)

@MappedSuperclass
public class UuidEntity implements Serializable, Cloneable{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "uuid")
    protected UUID uuid;

    protected Long id;

    public UUID getUuid(){
        return uuid;
    }

    public void setUuid(UUID uuid){
        this.uuid = uuid;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof UuidEntity)) return false;

        UuidEntity that = (UuidEntity)o;
        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
    }

    @Override
    public int hashCode(){
        return uuid != null ? uuid.hashCode() : 0;
    }
}
