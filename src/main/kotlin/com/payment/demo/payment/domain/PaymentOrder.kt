package com.payment.demo.payment.domain

import java.math.BigDecimal

data class PaymentOrder (
    val id: Long? = null,
    val paymentEventId: Long? = null,
    val sellerId: Long,
    val buyerId: Long,
    val productId: Long,
    val orderId: String,
    val amount: BigDecimal,
    val paymentStatus: PaymentStatus,
    // flag variable로 외부에서 마음대로 변경하지 못하는 private 변수를 사용한다.
    private var isLedgerUpdated: Boolean = false,
    private var isWalletUpdated: Boolean = false,
) {
    fun isLedgerUpdated(): Boolean = isLedgerUpdated
    fun isWalletUpdated(): Boolean = isWalletUpdated
}
