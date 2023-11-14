package sport_maps.smap.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GoogleGeoApi {

    @Bean
    public RestTemplate googleApi(RestTemplateBuilder builder) {
        return builder
                .rootUri("https://maps.googleapis.com/maps/api/geocode")
                .setConnectTimeout(Duration.ofSeconds(4))
                .setReadTimeout(Duration.ofSeconds(4))
                .build();
    }
}
