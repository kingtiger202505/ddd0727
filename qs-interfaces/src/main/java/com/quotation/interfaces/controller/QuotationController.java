package com.quotation.interfaces.controller;

import com.quotation.application.dto.CreateQuotationCommand;
import com.quotation.application.dto.QuotationDTO;
import com.quotation.application.service.QuotationApplicationService;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/quotation")
public class QuotationController {

    @Resource
    private QuotationApplicationService quotationService;

    /**
     * 创建报价单
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody CreateQuotationCommand command) {
        return quotationService.createQuotation(command);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    public Result<QuotationDTO> detail(@PathVariable Long id) {
        return quotationService.getQuotationDetail(id);
    }

    /**
     * 列表查询
     */
    @GetMapping("/list")
    public Result<List<QuotationDTO>> list(
            @RequestParam Long userId,
            @RequestParam(required = false) String status) {
        return quotationService.listQuotations(userId, status);
    }

    /**
     * 买家还价
     */
    @PostMapping("/counter-offer")
    public Result<Boolean> counterOffer(
            @RequestParam Long quotationId,
            @RequestParam Long buyerId,
            @RequestParam Long newPrice) {
        return quotationService.counterOffer(quotationId, buyerId, newPrice);
    }

    /**
     * 卖家接受报价
     */
    @PostMapping("/accept")
    public Result<Boolean> accept(
            @RequestParam Long quotationId,
            @RequestParam Long sellerId) {
        return quotationService.acceptQuotation(quotationId, sellerId);
    }

    /**
     * 卖家拒绝报价
     */
    @PostMapping("/reject")
    public Result<Boolean> reject(
            @RequestParam Long quotationId,
            @RequestParam Long sellerId) {
        return quotationService.rejectQuotation(quotationId, sellerId);
    }
}
