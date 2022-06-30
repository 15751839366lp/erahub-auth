package com.erahub;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @Author lipeng
 * @Date 2020/12/15 13:14
 * @Version 1.0
 **/
@SpringBootApplication
@EnableTransactionManagement  //开启事务管理
@Import(FdfsClientConfig.class)
public class ErahubAuthApplication {

    public static class SpringBeanNameGenerator extends AnnotationBeanNameGenerator {
        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            return definition.getBeanClassName();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ErahubAuthApplication.class,args);
    }
}

