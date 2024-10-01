package com.trongtin.blog.identity.controller;


import com.trongtin.blog.identity.dto.request.email.EmailRequest;
import com.trongtin.blog.identity.dto.request.email.EmailResponse;
import com.trongtin.blog.identity.dto.request.email.SendEmailRequest;
import com.trongtin.blog.identity.dto.response.ApiResponse;
import com.trongtin.blog.identity.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EmailController {

    @Autowired
    private EmailService emailService;

//    @PostMapping("/email/send")
//    public ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest emailRequest) {
//        return ApiResponse.<EmailResponse>
//                builder().
//                result(emailService.sendEmail(emailRequest))
//                .build();
//    }
}
