package com.quotation.domain.quotation.event;

/**
 * 报价单已接受事件
 */
public class QuotationAcceptedEvent extends DomainEvent {
    private Long quotationId;
    private Long buyerId;
    private Long sellerId;
    private String productName;

    public QuotationAcceptedEvent(Long quotationId, Long buyerId, Long sellerId, String productName) {
        super();
        this.quotationId = quotationId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productName = productName;
    }

    // Getters and Setters
    public Long getQuotationId() { return quotationId; }
    public void setQuotationId(Long quotationId) { this.quotationId = quotationId; }
    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}
