package org.example.sd_94vs1.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String name;
    String email;
    String password;
    String confirmPassword;
    String province;
    String district;
    String town;
    String address;
    String sdt;
}
