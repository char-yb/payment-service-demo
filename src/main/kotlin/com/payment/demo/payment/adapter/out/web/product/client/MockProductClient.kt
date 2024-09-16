package com.payment.demo.payment.adapter.out.web.product.client

import com.payment.demo.payment.domain.Product
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.math.BigDecimal

// 결제 기능에 좀 집중하기 위해서 상품 정보를 제공해주는 프로덕트 서비스를 구현하지는 않을 것이다.
// 실제 서비스를 흉내내는 MockProductClient를 구현한다.
@Component
class MockProductClient : ProductClient {
    override fun findProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        return Flux.fromIterable(
            productIds.map {
                Product(
                    id = it,
                    amount = BigDecimal(it * 10000),
                    quantity = 2,
                    name =  "test_product_$it",
                    sellerId = 1,
                )
            }
        )
    }
}