package com.payment.demo.test

import com.payment.demo.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentDatabaseHelper {

    fun findPayments(orderId: String): PaymentEvent?

    fun cleanUp() : Mono<Void>
}