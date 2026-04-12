package fr.pir.model.rdf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import fr.pir.model.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "invitations", indexes = {
        @Index(name = "idx_invitation_campaign_id", columnList = "campaign_id"),
        @Index(name = "idx_invitation_user_id", columnList = "user_id")
})
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Campaign campaign;

    @ManyToOne
    @JsonIncludeProperties({ "email" })
    private User user;

    @Override
    public String toString() {
        return String.format("Ip(id=%d)", this.id);
    }

    public Invitation(Campaign campaign, User user) {
        this.campaign = campaign;
        this.user = user;
    }

}
