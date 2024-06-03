package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Follow;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.repositories.FollowRepository;
import com.platform.platformOffreM.services.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

    private FollowRepository followRepository;

    public void subscribe(Talent talent, Entreprise entreprise) {
        Follow follow = new Follow();
        follow.setTalent(talent);
        follow.setEntreprise(entreprise);
        followRepository.save(follow);
    }

    public void unsubscribe(Talent talent, Entreprise entreprise) {
        Follow follow = followRepository.findByTalentAndEntreprise(talent, entreprise);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    public List<Talent> findTalentsByEntreprise(Entreprise entreprise) {
        List<Follow> followers = followRepository.findByEntreprise(entreprise);
        List<Talent> talents = new ArrayList<>();
        for (Follow follower : followers) {
                talents.add(follower.getTalent());
        }
        return talents;
    }

}
