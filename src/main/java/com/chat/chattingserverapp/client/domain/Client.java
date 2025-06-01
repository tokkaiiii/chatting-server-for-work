package com.chat.chattingserverapp.client.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Client {


  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long dataKey;

  @Getter
  @Column(unique = true)
  private UUID id;

  @Column(unique = true)
  @Getter
  private String email;

  @Getter
  @Column(unique = true)
  private String username;

  private String hashedPassword;

  @Getter
  private final LocalDateTime createdAt = LocalDateTime.now();

  private Client(UUID id, String email, String username, String hashedPassword) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public static Client of(UUID id, String email, String username, String hashedPassword) {
    return new Client(id, email, username, hashedPassword);
  }

  public boolean decodePassword(PasswordEncoder encoder, String password) {
    return encoder.matches(password, this.hashedPassword);
  }
}
