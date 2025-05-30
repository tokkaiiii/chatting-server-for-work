package com.chat.chattingserverapp.client.controller;

import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidPassword;
import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidUsername;

import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.response.ClientResponse;
import com.chat.chattingserverapp.client.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ClientLoginController(
    ClientService clientService
) {

  @PostMapping("/client/login")
  public ResponseEntity<?> login(@RequestBody LoginClientCommand command) {
    if (!isCommandValid(command)) {
      return ResponseEntity.badRequest().build();
    }

    var response = clientService.login(command);
    return ResponseEntity.ok(response);
  }

  private static boolean isCommandValid(LoginClientCommand command) {
    return isValidUsername(command.username())
        && isValidPassword(command.password());
  }

}
