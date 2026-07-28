package com.quotation.application.user;

import com.quotation.domain.user.model.Permission;
import com.quotation.domain.user.model.Role;
import com.quotation.domain.user.repository.PermissionRepository;
import com.quotation.domain.user.repository.RoleRepository;
import com.quotation.application.user.dto.PermissionDTO;
import com.quotation.application.user.dto.RoleDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色应用服务
 */
@Service
public class RoleApplicationService {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private PermissionRepository permissionRepository;

    /**
     * 根据 ID 获取角色
     */
    public Optional<RoleDTO> getById(Long id) {
        return roleRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * 根据编码获取角色
     */
    public Optional<RoleDTO> getByCode(String code) {
        return roleRepository.findByCode(code).map(this::convertToDTO);
    }

    /**
     * 获取所有角色
     */
    public List<RoleDTO> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 根据用户 ID 获取角色列表
     */
    public List<RoleDTO> getByUserId(Long userId) {
        List<Role> roles = roleRepository.findByUserId(userId);
        return roles.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 保存角色
     */
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO save(RoleDTO dto) {
        Role role = convertToEntity(dto);
        Role saved = roleRepository.save(role);
        return convertToDTO(saved);
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * 检查角色编码是否存在
     */
    public boolean existsByCode(String code) {
        return roleRepository.existsByCode(code);
    }

    /**
     * 为角色分配权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        roleRepository.assignPermissions(roleId, permissionIds);
    }

    /**
     * 获取角色的权限 ID 列表
     */
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return roleRepository.getPermissionIdsByRoleId(roleId);
    }

    /**
     * 获取角色的权限详情列表
     */
    public List<PermissionDTO> getPermissionsByRoleId(Long roleId) {
        List<Permission> permissions = permissionRepository.findByRoleId(roleId);
        return permissions.stream().map(this::convertPermissionToDTO).collect(Collectors.toList());
    }

    /**
     * 实体转 DTO
     */
    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setCode(role.getCode());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setStatus(role.getStatus());
        dto.setCreatedAt(role.getCreatedAt());
        return dto;
    }

    /**
     * DTO 转实体
     */
    private Role convertToEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setCode(dto.getCode());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setCreatedAt(dto.getCreatedAt());
        return role;
    }

    /**
     * 权限实体转 DTO
     */
    private PermissionDTO convertPermissionToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setPermissionName(permission.getPermissionName());
        dto.setType(permission.getType());
        dto.setParentId(permission.getParentId());
        dto.setPath(permission.getPath());
        dto.setIcon(permission.getIcon());
        dto.setSort(permission.getSort());
        return dto;
    }
}
