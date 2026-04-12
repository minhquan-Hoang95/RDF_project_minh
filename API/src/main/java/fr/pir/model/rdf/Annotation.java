package fr.pir.model.rdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import fr.pir.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@Table(name = "annotations", indexes = {
        @Index(name = "idx_annotation_campaign_id", columnList = "campaign_id"),
        @Index(name = "idx_annotation_creator_id", columnList = "creator_id")
})
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String annotationType;

    @Column(nullable = false)
    private String pageUrl;

    @Column(nullable = false)
    private Item item;

    @ElementCollection
    private List<Concept> concepts = new ArrayList<>();

    @Column(nullable = false)
    private Date date = new Date();

    @ManyToOne
    @JsonIncludeProperties({ "id", "name" })
    private Campaign campaign;

    @ManyToOne
    @JsonIncludeProperties({ "email" })
    private User creator;

    @Override
    public String toString() {
        return String.format("Ip(id=%d, description=%s)", this.id, this.description);
    }

}
