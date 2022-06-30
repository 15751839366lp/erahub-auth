package com.erahub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @Author lipeng
 * @Date 2020/8/9 11:49
 * @Version 1.0
 **/
@Configuration
@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
public class FdfsConfiguration {

}
