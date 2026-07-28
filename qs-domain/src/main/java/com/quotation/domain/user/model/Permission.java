package com.quotation.domain.user.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 权限领域模型
 */
@Data
public class Permission {
    private Long id;
    private String code;
    private String permissionName;
    private String type; // MENU-菜单，BUTTON-按钮，API-接口
    private Long parentId;
    private String path;
    private String icon;
    private Integer sort;
    private String description;
    private Integer status;
    private LocalDateTime createdAt;
}
