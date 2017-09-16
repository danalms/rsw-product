package com.rsw.product.fixture

import com.rsw.product.entity.Product
import org.apache.commons.lang3.RandomStringUtils

import java.time.LocalDate
import java.time.LocalDateTime

class ProductBuilder {

    Product product

    private ProductBuilder() {
        product = new Product()
    }

    static ProductBuilder instance() {
        return new ProductBuilder()
    }

    ProductBuilder id(long id) {
        product.setProductId(id)
        return this
    }
    ProductBuilder name(String name) {
        product.setProductName(name)
        return this
    }
    ProductBuilder description(String description) {
        product.setDescription(description)
        return this
    }
    ProductBuilder startDate(LocalDate startDate) {
        product.setStartDate(startDate)
        return this
    }
    ProductBuilder endDate(LocalDate endDate) {
        product.setEndDate(endDate)
        return this
    }
    ProductBuilder sku(String sku) {
        product.setSku(sku)
        return this
    }
    ProductBuilder price(BigDecimal price) {
        product.setPrice(price)
        return this
    }

    Product build() {
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
