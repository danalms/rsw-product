package com.rsw.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Enable global method security for the REST APIs
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * permit swagger UI and reveal of the API contract for unauthenticated users
     * invoking the API still requires an oauth2 token; @ApiImplicitParams on the endpoint makes a placeholder for that
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**");
    }

    /**
     * this is mostly redundant with default behavior, but explicitly disabling csrf here temporarily
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

}
