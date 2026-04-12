package fr.pir.repository.rdf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.pir.model.rdf.Annotation;
import fr.pir.model.user.User;

public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

    List<Annotation> findAllByCreator(User user);

}
