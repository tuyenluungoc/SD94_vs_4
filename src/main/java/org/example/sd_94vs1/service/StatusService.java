package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.Status;
import org.example.sd_94vs1.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;
    public void updateStatus(String statusCode, String newStatus) {
        Status status = statusRepository.findByStatusCode(statusCode);

        if (status != null) {
            status.setNameStatus(newStatus);
            statusRepository.save(status);  // Cập nhật vào cơ sở dữ liệu
            System.out.println("thành công");
        } else {
            throw new RuntimeException("Trạng thái không tồn tại!");
        }
    }

}
