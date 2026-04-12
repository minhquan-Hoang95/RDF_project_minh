package fr.pir.repository.rdf;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.pir.model.rdf.Campaign;
import fr.pir.model.user.User;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE c.creator = :user OR :user MEMBER OF c.participants")
    List<Campaign> findAllCampaignsByCreatorOrParticipants(@Param("user") User user);

    Optional<Campaign> findByName(String campaignName);

}
