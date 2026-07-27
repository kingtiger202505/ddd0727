package com.quotation.interfaces.controller;

import com.quotation.application.product.SkuApplicationService;
import com.quotation.application.product.dto.SkuDTO;
import com.quotation.interfaces.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * SKU 管理控制器
 */
@RestController
@RequestMapping("/api/sku")
public class SkuController {

    @Resource
    private SkuApplicationService skuApplicationService;

    /**
     * 根据 ID 获取 SKU
     */
    @GetMapping("/{id}")
    public ApiResponse<SkuDTO> getById(@PathVariable Long id) {
        return skuApplicationService.getById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("SKU 不存在"));
    }

    /**
     * 根据商品 ID 获取 SKU 列表
     */
    @GetMapping("/product/{productId}")
    public ApiResponse<List<SkuDTO>> getByProductId(@PathVariable Long productId) {
        List<SkuDTO> list = skuApplicationService.getByProductId(productId);
        return ApiResponse.success(list);
    }

    /**
     * 根据 SKU 编码获取
     */
    @GetMapping("/code/{skuCode}")
    public ApiResponse<SkuDTO> getBySkuCode(@PathVariable String skuCode) {
        return skuApplicationService.getBySkuCode(skuCode)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("SKU 不存在"));
    }

    /**
     * 获取所有 SKU
     */
    @GetMapping("/list")
    public ApiResponse<List<SkuDTO>> getAll() {
        List<SkuDTO> list = skuApplicationService.getAll();
        return ApiResponse.success(list);
    }

    /**
     * 保存 SKU
     */
    @PostMapping
    public ApiResponse<SkuDTO> save(@RequestBody SkuDTO dto) {
        SkuDTO saved = skuApplicationService.save(dto);
        return ApiResponse.success(saved);
    }

    /**
     * 批量保存 SKU
     */
    @PostMapping("/batch")
    public ApiResponse<List<SkuDTO>> batchSave(@RequestBody List<SkuDTO> dtos) {
        List<SkuDTO> savedList = skuApplicationService.batchSave(dtos);
        return ApiResponse.success(savedList);
    }

    /**
     * 删除 SKU
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable Long id) {
        skuApplicationService.deleteById(id);
        return ApiResponse.success();
    }

    /**
     * 检查 SKU 编码是否存在
     */
    @GetMapping("/exists/{skuCode}")
    public ApiResponse<Boolean> existsBySkuCode(@PathVariable String skuCode) {
        boolean exists = skuApplicationService.existsBySkuCode(skuCode);
        return ApiResponse.success(exists);
    }
}
