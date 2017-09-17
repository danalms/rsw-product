package com.rsw.acc.product.config

class SpecConfig {

    def promoterId = 1
    def tokenConfig = ["serviceHost": "http://localhost:8085",
                       "tokenUri": "/util/token",
                       "username": "apiadmin",
                       "password": "P@ssw0rd"]

    def productConfig = ["serviceHost": "http://localhost:8090",
                         "baseUri": "/api/${promoterId}/product"]

}
