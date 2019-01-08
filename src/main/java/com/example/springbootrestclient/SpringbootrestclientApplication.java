package com.example.springbootrestclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpringbootrestclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootrestclientApplication.class, args);
    }

    //이렇게 bean설정하면 기본 baseurl 전역설정
   /* @Bean
    public WebClientCustomizer webClientCustomizer(){
        return new WebClientCustomizer() {
            @Override
            public void customize(WebClient.Builder webClientBuilder) {
                webClientBuilder.baseUrl("http://localhost:8080");
            }
        };
    }*/

   @Bean
    public RestTemplateCustomizer restTemplateCustomizer(){
    return new RestTemplateCustomizer() {
        @Override
        public void customize(RestTemplate restTemplate) {
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            //이렇게하면 자바에 있는 httpconnetion을 사용하는 것이 아니라 apache httpcomponentclient를 사용함
        }
    };
   }
}

