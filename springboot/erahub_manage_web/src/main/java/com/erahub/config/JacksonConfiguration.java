package com.erahub.config;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lipeng
 * @Date 2022/5/11 18:24
 * @Version 1.0
 **/

@Configuration
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

}
