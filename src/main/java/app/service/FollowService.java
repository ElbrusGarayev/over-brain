package app.service;

import app.entity.Follow;
import app.entity.User;
import app.repository.FollowRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class FollowService {

    private final FollowRepo followRepo;

    public void follow(Follow follow){
        followRepo.save(follow);
    }

    public List<User> getAllFollowings(User user){
        return followRepo.findAllByWho(user)
                .stream()
                .map(follow -> follow.getWhom())
                .collect(Collectors.toList());
    }

    public void unfollow(User who, User whom){
        followRepo.deleteByWhoAndWhom(who, whom);
    }
}
