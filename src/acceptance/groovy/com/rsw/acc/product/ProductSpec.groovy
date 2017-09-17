package com.rsw.acc.product

import com.rsw.acc.product.config.SpecConfig
import com.rsw.acc.product.service.ProductService
import com.rsw.acc.product.service.TokenService
import spock.lang.Shared
import spock.lang.Specification

class ProductSpec extends Specification {

    @Shared
    SpecConfig config
    @Shared
    TokenService tokenService
    @Shared
    ProductService productService

    def setupSpec() {
        config = new SpecConfig()
        tokenService = new TokenService(config.tokenConfig)
        productService = new ProductService(config.productConfig)
    }

    def 'get by id: happy'() {
        given:
        String token = tokenService.getToken()
        def body = ["productName": "getProdName", "price": "14.05", "description": "test", "sku": "testsku",
                    "startDate": "2017-10-01", "endDate": "2017-10-31"]
        def productId = productService.create(token, body).data.id

        when:
        def response = productService.getById(token, productId)

        then:
        response != null
        response.status == 200
        response.data['productId'] == productId
        response.data['productName'] == "getProdName"
        response.data['price'] == 14.05
        response.data['description'] == "test"
        response.data['sku'] == "testsku"

        cleanup:
        if (productId) {
            productService.delete(token, productId)
        }
    }

    def 'create: happy'() {
        given:
        String token = tokenService.getToken()

        when:
        def body = ["productName": "createProdName", "price": "12.15", "description": "test", "sku": "testsku",
                    "startDate": "2017-10-01", "endDate": "2017-10-31"]
        def response = productService.create(token, body)
        def productId = response.data['id']

        then:
        response.status == 201
        productId > 0

        cleanup:
        if (productId) {
            productService.delete(token, productId)
        }
    }

    def 'delete: happy'() {
        given:
        String token = tokenService.getToken()
        def body = ["productName": "getProdName", "price": "14.05", "description": "test", "sku": "testsku",
                    "startDate": "2017-10-01", "endDate": "2017-10-31"]
        def productId = productService.create(token, body).data.id

        when:
        def response = productService.delete(token, productId)

        then:
        response != null
        response.status == 204
    }

}
