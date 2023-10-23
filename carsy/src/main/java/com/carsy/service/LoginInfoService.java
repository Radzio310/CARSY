package com.carsy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carsy.entity.LoginInfo;
import com.carsy.repository.LoginInfoRepository;

import java.util.Optional;

@Service
public class LoginInfoService {

    private final LoginInfoRepository loginInfoRepository;

    @Autowired
    public LoginInfoService(LoginInfoRepository loginInfoRepository) {
        this.loginInfoRepository = loginInfoRepository;
    }

    public Optional<LoginInfo> getLoginInfoByUserId(Long userId) {
        return loginInfoRepository.findById(userId);
    }

    public void addLoginInfo(LoginInfo loginInfo) {
        loginInfoRepository.save(loginInfo);
    }

    public void deleteLoginInfo(Long userId) {
        loginInfoRepository.deleteById(userId);
    }
}