package com.login.authentication.login;

import lombok.AllArgsConstructor;

import java.util.*;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Boolean>> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute("emailId", request.getEmailId());
        if (loginService.loginUser(request.getEmailId()))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/verify")
    @Produces("application/json")
    public ResponseEntity<Map<String, Boolean>> verifyOtp(@RequestBody String otpObj, HttpServletRequest httpServletRequest) {
        String emailId = (String) httpServletRequest.getSession().getAttribute("emailId");
        JSONObject json = new JSONObject(otpObj);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "POST");
        headers.set("Access-Control-Allow-Headers", "Content-Type");

        if (loginService.verifyOtp(json.getString("otp"), emailId))
            return new ResponseEntity<>(headers, HttpStatus.OK);
        else
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Map<String, Boolean>> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
