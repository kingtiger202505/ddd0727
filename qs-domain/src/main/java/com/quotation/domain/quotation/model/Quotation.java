package com.quotation.domain.quotation.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报价单聚合根
 */
@Data
public class Quotation {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String status; // PENDING, COUNTER_OFFER, ACCEPTED, REJECTED, CLOSED
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Quotation create(Long buyerId, Long sellerId, Long productId, String productName, 
                                   BigDecimal price, Integer quantity) {
        Quotation q = new Quotation();
        q.setBuyerId(buyerId);
        q.setSellerId(sellerId);
        q.setProductId(productId);
        q.setProductName(productName);
        q.setPrice(price);
        q.setQuantity(quantity);
        q.setStatus("PENDING");
        q.setVersion(1);
        q.setCreatedAt(LocalDateTime.now());
        q.setUpdatedAt(LocalDateTime.now());
        return q;
    }

    public void counterOffer(BigDecimal newPrice) {
        if ("ACCEPTED".equals(this.status) || "REJECTED".equals(this.status) || "CLOSED".equals(this.status)) {
            throw new IllegalStateException("Cannot counter offer on closed quotation");
        }
        this.price = newPrice;
        this.status = "COUNTER_OFFER";
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void accept() {
        if (!"PENDING".equals(this.status) && !"COUNTER_OFFER".equals(this.status)) {
            throw new IllegalStateException("Cannot accept quotation in current status");
        }
        this.status = "ACCEPTED";
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void reject() {
        if (!"PENDING".equals(this.status) && !"COUNTER_OFFER".equals(this.status)) {
            throw new IllegalStateException("Cannot reject quotation in current status");
        }
        this.status = "REJECTED";
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }
}
