package com.rsw.product.service

import com.rsw.product.domain.ProductDetailsData
import com.rsw.product.domain.ProductRequestData
import com.rsw.product.entity.Product
import com.rsw.product.exception.EntityNotFoundException
import com.rsw.product.fixture.ProductBuilder
import com.rsw.product.fixture.ProductDetailsBuilder
import com.rsw.product.repository.ProductRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Sort
import spock.lang.Specification

class ProductServiceSpec extends Specification {

    ProductRepository productRepository = Mock(ProductRepository.class)
    ModelMapper mapper = Mock(ModelMapper.class)
    ProductService productService = new ProductService(productRepository, mapper)

    def "findAllProducts"() {
        given:
        def productFixtures = ProductBuilder.generate([1,2,3])
        def productDetailFixtures = ProductDetailsBuilder.generate([1,2,3])

        when:
        def products = productService.findAllProducts()

        then:
        products.size() == 3
        1 * productRepository.findAll() >> productFixtures
        3 * mapper.map(_,_) >>> productDetailFixtures
        productFixtures[2].productId == productDetailFixtures[2].productId

    }

    def "findAllProducts exception"() {
        given:
        productRepository.findAll() >> {throw new IllegalArgumentException()}

        when:
        productService.findAllProducts()

        then:
        thrown(IllegalArgumentException)
        0 * mapper.map(_,_)

    }

    def "findAllProducts with Sort"() {
        given:
        Sort sort = new Sort(Sort.Direction.ASC, "productName")
        when:
        def products = productService.findAllProducts(sort)

        then:
        1 * productRepository.findAll(sort) >> ProductBuilder.generate([1,2,3])
        3 * mapper.map(_,_) >>> ProductDetailsBuilder.generate([1,2,3])
    }

    def "getProduct happy"() {
        given:
        Product product = ProductBuilder.instance().id(1).name("blah").build()
        ProductDetailsData productDetails = ProductDetailsBuilder.instance().id(1).name("blah").build()
        when:
        def result = productService.getProduct(1)

        then:
        1 * productRepository.findOne(1) >> product
        1 * mapper.map(_,_) >> productDetails
        result == productDetails
    }

    def "getProduct not found"() {
        when:
        productService.getProduct(1)

        then:
        1 * productRepository.findOne(1) >> null
        0 * mapper.map(_,_)
        thrown(EntityNotFoundException)
    }

    def "createProduct"() {
        given:
        ProductRequestData productRequest = new ProductRequestData()
        Product product = ProductBuilder.instance().name("blah").build()
        Product productResult = ProductBuilder.instance().id(1).name("blah").build()

        when:
        def id = productService.createProduct(productRequest)

        then:
        id == 1
        1 * mapper.map(productRequest, Product) >> product
        1 * productRepository.save(product) >> productResult

    }

    def "updateProduct"() {
        given:
        ProductRequestData productRequest = new ProductRequestData()
        Product productAfter = ProductBuilder.instance().id(1).name("new blah").build()
        Product productBefore = ProductBuilder.instance().id(1).name("blah").build()

        when:
        productService.updateProduct(1, productRequest)

        then:
        1 * productRepository.findOne(1) >> productBefore
        1 * mapper.map(productRequest, Product) >> productAfter
        1 * productRepository.save(productAfter)
    }

    def "updateProduct not found"() {
        given:
        ProductRequestData productRequest = new ProductRequestData()
        Product productAfter = ProductBuilder.instance().id(1).name("new blah").build()

        when:
        productService.updateProduct(1, productRequest)

        then:
        1 * productRepository.findOne(1) >> null
        0 * mapper.map(productRequest, Product)
        0 * productRepository.save(productAfter)
        thrown(EntityNotFoundException)
    }

    def "deleteProduct"() {
        given:
        ProductRequestData productRequest = new ProductRequestData()
        Product productBefore = ProductBuilder.instance().id(1).name("blah").build()

        when:
        productService.deleteProduct(1)

        then:
        1 * productRepository.findOne(1) >> productBefore
        1 * productRepository.delete(productBefore)
    }

    def "deleteProduct not found"() {
        when:
        productService.deleteProduct(1)

        then:
        1 * productRepository.findOne(1) >> null
        0 * productRepository.delete(_)
        thrown(EntityNotFoundException)
    }
}
