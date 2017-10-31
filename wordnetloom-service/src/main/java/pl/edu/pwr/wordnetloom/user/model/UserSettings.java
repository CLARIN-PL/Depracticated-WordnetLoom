package pl.edu.pwr.wordnetloom.user.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserSettings extends GenericEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "lexicon_marker")
    private final Boolean lexionMarker = true;

    @Column(name = "chosen_lexicons")
    private String chosenLexicons;

    @Column(name = "show_tool_tips")
    private final Boolean showToolTips = true;

    public UserSettings() {
    }

}
