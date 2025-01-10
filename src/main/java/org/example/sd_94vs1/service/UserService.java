package org.example.sd_94vs1.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.model.enums.UserRole;
import org.example.sd_94vs1.model.request.UserRequest;
import org.example.sd_94vs1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }
        User user = new User();

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
//        user.setAvatar(userDTO.getAvatar());
        user.setSdt(userDTO.getSdt());
        user.setProvince(userDTO.getProvince());
        user.setDistrict(userDTO.getDistrict());
        user.setTown(userDTO.getTown());
        user.setAddress(userDTO.getAddress());
        if (userDTO.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        return userRepository.save(user);
    }
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    @Transactional
    public void deleteUserByCode(String userCode) {
        try {
            if (userRepository.existsByUserCode(userCode)) {
                userRepository.deleteByUserCode(userCode);
                System.out.println("User đã được xóa: " + userCode);
            } else {
                System.out.println("Không tìm thấy user với mã: " + userCode);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa user: " + e.getMessage());
            throw new RuntimeException("Lỗi khi xóa user");
        }
    }
    public Optional<User> getUserDetailByCode(String userCode) {
        return userRepository.findUserByUserCode(userCode);
    }

    public Optional<User> findByUserCode(String userCode ) {
        return userRepository.findUserByUserCode(userCode);
    }

    public User updateUser(String userCode, User userCu) {

        if (userCu == null) {
            throw new IllegalArgumentException("Thông tin người dùng không hợp lệ.");
        }
        Optional<User> userOpt = userRepository.findUserByUserCode(userCode);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với mã " + userCode);
        }
        User user = userOpt.get();

        user.setName(userCu.getName());
        user.setEmail(userCu.getEmail());
        user.setSdt(userCu.getSdt());
        user.setPassword(userCu.getPassword());
        user.setAddress(userCu.getAddress());
        user.setRole(userCu.getRole());

        return userRepository.save(user);
    }
}
