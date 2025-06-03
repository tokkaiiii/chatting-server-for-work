package com.chat.chattingserverapp.security.config;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

import com.chat.chattingserverapp.security.JwtKeyHolder;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return createDelegatingPasswordEncoder();
  }

  @Bean
  JwtKeyHolder jwtKeyHolder(@Value("${security.jwt.secret}") String secret) {
    SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    return new JwtKeyHolder(key);
  }

  @Bean
  public JwtDecoder jwtDecoder(JwtKeyHolder jwtKeyHolder) {
    return NimbusJwtDecoder.withSecretKey(jwtKeyHolder.key()).build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder)
      throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder)))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/view/**").permitAll()
            .requestMatchers("/client/signup").permitAll()
            .requestMatchers("/client/login").permitAll()
            .requestMatchers("/client/issueToken").permitAll()
            .requestMatchers("/admin/issueToken").permitAll()
            .requestMatchers("/ws-endpoint/**").permitAll()
            .requestMatchers("/ws/**").permitAll()
//            .requestMatchers(GET,"/chat/rooms").permitAll()
            .anyRequest().authenticated()
        )

    ;

    return http.build();
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*")); // 또는 List.of("http://localhost:3000")
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("http://localhost:3000","https://chatting-client-for-work-blex.vercel.app"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }


}
