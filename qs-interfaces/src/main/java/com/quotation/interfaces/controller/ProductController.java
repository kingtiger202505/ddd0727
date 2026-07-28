package com.quotation.interfaces.controller;

import com.quotation.application.dto.product.CreateProductCommand;
import com.quotation.application.dto.product.ProductDTO;
import com.quotation.application.dto.product.UpdateProductCommand;
import com.quotation.application.service.ProductApplicationService;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品管理控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping({"/api/products", "/api/product"})
public class ProductController {

    @Resource
    private ProductApplicationService productService;

    /**
     * 创建商品
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody CreateProductCommand command) {
        Long productId = productService.createProduct(command);
        return Result.success(productId);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{id}")
    public Result<ProductDTO> detail(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    /**
     * 获取卖家商品列表
     */
    @GetMapping("/my-list")
    public Result<List<ProductDTO>> myProducts() {
        return Result.success(productService.getSellerProducts());
    }

    /**
     * 获取所有上架商品 (小程序用)
     */
    @GetMapping("/list")
    public Result<List<ProductDTO>> list() {
        return Result.success(productService.getEnabledProducts());
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody UpdateProductCommand command) {
        productService.updateProduct(command);
        return Result.success(true);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success(true);
    }

    /**
     * 上架商品
     */
    @PostMapping("/enable/{id}")
    public Result<Boolean> enable(@PathVariable Long id) {
        productService.enableProduct(id);
        return Result.success(true);
    }

    /**
     * 下架商品
     */
    @PostMapping("/disable/{id}")
    public Result<Boolean> disable(@PathVariable Long id) {
        productService.disableProduct(id);
        return Result.success(true);
    }
}
