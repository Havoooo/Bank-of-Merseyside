package com.banking.BackOfficeSystem.controller;

import com.common.BankData.dao.AdminDao;
import com.common.BankData.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@RequestMapping("/login/api")
@RestController
public class AdminLoginController {

    @Autowired
    AdminDao adminDao;

    @PostMapping("/secured/token")
    public ResponseEntity generateToken(@RequestHeader("Authorization") String authString, HttpServletRequest rs, HttpServletResponse res) throws Exception {
        System.out.println(authString);
        String decodedAuth = "";
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            return new ResponseEntity<>("BAD REQUEST", new HttpHeaders(), HttpStatus.BAD_REQUEST);

        }
        decodedAuth = new String(bytes);
        System.out.println(decodedAuth);

        String username = decodedAuth.split(":")[0];
        String enteredPassword = decodedAuth.split(":")[1];
        username = username.toLowerCase();

        Admin admin = adminDao.findByUserName(username);
        if (admin == null) {
            return new ResponseEntity<>("Username not found", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        } else {
            if (admin.getPasswordHash().equals(enteredPassword)) {
                String token = UUID.randomUUID().toString();
                admin.setToken(token);
                adminDao.save(admin);
                return ResponseEntity.ok(admin);
            } else {
                return new ResponseEntity<>("Username or password is Wrong", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        }
    }

}
