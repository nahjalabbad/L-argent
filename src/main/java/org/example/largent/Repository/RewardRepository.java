package org.example.largent.Repository;

import org.example.largent.Model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Integer> {
    Reward findRewardByRewardId(Integer rewardId);

    @Query("select r from Reward r where r.pointsRequired <= :points")
    List<Reward> filterByAvailablePoints(int points);
    @Query("select reward from Reward reward where reward.userID=?1 ")
    List<Reward> findUserRewardByRewardId(Integer rewardId);
}
