package com.payment.demo.payment.adapter.out.persistent.repository

import com.payment.demo.payment.domain.PaymentEvent
import org.hibernate.sql.Insert
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import java.math.BigInteger

@Repository
class R2DBCPaymentRepository(
    private val databaseClient: DatabaseClient,
    // Reactive Transaction 처리를 위한 TransactionalOperator 생성자 주입
    private val transactionalOperator: TransactionalOperator
) : PaymentRepository {
    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return insertPaymentEvent(paymentEvent)
            .flatMap { selectPaymentEventId() }
            .flatMap { paymentEventId -> insertPaymentOrders(paymentEvent, paymentEventId) }
            // 해당 명령들을 하나의 트랜잭션으로 처리하게끔 처리
            // 이걸 하지 않으면 각각의 SQL 명령들은 별도의 트랜잭션 처리가 된다.
            .`as`(transactionalOperator::transactional)
            .then()
    }

    private fun insertPaymentEvent(paymentEvent: PaymentEvent): Mono<Long> {
        return databaseClient.sql(INSERT_PAYMENT_EVENT_QUERY)
            .bind("buyerId", paymentEvent.buyerId)
            .bind("orderName", paymentEvent.orderName)
            .bind("orderId", paymentEvent.orderId)
            .fetch()
            .rowsUpdated()
    }

    private fun selectPaymentEventId() = databaseClient.sql(LAST_INSERT_ID_QUERY)
        .fetch()
        .first()
        .map {
            (it["LAST_INSERT_ID()"] as BigInteger).toLong()
        }


    private fun insertPaymentOrders(
        paymentEvent: PaymentEvent,
        paymentEventId: Long
    ): Mono<Long> {
        val valueClauses = paymentEvent.paymentOrders.joinToString(",") { paymentOrder ->
            "($paymentEventId, ${paymentOrder.sellerId}, '${paymentOrder.orderId}', '${paymentOrder.productId}', ${paymentOrder.amount}, '${paymentOrder.paymentStatus}')"
        }
        return databaseClient.sql(INSERT_PAYMENT_ORDER_QUERY(valueClauses))
            .fetch()
            .rowsUpdated()
    }

    companion object {
        val INSERT_PAYMENT_EVENT_QUERY = """
            INSERT INTO payment_events (buyer_id, order_name, order_id)
            VALUES (:buyerId, :orderName, :orderId)
        """.trimIndent()

        val LAST_INSERT_ID_QUERY = """
            SELECT LAST_INSERT_ID()
        """.trimIndent()

        // 하나의 insert 쿼리로 여러 개의 데이터들을 한 번에 삽입하는 코드(bulk insert)
        val INSERT_PAYMENT_ORDER_QUERY = fun (valueClauses: String) = """
            INSERT INTO payment_orders (payment_event_id, seller_id, order_id, product_id, amount, payment_status)
            VALUES $valueClauses
        """.trimIndent()
    }
}