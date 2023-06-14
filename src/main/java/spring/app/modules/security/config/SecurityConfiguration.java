package spring.app.modules.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.app.modules.security.general.filter.JwtAuthenticationFilter;
import spring.app.modules.security.general.filter.UserDataFilter;

import static spring.app.modules.security.general.SecurityDefinedConst.ENDPOINTS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public UserDataFilter userDataFilter() {
        return new UserDataFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()
                .requestMatchers(ENDPOINTS.get("ALL")).permitAll()
                .requestMatchers(HttpMethod.GET, ENDPOINTS.get("GET")).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, ENDPOINTS.get("OPTIONS")).permitAll()
                .requestMatchers(HttpMethod.POST,ENDPOINTS.get("POST")).hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,ENDPOINTS.get("PUT")).hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,ENDPOINTS.get("DELETE")).hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userDataFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);

        return http.build();
    }
}
