package com.payment.demo.payment.adapter.`in`.web.view

import com.payment.demo.common.annotation.WebAdapter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

/**
 * 외부의 웹 요청을 받아서 어플리케이션으로 요청을 전달하는 역할을 담당하는 웹 어댑터 역할
 */

@Controller
@WebAdapter
@RequestMapping("/v1/toss")
class PaymentController {

    @GetMapping("/success")
    fun successPage(): Mono<String> {
        return Mono.just("success")
    }

    @GetMapping("/fail")
    fun failPage(): Mono<String> {
        return Mono.just("fail")
    }
}