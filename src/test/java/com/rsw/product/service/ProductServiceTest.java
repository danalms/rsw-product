package com.rsw.product.service;

import com.rsw.product.domain.ProductDetailsData;
import com.rsw.product.domain.ProductRequestData;
import com.rsw.product.entity.Product;
import com.rsw.product.exception.EntityNotFoundException;
import com.rsw.product.repository.ProductRepository;
import com.rsw.product.utils.ProductModelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by DAlms on 12/18/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService subject;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void findAllProducts() throws Exception {
        List<Product> entityList = ProductModelUtils.buildProductList(2);
        when(productRepository.findAll()).thenReturn(entityList);
        when(modelMapper.map(entityList.get(0), ProductDetailsData.class)).thenReturn(ProductModelUtils.buildDetails());
        when(modelMapper.map(entityList.get(1), ProductDetailsData.class)).thenReturn(ProductModelUtils.buildDetails());

        List<ProductDetailsData> result = subject.findAllProducts();

        assertThat(result.size()).isEqualTo(2);
        verify(productRepository).findAll();
        verify(modelMapper).map(entityList.get(0), ProductDetailsData.class);
        verify(modelMapper).map(entityList.get(1), ProductDetailsData.class);
    }

    @Test
    public void getProduct_happyPath() throws Exception {
        Product entity = ProductModelUtils.buildProduct();
        when(productRepository.findOne(100L)).thenReturn(entity);
        when(modelMapper.map(entity, ProductDetailsData.class)).thenReturn(ProductModelUtils.buildDetails());

        ProductDetailsData result = subject.getProduct(100L);

        assertThat(result).isNotNull();
        verify(productRepository).findOne(100L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getProduct_notFound() throws Exception {
        when(productRepository.findOne(100L)).thenReturn(null);

        subject.getProduct(100L);

        verify(productRepository).findOne(100L);
    }

    @Test
    public void createProduct() throws Exception {
        Product entity = ProductModelUtils.buildProduct();
        ProductRequestData model = ProductModelUtils.buildRequest();
        when(modelMapper.map(model, Product.class)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);

        Long result = subject.createProduct(model);

        assertThat(result).isEqualTo(entity.getProductId());
        verify(productRepository).save(entity);
    }

    @Test
    public void updateProduct_happyPath() throws Exception {
        Product updatedEntity = ProductModelUtils.buildProduct();
        updatedEntity.setCreatedDate(null);
        Product existingEntity = ProductModelUtils.buildProduct();
        ProductRequestData model = ProductModelUtils.buildRequest();
        when(modelMapper.map(model, Product.class)).thenReturn(updatedEntity);
        when(productRepository.findOne(100L)).thenReturn(existingEntity);
        when(productRepository.save(updatedEntity)).thenReturn(updatedEntity);

        subject.updateProduct(100L, model);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getCreatedDate()).isEqualTo(existingEntity.getCreatedDate());
        assertThat(captor.getValue().getUpdatedDate()).isEqualTo(updatedEntity.getUpdatedDate());

        verify(productRepository).findOne(100L);
        verify(productRepository).save(updatedEntity);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateProduct_notFound() throws Exception {
        ProductRequestData model = ProductModelUtils.buildRequest();
        when(productRepository.findOne(100L)).thenReturn(null);

        subject.updateProduct(100L, model);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void deleteProduct_happyPath() throws Exception {
        Product entity = ProductModelUtils.buildProduct();
        when(productRepository.findOne(100L)).thenReturn(entity);
        subject.deleteProduct(100L);

        verify(productRepository).findOne(100L);
        verify(productRepository).delete(entity);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteProduct_notFound() throws Exception {
        when(productRepository.findOne(100L)).thenReturn(null);

        subject.deleteProduct(100L);

        verify(productRepository, never()).delete(100L);
    }

}