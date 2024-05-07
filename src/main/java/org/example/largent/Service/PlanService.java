package org.example.largent.Service;

import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiException;
import org.example.largent.Model.Plan;
import org.example.largent.Model.User;
import org.example.largent.Repository.PlanRepository;
import org.example.largent.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    public List<Plan> getAllPlans(){
        return planRepository.findAll();
    }

    public void addPlan(Plan plan){
        User user = userRepository.findUserByUserId(plan.getUserId());
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("Please log in to your account");
        }
        user.setPlanId(plan.getPlanId());
        plan.setStatus("not started");
        userRepository.save(user);
        planRepository.save(plan);
    }

    public void updatePlan(Integer id, Plan plan){
        Plan existingPlan = planRepository.findPlanByPlanId(id);
        if (existingPlan == null) {
            throw new ApiException("ID not found");
        }
        if (!userRepository.findUserByUserId(existingPlan.getUserId()).getIsLogin()) {
            throw new ApiException("Please log in to your account");
        }
        existingPlan.setAmountToAdd(plan.getAmountToAdd());
        existingPlan.setTitle(plan.getTitle());
        existingPlan.setStatus(plan.getStatus());
        existingPlan.setDescription(plan.getDescription());
        existingPlan.setAmountTarget(plan.getAmountTarget());
        planRepository.save(existingPlan);
    }

    public void deletePlan(Integer id){
        Plan plan = planRepository.findPlanByPlanId(id);
        if (plan == null) {
            throw new ApiException("ID cannot be found");
        }
        planRepository.delete(plan);
    }

    //                  EXTRA

    public void addAmount(String title, Double amount) {
        Plan plan = planRepository.findPlanByTitle(title);
        if (plan == null) {
            throw new ApiException("No plan with this name exists");
        }
        User user = userRepository.findUserByUserId(plan.getUserId());
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("Invalid user or user not logged in");
        }
        if (plan.getAmountTarget() != null && plan.getAmountTarget() >= amount) {
            Double userNewBalance = user.getBalance() - amount;
            user.setBalance(userNewBalance);
            userRepository.save(user);
            plan.setAmountToAdd(amount);
        }
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(plan.getEndingDate()) || (plan.getAmountToAdd() != null && plan.getAmountTarget() != null && plan.getAmountToAdd().equals(plan.getAmountTarget()))) {
            plan.setStatus("done");
        } else if (plan.getAmountTarget() != null && plan.getAmountTarget() > 0) {
            plan.setStatus("in progress");
        } else {
            plan.setStatus("Not started");
        }
        planRepository.save(plan);
    }

    public List<Plan> filterByStatus(String status){
        List<Plan> getByStatus = planRepository.getPlanByStatus(status);
        if (getByStatus.isEmpty()){
            throw new ApiException("You have no plans with status " + status);
        }
        return getByStatus;
    }
}

