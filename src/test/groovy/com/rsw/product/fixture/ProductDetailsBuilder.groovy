package com.rsw.product.fixture

import com.rsw.product.domain.ProductDetailsData
import com.rsw.product.entity.Product
import org.apache.commons.lang3.RandomStringUtils

import java.time.LocalDate
import java.time.LocalDateTime

class ProductDetailsBuilder {

    ProductDetailsData product

    private ProductDetailsBuilder() {
        product = new ProductDetailsData()
    }

    static ProductDetailsBuilder instance() {
        return new ProductDetailsBuilder()
    }

    ProductDetailsBuilder id(long id) {
        product.setProductId(id)
        return this
    }
    ProductDetailsBuilder name(String name) {
        product.setProductName(name)
        return this
    }
    ProductDetailsBuilder description(String description) {
        product.setDescription(description)
        return this
    }
    ProductDetailsBuilder startDate(LocalDate startDate) {
        product.setStartDate(startDate)
        return this
    }
    ProductDetailsBuilder endDate(LocalDate endDate) {
        product.setEndDate(endDate)
        return this
    }
    ProductDetailsBuilder sku(String sku) {
        product.setSku(sku)
        return this
    }
    ProductDetailsBuilder price(BigDecimal price) {
        product.setPrice(price)
        return this
    }

    ProductDetailsData build() {
        product.setCreatedDate(LocalDateTime.now())
        product.setUpdatedDate(LocalDateTime.now())
        return product
    }

    static List<Product> generate(List<Integer> ids) {
        def product = []
        ids.each {product.add(instance().id(it).name(RandomStringUtils.randomAlphabetic(5)).price(new BigDecimal("10.0")).build())}
        return product
    }
}
