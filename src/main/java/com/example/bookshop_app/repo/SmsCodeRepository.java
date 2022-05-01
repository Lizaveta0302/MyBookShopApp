package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    public SmsCode findByCode(String code);
}
