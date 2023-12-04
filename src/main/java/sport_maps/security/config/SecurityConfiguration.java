package sport_maps.security.config;

import sport_maps.security.general.SecurityDefinedConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sport_maps.security.general.filter.JwtAuthenticationFilter;
import sport_maps.security.general.filter.UserDataFilter;

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
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityDefinedConst.ALL).permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityDefinedConst.GET).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, SecurityDefinedConst.OPTIONS).permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityDefinedConst.POST).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, SecurityDefinedConst.PUT).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, SecurityDefinedConst.DELETE).hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userDataFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint));

        return http.build();
    }
}
