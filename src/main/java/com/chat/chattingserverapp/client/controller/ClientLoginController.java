package com.chat.chattingserverapp.client.controller;

import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isEmailValid;
import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidPassword;

import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public record ClientLoginController(
    ClientService clientService
) {

  @PostMapping("/client/login")
  public ResponseEntity<?> login(@RequestBody LoginClientCommand command) {
    log.info("login command: {}", command);

    if (!isCommandValid(command)) {
      return ResponseEntity.badRequest().build();
    }

    return clientService.login(command)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(401).build());
  }

  private static boolean isCommandValid(LoginClientCommand command) {
    return isEmailValid(command.email())
        && isValidPassword(command.password());
  }

}
