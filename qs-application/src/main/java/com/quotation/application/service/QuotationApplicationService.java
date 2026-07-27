package com.quotation.application.service;

import com.quotation.application.dto.CreateQuotationCommand;
import com.quotation.application.dto.QuotationDTO;
import com.quotation.application.dto.UpdateQuotationCommand;
import com.quotation.common.result.Result;
import com.quotation.domain.quotation.model.Quotation;
import com.quotation.domain.quotation.repository.QuotationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报价应用服务
 */
@Service
public class QuotationApplicationService {

    @Resource
    private QuotationRepository quotationRepository;

    /**
     * 创建报价单
     */
    @Transactional
    public Result<Long> createQuotation(CreateQuotationCommand command) {
        try {
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
        } catch (Exception e) {
            return Result.fail("创建报价单失败：" + e.getMessage());
        }
    }

    /**
     * 更新报价单
     */
    @Transactional
    public Result<Boolean> updateQuotation(UpdateQuotationCommand command) {
        try {
            Quotation quotation = quotationRepository.findById(command.getId());
            if (quotation == null) {
                return Result.fail("报价单不存在");
            }
            
            quotation.setCustomerName(command.getCustomerName());
            quotation.setProductName(command.getProductName());
            quotation.setQuantity(command.getQuantity());
            quotation.setPrice(command.getUnitPrice());
            quotation.setUpdateTime(new Date());
            
            quotationRepository.updateWithOptimisticLock(quotation);
            return Result.success(true);
        } catch (Exception e) {
            return Result.fail("更新报价单失败：" + e.getMessage());
        }
    }

    /**
     * 查询报价单详情
     */
    public Result<QuotationDTO> getQuotationDetail(Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id);
            if (quotation == null) {
                return Result.fail("报价单不存在");
            }
            QuotationDTO dto = new QuotationDTO();
            BeanUtils.copyProperties(quotation, dto);
            return Result.success(dto);
        } catch (Exception e) {
            return Result.fail("查询报价单详情失败：" + e.getMessage());
        }
    }

    /**
     * 查询报价单列表（从登录上下文获取用户 ID）
     */
    public Result<List<QuotationDTO>> listQuotations(String status) {
        try {
            // TODO: 从登录上下文获取当前用户 ID
            Long currentUserId = 1L; // 临时使用固定值
            
            List<Quotation> quotations = quotationRepository.findByUserIdAndStatus(currentUserId, status);
            List<QuotationDTO> dtoList = quotations.stream().map(q -> {
                QuotationDTO dto = new QuotationDTO();
                BeanUtils.copyProperties(q, dto);
                return dto;
            }).collect(Collectors.toList());
            return Result.success(dtoList);
        } catch (Exception e) {
            return Result.fail("查询报价单列表失败：" + e.getMessage());
        }
    }

    /**
     * 批准报价
     */
    @Transactional
    public Result<Boolean> approveQuotation(Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id);
            if (quotation == null) {
                return Result.fail("报价单不存在");
            }
            
            quotation.setStatus("APPROVED");
            quotation.setUpdateTime(new Date());
            
            quotationRepository.updateWithOptimisticLock(quotation);
            return Result.success(true);
        } catch (Exception e) {
            return Result.fail("批准报价失败：" + e.getMessage());
        }
    }

    /**
     * 拒绝报价
     */
    @Transactional
    public Result<Boolean> rejectQuotation(Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id);
            if (quotation == null) {
                return Result.fail("报价单不存在");
            }
            
            quotation.setStatus("REJECTED");
            quotation.setUpdateTime(new Date());
            
            quotationRepository.updateWithOptimisticLock(quotation);
            return Result.success(true);
        } catch (Exception e) {
            return Result.fail("拒绝报价失败：" + e.getMessage());
        }
    }
}
