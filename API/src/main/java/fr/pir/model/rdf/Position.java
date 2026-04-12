package fr.pir.model.rdf;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class Position {

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

}
