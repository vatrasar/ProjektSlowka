package net.zembrowski.julian.config;


import net.zembrowski.julian.domain.Uzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;


@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests().antMatchers("/rejestruj").permitAll()
                .antMatchers("/hello").permitAll()
                .antMatchers("/rejestrujPoBledzie").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/pokarzMenu");
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void securityUsers(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login,haslo,enabled FROM Uzytkownik WHERE login = ?")
                .authoritiesByUsernameQuery("SELECT username,role FROM ROLE WHERE username = ?");


    }

}
