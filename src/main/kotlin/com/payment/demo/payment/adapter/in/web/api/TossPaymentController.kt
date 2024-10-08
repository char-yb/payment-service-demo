package com.payment.demo.payment.adapter.`in`.web.api

import com.payment.demo.common.annotation.WebAdapter
import com.payment.demo.payment.adapter.`in`.web.request.TossPaymentConfirmRequest
import com.payment.demo.payment.adapter.`in`.web.response.ApiResponse
import com.payment.demo.payment.adapter.out.web.toss.executor.TossPaymentExecutor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@WebAdapter
@RequestMapping("/v1/toss")
@RestController
class TossPaymentController (
    private val tossPaymentExecutor: TossPaymentExecutor
){

    @PostMapping("/confirm")
    fun confirm(
        @RequestBody
        request: TossPaymentConfirmRequest
    ): Mono<ResponseEntity<ApiResponse<String>>> {
        return tossPaymentExecutor.execute(
            request.paymentKey,
            request.orderId,
            request.amount.toString()
        ).map {
            ResponseEntity.ok().body(
                ApiResponse.with(
                    httpStatus = HttpStatus.OK,
                    message = "결제가 완료되었습니다.",
                    data = it
                )
            )
        }
    }
}