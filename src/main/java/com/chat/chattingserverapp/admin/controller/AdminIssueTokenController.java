package com.chat.chattingserverapp.admin.controller;

import com.chat.chattingserverapp.admin.query.IssueAdminToken;
import com.chat.chattingserverapp.admin.response.AdminResponse;
import com.chat.chattingserverapp.admin.service.AdminService;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import com.chat.chattingserverapp.security.JwtKeyHolder;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record AdminIssueTokenController(
    AdminService adminService,
    JwtKeyHolder jwtKeyHolder
) {

  @PostMapping("/admin/issueToken")
  public ResponseEntity<?> issueToken(@RequestBody IssueAdminToken query) {
    return adminService.findByEmail(query.email(),query.password())
        .map(this::composeToken)
        .map(AccessTokenCarrier::new)
        .map(ResponseEntity::ok)
        .orElseGet(ResponseEntity.badRequest()::build);

//    return new AccessTokenCarrier(composeToken());
  }

  private String composeToken(AdminResponse adminResponse) {
    return Jwts.builder()
        .setSubject(adminResponse.id().toString())
        .signWith(jwtKeyHolder.key())
        .compact();
  }
}
