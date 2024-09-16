package com.payment.demo.payment.domain

enum class PaymentStatus(description: String){
    NOT_STARTED("결제 승인 시작 전"),
    IN_PROGRESS("결제 승인 진행 중"),
    SUCCESS("결제 승인 완료"),
    FAILURE("결제 승인 실패"),
    UNKNOWN("결제 승인 알 수 없음"),
}
