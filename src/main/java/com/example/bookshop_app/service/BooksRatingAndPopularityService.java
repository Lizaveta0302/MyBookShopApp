package com.example.bookshop_app.service;

import com.example.bookshop_app.aop.annotation.DurationTrackable;
import com.example.bookshop_app.entity.book.Book;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksRatingAndPopularityService {

    public List<Book> sortBooksAccordingItsPopularity(List<Book> books) {
        return books.stream().sorted(new PopularityComparator()).collect(Collectors.toList());
    }

    @DurationTrackable
    static class PopularityComparator implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) {
            int popularityOfBook1 = (int) (b1.getNumberOfPurchased() + b1.getQuantityInBasket() * 0.7 + b1.getNumberOfPostponed() * 0.4);
            int popularityOfBook2 = (int) (b2.getNumberOfPurchased() + b2.getQuantityInBasket() * 0.7 + b2.getNumberOfPostponed() * 0.4);
            return (popularityOfBook1 > popularityOfBook2) ? 1 : 0;
        }
    }

}
