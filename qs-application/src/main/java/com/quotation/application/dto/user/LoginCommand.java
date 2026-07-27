package com.quotation.application.dto.user;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录命令
 */
@Data
public class LoginCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
}
