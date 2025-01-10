package org.example.sd_94vs1.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sd_94vs1.model.enums.UserRole;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String userCode = UUID.randomUUID().toString();
    private String name;
    private String email;
    private String password;
//    private String avatar;
    private String sdt;
    private String province;
    private String district;
    private String town;
    private String address;
    private UserRole role;


}