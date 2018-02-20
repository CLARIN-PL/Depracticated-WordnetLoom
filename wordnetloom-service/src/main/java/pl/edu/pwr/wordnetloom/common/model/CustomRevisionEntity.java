package pl.edu.pwr.wordnetloom.common.model;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import pl.edu.pwr.wordnetloom.common.listener.CustomRevisionEntityListener;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tracker_rev_info")
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity extends DefaultRevisionEntity {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
