package com.payment.demo.payment.application.port.`in`

// CheckoutRequest와 유사하지만 멱등성을 보장하기 위한 key-value pair로 구성
data class CheckoutCommand (
    val cartId: Long,
    val buyerId: Long,
    val productIds: List<Long>,
    val idempotencyKey: String, // 멱등성 키
)