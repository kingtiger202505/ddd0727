package com.quotation.domain.user.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色领域模型
 */
public class Role {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer status; // 0-禁用 1-正常
    private LocalDateTime createdAt;
    private List<Long> permissionIds;

    public Role() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Long> getPermissionIds() { return permissionIds; }
    public void setPermissionIds(List<Long> permissionIds) { this.permissionIds = permissionIds; }
}
