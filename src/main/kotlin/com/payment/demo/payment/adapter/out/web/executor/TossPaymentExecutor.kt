package com.payment.demo.payment.adapter.out.web.executor

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * 실제로 외부로 API 요청을 보내는 Toss Payments Executor
 */
@Component
class TossPaymentExecutor (
    private val tossPaymentClient: WebClient,
    private val uri: String = "/v1/payments/confirm"
){
    fun execute(paymentKey: String, orderId: String, amount: String): Mono<String> {
        return tossPaymentClient.post()
            .uri(uri)
            .bodyValue(
                mapOf(
                    "paymentKey" to paymentKey,
                    "orderId" to orderId,
                    "amount" to amount
                )
            )
            .retrieve()
            .bodyToMono(String::class.java)
    }
}