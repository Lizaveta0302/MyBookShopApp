package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.PayDto;
import com.example.bookshop_app.service.PaymentService;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
@Secured("ROLE_USER")
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @ResponseBody
    public PayDto handlePay(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ParseException {
        String url = paymentService.getPaymentUrl(payload.get("sum"));
        return new PayDto(url);
    }
}
