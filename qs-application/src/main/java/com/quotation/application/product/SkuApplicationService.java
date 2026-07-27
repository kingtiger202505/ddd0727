package com.quotation.application.product;

import com.quotation.domain.product.model.Sku;
import com.quotation.domain.product.repository.SkuRepository;
import com.quotation.application.product.dto.SkuDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SKU 应用服务
 */
@Service
public class SkuApplicationService {

    @Resource
    private SkuRepository skuRepository;

    /**
     * 根据 ID 获取 SKU
     */
    public Optional<SkuDTO> getById(Long id) {
        return skuRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * 根据商品 ID 获取 SKU 列表
     */
    public List<SkuDTO> getByProductId(Long productId) {
        List<Sku> skus = skuRepository.findByProductId(productId);
        return skus.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 根据 SKU 编码获取
     */
    public Optional<SkuDTO> getBySkuCode(String skuCode) {
        return skuRepository.findBySkuCode(skuCode).map(this::convertToDTO);
    }

    /**
     * 获取所有 SKU
     */
    public List<SkuDTO> getAll() {
        List<Sku> skus = skuRepository.findAll();
        return skus.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 保存 SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public SkuDTO save(SkuDTO dto) {
        Sku sku = convertToEntity(dto);
        Sku saved = skuRepository.save(sku);
        return convertToDTO(saved);
    }

    /**
     * 批量保存 SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SkuDTO> batchSave(List<SkuDTO> dtos) {
        List<Sku> skus = dtos.stream().map(this::convertToEntity).collect(Collectors.toList());
        for (Sku sku : skus) {
            skuRepository.save(sku);
        }
        return skus.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 删除 SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        skuRepository.deleteById(id);
    }

    /**
     * 检查 SKU 编码是否存在
     */
    public boolean existsBySkuCode(String skuCode) {
        return skuRepository.existsBySkuCode(skuCode);
    }

    /**
     * 根据 IDs 批量查询
     */
    public List<SkuDTO> getByIds(List<Long> ids) {
        List<Sku> skus = skuRepository.findByIds(ids);
        return skus.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 实体转 DTO
     */
    private SkuDTO convertToDTO(Sku sku) {
        SkuDTO dto = new SkuDTO();
        dto.setId(sku.getId());
        dto.setProductId(sku.getProductId());
        dto.setSkuCode(sku.getSkuCode());
        dto.setAttrs(sku.getAttrs());
        dto.setPrice(sku.getPrice());
        dto.setCostPrice(sku.getCostPrice());
        dto.setStock(sku.getStock());
        dto.setLockStock(sku.getLockStock());
        dto.setStatus(sku.getStatus());
        dto.setCreatedAt(sku.getCreatedAt());
        dto.setUpdatedAt(sku.getUpdatedAt());
        dto.setAvailableStock(sku.getAvailableStock());
        return dto;
    }

    /**
     * DTO 转实体
     */
    private Sku convertToEntity(SkuDTO dto) {
        Sku sku = new Sku();
        sku.setId(dto.getId());
        sku.setProductId(dto.getProductId());
        sku.setSkuCode(dto.getSkuCode());
        sku.setAttrs(dto.getAttrs());
        sku.setPrice(dto.getPrice());
        sku.setCostPrice(dto.getCostPrice());
        sku.setStock(dto.getStock());
        sku.setLockStock(dto.getLockStock());
        sku.setStatus(dto.getStatus());
        sku.setCreatedAt(dto.getCreatedAt());
        sku.setUpdatedAt(dto.getUpdatedAt());
        return sku;
    }
}
