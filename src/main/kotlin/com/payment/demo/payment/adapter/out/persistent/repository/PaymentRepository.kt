package com.payment.demo.payment.adapter.out.persistent.repository

import com.payment.demo.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentRepository {

    fun save(paymentEvent: PaymentEvent): Mono<Void>
}