package com.products.app.service;

import java.util.List;

import com.products.app.model.Product;

import reactor.core.publisher.Mono;

public interface IProductService {

	Mono<Integer[]> findSimilarProductIds(String id);

	Mono<Product> findProductById(Integer id);

	Mono<List<Product>> findSimilarProducts(String id);

}
