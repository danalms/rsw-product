package com.rsw.product.entity;

import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by DAlms on 12/10/16.
 *
 * Sample Product entity
 *
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id @Column(name="product_id", insertable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_product_id_seq")
    @SequenceGenerator(name = "product_product_id_seq", sequenceName = "product_product_id_seq", allocationSize = 1)
    private Long productId;

    @Column(name="product_name", nullable=false)
    @NotEmpty
    private String productName;

    @Column(name="description", nullable=false)
    @NotEmpty
    private String description;

    @Column(name="start_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    @Column(name="end_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    @Column(name="sku", nullable=false)
    @NotEmpty
    private String sku;

    @Column(name="price", nullable=false)
    @NotEmpty
    private BigDecimal price;

    @Column(name="created_date", nullable=false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @Column(name="updated_date", nullable=false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedDate;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return Objects.equals(productName, product.productName) &&
                Objects.equals(startDate, product.startDate) &&
                Objects.equals(endDate, product.endDate) &&
                Objects.equals(sku, product.sku) &&
                Objects.equals(price, product.price);
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
