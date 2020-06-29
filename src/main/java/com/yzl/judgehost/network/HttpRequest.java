package com.yzl.judgehost.network;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * @author yuzhanglong
 * @description 网络请求的封装
 * @date 2020-6-29 19:52:26
 */
public class HttpRequest {
    public static Resource getFile(String url) {
        Mono<ClientResponse> responseMono = WebClient.create()
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchange();
        ClientResponse response = responseMono.block();
        return response.bodyToMono(Resource.class).block();
    }
}
