package com.payment.demo.payment.adapter.out.web.toss.config

import com.payment.demo.common.properties.PspTossProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.util.*

@Configuration
class TossWebClientConfiguration (
    private val pspTossProperties: PspTossProperties
){

    @Bean
    fun tossPaymentWebClient(): WebClient {
        // Toss Payment API를 호출하기 위해서는 인증 정보를 제공해야 하는데, 이걸 위해 Secret Key를 Base64로 인코딩하여 요청 헤더에 추가한다.
        val encodeToString = Base64.getEncoder().encodeToString("${pspTossProperties.secretKey}:".toByteArray())
        return WebClient.builder()
            .baseUrl(pspTossProperties.url)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic $encodeToString")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(reactorClientHttpConnector())
            .codecs { it.defaultCodecs()}
            .build()
    }

    private fun reactorClientHttpConnector(): ClientHttpConnector {
        val provider = ConnectionProvider.builder("toss-payment")
            .build()
        return ReactorClientHttpConnector(HttpClient.create(provider))
    }
}