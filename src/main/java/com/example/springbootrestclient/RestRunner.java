package com.example.springbootrestclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestRunner implements ApplicationRunner {
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    //springbootweb에 resttemplate이 설정되어 있다. resttemplatebuilder로 빈설정 되어 있음

    @Autowired
    //webclient 주입받으려면 webflux 디펜전시 추가
    WebClient.Builder builder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RestTemplate restTemplate = restTemplateBuilder.build();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //restTemplate는 sync방식이기 때문에 한 처리가 끝나야지 다음 처리로 넘어간다.

        //TODO /hello
        String helloResult = restTemplate.getForObject("http://localhost:8080/hello", String.class);

        //TODO /world
        String worldResutlt = restTemplate.getForObject("http://localhost:8080/world", String.class);

        stopWatch.stop();
        System.out.println("restTemplate : "+stopWatch.prettyPrint());

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        //async방식
        stopWatch.start();

        Mono<String> helloMono = webClient.get().uri("/hello")
                .retrieve() //응답받아라
                .bodyToMono(String.class);//body를 string으로
        //Mono type도 stream이다. 그냥 저 타입에 받아 놓은 상태이다. 이거를 동작시키려면 subscribe로 해서 stream이라는 물이 흐르도록 해야하마
        helloMono.subscribe(s -> {
            System.out.println(s);
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
            //어떤게 먼저 끝날지 모르니, 다시 스탑워치 동작
        });

        Mono<String> worldMono = webClient.get().uri("/world")
                .retrieve() //응답받아라
                .bodyToMono(String.class);//body를 string으로
        //Mono type도 stream이다. 그냥 저 타입에 받아 놓은 상태이다. 이거를 동작시키려면 subscribe로 해서 stream이라는 물이 흐르도록 해야하마
        worldMono.subscribe(s -> {
            System.out.println(s);
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
            //어떤게 먼저 끝날지 모르니, 다시 스탑워치 동작
        });

        stopWatch.stop();

    }
}
