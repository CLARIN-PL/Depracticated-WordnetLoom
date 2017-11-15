package pl.edu.pwr.wordnetloom.user.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.*;

@Entity
@Table(name = "users_settings")
public class UserSettings extends GenericEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "lexicon_marker")
    private Boolean lexionMarker = true;

    @Column(name = "chosen_lexicons")
    private String chosenLexicons;

    @Column(name = "show_tool_tips")
    private Boolean showToolTips = true;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getLexionMarker() {
        return lexionMarker;
    }

    public String getChosenLexicons() {
        return chosenLexicons;
    }

    public void setChosenLexicons(String chosenLexicons) {
        this.chosenLexicons = chosenLexicons;
    }

    public Boolean getShowToolTips() {
        return showToolTips;
    }
}
