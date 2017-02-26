package com.rsw.product.domain;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by DAlms on 10/2/16.
 */
public class ProductDetailsData {
    private Long productId;
    private String productName;
    private String description;
    @ApiModelProperty(required = true, example = "2016-10-01")
    private LocalDate startDate;
    @ApiModelProperty(required = true, example = "2016-10-01")
    private LocalDate endDate;
    private String sku;
    private BigDecimal price;

    @ApiModelProperty(required = true, example = "2016-10-31T01:30:00.000")
    private LocalDateTime createdDate;
    @ApiModelProperty(required = true, example = "2016-10-31T01:30:00.000")
    private LocalDateTime updatedDate;

    public Long getProductId() {
        return productId;
    }

    public ProductDetailsData setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public ProductDetailsData setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetailsData setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ProductDetailsData setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ProductDetailsData setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public ProductDetailsData setSku(String sku) {
        this.sku = sku;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDetailsData setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public ProductDetailsData setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public ProductDetailsData setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductDetailsData that = (ProductDetailsData) o;
        return Objects.equals(productName, that.productName) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(sku, that.sku) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, startDate, endDate, sku, price);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("productId", productId)
                .add("productName", productName)
                .add("description", description)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("sku", sku)
                .add("price", price)
                .add("createdDate", createdDate)
                .add("updatedDate", updatedDate)
                .toString();
    }
}
