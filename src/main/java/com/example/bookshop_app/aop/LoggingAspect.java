package com.example.bookshop_app.aop;

import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.entity.book.review.BookReviewLike;
import io.jsonwebtoken.JwtException;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut(value = "@annotation(com.example.bookshop_app.aop.annotation.Loggable)")
    public void loggablePointcut() {
    }

    @Before(value = "@annotation(com.example.bookshop_app.aop.annotation.ExceptionHandlerLoggable) && args(ex)", argNames = "ex")
    public void beforeAdvice(Exception ex) {
        if (ex instanceof AuthenticationException) {
            logger.error("Authentication exception is occurred: {}", ex.getLocalizedMessage());
        } else if (ex instanceof JwtException) {
            logger.error("Jwt exception is occurred: {}", ex.getLocalizedMessage());
        } else
            logger.error(ex.getLocalizedMessage());
    }

    @AfterReturning(pointcut = "loggablePointcut()", returning = "like")
    public void afterReturningAdvice(BookReviewLike like) {
        logger.info("Book '{}' was rated by user {}", like.getReview().getBook().getTitle(),
                Optional.ofNullable(like.getUser()).map(BookstoreUser::getName).orElse(""));
    }

    @AfterReturning(pointcut = "loggablePointcut()", returning = "savedBook")
    public void afterReturningAdvice(Book savedBook) {
        logger.info("New image of the book '{}' was successfully saved", savedBook.getTitle());
    }

    @AfterThrowing(pointcut = "loggablePointcut()", throwing = "ex")
    public void afterThrowingAdvice(Exception ex) {
        logger.warn("Exception has occurred: {}", ex.getLocalizedMessage());
    }
}
