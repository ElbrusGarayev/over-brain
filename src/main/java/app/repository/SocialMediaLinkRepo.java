package app.repository;

import app.entity.SocialMediaLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaLinkRepo extends JpaRepository<SocialMediaLink, Long> {
}
