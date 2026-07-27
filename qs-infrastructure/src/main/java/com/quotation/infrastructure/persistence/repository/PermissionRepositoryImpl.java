package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.user.model.Permission;
import com.quotation.domain.user.repository.PermissionRepository;
import com.quotation.infrastructure.persistence.mapper.PermissionMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限仓储实现类
 */
@Repository
public class PermissionRepositoryImpl implements PermissionRepository {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public Optional<Permission> findById(Long id) {
        Permission permission = permissionMapper.selectById(id);
        return Optional.ofNullable(permission);
    }

    @Override
    public Optional<Permission> findByCode(String code) {
        Permission permission = permissionMapper.selectByPermCode(code);
        return Optional.ofNullable(permission);
    }

    @Override
    public List<Permission> findAll() {
        return permissionMapper.selectList(new LambdaQueryWrapper<Permission>().orderByAsc(Permission::getSort));
    }

    @Override
    public List<Permission> findByParentId(Long parentId) {
        return permissionMapper.selectByParentId(parentId);
    }

    @Override
    public List<Permission> saveAll(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return permissions;
        }
        
        for (Permission permission : permissions) {
            if (permission.getId() == null) {
                permissionMapper.insert(permission);
            } else {
                permissionMapper.updateById(permission);
            }
        }
        return permissions;
    }

    @Override
    public void deleteById(Long id) {
        permissionMapper.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        Permission permission = permissionMapper.selectByPermCode(code);
        return permission != null;
    }

    /**
     * 根据用户 ID 查询权限列表
     */
    public List<Permission> findByUserId(Long userId) {
        return permissionMapper.selectByUserId(userId);
    }

    /**
     * 根据角色 ID 查询权限列表
     */
    public List<Permission> findByRoleId(Long roleId) {
        return permissionMapper.selectByRoleId(roleId);
    }

    /**
     * 查询所有菜单树
     */
    public List<Permission> findMenuTree() {
        List<Permission> allPermissions = findAll();
        // 只返回菜单类型
        return allPermissions.stream()
                .filter(p -> p.getType() == 1)
                .collect(Collectors.toList());
    }
}
