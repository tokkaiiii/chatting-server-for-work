package com.chat.chattingserverapp.client.controller;

import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import com.chat.chattingserverapp.client.service.ClientService;
import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ClientIssueTokenController(
    ClientService clientService,
    @Value("${security.jwt.secret}") String jwtSecret
) {

  @PostMapping("/client/issueToken")
  public ResponseEntity<?> issueToken(@RequestBody IssueClientToken query) {
    return clientService
        .findByUsername(query)
        .map(clientResponse -> composeToken())
        .map(AccessTokenCarrier::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());

  }

  private String composeToken() {
    return Jwts.builder()
        .signWith(new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256"))
        .compact();
  }


}
