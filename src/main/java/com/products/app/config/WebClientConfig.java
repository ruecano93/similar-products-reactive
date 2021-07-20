package com.products.app.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Value("${config.webclient.timeout}")
	private Long timeout;
	
	@Bean
	public WebClient registerWebClient() {
		HttpClient client = HttpClient.create()
				  .responseTimeout(Duration.ofSeconds(timeout));
		return WebClient.builder()
				.baseUrl(url)
				.clientConnector(new ReactorClientHttpConnector(client))
				.build();
	}

}
