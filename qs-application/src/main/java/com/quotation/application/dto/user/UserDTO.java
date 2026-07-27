package com.quotation.application.dto.user;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息 DTO
 */
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
}
