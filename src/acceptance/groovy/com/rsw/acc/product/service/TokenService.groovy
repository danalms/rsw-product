package com.rsw.acc.product.service

import groovyx.net.http.RESTClient
import org.springframework.http.MediaType

class TokenService {
    Map tokenConfig
    RESTClient restClient

    TokenService(Map tokenConfig) {
        this.tokenConfig = tokenConfig
        restClient = new RESTClient(tokenConfig.serviceHost)
        restClient.headers['Accept'] = "application/json;charset=UTF-8"
    }

    def getToken() {
        def response = restClient.post(path: tokenConfig.tokenUri,
                body: ['password': tokenConfig.password, 'userName': tokenConfig.username],
                contentType: MediaType.APPLICATION_JSON_VALUE
        )
        if (response) {
           return response.responseData['authToken']
        }
        return null
    }

}
