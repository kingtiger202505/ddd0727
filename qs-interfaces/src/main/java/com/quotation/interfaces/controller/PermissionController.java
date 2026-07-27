package com.quotation.interfaces.controller;

import com.quotation.application.user.PermissionApplicationService;
import com.quotation.application.user.dto.PermissionDTO;
import com.quotation.interfaces.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Resource
    private PermissionApplicationService permissionApplicationService;

    /**
     * 根据 ID 获取权限
     */
    @GetMapping("/{id}")
    public ApiResponse<PermissionDTO> getById(@PathVariable Long id) {
        return permissionApplicationService.getById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("权限不存在"));
    }

    /**
     * 根据编码获取权限
     */
    @GetMapping("/code/{code}")
    public ApiResponse<PermissionDTO> getByCode(@PathVariable String code) {
        return permissionApplicationService.getByCode(code)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("权限不存在"));
    }

    /**
     * 获取所有权限
     */
    @GetMapping("/list")
    public ApiResponse<List<PermissionDTO>> getAll() {
        List<PermissionDTO> list = permissionApplicationService.getAll();
        return ApiResponse.success(list);
    }

    /**
     * 根据父级 ID 查询子权限
     */
    @GetMapping("/parent/{parentId}")
    public ApiResponse<List<PermissionDTO>> getByParentId(@PathVariable Long parentId) {
        List<PermissionDTO> list = permissionApplicationService.getByParentId(parentId);
        return ApiResponse.success(list);
    }

    /**
     * 根据用户 ID 查询权限列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<PermissionDTO>> getByUserId(@PathVariable Long userId) {
        List<PermissionDTO> list = permissionApplicationService.getByUserId(userId);
        return ApiResponse.success(list);
    }

    /**
     * 获取菜单树（仅菜单类型）
     */
    @GetMapping("/menu-tree")
    public ApiResponse<List<PermissionDTO>> getMenuTree() {
        List<PermissionDTO> list = permissionApplicationService.getMenuTree();
        return ApiResponse.success(list);
    }

    /**
     * 保存权限
     */
    @PostMapping
    public ApiResponse<PermissionDTO> save(@RequestBody PermissionDTO dto) {
        // 检查编码是否已存在
        if (dto.getId() == null && permissionApplicationService.existsByCode(dto.getCode())) {
            return ApiResponse.error("权限编码已存在");
        }
        PermissionDTO saved = permissionApplicationService.save(dto);
        return ApiResponse.success(saved);
    }

    /**
     * 批量保存权限
     */
    @PostMapping("/batch")
    public ApiResponse<List<PermissionDTO>> batchSave(@RequestBody List<PermissionDTO> dtos) {
        List<PermissionDTO> savedList = permissionApplicationService.batchSave(dtos);
        return ApiResponse.success(savedList);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable Long id) {
        permissionApplicationService.deleteById(id);
        return ApiResponse.success();
    }
}
