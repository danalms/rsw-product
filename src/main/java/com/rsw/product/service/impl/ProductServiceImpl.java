package com.rsw.product.service.impl;

import com.rsw.product.domain.ProductDetailsData;
import com.rsw.product.domain.ProductRequestData;
import com.rsw.product.entity.Product;
import com.rsw.product.exception.EntityNotFoundException;
import com.rsw.product.repository.ProductRepository;
import com.rsw.product.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by DAlms on 2/19/17.
 *
 * sample Product service
 *
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDetailsData> findAllProducts() {
        List<Product> results = productRepository.findAll();
        return mapResults(results);
    }

    @Override
    public List<ProductDetailsData> findAllProducts(Sort sort) {
        List<Product> results = productRepository.findAll(sort);
        return mapResults(results);
    }

    @Override
    public ProductDetailsData getProduct(Long productId) {
        return mapResult(getOne(productId));
    }

    @Override
    public Long createProduct(ProductRequestData productData) {
        Product product = modelMapper.map(productData, Product.class);
        LocalDateTime timeStamp = LocalDateTime.now();
        product.setCreatedDate(timeStamp);
        product.setUpdatedDate(timeStamp);

        Product newProduct = productRepository.save(product);
        return newProduct.getProductId();
    }

    @Override
    public void updateProduct(Long productId, ProductRequestData productData) {
        Product existingProduct = getOne(productId);
        Product product = modelMapper.map(productData, Product.class);
        product.setProductId(productId);
        product.setCreatedDate(existingProduct.getCreatedDate());
        LocalDateTime timeStamp = LocalDateTime.now();
        product.setUpdatedDate(timeStamp);

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = getOne(productId);
        productRepository.delete(product);
    }

    private List<ProductDetailsData> mapResults(List<Product> products) {
        return products.stream().map(p -> modelMapper.map(p, ProductDetailsData.class)).collect(Collectors.toList());
    }

    private ProductDetailsData mapResult(Product product) {
        return modelMapper.map(product, ProductDetailsData.class);
    }

    private Product getOne(Long productId) {
        Optional<Product> result = Optional.ofNullable(productRepository.findOne(productId));
        if (! result.isPresent()) {
            LOG.warn("method=getOne message=ProductNotFound productId={}", productId);
            throw new EntityNotFoundException(productId);
        }
        return result.get();
    }

}
