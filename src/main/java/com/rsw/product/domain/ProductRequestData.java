package com.rsw.product.domain;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by DAlms on 10/2/16.
 */
public class ProductRequestData {

    @NotEmpty(message = "validate.productname.notempty")
    @ApiModelProperty(required = true)
    private String productName;
    @NotEmpty(message = "validate.productdescription.notempty")
    @ApiModelProperty(required = true)
    private String description;
    @ApiModelProperty(example = "2016-10-01")
    private LocalDate startDate;
    @ApiModelProperty(example = "2016-10-01")
    private LocalDate endDate;
    @NotEmpty(message = "validate.productsku.notempty")
    private String sku;
    @NotNull(message = "validate.productprice.notnull")
    private BigDecimal price;

    public String getProductName() {
        return productName;
    }

    public ProductRequestData setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductRequestData setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ProductRequestData setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ProductRequestData setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public ProductRequestData setSku(String sku) {
        this.sku = sku;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductRequestData setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductRequestData that = (ProductRequestData) o;
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
                .add("productName", productName)
                .add("description", description)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("sku", sku)
                .add("price", price)
                .toString();
    }
}
