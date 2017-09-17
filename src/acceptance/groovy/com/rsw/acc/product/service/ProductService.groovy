package com.rsw.acc.product.service

import groovyx.net.http.RESTClient
import org.springframework.http.MediaType

class ProductService {
    Map productConfig
    RESTClient restClient

    ProductService(Map productConfig) {
        this.productConfig = productConfig
        restClient = new RESTClient(productConfig.serviceHost)
        restClient.headers['Accept'] = "application/json;charset=UTF-8"
        restClient.headers['Authorization'] = "application/json;charset=UTF-8"
    }

    def getById(String token, long productId) {
        restClient.headers['Authorization'] = "Bearer ${token}"
        def response = restClient.get(path: "${productConfig.baseUri}/${productId}",
                contentType: MediaType.APPLICATION_JSON_VALUE
        )
        return response
    }

    def create(String token, Map body) {
        restClient.headers['Authorization'] = "Bearer ${token}"
        def response = restClient.post(path: productConfig.baseUri,
                body: body,
                contentType: MediaType.APPLICATION_JSON_VALUE
        )
        return response
    }

    def delete(String token, long productId) {
        restClient.headers['Authorization'] = "Bearer ${token}"
        def response = restClient.delete(path: "${productConfig.baseUri}/${productId}",
                contentType: MediaType.APPLICATION_JSON_VALUE
        )
        return response
    }

}
