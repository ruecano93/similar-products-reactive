package com.products.app.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.products.app.model.Product;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

	ProductServiceImpl service;
	
	@Mock
	private WebClient webClientMock;
	
	@Mock
	private WebClient.RequestHeadersSpec requestHeadersMock;
	@Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
	@Mock
	private WebClient.RequestBodySpec requestBodyMock;
	@Mock
	private WebClient.RequestBodyUriSpec requestBodyUriMock;
	@Mock
	private WebClient.ResponseSpec responseMock;
	
	@BeforeEach
	void setUp() {
		service = new ProductServiceImpl(webClientMock);
	}
	
	@Test 
	void getProductIdList_OK() {
		String productId = "1";
		Integer[] similarProductIds = {2,3,4};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", productId);
		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
		Mockito.when(requestHeadersUriMock.uri("product/{id}/similarids", params)).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
		Mockito.when(responseMock.bodyToMono(Integer[].class)).thenReturn(Mono.just(similarProductIds));
		
		Mono<Integer[]> productLists = service.findSimilarProductIds(productId);
		
		StepVerifier.create(productLists)
			.expectNextMatches(products -> products.length == 3)
			.verifyComplete();	
	}
	
	@Test 
	void getProductDetail_OK() {
		Integer productId = 1;
		Product productMock = Product.builder()
				.id(productId.toString())
				.name("Trousers")
				.price(BigDecimal.valueOf(30))
				.availability(true).build();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", productId);
		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
		Mockito.when(requestHeadersUriMock.uri("product/{id}", params)).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
		Mockito.when(responseMock.bodyToMono(Product.class)).thenReturn(Mono.just(productMock));
		
		Mono<Product> productLists = service.findProductById(productId);
		
		StepVerifier.create(productLists)
			.expectNextMatches(products -> products.getName().equalsIgnoreCase("Trousers") && products.getAvailability())
			.verifyComplete();	
	}
	
	
	@Test 
	void listProductDetail_OK() {
		Integer productId = 1;
		Integer[] similarProductIds = {2};
		Product productMock = Product.builder()
				.id("2")
				.name("Trousers")
				.price(BigDecimal.valueOf(30))
				.availability(true).build();
		
		Map<String, Object> paramsSimilar = new HashMap<String, Object>();
		paramsSimilar.put("id", productId.toString());
		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
		Map<String, Object> paramsProducts = new HashMap<String, Object>();
		paramsProducts.put("id", 2);
		
		
		Mockito.when(requestHeadersUriMock.uri("product/{id}", paramsProducts)).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersUriMock.uri("product/{id}/similarids", paramsSimilar)).thenReturn(requestHeadersMock);
		
		Mockito.when(requestHeadersMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
		
		Mockito.when(responseMock.bodyToMono(Integer[].class)).thenReturn(Mono.just(similarProductIds));
		Mockito.when(responseMock.bodyToMono(Product.class)).thenReturn(Mono.just(productMock));
		
		Mono<List<Product>> productLists = service.findSimilarProducts(productId.toString());
		
		StepVerifier.create(productLists)
			.expectNextMatches(products -> products.size() == 1)
			.verifyComplete();	
	}
	
}
