package org.example.largent.Service;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiException;
import org.example.largent.Model.Reward;
import org.example.largent.Model.User;
import org.example.largent.Repository.RewardRepository;
import org.example.largent.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;

    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    public void addReward(Reward reward) {
        rewardRepository.save(reward);
    }

    public void updateReward(Integer id, Reward reward) {
        Reward existingReward = rewardRepository.findRewardByRewardId(id);
        if (existingReward == null) {
            throw new ApiException("ID not found");
        }
        reward.setRewardId(id);
        rewardRepository.save(reward);
    }

    public void deleteReward(Integer id) {
        Reward reward = rewardRepository.findRewardByRewardId(id);
        if (reward == null) {
            throw new ApiException("ID cannot be found");
        }
        rewardRepository.delete(reward);
    }


    //                  EXTRA

    public void redeemPoints(Integer userId, Integer rewardId) {
        Reward reward = rewardRepository.findRewardByRewardId(rewardId);
        if (reward == null) {
            throw new ApiException("Reward not found");
        }

        User user = userRepository.findUserByUserId(userId);
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("User not found or user not logged in");
        }

        if (user.getPoints() < reward.getPointsRequired()) {
            throw new ApiException("You don't have enough points");
        }

        user.setRewardId(reward.getRewardId());
        user.setPoints(user.getPoints() - reward.getPointsRequired());
        userRepository.save(user);

        LocalDate expireDate = LocalDate.now().plusMonths(3);
        reward.setExpireDate(expireDate);
        reward.setStatus("not expired");
        reward.setUserID(user.getUserId());
        rewardRepository.save(reward);
    }


    public List<Reward> getByExpiration(String status) {
        List<Reward> allRewards = rewardRepository.findAll();
        List<Reward> expirationD = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Reward reward : allRewards) {
            LocalDate expireDate = reward.getExpireDate();

            if (expireDate != null) {
                if (today.isAfter(expireDate)) {
                    reward.setStatus("expired");
                    rewardRepository.save(reward);
                } else {
                    reward.setStatus("not expired");
                    expirationD.add(reward);
                }
            }
        }

        if (expirationD.isEmpty()) {
            throw new ApiException("You have no rewards available");
        }

        return expirationD;
    }


    public List<Reward> filterByPoints(Integer userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("User not found or user not logged in");
        }
        if (rewardRepository.filterByAvailablePoints(user.getPoints()).isEmpty()) {
            throw new ApiException("You have no rewards available to this amount of points "+user.getPoints());
        }
        return rewardRepository.filterByAvailablePoints(user.getPoints());
    }

    public List<Reward> myRewards(Integer userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("User not found or user not logged in");
        }
        if (rewardRepository.filterByAvailablePoints(user.getRewardId()).isEmpty()) {
            throw new ApiException("You have used all your rewards");
        }
        return rewardRepository.findUserRewardByRewardId(userId);
    }
}
