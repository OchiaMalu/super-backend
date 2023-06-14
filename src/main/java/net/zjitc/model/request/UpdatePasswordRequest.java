package net.zjitc.model.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String phone;
    private String code;
    private String password;
    private String confirmPassword;
}
