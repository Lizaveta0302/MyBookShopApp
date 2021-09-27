package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Map<String, List<Author>> getFilteredAuthors() {
        return getAllAuthors().stream().collect(Collectors.groupingBy(a -> a.getLastName().substring(0, 1)));
    }

    public Author getAuthorById(String id) {
        return authorRepository.findById(Integer.valueOf(id)).orElse(new Author());
    }
}
