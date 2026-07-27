package com.quotation.application.dto.user;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户注册命令
 */
@Data
public class RegisterCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String role; // BUYER, SELLER
}
