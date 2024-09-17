package com.payment.demo.payment.domain

enum class PaymentStatus(description: String){
    NOT_STARTED("결제 승인 시작 전"),
    IN_PROGRESS("결제 승인 진행 중"),
    SUCCESS("결제 승인 완료"),
    FAILURE("결제 승인 실패"),
    UNKNOWN("결제 승인 알 수 없음"),;

    companion object {
        fun get(status: String): PaymentStatus {
            return entries.find { it.name == status } ?: throw IllegalArgumentException("PaymentStatus: $status 는 올바르지 않는 결제 상태입니다.")
        }
    }
}
