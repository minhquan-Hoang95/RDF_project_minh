package fr.pir.repository.rdf;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.pir.model.rdf.Campaign;
import fr.pir.model.rdf.Invitation;
import fr.pir.model.user.User;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Invitation findInvitationByUserAndCampaign(User user, Campaign campaign);

}
