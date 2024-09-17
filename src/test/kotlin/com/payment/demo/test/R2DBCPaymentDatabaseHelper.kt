package com.payment.demo.test

import com.payment.demo.payment.domain.PaymentEvent
import com.payment.demo.payment.domain.PaymentOrder
import com.payment.demo.payment.domain.PaymentStatus
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal

class R2DBCPaymentDatabaseHelper(
    private val databaseClient: DatabaseClient,
    private val transactionalOperator: TransactionalOperator,
) : PaymentDatabaseHelper {
    override fun findPayments(orderId: String): PaymentEvent? {
        return databaseClient.sql(SELECT_PAYMENT_QUERY)
            .bind("orderId", orderId)
            .fetch()
            .all()
            .groupBy { it["payment_event_id"] as Long }
            .flatMap { groupedFlux ->
                groupedFlux.collectList().map { results ->
                    PaymentEvent(
                        id = groupedFlux.key(),
                        orderId = results.first()["order_id"] as String,
                        orderName = results.first()["order_name"] as String,
                        buyerId = results.first()["buyer_id"] as Long,
                        isPaymentDone = ((results.first()["is_payment_done"] as Int) == 1),
                        paymentOrders = results.map { row ->
                            PaymentOrder(
                                id = row["id"] as Long,
                                paymentEventId = groupedFlux.key(),
                                sellerId = row["seller_id"] as Long,
                                buyerId = row["buyer_id"] as Long,
                                orderId = row["order_id"] as String,
                                productId = row["product_id"] as Long,
                                amount = row["amount"] as BigDecimal,
                                paymentStatus = PaymentStatus.get(row["payment_order_status"] as String),
                                isLedgerUpdated = ((row["is_ledger_updated"] as Int) == 1),
                                isWalletUpdated = ((row["is_wallet_updated"] as Int) == 1),
                            )
                        }
                    )

                }
            }
            .toMono()
            .block()
    }

    override fun cleanUp(): Mono<Void> {
        return deletePaymentOrders()
            .flatMap { deletePaymentEvent() }
            .`as`(transactionalOperator::transactional)
            .then()
    }

    private fun deletePaymentOrders(): Mono<Long> {
        return databaseClient.sql(DELETE_PAYMENT_ORDER_QUERY)
            .fetch()
            .rowsUpdated()
    }

    private fun deletePaymentEvent(): Mono<Long> {
        return databaseClient.sql(DELETE_PAYMENT_EVENT_QUERY)
            .fetch()
            .rowsUpdated()
    }

    companion object {
        val SELECT_PAYMENT_QUERY = """
            SELECT * FROM payment_events pe
             INNER JOIN payment_orders po ON pe.order_id = po.order_id
             WHERE pe.order_id = :orderId
        """.trimIndent()

        val DELETE_PAYMENT_EVENT_QUERY = """
            DELETE FROM payment_events
        """.trimIndent()

        val DELETE_PAYMENT_ORDER_QUERY = """
            DELETE FROM payment_orders
        """.trimIndent()
    }
}