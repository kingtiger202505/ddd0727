package com.quotation.interfaces.controller;

import com.quotation.application.dto.product.CreateProductCommand;
import com.quotation.application.dto.product.ProductDTO;
import com.quotation.application.dto.product.UpdateProductCommand;
import com.quotation.application.service.ProductApplicationService;
import com.quotation.interfaces.common.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品管理控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Resource
    private ProductApplicationService productService;

    /**
     * 创建商品
     */
    @PostMapping("/create")
    public R<Long> create(@RequestBody CreateProductCommand command) {
        Long productId = productService.createProduct(command);
        return R.ok(productId);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{id}")
    public R<ProductDTO> detail(@PathVariable Long id) {
        return R.ok(productService.getProductById(id));
    }

    /**
     * 获取卖家商品列表
     */
    @GetMapping("/my-list")
    public R<List<ProductDTO>> myProducts() {
        return R.ok(productService.getSellerProducts());
    }

    /**
     * 获取所有上架商品 (小程序用)
     */
    @GetMapping("/list")
    public R<List<ProductDTO>> list() {
        return R.ok(productService.getEnabledProducts());
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody UpdateProductCommand command) {
        productService.updateProduct(command);
        return R.ok(true);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return R.ok(true);
    }

    /**
     * 上架商品
     */
    @PostMapping("/enable/{id}")
    public R<Boolean> enable(@PathVariable Long id) {
        productService.enableProduct(id);
        return R.ok(true);
    }

    /**
     * 下架商品
     */
    @PostMapping("/disable/{id}")
    public R<Boolean> disable(@PathVariable Long id) {
        productService.disableProduct(id);
        return R.ok(true);
    }
}
