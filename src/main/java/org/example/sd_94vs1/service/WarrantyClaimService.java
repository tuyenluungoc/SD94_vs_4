package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.repository.WarrantyClaimRepo;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WarrantyClaimService {

    @Autowired
    private WarrantyClaimRepo warrantyClaimRepository;
    @Autowired
    private WarrantyRepo warrantyRepository;

    public void createWarrantyClaim(WarrantyClaim warrantyClaim) {
        // Thêm dữ liệu ngày hiện tại vào claimDate
//        warrantyClaim.setWarranty(warrantyClaim.getWarranty());
        warrantyClaim.setClaimDate(new Date());
        warrantyClaimRepository.save(warrantyClaim);
    }

    public boolean isWarrantyCodeValid(String warrantyCode) {
        return warrantyRepository.existsByWarrantyCode(warrantyCode);
    }
}
