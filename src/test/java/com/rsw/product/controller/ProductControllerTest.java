package com.rsw.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsw.product.domain.ProductDetailsData;
import com.rsw.product.domain.ProductRequestData;
import com.rsw.product.domain.Promoter;
import com.rsw.product.exception.EntityNotFoundException;
import com.rsw.product.service.ProductService;
import com.rsw.product.tenant.PromoterCache;
import com.rsw.product.utils.ProductModelUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by DAlms on 12/18/16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(secure = false)
public class ProductControllerTest {
    private static final String PROMOTER_ID = "1";
    private static final Long PROMOTER_ID_LONG = 1L;
    private static final String BASE_URI = "/api/{promoterId}/product";
    private static final String PRODUCT_URI = BASE_URI + "/{productId}";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private PromoterCache promoterCache;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        assertThat(productService).isNotNull();
        when(promoterCache.findPromoter(anyLong()))
                .thenReturn(Optional.of(new Promoter().setPromoterId(PROMOTER_ID_LONG)));
    }

    @Test
    public void findAllProducts() throws Exception {
        when(productService.findAllProducts()).thenReturn(ProductModelUtils.buildDetailsList(3));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI, PROMOTER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void getProduct_happyPath() throws Exception {
        ProductDetailsData productDetails = ProductModelUtils.buildDetails(100L);

        when(productService.getProduct(productDetails.getProductId())).thenReturn(productDetails);

        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_URI, PROMOTER_ID, "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(100)));
    }

    @Test
    public void getProduct_notFound() throws Exception {
        ProductDetailsData productDetails = ProductModelUtils.buildDetails(100L);

        when(productService.getProduct(productDetails.getProductId())).thenThrow(new EntityNotFoundException(100L));

        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_URI, PROMOTER_ID, "100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createProduct_happyPath() throws Exception {
        ProductRequestData productRequest = ProductModelUtils.buildRequest();
        when(productService.createProduct(productRequest)).thenReturn(200L);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI, PROMOTER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(200)));
    }

    @Test
    public void createProduct_badRequest() throws Exception {
        String badRequest = "{\"nonconforming\":\"data\":}";

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI, PROMOTER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProduct_happyPath() throws Exception {
        ProductRequestData productRequest = ProductModelUtils.buildRequest();

        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_URI, PROMOTER_ID, "100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateProduct_methodNotAllowed() throws Exception {
        ProductRequestData productRequest = ProductModelUtils.buildRequest();

        mockMvc.perform(MockMvcRequestBuilders.post(PRODUCT_URI, PROMOTER_ID, "100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void updateProduct_badRequest() throws Exception {
        String badRequest = "{\"nonconforming\":\"data\":}";

        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_URI, PROMOTER_ID, "100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProduct_notFound() throws Exception {
        ProductRequestData productRequest = ProductModelUtils.buildRequest();
        doThrow(new EntityNotFoundException(100L)).when(productService).updateProduct(100L, productRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_URI, PROMOTER_ID, "100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProduct_happyPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_URI, PROMOTER_ID, "100"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProduct_notFound() throws Exception {
        doThrow(new EntityNotFoundException(100L)).when(productService).deleteProduct(100L);
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_URI, PROMOTER_ID, "100"))
                .andExpect(status().isNotFound());
    }
}