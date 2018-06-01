package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/oauth/")
public class AuthController {
    @Autowired
    private DefaultTokenServices tokenServices;

    @RequestMapping(value = "revoke-token", method = RequestMethod.GET)
    public ResponseEntity logout(@RequestHeader(value = "Authorization") String authorization) {
        try {
            String tokenValue = authorization.replace("Bearer", "").trim();
            this.tokenServices.revokeToken(tokenValue);
            return CommonResponseEntity.OkResponseEntity("SUCCESS");
        } catch (Exception e) {
            return CommonResponseEntity.OkResponseEntity("ERROR");
        }
    }
}
