package com.payment.demo.payment.adapter.out.web.product

import com.payment.demo.common.annotation.WebAdapter
import com.payment.demo.payment.application.port.out.LoadProductPort
import com.payment.demo.payment.domain.Product
import reactor.core.publisher.Flux


@WebAdapter
class ProductWebAdapter: LoadProductPort {
    override fun findProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        
    }
}