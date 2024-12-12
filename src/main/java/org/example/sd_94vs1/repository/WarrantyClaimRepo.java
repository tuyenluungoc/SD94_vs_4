package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyClaimRepo extends JpaRepository<WarrantyClaim,String> {
    WarrantyClaim findWarrantyClaimByClaimCode(String claimCode);

//    @Query("SELECT wc FROM WarrantyClaim wc WHERE wc.claimCode IN " +
//            "(SELECT s.shoppingCart.warrantyClaim.claimCode FROM Status s WHERE s.user.userCode = :userCode)")
//    List<WarrantyClaim> findClaimsByUserCode(@Param("userCode") String userCode);

}
