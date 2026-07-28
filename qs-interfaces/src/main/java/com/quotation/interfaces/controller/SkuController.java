package com.quotation.interfaces.controller;

import com.quotation.application.product.SkuApplicationService;
import com.quotation.application.product.dto.SkuDTO;
import com.quotation.common.result.Result;
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
    public Result<SkuDTO> getById(@PathVariable Long id) {
        return skuApplicationService.getById(id)
                .map(Result::success)
                .orElse(Result.error("SKU 不存在"));
    }

    /**
     * 根据商品 ID 获取 SKU 列表
     */
    @GetMapping("/product/{productId}")
    public Result<List<SkuDTO>> getByProductId(@PathVariable Long productId) {
        List<SkuDTO> list = skuApplicationService.getByProductId(productId);
        return Result.success(list);
    }

    /**
     * 根据 SKU 编码获取
     */
    @GetMapping("/code/{skuCode}")
    public Result<SkuDTO> getBySkuCode(@PathVariable String skuCode) {
        return skuApplicationService.getBySkuCode(skuCode)
                .map(Result::success)
                .orElse(Result.error("SKU 不存在"));
    }

    /**
     * 获取所有 SKU
     */
    @GetMapping("/list")
    public Result<List<SkuDTO>> getAll() {
        List<SkuDTO> list = skuApplicationService.getAll();
        return Result.success(list);
    }

    /**
     * 保存 SKU
     */
    @PostMapping
    public Result<SkuDTO> save(@RequestBody SkuDTO dto) {
        SkuDTO saved = skuApplicationService.save(dto);
        return Result.success(saved);
    }

    /**
     * 批量保存 SKU
     */
    @PostMapping("/batch")
    public Result<List<SkuDTO>> batchSave(@RequestBody List<SkuDTO> dtos) {
        List<SkuDTO> savedList = skuApplicationService.batchSave(dtos);
        return Result.success(savedList);
    }

    /**
     * 删除 SKU
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        skuApplicationService.deleteById(id);
        return Result.success();
    }

    /**
     * 检查 SKU 编码是否存在
     */
    @GetMapping("/exists/{skuCode}")
    public Result<Boolean> existsBySkuCode(@PathVariable String skuCode) {
        boolean exists = skuApplicationService.existsBySkuCode(skuCode);
        return Result.success(exists);
    }
}
