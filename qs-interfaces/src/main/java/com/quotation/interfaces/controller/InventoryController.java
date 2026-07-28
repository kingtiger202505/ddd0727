package com.quotation.interfaces.controller;

import com.quotation.application.dto.inventory.CreateInventoryCommand;
import com.quotation.application.dto.inventory.InventoryDTO;
import com.quotation.application.dto.inventory.UpdateInventoryCommand;
import com.quotation.application.service.InventoryApplicationService;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 库存管理控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Resource
    private InventoryApplicationService inventoryService;

    /**
     * 创建库存记录
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody CreateInventoryCommand command) {
        Long inventoryId = inventoryService.createInventory(command);
        return Result.success(inventoryId);
    }

    /**
     * 获取库存详情
     */
    @GetMapping("/detail/{id}")
    public Result<InventoryDTO> detail(@PathVariable Long id) {
        return Result.success(inventoryService.getInventoryById(id));
    }

    /**
     * 根据商品 ID 获取库存
     */
    @GetMapping("/product/{productId}")
    public Result<InventoryDTO> getByProduct(@PathVariable Long productId) {
        return Result.success(inventoryService.getInventoryByProductId(productId));
    }

    /**
     * 获取所有库存
     */
    @GetMapping("/list")
    public Result<List<InventoryDTO>> list() {
        return Result.success(inventoryService.getAllInventory());
    }

    /**
     * 获取低库存预警列表
     */
    @GetMapping({"/low-stock", "/warning"})
    public Result<List<InventoryDTO>> lowStockList() {
        return Result.success(inventoryService.getLowStockItems());
    }

    /**
     * 更新库存
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody UpdateInventoryCommand command) {
        inventoryService.updateInventory(command);
        return Result.success(true);
    }

    /**
     * 增加库存
     */
    @PostMapping("/add/{inventoryId}")
    public Result<Boolean> addStock(@PathVariable Long inventoryId, 
                                     @RequestParam Integer quantity) {
        inventoryService.addStock(inventoryId, quantity);
        return Result.success(true);
    }

    /**
     * 锁定库存
     */
    @PostMapping("/lock/{inventoryId}")
    public Result<Boolean> lockStock(@PathVariable Long inventoryId, 
                                      @RequestParam Integer quantity) {
        inventoryService.lockStock(inventoryId, quantity);
        return Result.success(true);
    }

    /**
     * 释放锁定库存
     */
    @PostMapping("/release/{inventoryId}")
    public Result<Boolean> releaseStock(@PathVariable Long inventoryId, 
                                         @RequestParam Integer quantity) {
        inventoryService.releaseLockedStock(inventoryId, quantity);
        return Result.success(true);
    }

    /**
     * 扣减库存
     */
    @PostMapping("/deduct/{inventoryId}")
    public Result<Boolean> deductStock(@PathVariable Long inventoryId, 
                                        @RequestParam Integer quantity) {
        inventoryService.deductStock(inventoryId, quantity);
        return Result.success(true);
    }
}
