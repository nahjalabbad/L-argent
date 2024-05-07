package org.example.largent.Repository;

import org.example.largent.Model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Integer> {
    Plan findPlanByPlanId(Integer planId);

    Plan findPlanByTitle(String title);

    List<Plan> getPlanByStatus(String status);
}
