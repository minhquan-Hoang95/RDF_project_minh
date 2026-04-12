package fr.pir.model.rdf;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class Concept {

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String value;

}
