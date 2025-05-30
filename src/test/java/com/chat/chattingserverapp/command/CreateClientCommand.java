package com.chat.chattingserverapp.command;

public record CreateClientCommand(
    String username,
    String password
) {

}
