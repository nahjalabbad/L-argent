package org.example.largent.Repository;

import org.example.largent.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Integer> {
    Transfer findTransferByTransferId(Integer transferId);
}
