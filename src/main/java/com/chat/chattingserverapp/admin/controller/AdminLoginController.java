package com.chat.chattingserverapp.admin.controller;

import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidEmail;
import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidPassword;

import com.chat.chattingserverapp.admin.command.LoginAdminCommand;
import com.chat.chattingserverapp.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record AdminLoginController(
    AdminService adminService
) {

  @PostMapping("/admin/login")
  public ResponseEntity<?> login(@RequestBody LoginAdminCommand command) {
    if(isCommandValid(command) == false){
      return ResponseEntity.badRequest().build();
    }

    return adminService.findByEmail(command.email(), command.password())
        .map(ResponseEntity::ok)
        .orElseGet(ResponseEntity.status(401)::build);
  }

  private boolean isCommandValid(LoginAdminCommand command) {
    return isValidEmail(command.email())
        && isValidPassword(command.password());
  }
}
