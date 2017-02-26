package com.rsw.product.controller;

import com.rsw.product.domain.EntityIdResponse;
import com.rsw.product.domain.ProductRequestData;
import com.rsw.product.domain.ProductDetailsData;
import com.rsw.product.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "searchProducts")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ResponseEntity<List<ProductDetailsData>> searchProducts(HttpServletRequest request,
                                                                   @PathVariable("promoterId") Long promoterId,
                                                                   @RequestParam(name = "sortField", required = false)
                                                                           String sortField ) {
        List<ProductDetailsData> results;
        if (StringUtils.isEmpty(sortField)) {
            results = productService.findAllProducts();
        } else {
            results = productService.findAllProducts(new Sort(sortField));
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "getProduct")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ResponseEntity<ProductDetailsData> getProduct(HttpServletRequest request,
                                                         @PathVariable("promoterId") Long promoterId,
                                                         @PathVariable("productId") Long productId) {

        ProductDetailsData result = productService.getProduct(productId);
        return new ResponseEntity<ProductDetailsData>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "createProduct")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ResponseEntity<EntityIdResponse> createProduct(@PathVariable("promoterId") Long promoterId,
                                                          @RequestBody ProductRequestData productRequestData) {

        Long createdId = productService.createProduct(productRequestData);
        return new ResponseEntity<>(new EntityIdResponse(createdId), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{productId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "createProduct")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ResponseEntity<String> updateProduct(@PathVariable("promoterId") Long promoterId,
                                                          @PathVariable("productId") Long productId,
                                                          @RequestBody ProductRequestData productRequestData) {

        productService.updateProduct(productId, productRequestData);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{productId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "deleteProduct")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "OAuth2 token",
            required = false, dataType = "string", paramType = "header", defaultValue = "Bearer X") })
    public ResponseEntity<String> deleteProduct(@PathVariable("promoterId") Long promoterId,
                                                @PathVariable("productId") Long productId) {

        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
