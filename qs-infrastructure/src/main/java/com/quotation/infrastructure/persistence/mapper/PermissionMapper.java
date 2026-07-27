package com.quotation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quotation.domain.user.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限 Mapper 接口
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据角色 ID 查询权限列表
     */
    List<Permission> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户 ID 查询权限列表
     */
    List<Permission> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询子权限
     */
    List<Permission> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据权限编码查询
     */
    Permission selectByPermCode(@Param("permCode") String permCode);
}
