package com.example.bookshop_app.security;

import com.example.bookshop_app.entity.SmsCode;
import com.example.bookshop_app.repo.SmsCodeRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {

    private static final Random rand = new Random();
    private final SmsCodeRepository smsCodeRepository;

    @Value("${twilio.ACCOUNT_SID}")
    private String accountSid;
    @Value("${twilio.AUTH_TOKEN}")
    private String authToken;
    @Value("${twilio.TWILIO_NUMBER}")
    private String twilioNumber;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository) {
        this.smsCodeRepository = smsCodeRepository;
    }

    public String sendSecretCodeSms(String contact) {
        Twilio.init(accountSid, authToken);
        String formattedContact = contact.replaceAll("[( )-]", "");
        String generatedCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(twilioNumber),
                "Your secret code is: " + generatedCode
        ).create();
        return generatedCode;
    }

    public String generateCode() {
        //nnn nnn - pattern
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(rand.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

    public void saveNewCode(SmsCode code) {
        if (smsCodeRepository.findByCode(code.getCode()) == null) {
            smsCodeRepository.save(code);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        return smsCode != null && !smsCode.isExpired();
    }
}
