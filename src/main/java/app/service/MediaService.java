package app.service;

import app.entity.SocialMediaLink;
import app.repository.SocialMediaLinkRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MediaService {

    private final SocialMediaLinkRepo linkRepo;

    public void save(SocialMediaLink links){
        linkRepo.save(links);
    }
}
