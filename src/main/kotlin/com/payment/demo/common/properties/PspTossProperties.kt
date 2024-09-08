package com.payment.demo.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "psp.toss")
data class PspTossProperties (
    var secretKey: String,
    var url: String,
)