package com.rsw.product.controller;

import static org.junit.Assert.*;
import com.rsw.product.domain.ProductCreateData;
import com.rsw.product.domain.ProductDetails;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by DAlms on 10/10/16.
 * This is one technique for an in-container integration test when using Spring Boot.
 * Spring Boot apps can also be tested using MockMvc, with either as an in-container test, or as a unit test.
 * See http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications
 *
 * There is also Rest Assured, an alternative but similar to MockMvc:
 * See https://github.com/rest-assured/rest-assured/wiki/Usage
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    // FIXME: need proper integration test for OAuth2 Resource Server
    @Ignore
    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void createEvent() throws Exception {

        ProductCreateData body = new ProductCreateData();
        body.setName("myName");
        body.setSku("123");
        body.setStartDate(LocalDate.now());
        body.setEndDate(LocalDate.now());
        body.setPrice(new BigDecimal("100.00"));
        HttpEntity<ProductCreateData> request = new HttpEntity<>(body);
        ResponseEntity<String> createdId = restTemplate.exchange("/api/v1/product/event", HttpMethod.POST, request, String.class);

        assertNotNull(createdId);
    }

    // FIXME: need proper integration test for OAuth2 Resource Server
    @Ignore
    @Test
    public void getEvent() throws Exception {
        ProductDetails result = restTemplate.getForObject("/api/v1/product/event/{eventId}/", ProductDetails.class, "myId");
        assertNotNull(result);
        assertEquals("myId", result.getId());
    }

    // FIXME: need proper integration test for OAuth2 Resource Server
    @Ignore
    @Test
    public void searchEvents() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/api/v1/product/events").queryParam("startingFromDate");

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<List<ProductDetails>> resultEntity = restTemplate.exchange(uriBuilder.build().toString(),
                HttpMethod.GET, request, new ParameterizedTypeReference<List<ProductDetails>>(){});

        List<ProductDetails> result = resultEntity.getBody();
        assertTrue(result.size() > 1);
        assertTrue(result.get(0).getId() != result.get(1).getId());
    }

}