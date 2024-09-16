package com.payment.demo.payment.adapter.`in`.web.view

import com.payment.demo.common.IdempotencyCreator
import com.payment.demo.common.annotation.WebAdapter
import com.payment.demo.payment.adapter.`in`.web.request.CheckoutRequest
import com.payment.demo.payment.application.port.`in`.CheckoutCommand
import com.payment.demo.payment.application.port.`in`.CheckoutUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@WebAdapter
@Controller
class CheckoutController (
    private val checkoutUseCase: CheckoutUseCase
){

    @GetMapping
    fun checkout(request: CheckoutRequest, model: Model): Mono<String> {
        val command = CheckoutCommand(
            cartId = request.cartId,
            buyerId = request.buyerId,
            productIds = request.productIds,
            idempotencyKey = IdempotencyCreator.create(request.seed)
        )

        return checkoutUseCase.checkout(command).map {
            model.addAttribute("orderId", it.orderId)
            model.addAttribute("orderName", it.orderName)
            model.addAttribute("amount", it.amount)
            "checkout"
        }
    }
}