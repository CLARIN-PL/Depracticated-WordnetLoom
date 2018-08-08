package pl.edu.pwr.wordnetloom.lexicon.model;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Audited
@Entity
@Table(name = "lexicon")
public class Lexicon extends GenericEntity {

    private static final long serialVersionUID = -1256292370070216845L;

    @NotNull
    private String name;

    @NotNull
    private String identifier;

    @NotNull
    @Column(name = "language_name")
    private String languageName;

    @Column(name = "language_shortcut")
    private String languageShortcut;

    @Column(name = "lexicon_version")
    private String lexiconVersion;

    private String license;

    @Email
    private String email;

    @Column(name = "reference_url")
    private String referenceUrl;

    @Lob
    private String citation;

    @Column(name = "confidence_score")
    private String confidenceScore;

    private String description;

    public Lexicon() {
    }

    public Lexicon(String name, String identifier, String languageName) {
        this.name = name;
        this.identifier = identifier;
        this.languageName = languageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLexiconVersion() {
        return lexiconVersion;
    }

    public void setLexiconVersion(String lexiconVersion) {
        this.lexiconVersion = lexiconVersion;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLanguageShortcut() {
        return languageShortcut;
    }

    public void setLanguageShortcut(String languageShortcut) {
        this.languageShortcut = languageShortcut;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(String confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Lexicon other = (Lexicon) obj;
        return Objects.equals(id, other.id);
    }


    @Override
    public String toString() {
        return name;
    }


}
