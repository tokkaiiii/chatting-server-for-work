package com.chat.chattingserverapp.client.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Client {


  @Id @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Getter
  @Column(unique = true)
  private String username;

  private String hashedPassword;

  @Getter
  private final LocalDateTime createdAt = LocalDateTime.now();

  private Client(String username, String hashedPassword) {
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public static Client of(String username, String hashedPassword) {
    return new Client(username, hashedPassword);
  }

  public boolean decodePassword(PasswordEncoder encoder,String password) {
    return encoder.matches(password, this.hashedPassword);
  }
}
