package com.quotation.domain.user.repository;

import com.quotation.domain.user.model.Permission;
import java.util.List;
import java.util.Optional;

/**
 * 权限仓储接口
 */
public interface PermissionRepository {
    
    Optional<Permission> findById(Long id);
    
    Optional<Permission> findByCode(String code);
    
    List<Permission> findAll();
    
    List<Permission> findByParentId(Long parentId);
    
    List<Permission> saveAll(List<Permission> permissions);
    
    void deleteById(Long id);
    
    List<Permission> findByRoleId(Long roleId);
    
    boolean existsByCode(String code);
}
