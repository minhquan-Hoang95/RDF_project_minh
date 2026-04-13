package fr.pir.model.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.pir.model.rdf.Annotation;
import fr.pir.model.rdf.Campaign;
import fr.pir.model.rdf.Invitation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_role_id", columnList = "role_id"),
        @Index(name = "idx_user_activate", columnList = "activate")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 8, max = 255, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password = "***";

    @Column(nullable = false)
    private boolean activate = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Campaign> campaigns = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Invitation> invitations = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Annotation> annotations = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "campaigns_participants", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "campaign_id"))
    private Set<Campaign> campaignsParticipated = new HashSet<>();

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public void setPasswordWithoutBCrypt(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User(id=%d, firstName=%s, lastName=%s, email=%s)", id, firstName, lastName, email);
    }

}
