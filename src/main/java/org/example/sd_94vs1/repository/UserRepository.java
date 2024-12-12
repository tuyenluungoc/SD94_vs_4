package org.example.sd_94vs1.repository;



import jakarta.transaction.Transactional;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRole(UserRole userRole);

    List<User> findByRoleTrue();

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    List<User> findByUserCode(String userCode);
    void deleteByUserCode(String userCode);
    boolean existsByUserCode(String userCode);
    Optional<User> findUserByUserCode(String userCode);
    // Tìm mã OTP theo email


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.otp = :otp WHERE u.email = :email")
    void updateOtp(@Param("email") String email, @Param("otp") String otp);




}