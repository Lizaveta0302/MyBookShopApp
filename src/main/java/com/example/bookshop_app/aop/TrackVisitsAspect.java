package com.example.bookshop_app.aop;

import com.example.bookshop_app.entity.Visit;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.service.VisitService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
public class TrackVisitsAspect {

    @Autowired
    private VisitService visitService;
    @Autowired
    private BookstoreUserRegister userRegister;

    private static final Logger logger = LoggerFactory.getLogger(TrackVisitsAspect.class);

    @After(value = "execution(* *getBookById*(..)) && args(id)")
    public void trackVisitsAdvice(String id) {
        Object curUser = userRegister.getCurrentUser();
        Integer userId = null;
        Integer bookId = Integer.valueOf(id);
        if (curUser instanceof BookstoreUserDetails) {
            userId = ((BookstoreUserDetails) curUser).getBookstoreUser().getId();
        }
        if (Objects.nonNull(userId)) {
            Visit newVisit = new Visit();
            newVisit.setUserId(userId);
            newVisit.setBookId(bookId);
            newVisit.setVisitedAt(LocalDateTime.now());
            Visit visit = visitService.getByUniqueKey(userId, bookId);
            if (Objects.isNull(visit)) {
                visitService.insertVisit(newVisit);
            } else {
                visitService.updateVisitedAt(visit.getId(), LocalDateTime.now());
            }
            logger.info("user {} visits a page with book {}", userId, bookId);
        }
    }

    public void setVisitService(VisitService visitService) {
        this.visitService = visitService;
    }

    public void setUserRegister(BookstoreUserRegister userRegister) {
        this.userRegister = userRegister;
    }

}
