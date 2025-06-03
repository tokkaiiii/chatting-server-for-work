package com.chat.chattingserverapp.admin.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class Admin {

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

  protected Admin() {
  }

  private Admin(UUID id, String email, String username, String hashedPassword) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public static Admin of(UUID id, String email, String username, String hashedPassword) {
    return new Admin(id, email, username, hashedPassword);
  }

  public boolean decodePassword(PasswordEncoder encoder, String password) {
    return encoder.matches(password, this.hashedPassword);
  }
}
