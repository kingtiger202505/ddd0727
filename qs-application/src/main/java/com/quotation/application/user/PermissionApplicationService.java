package com.quotation.application.user;

import com.quotation.domain.user.model.Permission;
import com.quotation.domain.user.repository.PermissionRepository;
import com.quotation.application.user.dto.PermissionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限应用服务
 */
@Service
public class PermissionApplicationService {

    @Resource
    private PermissionRepository permissionRepository;

    /**
     * 根据 ID 获取权限
     */
    public Optional<PermissionDTO> getById(Long id) {
        return permissionRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * 根据编码获取权限
     */
    public Optional<PermissionDTO> getByCode(String code) {
        return permissionRepository.findByCode(code).map(this::convertToDTO);
    }

    /**
     * 获取所有权限
     */
    public List<PermissionDTO> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 根据父级 ID 查询子权限
     */
    public List<PermissionDTO> getByParentId(Long parentId) {
        List<Permission> permissions = permissionRepository.findByParentId(parentId);
        return permissions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 根据用户 ID 查询权限列表
     */
    public List<PermissionDTO> getByUserId(Long userId) {
        List<Permission> permissions = permissionRepository.findByUserId(userId);
        return permissions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 获取菜单树（仅菜单类型）
     */
    public List<PermissionDTO> getMenuTree() {
        List<Permission> permissions = permissionRepository.findMenuTree();
        return permissions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 保存权限
     */
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO save(PermissionDTO dto) {
        Permission permission = convertToEntity(dto);
        List<Permission> savedList = permissionRepository.saveAll(List.of(permission));
        return convertToDTO(savedList.get(0));
    }

    /**
     * 批量保存权限
     */
    @Transactional(rollbackFor = Exception.class)
    public List<PermissionDTO> batchSave(List<PermissionDTO> dtos) {
        List<Permission> permissions = dtos.stream().map(this::convertToEntity).collect(Collectors.toList());
        List<Permission> savedList = permissionRepository.saveAll(permissions);
        return savedList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 删除权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    /**
     * 检查权限编码是否存在
     */
    public boolean existsByCode(String code) {
        return permissionRepository.existsByCode(code);
    }

    /**
     * 实体转 DTO
     */
    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setPermissionName(permission.getPermissionName());
        dto.setType(permission.getType());
        dto.setParentId(permission.getParentId());
        dto.setPath(permission.getPath());
        dto.setIcon(permission.getIcon());
        dto.setSort(permission.getSort());
        dto.setDescription(permission.getDescription());
        dto.setStatus(permission.getStatus());
        dto.setCreatedAt(permission.getCreatedAt());
        return dto;
    }

    /**
     * DTO 转实体
     */
    private Permission convertToEntity(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setId(dto.getId());
        permission.setCode(dto.getCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setType(dto.getType());
        permission.setParentId(dto.getParentId());
        permission.setPath(dto.getPath());
        permission.setIcon(dto.getIcon());
        permission.setSort(dto.getSort());
        permission.setDescription(dto.getDescription());
        permission.setStatus(dto.getStatus());
        permission.setCreatedAt(dto.getCreatedAt());
        return permission;
    }
}
