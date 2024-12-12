package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StatusRepository extends JpaRepository<Status, String> {
    List<Status> findByUser_UserCode(String userCode);

    Status findByStatusCode(String statusCode);
}
