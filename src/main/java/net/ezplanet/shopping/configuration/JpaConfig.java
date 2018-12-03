package net.ezplanet.shopping.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan (basePackages = "net.ezplanet.shopping.data")
@PropertySource("classpath:application.properties")
public class JpaConfig {
}
