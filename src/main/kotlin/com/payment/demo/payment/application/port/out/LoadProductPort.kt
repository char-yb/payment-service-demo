package com.payment.demo.payment.application.port.out

import com.payment.demo.payment.domain.Product
import reactor.core.publisher.Flux

// 어플리케이션 내부 로직이 외부에 접근하기 위해 만든 인터페이스
interface LoadProductPort {

    fun findProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}