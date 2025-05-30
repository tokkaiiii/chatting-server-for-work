package com.chat.chattingserverapp.api.controller;

import com.chat.chattingserverapp.command.CreateClientCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ClientSignupController(

) {

  @PostMapping("/client/signup")
  public ResponseEntity<?> signup(@RequestBody CreateClientCommand command) {
    String usernameRegex = "^[가-힣0-9\\-_]{3,}$";
    if (command.username() == null) {
      return ResponseEntity.badRequest().build();
    }else if(command.username().matches(usernameRegex) == false){
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.created(null).build();
  }

}
