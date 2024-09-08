package com.payment.demo.common.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(
    PspTossProperties::class,
)
@Configuration
class PropertiesConfiguration