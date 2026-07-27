package com.quotation.interfaces.controller;

import com.quotation.application.dto.inventory.CreateInventoryCommand;
import com.quotation.application.dto.inventory.InventoryDTO;
import com.quotation.application.dto.inventory.UpdateInventoryCommand;
import com.quotation.application.service.InventoryApplicationService;
import com.quotation.interfaces.common.R;
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
    public R<Long> create(@RequestBody CreateInventoryCommand command) {
        Long inventoryId = inventoryService.createInventory(command);
        return R.ok(inventoryId);
    }

    /**
     * 获取库存详情
     */
    @GetMapping("/detail/{id}")
    public R<InventoryDTO> detail(@PathVariable Long id) {
        return R.ok(inventoryService.getInventoryById(id));
    }

    /**
     * 根据商品 ID 获取库存
     */
    @GetMapping("/product/{productId}")
    public R<InventoryDTO> getByProduct(@PathVariable Long productId) {
        return R.ok(inventoryService.getInventoryByProductId(productId));
    }

    /**
     * 获取所有库存
     */
    @GetMapping("/list")
    public R<List<InventoryDTO>> list() {
        return R.ok(inventoryService.getAllInventory());
    }

    /**
     * 获取低库存预警列表
     */
    @GetMapping("/low-stock")
    public R<List<InventoryDTO>> lowStockList() {
        return R.ok(inventoryService.getLowStockItems());
    }

    /**
     * 更新库存
     */
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody UpdateInventoryCommand command) {
        inventoryService.updateInventory(command);
        return R.ok(true);
    }

    /**
     * 增加库存
     */
    @PostMapping("/add/{inventoryId}")
    public R<Boolean> addStock(@PathVariable Long inventoryId, 
                                     @RequestParam Integer quantity) {
        inventoryService.addStock(inventoryId, quantity);
        return R.ok(true);
    }

    /**
     * 锁定库存
     */
    @PostMapping("/lock/{inventoryId}")
    public R<Boolean> lockStock(@PathVariable Long inventoryId, 
                                      @RequestParam Integer quantity) {
        inventoryService.lockStock(inventoryId, quantity);
        return R.ok(true);
    }

    /**
     * 释放锁定库存
     */
    @PostMapping("/release/{inventoryId}")
    public R<Boolean> releaseStock(@PathVariable Long inventoryId, 
                                         @RequestParam Integer quantity) {
        inventoryService.releaseLockedStock(inventoryId, quantity);
        return R.ok(true);
    }

    /**
     * 扣减库存
     */
    @PostMapping("/deduct/{inventoryId}")
    public R<Boolean> deductStock(@PathVariable Long inventoryId, 
                                        @RequestParam Integer quantity) {
        inventoryService.deductStock(inventoryId, quantity);
        return R.ok(true);
    }
}
