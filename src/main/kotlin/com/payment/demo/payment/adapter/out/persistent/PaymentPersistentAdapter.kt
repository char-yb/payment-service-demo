package com.payment.demo.payment.adapter.out.persistent

import com.payment.demo.common.annotation.PersistentAdapter
import com.payment.demo.payment.adapter.out.persistent.repository.PaymentRepository
import com.payment.demo.payment.application.port.out.SavePaymentPort
import com.payment.demo.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository
) : SavePaymentPort {
    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }
}