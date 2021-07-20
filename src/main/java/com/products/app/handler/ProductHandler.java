package com.products.app.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.products.app.service.IProductService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ProductHandler {
	
	private final IProductService service;

	public Mono<ServerResponse> listProducts(ServerRequest request){
		String id = request.pathVariable("id");
		return errorHandler(
				service.findSimilarProducts(id)
				.flatMap(p -> ServerResponse.ok()
				.contentType(APPLICATION_JSON)
				.bodyValue(p))
				// WHEN SERVER IS TOO SLOW WITH PRODUCT DETAILS (MADE BETTER STATUS POLICY)
				.switchIfEmpty(ServerResponse.status(HttpStatus.CONFLICT).build())
				);
	}
	
	private Mono<ServerResponse> errorHandler(Mono<ServerResponse> response){
		return response.onErrorResume(error -> {
			if(error instanceof WebClientResponseException) {
				WebClientResponseException errorResponse = (WebClientResponseException) error;
				Map<String, Object> body = new HashMap<>();
				body.put("error", errorResponse.getStatusCode().toString());
				body.put("timestamp", new Date());
				body.put("status", errorResponse.getStatusCode().value());
				return ServerResponse.status(errorResponse.getStatusCode()).bodyValue(body);
			} else {
				// NEED TO DISCUSS A ERROR HANDLER POLICY
				Map<String, Object> body = new HashMap<>();
				body.put("error", HttpStatus.CONFLICT.toString());
				body.put("timestamp", new Date());
				body.put("status", HttpStatus.CONFLICT.toString());
				return ServerResponse.status(HttpStatus.CONFLICT).bodyValue(body);
			}
		});
	}

}
