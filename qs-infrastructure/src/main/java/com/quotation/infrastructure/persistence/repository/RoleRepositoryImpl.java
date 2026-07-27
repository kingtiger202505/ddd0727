package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.user.model.Role;
import com.quotation.domain.user.repository.RoleRepository;
import com.quotation.infrastructure.persistence.mapper.RoleMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 角色仓储实现类
 */
@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Optional<Role> findById(Long id) {
        Role role = roleMapper.selectById(id);
        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> findByCode(String code) {
        Role role = roleMapper.selectByRoleCode(code);
        return Optional.ofNullable(role);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(new LambdaQueryWrapper<Role>().eq(Role::getStatus, 1));
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role save(Role role) {
        if (role.getId() == null) {
            roleMapper.insert(role);
        } else {
            roleMapper.updateById(role);
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        // 先删除角色权限关联
        roleMapper.deleteRolePermissions(id);
        // 再删除角色
        roleMapper.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        Role role = roleMapper.selectByRoleCode(code);
        return role != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除原有权限
        roleMapper.deleteRolePermissions(roleId);
        // 再分配新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.saveRolePermissions(roleId, permissionIds);
        }
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return roleMapper.selectPermissionIdsByRoleId(roleId);
    }
}
