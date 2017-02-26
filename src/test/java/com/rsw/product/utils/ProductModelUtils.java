package com.rsw.product.utils;

import com.rsw.product.domain.ProductDetailsData;
import com.rsw.product.domain.ProductRequestData;
import com.rsw.product.entity.Product;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by DAlms on 2/26/17.
 */
public abstract class ProductModelUtils {

    public static List<ProductDetailsData> buildDetailsList(int count) {
        List<ProductDetailsData> result = new ArrayList<>(count);
        IntStream.range(1, count + 1).forEach(i -> result.add(buildDetails()));
        return result;
    }

    public static ProductDetailsData buildDetails(Long id)  {
        return buildDetails().setProductId(id);
    }

    public static ProductDetailsData buildDetails() {
        return new ProductDetailsData()
                .setCreatedDate(LocalDateTime.now())
                .setUpdatedDate(LocalDateTime.now())
                .setStartDate(LocalDate.of(2017, 1, 29))
                .setEndDate(LocalDate.of(2017,6, 1))
                .setProductName(RandomStringUtils.randomAlphabetic(15))
                .setDescription(RandomStringUtils.randomAlphabetic(30))
                .setPrice(new BigDecimal(124.50))
                .setSku(RandomStringUtils.randomAlphanumeric(6))
                .setProductId(RandomUtils.nextLong());
    }

    public static ProductRequestData buildRequest() {
        return new ProductRequestData()
                .setStartDate(LocalDate.of(2017, 1, 29))
                .setEndDate(LocalDate.of(2017,6, 1))
                .setProductName(RandomStringUtils.randomAlphabetic(15))
                .setDescription(RandomStringUtils.randomAlphabetic(30))
                .setPrice(new BigDecimal(124.50))
                .setSku(RandomStringUtils.randomAlphanumeric(6));
    }

    public static List<Product> buildProductList(int count) {
        List<Product> result = new ArrayList<>(count);
        IntStream.range(1, count + 1).forEach(i -> result.add(buildProduct()));
        return result;
    }

    public static Product buildProduct() {
        Product patient = new Product();
        patient.setCreatedDate(LocalDateTime.now());
        patient.setUpdatedDate(LocalDateTime.now());
        patient.setProductId(RandomUtils.nextLong());
        patient.setStartDate(LocalDate.of(2017, 1, 29));
        patient.setEndDate(LocalDate.of(2017,6, 1));
        patient.setProductName(RandomStringUtils.randomAlphabetic(15));
        patient.setDescription(RandomStringUtils.randomAlphabetic(30));
        patient.setPrice(new BigDecimal(124.50));
        patient.setSku(RandomStringUtils.randomAlphanumeric(6));
        return patient;
    }
}
