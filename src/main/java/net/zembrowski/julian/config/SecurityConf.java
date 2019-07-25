package net.zembrowski.julian.config;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Autowired
    BCryptPasswordEncoder encoder;


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests().antMatchers("/rejestruj").permitAll()
                .antMatchers("/hello").permitAll()
                .antMatchers("/rejestrujPoBledzie").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/badLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/pokarzMenu")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/badLogin");
    }



    @Autowired
    public void securityUsers(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login,haslo,enabled FROM uzytkownik WHERE login = ?")
                .authoritiesByUsernameQuery("SELECT username,role FROM role WHERE username = ?")
                .passwordEncoder(encoder);


    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/webjars/**");
    }

}
