package com.carsy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.carsy.entity.LoginInfo;
import com.carsy.service.LoginInfoService;

@RestController
@RequestMapping("/api/login-info")
public class LoginInfoController {

    private final LoginInfoService loginInfoService;

    @Autowired
    public LoginInfoController(LoginInfoService loginInfoService) {
        this.loginInfoService = loginInfoService;
    }

    @GetMapping("/{userId}")
    public LoginInfo getLoginInfoByUserId(@PathVariable Long userId) {
        return loginInfoService.getLoginInfoByUserId(userId).orElse(null);
    }

    @PostMapping
    public void addLoginInfo(@RequestBody LoginInfo loginInfo) {
        loginInfoService.addLoginInfo(loginInfo);
    }

    @DeleteMapping("/{userId}")
    public void deleteLoginInfo(@PathVariable Long userId) {
        loginInfoService.deleteLoginInfo(userId);
    }
}