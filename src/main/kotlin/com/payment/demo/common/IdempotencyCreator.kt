package com.payment.demo.common

import java.util.UUID

object IdempotencyCreator {

    fun create(data: Any): String {
        return UUID.nameUUIDFromBytes(data.toString().toByteArray()).toString() // 멱등성 키 생성
    }
}