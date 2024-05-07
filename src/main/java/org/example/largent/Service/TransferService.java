package org.example.largent.Service;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiException;
import org.example.largent.Model.Transfer;
import org.example.largent.Model.User;
import org.example.largent.Repository.TransferRepository;
import org.example.largent.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transfers;
    private final UserRepository users;

    public List<Transfer> getTransfers(){
        return transfers.findAll();
    }

    public void addTransfer(Transfer transfer) {
        User user = users.findUserByUserId(transfer.getUserId());
        User teen = users.findUserByUserId(transfer.getTeenId());
        if (user == null || teen == null || !user.getIsLogin()) {
            throw new ApiException("Invalid user or teen, or user not logged in");
        }
        if (!user.getHasTeen() || !teen.getHasParent()) {
            throw new ApiException("Unmatched family members");
        }

        double newUserBalance = user.getBalance() - transfer.getAmount();
        double newTeenBalance = teen.getBalance() + transfer.getAmount();

        user.setBalance(newUserBalance);
        teen.setBalance(newTeenBalance);

        transfer.setTransferTime(LocalDateTime.now());

        users.save(user);
        users.save(teen);
        transfers.save(transfer);
    }

    public void updateTransfer(Integer id, Transfer transfer){
        Transfer existingTransfer = transfers.findTransferByTransferId(id);
        if (existingTransfer == null) {
            throw new ApiException("ID not found");
        }
        existingTransfer.setAmount(transfer.getAmount());
        existingTransfer.setPurpose(transfer.getPurpose());
        transfers.save(existingTransfer);
    }

    public void deleteTransfer(Integer id){
        Transfer transfer = transfers.findTransferByTransferId(id);
        if (transfer == null){
            throw new ApiException("ID cannot be found");
        }
        transfers.delete(transfer);
    }

    //                  EXTRA

    public void transferCurrency(Integer transferId, String currency, Double amount) {
        Transfer transfer = transfers.findTransferByTransferId(transferId);
        if (transfer == null) {
            throw new ApiException("Transfer not found");
        }

        User user = users.findUserByUserId(transfer.getUserId());
        User teen = users.findUserByUserId(transfer.getTeenId());

        if (user == null || teen == null || !user.getIsLogin()) {
            throw new ApiException("Invalid user or teen, or user not logged in");
        }

        if (!user.getHasTeen()) {
            throw new ApiException("User does not have a teenager or child connected as family member");
        }

        double convertedAmount = getConvertedAmount(currency, user, transfer, amount);
        if (convertedAmount > user.getBalance()) {
            throw new ApiException("Insufficient balance");
        }

        double newUserBalance = user.getBalance() - convertedAmount;
        double newTeenBalance = teen.getBalance() + convertedAmount;

        user.setBalance(newUserBalance);
        teen.setBalance(newTeenBalance);

        transfer.setTransferTime(LocalDateTime.now());
        transfer.setCurrency(currency);

        users.save(user);
        users.save(teen);
        transfers.save(transfer);
    }

    private static double getConvertedAmount(String currency, User user, Transfer transfer, Double amount) {
        double conversionRate;
        if (currency.equalsIgnoreCase("SAR")){
            conversionRate=1;
        } else if (currency.equalsIgnoreCase("EUR")) {
            conversionRate = 4.05;
        } else if (currency.equalsIgnoreCase("USD")) {
            conversionRate = 3.75;
        } else {
            throw new ApiException("Unsupported currency");
        }

        return amount * conversionRate;
    }
}

