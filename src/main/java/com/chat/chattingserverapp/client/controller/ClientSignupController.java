package com.chat.chattingserverapp.client.controller;

import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidPassword;
import static com.chat.chattingserverapp.client.validator.ClientPropertyValidator.isValidUsername;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ClientSignupController(
    ClientService clientService
) {

  @PostMapping("/client/signup")
  public ResponseEntity<?> signup(@RequestBody CreateClientCommand command) {

    if (!isCommandValid(command)) {
      return ResponseEntity.badRequest().build();
    }

    var register = clientService.register(command);
    return ResponseEntity.created(null).body(register);
  }

  private static boolean isCommandValid(CreateClientCommand command) {
    return isValidUsername(command.username())
        && isValidPassword(command.password());
  }

}
