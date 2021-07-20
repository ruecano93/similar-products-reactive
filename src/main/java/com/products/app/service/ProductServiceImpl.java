package com.products.app.service;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.products.app.model.Product;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {
	
	private final WebClient client;
	
	@Override
	public Mono<Integer[]> findSimilarProductIds(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return client.get().uri("product/{id}/similarids", params)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Integer[].class);
	}

	@Override
	public Mono<Product> findProductById(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return client.get().uri("product/{id}", params)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Product.class)
				.onErrorReturn(error -> error instanceof Throwable, new Product());
	}
	
	@Override
	public Mono<List<Product>> findSimilarProducts(String id){
		Mono<Integer[]> productsIds = this.findSimilarProductIds(id);
		return productsIds.zipWhen(productIds -> {
			Flux<Product> products = this.findProductList(productIds);
			return products.collectList();
		}).map(tuple2 -> {
			return tuple2.getT2();
		}).filter(p -> !p.isEmpty());
		
	}
	
	private Flux<Product> findProductList(Integer[] productIds){
		return Flux.fromIterable(Arrays.asList(productIds))
				.parallel()
				.runOn(Schedulers.boundedElastic())
				.flatMap(this::findProductById)
				.filter(p -> StringUtils.hasLength(p.getId()))
				// ID IS A NUMBER??, EXCEPTION IF NOT
				.ordered((u1, u2) -> Integer.valueOf(u1.getId()) - Integer.valueOf(u2.getId()));
	}

}
