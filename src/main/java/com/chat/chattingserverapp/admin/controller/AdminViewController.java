package com.chat.chattingserverapp.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/view")
@Controller
public record AdminViewController() {

  @GetMapping("/admin/login")
  public String adminLogin() {
    return "admin-login";
  }
} 