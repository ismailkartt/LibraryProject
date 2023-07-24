package com.tm3library.controller;

import com.tm3library.dto.request.RegisterRequest;
import com.tm3library.dto.request.SignInRequest;
import com.tm3library.dto.response.LbResponse;
import com.tm3library.dto.response.ResponseMessage;
import com.tm3library.dto.response.SignInResponse;
import com.tm3library.dto.response.UserResponse;
import com.tm3library.security.jwt.JwtUtils;
import com.tm3library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserJwtController {
    // !!! Amacım sadece login ve register işlemleri yapılacak
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // !!! Register
    @PostMapping("/register")
    public ResponseEntity<LbResponse> registerUser(@Valid
                                                   @RequestBody RegisterRequest registerRequest) {
        userService.saveUser(registerRequest);

        LbResponse response = new LbResponse();
        response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // !!! Login
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> authenticate(@Valid @RequestBody SignInRequest signInRequest){

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // !!! Kullanıcı bu aşamada valide edildi ve token üretimine geçiliyor
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        // !!! JWT Token client tarafına gönderiliyor
        SignInResponse signInResponse= new SignInResponse(jwtToken);

        return new ResponseEntity<>(signInResponse,HttpStatus.OK);

    }

}
