package com.rsw.product.repository;

import com.rsw.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by DAlms on 2/19/17.
 *
 * see JpaRepository/CrudRepository for standard methods available out of the box
 *
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
