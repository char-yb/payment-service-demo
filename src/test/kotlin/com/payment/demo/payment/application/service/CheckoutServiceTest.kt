package com.payment.demo.payment.application.service

import com.payment.demo.payment.application.port.`in`.CheckoutCommand
import com.payment.demo.payment.application.port.`in`.CheckoutUseCase
import com.payment.demo.test.PaymentDatabaseHelper
import com.payment.demo.test.PaymentTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
@Import(PaymentTestConfiguration::class)
class CheckoutServiceTest (
    @Autowired private val checkoutUseCase: CheckoutUseCase,
    @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper,
) {

    @BeforeEach
    fun setUp() {
        paymentDatabaseHelper.cleanUp().block()
    }
    
    @Test
    fun `should save PaymentEvent and PaymentOrder successfully` () {
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
            cartId = 1,
            buyerId = 1,
            productIds = listOf(1,2,3),
            idempotencyKey = orderId
        )

        // TODO: StepVerifier에 대해 공부 필요
        StepVerifier.create(checkoutUseCase.checkout(checkoutCommand))
            .expectNextMatches {
                it.amount.toInt() == 60000 && it.orderId == orderId
            }
            .verifyComplete()

        val paymentEvent = paymentDatabaseHelper.findPayments(orderId)!!

        assertNotNull(paymentEvent)
        assertThat(paymentEvent.totalAmount()).isEqualTo(60000)
        assertThat(paymentEvent.orderId).isEqualTo(orderId)
        assertThat(paymentEvent.paymentOrders.size).isEqualTo(checkoutCommand.productIds.size)
        assertFalse(paymentEvent.isPaymentDone())
        assertThat(paymentEvent.paymentOrders.all { !it.isLedgerUpdated() })
        assertThat(paymentEvent.paymentOrders.all { !it.isWalletUpdated() })
    }

    @Test
    fun `should fail to save PaymentEvent and PaymentOrder when trying to save for the second time` () {
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
            cartId = 1,
            buyerId = 1,
            productIds = listOf(1,2,3),
            idempotencyKey = orderId
        )

        checkoutUseCase.checkout(checkoutCommand).block()
        assertThrows<DataIntegrityViolationException> {
            checkoutUseCase.checkout(checkoutCommand).block()
        }
    }
}