package org.example.largent.Service;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiException;
import org.example.largent.Model.PurchaseHistory;
import org.example.largent.Model.User;
import org.example.largent.Repository.PurchaseHistoryRepository;
import org.example.largent.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository purchaseHistory;
    private final UserRepository userRepository;

    public List<PurchaseHistory> getAllPurchaseHistory() {
        return purchaseHistory.findAll();
    }

    public void addPurchHistory(PurchaseHistory history) {
        purchaseHistory.save(history);
    }

    public void updatePurchHistory(Integer id, PurchaseHistory purchHistory) {
        PurchaseHistory history = purchaseHistory.findPurchaseHistoriesByPurchHistoryId(id);
        if (history == null) {
            throw new ApiException("ID not found");
        }
        User user = userRepository.findUserByUserId(history.getUserId());
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("Please log in to your account");
        }
        history.setCategory(purchHistory.getCategory());
        history.setTotalAmount(purchHistory.getTotalAmount());
        purchaseHistory.save(history);
    }

    public void deletePurchHistory(Integer id) {
        PurchaseHistory history = purchaseHistory.findPurchaseHistoriesByPurchHistoryId(id);
        if (history == null) {
            throw new ApiException("ID cannot be found");
        }
        purchaseHistory.delete(history);
    }

    //                    EXTRA

    public List<PurchaseHistory> getPurchases(Integer userId, String duration) {
        LocalDate startDate;
        User user = userRepository.findUserByUserId(userId);

        if (user == null || !user.getIsLogin()) {
            throw new ApiException("User ID not found or user is not logged in");
        }

        switch (duration) {
            case "1 month":
                startDate = LocalDate.now().withDayOfMonth(1);
                break;
            case "3 months":
                startDate = LocalDate.now().minusMonths(3).withDayOfMonth(1);
                break;
            case "this year":
                startDate = LocalDate.now().withDayOfYear(1);
                break;
            default:
                throw new ApiException("Invalid duration option");
        }

        List<PurchaseHistory> historyByDuration = purchaseHistory.getPurchaseHistoriesByUserIdAndDuration(userId, startDate);

        if (historyByDuration.isEmpty()) {
            throw new ApiException("No purchase history found for the specified duration");
        }

        return historyByDuration;
    }


}
