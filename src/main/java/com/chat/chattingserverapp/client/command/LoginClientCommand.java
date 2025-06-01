package com.chat.chattingserverapp.client.command;

public record LoginClientCommand(
    String email,
    String password
) {

}
