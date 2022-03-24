package com.example.bookshop_app.service;

import com.example.bookshop_app.repo.BookMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BookMarkService {

    private BookMarkRepository bookMarkRepository;

    @Autowired
    public BookMarkService(BookMarkRepository bookMarkRepository) {
        this.bookMarkRepository = bookMarkRepository;
    }

    public Map<Integer, Integer> getBookRating(Integer id) {
        Map<Integer, Integer> ratingMap = new HashMap<>();
        List<Integer> ratings = bookMarkRepository.getBookRating(id);
        for (int i = 0; i < ratings.size(); i++) {
            if (Objects.isNull(ratings.get(i))) {
                ratingMap.put(i + 1, 0);
            } else {
                ratingMap.put(i + 1, ratings.get(i));
            }
        }
        return ratingMap;
    }
}
