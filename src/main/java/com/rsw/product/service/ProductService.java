package com.rsw.product.service;

import com.rsw.product.domain.ProductDetailsData;
import com.rsw.product.domain.ProductRequestData;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by DAlms on 2/26/17.
 */
public interface ProductService {
    List<ProductDetailsData> findAllProducts();

    List<ProductDetailsData> findAllProducts(Sort sort);

    ProductDetailsData getProduct(Long productId);

    Long createProduct(ProductRequestData productData);

    void updateProduct(Long productId, ProductRequestData productData);

    void deleteProduct(Long productId);
}
