package net.zembrowski.julian.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class dateConf {


   @Bean
   public LocalEntityManagerFactoryBean entityManagerFactoryBean()
   {
       LocalEntityManagerFactoryBean emfb=new LocalEntityManagerFactoryBean();
       emfb.setPersistenceUnitName("baza");
       return emfb;
   }


}
