package fr.pir.model.rdf;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import fr.pir.model.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "campaigns", indexes = {
    @Index(name = "idx_campaign_name", columnList = "name"),
    @Index(name = "idx_campaign_creator_id", columnList = "creator_id")
})
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private List<String> selectedThesauri;

    @Column(nullable = false)
    private Date date = new Date();

    @ManyToOne
    @JsonIncludeProperties({"email"})
    private User creator;

    @JsonIncludeProperties({"email"})
    @ManyToMany(mappedBy = "campaignsParticipated", fetch = FetchType.LAZY)
    private Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    private Set<Invitation> invitations = new HashSet<>();

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    private Set<Annotation> annotations = new HashSet<>();

    @Override
    public String toString() {
        return String.format("Campaign(id=%d, name=%s)", id, name);
    }

}
