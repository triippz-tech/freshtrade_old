package com.triippztech.freshtrade.config;

import com.triippztech.freshtrade.security.*;
import com.triippztech.freshtrade.security.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        CorsFilter corsFilter,
        JHipsterProperties jHipsterProperties,
        SecurityProblemSupport problemSupport
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/**")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; fullscreen 'self'; payment 'none'")
        .and()
            .frameOptions()
            .deny()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            // Items
            .antMatchers(HttpMethod.GET, "/api/items/**").permitAll()
            .antMatchers(HttpMethod.PUT, "/api/items/{\\w+}/reserve").hasAuthority(AuthoritiesConstants.BUYER)
            .antMatchers(HttpMethod.GET, "/api/_search/items").permitAll()
            .antMatchers(HttpMethod.GET, "/api/items/seller").hasAuthority(AuthoritiesConstants.SELLER)
            .antMatchers(HttpMethod.POST, "/api/items/seller").hasAuthority(AuthoritiesConstants.SELLER)
            .antMatchers(HttpMethod.POST, "/api/items").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers(HttpMethod.PATCH, "/api/items/{id}").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers(HttpMethod.PUT, "/api/items/{id}").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers(HttpMethod.DELETE, "/api/items/{id}").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.SELLER)
            // Categories
            .antMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
            // Countries
            .antMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
            // User Locations
            .antMatchers(HttpMethod.GET, "/api/user-location/**").permitAll()
            //Reservations
            .antMatchers(HttpMethod.GET, "/api/reservations/seller").hasAuthority(AuthoritiesConstants.SELLER)
            .antMatchers(HttpMethod.GET, "/api/reservations/buyer").hasAuthority(AuthoritiesConstants.BUYER)
            .antMatchers(HttpMethod.GET, "/api/reservations/{id}/seller/cancel").hasAuthority(AuthoritiesConstants.SELLER)
            .antMatchers(HttpMethod.GET, "/api/reservations/{id}/buyer/cancel").hasAuthority(AuthoritiesConstants.BUYER)
            .antMatchers(HttpMethod.GET, "/api/reservations/buyer/redeem/{reservationNumber}").hasAuthority(AuthoritiesConstants.BUYER)
            // Seller Metrics
            .antMatchers( "/api/metrics/seller/**").hasAuthority(AuthoritiesConstants.SELLER)

            .antMatchers("/api/**").authenticated()
            .antMatchers("/websocket/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .httpBasic()
        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
