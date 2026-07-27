package com.quotation.application.service;

import com.quotation.application.dto.QuotationDTO;
import com.quotation.application.dto.CreateQuotationCommand;
import com.quotation.common.result.Result;
import com.quotation.domain.quotation.model.Quotation;
import com.quotation.domain.quotation.repository.QuotationRepository;
import com.quotation.infrastructure.persistence.mapper.QuotationMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuotationApplicationService {

    @Resource
    private QuotationRepository quotationRepository;

    @Resource
    private QuotationMapper quotationMapper;

    /**
     * 创建报价单
     */
    @Transactional
    public Result<Long> createQuotation(CreateQuotationCommand command) {
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(command, quotation);
        quotation.setSellerId(command.getSellerId());
        quotation.setBuyerId(command.getBuyerId());
        quotation.setPrice(command.getPrice());
        quotation.setStatus("PENDING");
        quotation.setVersion(1);
        quotation.setCreateTime(new Date());
        quotation.setUpdateTime(new Date());
        
        quotationRepository.save(quotation);
        return Result.success(quotation.getId());
    }

    /**
     * 查询报价单详情
     */
    public Result<QuotationDTO> getQuotationDetail(Long id) {
        Quotation quotation = quotationRepository.findById(id);
        if (quotation == null) {
            return Result.fail("报价单不存在");
        }
        QuotationDTO dto = new QuotationDTO();
        BeanUtils.copyProperties(quotation, dto);
        return Result.success(dto);
    }

    /**
     * 查询报价单列表
     */
    public Result<List<QuotationDTO>> listQuotations(Long userId, String status) {
        List<Quotation> quotations = quotationRepository.findByUserIdAndStatus(userId, status);
        List<QuotationDTO> dtoList = quotations.stream().map(q -> {
            QuotationDTO dto = new QuotationDTO();
            BeanUtils.copyProperties(q, dto);
            return dto;
        }).collect(Collectors.toList());
        return Result.success(dtoList);
    }

    /**
     * 买家还价
     */
    @Transactional
    public Result<Boolean> counterOffer(Long quotationId, Long buyerId, Long newPrice) {
        Quotation quotation = quotationRepository.findById(quotationId);
        if (quotation == null) {
            return Result.fail("报价单不存在");
        }
        if (!quotation.getBuyerId().equals(buyerId)) {
            return Result.fail("无权操作此报价单");
        }
        if (!"PENDING".equals(quotation.getStatus()) && !"COUNTERED".equals(quotation.getStatus())) {
            return Result.fail("当前状态不允许还价");
        }
        
        quotation.setPrice(newPrice);
        quotation.setStatus("COUNTERED");
        quotation.setUpdateTime(new Date());
        
        quotationRepository.updateWithOptimisticLock(quotation);
        return Result.success(true);
    }

    /**
     * 卖家接受报价
     */
    @Transactional
    public Result<Boolean> acceptQuotation(Long quotationId, Long sellerId) {
        Quotation quotation = quotationRepository.findById(quotationId);
        if (quotation == null) {
            return Result.fail("报价单不存在");
        }
        if (!quotation.getSellerId().equals(sellerId)) {
            return Result.fail("无权操作此报价单");
        }
        if (!"PENDING".equals(quotation.getStatus()) && !"COUNTERED".equals(quotation.getStatus())) {
            return Result.fail("当前状态不允许接受");
        }
        
        quotation.setStatus("ACCEPTED");
        quotation.setUpdateTime(new Date());
        
        quotationRepository.updateWithOptimisticLock(quotation);
        return Result.success(true);
    }

    /**
     * 卖家拒绝报价
     */
    @Transactional
    public Result<Boolean> rejectQuotation(Long quotationId, Long sellerId) {
        Quotation quotation = quotationRepository.findById(quotationId);
        if (quotation == null) {
            return Result.fail("报价单不存在");
        }
        if (!quotation.getSellerId().equals(sellerId)) {
            return Result.fail("无权操作此报价单");
        }
        
        quotation.setStatus("REJECTED");
        quotation.setUpdateTime(new Date());
        
        quotationRepository.updateWithOptimisticLock(quotation);
        return Result.success(true);
    }
}
