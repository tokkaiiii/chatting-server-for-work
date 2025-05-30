package com.chat.chattingserverapp.client.controller;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.service.ClientService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ClientSignupController(
    ClientService clientService
) {

  public static final String USERNAME_REGEX = "^[가-힣0-9\\-_]{3,}$";

  @PostMapping("/client/signup")
  public ResponseEntity<?> signup(@RequestBody CreateClientCommand command) {
    try {
    clientService.register(command);
    }catch (DataIntegrityViolationException e){
      return ResponseEntity.badRequest().build();
    }

    if (command.username() == null) {
      return ResponseEntity.badRequest().build();
    } else if (command.username().matches(USERNAME_REGEX) == false) {
      return ResponseEntity.badRequest().build();
    }else if (command.password() == null) {
      return ResponseEntity.badRequest().build();
    }else if (command.password().length() < 8) {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.created(null).build();
  }

}
