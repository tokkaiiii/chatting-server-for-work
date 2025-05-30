package com.chat.chattingserverapp.security.config;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

import com.chat.chattingserverapp.security.JwtKeyHolder;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

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
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder)))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/error").permitAll()
            .requestMatchers("/client/signup").permitAll()
            .requestMatchers("/client/login").permitAll()
            .requestMatchers("/client/issueToken").permitAll()
            .requestMatchers("/client/me").authenticated()
            .requestMatchers("/chat/rooms").authenticated()
        )

    ;

    return http.build();
  }

}
