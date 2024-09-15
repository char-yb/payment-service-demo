package com.payment.demo.payment.adapter.`in`.web.view

import com.payment.demo.common.annotation.WebAdapter
import com.payment.demo.payment.adapter.`in`.web.request.CheckoutRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@WebAdapter
@Controller
class CheckoutController {

    @GetMapping
    fun checkout(checkoutRequest: CheckoutRequest): Mono<String> {
        return Mono.just("checkout")
    }
}