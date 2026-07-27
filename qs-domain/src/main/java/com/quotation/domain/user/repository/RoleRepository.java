package com.quotation.domain.user.repository;

import com.quotation.domain.user.model.Role;
import java.util.List;
import java.util.Optional;

/**
 * 角色仓储接口
 */
public interface RoleRepository {
    
    Optional<Role> findById(Long id);
    
    Optional<Role> findByCode(String code);
    
    List<Role> findAll();
    
    List<Role> findByUserId(Long userId);
    
    Role save(Role role);
    
    void deleteById(Long id);
    
    boolean existsByCode(String code);
    
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    List<Long> getPermissionIdsByRoleId(Long roleId);
}
