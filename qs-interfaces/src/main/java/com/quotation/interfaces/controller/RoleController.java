package com.quotation.interfaces.controller;

import com.quotation.application.user.RoleApplicationService;
import com.quotation.application.user.dto.RoleDTO;
import com.quotation.application.user.dto.PermissionDTO;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Resource
    private RoleApplicationService roleApplicationService;

    /**
     * 根据 ID 获取角色
     */
    @GetMapping("/{id}")
    public Result<RoleDTO> getById(@PathVariable Long id) {
        return roleApplicationService.getById(id)
                .map(Result::success)
                .orElse(Result.error("角色不存在"));
    }

    /**
     * 根据编码获取角色
     */
    @GetMapping("/code/{code}")
    public Result<RoleDTO> getByCode(@PathVariable String code) {
        return roleApplicationService.getByCode(code)
                .map(Result::success)
                .orElse(Result.error("角色不存在"));
    }

    /**
     * 获取所有角色
     */
    @GetMapping("/list")
    public Result<List<RoleDTO>> getAll() {
        List<RoleDTO> list = roleApplicationService.getAll();
        return Result.success(list);
    }

    /**
     * 根据用户 ID 获取角色列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<RoleDTO>> getByUserId(@PathVariable Long userId) {
        List<RoleDTO> list = roleApplicationService.getByUserId(userId);
        return Result.success(list);
    }

    /**
     * 保存角色
     */
    @PostMapping
    public Result<RoleDTO> save(@RequestBody RoleDTO dto) {
        // 检查编码是否已存在
        if (dto.getId() == null && roleApplicationService.existsByCode(dto.getCode())) {
            return Result.error("角色编码已存在");
        }
        RoleDTO saved = roleApplicationService.save(dto);
        return Result.success(saved);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        roleApplicationService.deleteById(id);
        return Result.success();
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/{roleId}/permissions")
    public Result<Void> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        roleApplicationService.assignPermissions(roleId, permissionIds);
        return Result.success();
    }

    /**
     * 获取角色的权限 ID 列表
     */
    @GetMapping("/{roleId}/permission-ids")
    public Result<List<Long>> getPermissionIdsByRoleId(@PathVariable Long roleId) {
        List<Long> ids = roleApplicationService.getPermissionIdsByRoleId(roleId);
        return Result.success(ids);
    }

    /**
     * 获取角色的权限详情列表
     */
    @GetMapping("/{roleId}/permissions")
    public Result<List<PermissionDTO>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<PermissionDTO> list = roleApplicationService.getPermissionsByRoleId(roleId);
        return Result.success(list);
    }
}
