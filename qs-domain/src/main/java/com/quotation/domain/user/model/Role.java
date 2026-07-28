package com.quotation.domain.user.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色领域模型
 */
@Data
public class Role {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer status; // 0-禁用 1-正常
    private LocalDateTime createdAt;
    private List<Long> permissionIds;
}
