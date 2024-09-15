package com.payment.demo.payment.adapter.`in`.web.request

import java.time.LocalDateTime

data class CheckoutRequest(
    /*
    임시로 기본값 설정
     */
    val cartId: Long = 1,
    val productIds: List<Long> = listOf(1,2,3),
    val buyerId: Long = 1,
    val seed: String = LocalDateTime.now().toString(),
)