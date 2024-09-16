package com.payment.demo.payment.adapter.out.web.product.client

import com.payment.demo.payment.domain.Product
import reactor.core.publisher.Flux

interface ProductClient {
    fun findProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}