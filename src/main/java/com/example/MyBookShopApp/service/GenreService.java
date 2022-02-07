package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.Genre;
import com.example.MyBookShopApp.repo.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Page<Genre> getAllGenres(int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return genreRepository.findAll(nextPage);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(String genreId) {
        return genreRepository.getById(Integer.parseInt(genreId));
    }

}
