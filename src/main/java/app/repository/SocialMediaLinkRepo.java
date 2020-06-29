package app.repository;

import app.entity.SocialMediaLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMediaLinkRepo extends JpaRepository<SocialMediaLink, Long> {
}
