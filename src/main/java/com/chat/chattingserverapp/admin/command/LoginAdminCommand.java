package com.chat.chattingserverapp.admin.command;

public record LoginAdminCommand(
    String email,
    String password
) {

}
