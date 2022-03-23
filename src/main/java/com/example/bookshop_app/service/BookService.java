package com.example.bookshop_app.service;

import com.example.bookshop_app.entity.Genre;
import com.example.bookshop_app.entity.Tag;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBookById(String id) {
        return bookRepository.findById(Integer.valueOf(id)).orElse(new Book());
    }

    public Book getBookBySlug(String slug) {
        return bookRepository.findBookBySlug(slug);
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findBooksByTitle(title);
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTitleContaining(searchWord, nextPage);
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllByIsBestsellerTrue(nextPage);
    }

    public Page<Book> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllByOrderByPubDateDesc(nextPage);
    }

    public Page<Book> getPageOfRecentBooksWithPeriod(Integer offset, Integer limit, String from, String to) throws ParseException, IllegalArgumentException {
        Pageable nextPage = PageRequest.of(offset, limit);
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date startDate = Date.valueOf(newDateFormat.format(oldDateFormat.parse(from)));
        Date endDate = Date.valueOf(newDateFormat.format(oldDateFormat.parse(to)));
        return bookRepository.findAllByPubDateBetweenOrderByPubDateDesc(nextPage, startDate, endDate);
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getAllBooksByPopularity(nextPage);
    }

    public Page<Book> getBooksByAuthorId(Integer offset, Integer limit, String authorId) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByAuthorId(nextPage, Integer.valueOf(authorId));
    }

    public Page<Book> getBooksByTag(int offset, int limit, Tag tag) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTagsIsContaining(nextPage, tag);
    }

    public Page<Book> getBooksByGenre(int offset, int limit, Genre genre) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByGenresIsContaining(nextPage, genre);
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }

    public Book findBookBySlug(String slug) {
        return bookRepository.findBookBySlug(slug);
    }

    public List<Book> findBooksBySlugIn(String[] cookieSlugs) {
        return bookRepository.findBooksBySlugIn(cookieSlugs);
    }

    @Transactional
    public void updateNumberOfPostponed(String slug, int numberOfPostponed) {
        bookRepository.updateNumberOfPostponed(slug, numberOfPostponed);
    }

    @Transactional
    public void updateQuantityInBasket(String slug, int quantityInBasket) {
        bookRepository.updateQuantityInBasket(slug, quantityInBasket);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateQuantityInBasketAndNumberOfPostponed(String slug, Integer numberOfPostponed, Integer quantityInBasket) {
        updateNumberOfPostponed(slug, numberOfPostponed);
        updateQuantityInBasket(slug, quantityInBasket);
    }
}
