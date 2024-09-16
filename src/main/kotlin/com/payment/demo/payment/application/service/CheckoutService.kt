package com.payment.demo.payment.application.service

import com.payment.demo.common.annotation.UseCase
import com.payment.demo.payment.application.port.`in`.CheckoutCommand
import com.payment.demo.payment.application.port.`in`.CheckoutUseCase
import com.payment.demo.payment.application.port.out.LoadProductPort
import com.payment.demo.payment.application.port.out.SavePaymentPort
import com.payment.demo.payment.domain.*
import reactor.core.publisher.Mono

@UseCase
class CheckoutService(
    private val loadProductPort: LoadProductPort,
    private val savePaymentPort: SavePaymentPort
) : CheckoutUseCase {
    override fun checkout(command: CheckoutCommand): Mono<CheckoutResult> {
        return loadProductPort.findProducts(command.cartId, command.productIds)
            .collectList()
            .map {
                // 이를 기반으로 paymentEvent와 order를 생성한다.
                createPaymentEvent(command, it)
            }
            .flatMap { savePaymentPort.save(it).thenReturn(it) }
            .map {
                CheckoutResult(
                    amount = it.totalAmount(),
                    orderId = it.orderId,
                    orderName = it.orderName,
                    )
            }
    }

    private fun createPaymentEvent(command: CheckoutCommand, products: List<Product>) : PaymentEvent {
        return PaymentEvent(
            buyerId = command.buyerId,
            orderId = command.idempotencyKey,
            orderName = products.joinToString { it.name },
            paymentOrders =  products.map {
                PaymentOrder(
                    sellerId = it.sellerId,
                    buyerId = command.buyerId,
                    orderId = command.idempotencyKey,
                    productId = it.id,
                    amount = it.amount,
                    paymentStatus = PaymentStatus.NOT_STARTED
                )
            }
        )
    }
}