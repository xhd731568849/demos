package com.example.rabbit.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @description  多数据源配置
 * @author xuhandong
 * @time 2017/7/6
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.xhd")
    public DataSource businessDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "jdbcTemplate")
    public JdbcTemplate businessTemplate(@Qualifier("dataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
