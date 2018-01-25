package pl.edu.pwr.wordnetloom.tracker.model;

import pl.edu.pwr.wordnetloom.user.model.User;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "audit_event_tracker")
public class Tracker implements Serializable {
    private static final long serialVersionUID = -6737238567613975932L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    @NotNull
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public enum Action {
        CREATE, UPDATE, DELETE
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private Action action;

    @NotNull
    private String element;

    public Tracker() {
        this.createdAt = new Date();
    }

    public Tracker(User user, Action action, String element) {
        this();
        this.user = user;
        this.action = action;
        this.element = element;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tracker other = (Tracker) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Tracker [id=" + id + ", createdAt=" + createdAt + ", user=" + user + ", action=" + action
                + ", element=" + element + "]";
    }

}
