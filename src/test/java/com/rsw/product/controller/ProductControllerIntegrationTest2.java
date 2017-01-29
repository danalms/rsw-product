package com.rsw.product.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.google.gson.Gson;
import com.rsw.product.domain.ProductCreateData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

/**
 * Created by DAlms on 10/10/16.
 *
 * The MockMvc technique is more of a unit test. Service calls can be mocked!
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest2 {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception {

        ProductCreateData body = new ProductCreateData();
        body.setName("myName");
        body.setSku("123");
        // probably need to format dates specifically for ISO - can't de-marshall as is here:
//        body.setStartDate(LocalDate.now());
//        body.setEndDate(LocalDate.now());
        body.setPrice(new BigDecimal("100.00"));

        Gson gson = new Gson();
        String json = gson.toJson(body);

        mockMvc.perform(post("/api/v1/product/event").content(json).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

    }

}