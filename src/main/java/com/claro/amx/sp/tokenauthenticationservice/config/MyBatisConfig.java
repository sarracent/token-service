package com.claro.amx.sp.tokenauthenticationservice.config;


import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.claro.amx.sp.tokenauthenticationservice.config.MyBatisConfig.CCARD_SESSION_FACTORY;


@Configuration
@MapperScan(value = "com.claro.amx.sp.tokenauthenticationservice.mapper.ccard", sqlSessionFactoryRef = CCARD_SESSION_FACTORY)
public class MyBatisConfig {

    public static final String CCARD_SESSION_FACTORY = "ccardSessionFactory";

    @Bean(name = CCARD_SESSION_FACTORY, destroyMethod = "")
    public SqlSessionFactoryBean ccardSQLSessionFactory(@Qualifier(DataSourceConfig.CCARD_DATASOURCE) final DataSource dataSource) {
        final var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

}



