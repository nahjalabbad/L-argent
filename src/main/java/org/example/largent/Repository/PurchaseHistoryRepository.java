package org.example.largent.Repository;

import org.example.largent.Model.PurchaseHistory;
import org.example.largent.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory,Integer> {
    PurchaseHistory findPurchaseHistoriesByPurchHistoryId(Integer pHistoryId);


    @Query("select COALESCE(SUM(p.totalAmount), 0) from PurchaseHistory p where p.userId = :userId and p.purchaseDate between :startDate and :endDate")
    double sumTotalByUserAndDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT ph FROM PurchaseHistory ph WHERE ph.userId = ?1 AND ph.purchaseDate >= ?2")
    List<PurchaseHistory> getPurchaseHistoriesByUserIdAndDuration(Integer userId, LocalDate startDate);




}
