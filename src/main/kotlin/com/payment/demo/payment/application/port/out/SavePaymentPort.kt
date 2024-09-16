package com.payment.demo.payment.application.port.out

import com.payment.demo.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface SavePaymentPort {
    fun save(paymentEvent: PaymentEvent): Mono<Void>
}