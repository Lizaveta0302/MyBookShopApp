package com.example.bookshop_app.security;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.entity.SmsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController {

    private final SmsService smsService;
    private final JavaMailSender javaMailSender;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister, SmsService smsService, JavaMailSender javaMailSender) {
        this.userRegister = userRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");

        if (payload.getContact().contains("@")) {
            return response;//for email
        } else {
            //String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
            //smsService.saveNewCode(new SmsCode(smsCodeString, 60));//expires in 1 min
            return response;
        }
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bookstoreapp@mail.ru");
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(smsService.generateCode(), 300); //5 minutes
        smsService.saveNewCode(smsCode);
        message.setSubject("Bookstore email verification!");
        message.setText("Verification code is: " + smsCode.getCode());
        javaMailSender.send(message);
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (Boolean.TRUE.equals(smsService.verifyCode(payload.getCode()))) {
            response.setResult("true");
        }
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse response = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", response.getResult());
        httpServletResponse.addCookie(cookie);
        return response;
    }

    @PostMapping("/login-by-phone-number")
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPhoneNumber(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {
        if (smsService.verifyCode(payload.getCode())) {
            ContactConfirmationResponse response = userRegister.jwtLoginByPhoneNumber(payload);
            Cookie cookie = new Cookie("token", response.getResult());
            httpServletResponse.addCookie(cookie);
            return response;
        } else {
            return null;
        }
    }
}
