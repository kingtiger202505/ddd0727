package com.quotation.application.user.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 权限 DTO
 */
@Data
public class PermissionDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 权限 ID
     */
    private Long id;
    
    /**
     * 父级权限 ID
     */
    private Long parentId;
    
    /**
     * 权限编码
     */
    private String code;
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限类型：MENU-菜单，BUTTON-按钮，API-接口
     */
    private String type;
    
    /**
     * 路径/URL
     */
    private String path;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
    
    /**
     * 子权限列表
     */
    private List<PermissionDTO> children;
}
