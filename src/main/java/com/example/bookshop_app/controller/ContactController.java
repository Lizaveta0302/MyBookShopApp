package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.dto.form.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

    @Value("${appEmail.email}")
    private String appEmail;
    @Autowired
    private JavaMailSender javaMailSender;

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @PostMapping("/contact/send")
    public String handleContactForm(ContactForm contactForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appEmail);
        message.setFrom(appEmail);
        message.setSubject(contactForm.getTopic());
        message.setText(contactForm.getMessage() + "\n\n" + contactForm.getName() + ",\n" + contactForm.getMail());
        message.setReplyTo(contactForm.getMail());
        javaMailSender.send(message);
        return "redirect:/";
    }
}
