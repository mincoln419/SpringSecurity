package com.ideatec.resroucerun.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController
{

    @GetMapping("/api/user")
    public Authentication getUser(Authentication authentication){
        return authentication;
    }
}
