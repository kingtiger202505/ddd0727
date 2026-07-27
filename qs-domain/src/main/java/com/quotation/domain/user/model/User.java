package com.quotation.domain.user.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("nickname")
    private String nickname;
    
    @TableField("phone")
    private String phone;
    
    @TableField("email")
    private String email;
    
    @TableField("role")
    private String role; // BUYER, SELLER, ADMIN
    
    @TableField("status")
    private Integer status; // 0-禁用，1-正常
    
    @Version
    @TableField("version")
    private Integer version;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 创建买家用户
     */
    public static User createBuyer(String username, String password, String nickname, String phone, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole("BUYER");
        user.setStatus(1);
        user.setVersion(1);
        return user;
    }
    
    /**
     * 创建卖家用户
     */
    public static User createSeller(String username, String password, String nickname, String phone, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole("SELLER");
        user.setStatus(1);
        user.setVersion(1);
        return user;
    }
}
