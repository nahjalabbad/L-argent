package org.example.largent.Service;

import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiException;
import org.example.largent.Model.TeenInfo;
import org.example.largent.Model.User;
import org.example.largent.Repository.ProductRepository;
import org.example.largent.Repository.PurchaseHistoryRepository;
import org.example.largent.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository users;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public List<User> getAllUsers() {
        return users.findAll();
    }

    public void register(User user) {
        Period period = Period.between(user.getBirthDate(), LocalDate.now());

        if (period.getYears() <= 20 && period.getYears() > 11) {
            user.setRole("teenager");
        }
        if (period.getYears() <= 11 && period.getYears() >= 1) {
            user.setRole("child");
        }

        users.save(user);
    }

    public void updateUser(Integer id, User user) {

        User user1 = users.findUserByUserId(id);
        if (user1 == null) {
            throw new ApiException("ID not found");
        }
        if (user1.getIsLogin() == false) {
            throw new ApiException("user is not logged in");
        }
        if (user1.getIsLogin() == true) {
            user1.setName(user.getName());
            user1.setBudget(user1.getBudget());
            user1.setPassword(user1.getPassword());
            user1.setBalance(user1.getBalance());
            user1.setUsername(user.getUsername());
            users.save(user1);
        }

    }

    public void deleteUser(Integer id) {
        User user = users.findUserByUserId(id);

        if (user == null) {
            throw new ApiException("ID cannot be found");
        }
        users.delete(user);

    }

    //                  EXTRA

    public User logIn(String username, String password) {
        User user = users.logIn(username, password);
        if (user == null) {
            throw new ApiException("username or password are wrong ");
        }
        user.setIsLogin(true);
        users.save(user);
        return user;
    }

    public void connectMember(Integer parentId, Integer teenId) {
        User parent = users.findUserByUserId(parentId);
        User teen = users.findUserByUserId(teenId);

        if (parent == null || teen == null) {
            throw new ApiException("User ID not found");
        }

        if (!parent.getIsLogin()) {
            throw new ApiException("Parent is not logged in");
        }

        if (parentId.equals(teenId)) {
            throw new ApiException("User ID cannot be the same as the second user ID");
        }

        if (!teen.getRole().equalsIgnoreCase("teenager") && !teen.getRole().equalsIgnoreCase("child")) {
            throw new ApiException("The specified user is not set to a teenager or child account");
        }

        if (teen.getParentId() != null && !teen.getParentId().equals(parentId)) {
            throw new ApiException("This member is already connected to another family member");
        }

        if (teen.getParentId() != null) {
            throw new ApiException("This member is already connected");
        }

        parent.setRole("parent");
        parent.setParentId(parent.getUserId());
        parent.setHasTeen(true);

        teen.setHasParent(true);
        teen.setParentId(parent.getUserId());

        users.save(parent);
        users.save(teen);
    }

    public void requestMoney(Integer teenId, Double amount) {
        User teen = users.findUserByUserId(teenId);
        if (teen == null) {
            throw new ApiException("User ID not found");
        }

        String userRole = teen.getRole();
        if (!userRole.equalsIgnoreCase("teenager") && !userRole.equalsIgnoreCase("child")) {
            throw new ApiException("User is not under 20 to request money");
        }

        if (!teen.getHasParent()) {
            throw new ApiException("User must be associated with a parent to request money");
        }

        User parent = users.findUserByUserId(teen.getParentId());
        if (parent == null) {
            throw new ApiException("Associated parent not found");
        }

        double parentBalance = parent.getBalance();
        if (parentBalance < amount) {
            throw new ApiException("Insufficient funds in the parent's account");
        }
        teen.setBalance(teen.getBalance()+amount);
        parent.setBalance(parentBalance - amount);
        users.save(parent);
        users.save(teen);
    }

    public void setWeeklyLimit(Integer teenId, Double weeklyLimit) {
        User teen = users.findUserByUserId(teenId);
        if (teen == null) {
            throw new ApiException("User not found");
        }
        if ((teen.getRole().equalsIgnoreCase("teenager") || teen.getRole().equalsIgnoreCase("child")) && teen.getHasParent()) {
            teen.setWeeklyLimit(weeklyLimit);
            users.save(teen);
        } else {
            throw new ApiException("User account is not set to teenager or child or does not have a parent associated");
        }
    }


    public void setBudget(Integer parentId, Integer teenId, Double budget) {
        User parent = users.findUserByUserId(parentId);
        User teen = users.findUserByUserId(teenId);

        if (parent == null || teen == null) {
            throw new ApiException("Parent or teen user not found");
        }

        double weeklyLimit = budget / 4;

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        double totalSpendingThisWeek = getTotalSpendingForUserInPeriod(teenId, startOfWeek, endOfWeek);

        double currentSpending;
        if (teen.getCurrentSpending() != null) {
            currentSpending = teen.getCurrentSpending();
        } else {
            currentSpending = 0.0;
        }

        if (totalSpendingThisWeek + currentSpending > weeklyLimit) {
            throw new ApiException("Weekly spending limit exceeded");
        }

        teen.setBudget(budget);
        teen.setWeeklyLimit(weeklyLimit);

        users.save(teen);
    }


    private double getTotalSpendingForUserInPeriod(Integer userId, LocalDate startDate, LocalDate endDate) {
        return purchaseHistoryRepository.sumTotalByUserAndDateBetween(userId, startDate, endDate);
    }



    public List<TeenInfo> getParentFamily(Integer parentId) {
        List<User> teens = users.findUserByParentId(parentId);
        List<TeenInfo> familyInfo = new ArrayList<>();

        for (User teen : teens) {
            if (teen.getHasParent() && teen.getParentId().equals(parentId)) {
                TeenInfo teenInfo = new TeenInfo(
                        teen.getName(),
                        teen.getBirthDate(),
                        teen.getBudget(),
                        teen.getWeeklyLimit(),
                        teen.getCurrentSpending()
                );
                familyInfo.add(teenInfo);
            }
        }

        if (familyInfo.isEmpty()) {
            throw new ApiException("No family member connected to your account");
        }

        return familyInfo;
    }
}

