package com.rsw.product.controller;

import com.google.common.collect.Lists;
import com.rsw.product.domain.EntityIdResponse;
import com.rsw.product.domain.ProductCreateData;
import com.rsw.product.domain.ProductDetails;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by DAlms on 10/2/16.
 */
@RestController
@RequestMapping(value = "/api/{promoterId}/product")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Value("${product.tokenType}")
    private String tokenType;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "searchProducts")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public List<ProductDetails> searchProducts(HttpServletRequest request, @PathVariable("promoterId") Long promoterId,
                                               @RequestParam("startingFromDate")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        logRequest(request);

        List<ProductDetails> result = Lists.newArrayList();

        ProductDetails productDetails = new ProductDetails();
        productDetails.setCreatedTime(LocalDateTime.now());
        productDetails.setLastUpdatedTime(LocalDateTime.now());
        productDetails.setName("Big Event #1");
        productDetails.setDescription("The big event #1 description");
        productDetails.setStartDate(LocalDate.now());
        productDetails.setEndDate(LocalDate.now());
        productDetails.setId(101L);
        productDetails.setPrice(new BigDecimal("100.00"));
        productDetails.setSku("EV001");
        result.add(productDetails);

        productDetails = new ProductDetails();
        productDetails.setCreatedTime(LocalDateTime.now());
        productDetails.setLastUpdatedTime(LocalDateTime.now());
        productDetails.setName("Big Event #2");
        productDetails.setDescription("The big event #2 description");
        productDetails.setStartDate(LocalDate.now());
        productDetails.setEndDate(LocalDate.now());
        productDetails.setId(102L);
        productDetails.setPrice(new BigDecimal("110.00"));
        productDetails.setSku("EV002");
        result.add(productDetails);

        return result;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "getProduct")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ProductDetails getProduct(HttpServletRequest request, @PathVariable("promoterId") Long promoterId,
                                     @PathVariable("productId") Long productId) {

        logRequest(request);
        securityPeek();
        analyzeJwt();

        ProductDetails productDetails = new ProductDetails();
        productDetails.setCreatedTime(LocalDateTime.now());
        productDetails.setLastUpdatedTime(LocalDateTime.now());
        productDetails.setName("Big Event #1");
        productDetails.setDescription("The big event #1 description");
        productDetails.setStartDate(LocalDate.now());
        productDetails.setEndDate(LocalDate.now());
        productDetails.setId(productId);
        productDetails.setPrice(new BigDecimal("100.00"));
        productDetails.setSku("EV001");

        return productDetails;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "createProduct")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ResponseEntity<EntityIdResponse> createProduct(@PathVariable("promoterId") Long promoterId,
                                                          @RequestBody ProductCreateData productCreateData) {

        Long createdId = 100L;
        return new ResponseEntity<>(new EntityIdResponse(createdId), HttpStatus.CREATED);
    }

    private void analyzeJwt() {
        if (! "jwt".equals(tokenType)) {
            LOG.info("Not using JWT");
            return;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();

        String token = details.getTokenValue();
        Jwt jwt = JwtHelper.decode(token);

        String claims = jwt.getClaims();
        String encodedJwt = jwt.getEncoded();

        // to verify, get hold of the signing key property
//        MacSigner signer = new MacSigner(signingKey);
//        jwt.verifySignature(signer);
        LOG.info("Jwt UNVERIFIED, claims = {}, encoded = {}", claims, encodedJwt);
    }

    private void securityPeek() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        LOG.info("auth: {}", auth);
    }

    private void logRequest(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            LOG.info("product header: '{}' value: '{}'", headerName, request.getHeader(headerName));
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                LOG.info("product cookie: '{}' domain: '{}' value: '{}'", cookie.getName(), cookie.getDomain(),
                        cookie.getValue());
            }
        }
    }
}
