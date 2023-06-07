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
import spring.app.modules.security.general.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**"
    };

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()
                .requestMatchers("/sport-maps/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, AUTH_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/sport-maps/v1/news/new/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/sport-maps/v1/news/update/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/sport-maps/v1/news/delete/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/sport-maps/v1/news/photo/upload/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/sport-maps/v1/events/new/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/sport-maps/v1/events/update/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/sport-maps/v1/events/delete/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/sport-maps/v1/events/photo/upload/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/sport-maps/v1/coaches/new/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/sport-maps/v1/coaches/update/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/sport-maps/v1/coaches/delete/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/sport-maps/v1/coaches/photo/upload/**").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
