package com.quotation.application.service;

import cn.dev33.satoken.stp.StpUtil;
import com.quotation.application.dto.product.CreateProductCommand;
import com.quotation.application.dto.product.ProductDTO;
import com.quotation.application.dto.product.UpdateProductCommand;
import com.quotation.common.exception.BusinessException;
import com.quotation.domain.product.model.Product;
import com.quotation.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品应用服务
 */
@Service
public class ProductApplicationService {

    @Resource
    private ProductRepository productRepository;

    /**
     * 创建商品
     */
    @Transactional
    public Long createProduct(CreateProductCommand command) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        
        Product product = Product.create(
            currentUserId,
            command.getName(),
            command.getDescription(),
            command.getCategoryId(),
            command.getCategoryName(),
            command.getBasePrice(),
            command.getMinPrice(),
            command.getStockQuantity(),
            command.getUnit()
        );
        product.setImages(command.getImages());
        
        productRepository.save(product);
        return product.getId();
    }

    /**
     * 获取商品详情
     */
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new BusinessException("商品不存在"));
    }

    /**
     * 获取卖家商品列表
     */
    public List<ProductDTO> getSellerProducts() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return productRepository.findBySellerId(currentUserId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 获取所有上架商品
     */
    public List<ProductDTO> getEnabledProducts() {
        return productRepository.findEnabledProducts()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 更新商品
     */
    @Transactional
    public void updateProduct(UpdateProductCommand command) {
        Product product = productRepository.findById(command.getId())
            .orElseThrow(() -> new BusinessException("商品不存在"));
        
        if (command.getName() != null) {
            product.setName(command.getName());
        }
        if (command.getDescription() != null) {
            product.setDescription(command.getDescription());
        }
        if (command.getCategoryId() != null) {
            product.setCategoryId(command.getCategoryId());
        }
        if (command.getCategoryName() != null) {
            product.setCategoryName(command.getCategoryName());
        }
        if (command.getBasePrice() != null) {
            product.setBasePrice(command.getBasePrice());
        }
        if (command.getMinPrice() != null) {
            product.setMinPrice(command.getMinPrice());
        }
        if (command.getStockQuantity() != null) {
            product.setStockQuantity(command.getStockQuantity());
        }
        if (command.getUnit() != null) {
            product.setUnit(command.getUnit());
        }
        if (command.getImages() != null) {
            product.setImages(command.getImages());
        }
        if (command.getStatus() != null) {
            if (command.getStatus() == 1) {
                product.enable();
            } else {
                product.disable();
            }
        }
        
        productRepository.update(product);
    }

    /**
     * 删除商品
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("商品不存在"));
        
        // 验证是否是商品所有者
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!product.getSellerId().equals(currentUserId)) {
            throw new BusinessException("无权删除该商品");
        }
        
        productRepository.delete(id);
    }

    /**
     * 上架商品
     */
    @Transactional
    public void enableProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("商品不存在"));
        
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!product.getSellerId().equals(currentUserId)) {
            throw new BusinessException("无权操作该商品");
        }
        
        product.enable();
        productRepository.update(product);
    }

    /**
     * 下架商品
     */
    @Transactional
    public void disableProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("商品不存在"));
        
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!product.getSellerId().equals(currentUserId)) {
            throw new BusinessException("无权操作该商品");
        }
        
        product.disable();
        productRepository.update(product);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setSellerId(product.getSellerId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategoryId(product.getCategoryId());
        dto.setCategoryName(product.getCategoryName());
        dto.setBasePrice(product.getBasePrice());
        dto.setMinPrice(product.getMinPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setUnit(product.getUnit());
        dto.setImages(product.getImages());
        dto.setStatus(product.getStatus());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
