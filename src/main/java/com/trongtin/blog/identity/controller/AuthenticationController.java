package com.trongtin.blog.identity.controller;

import com.nimbusds.jose.JOSEException;
import com.trongtin.blog.identity.dto.request.AuthenticationRequest;
import com.trongtin.blog.identity.dto.request.IntrospectRequest;
import com.trongtin.blog.identity.dto.request.LogoutRequest;
import com.trongtin.blog.identity.dto.request.RefreshTokenRequest;
import com.trongtin.blog.identity.dto.response.ApiResponse;
import com.trongtin.blog.identity.dto.response.AuthenticationResponse;
import com.trongtin.blog.identity.dto.response.IntrospectResponse;
import com.trongtin.blog.identity.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
       var result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result).build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result).build();
    }


    @PostMapping("/logout")
    ApiResponse<Void> logut(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
               .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder().result(authenticationService.introspect(request))
                .build();
    }
}
