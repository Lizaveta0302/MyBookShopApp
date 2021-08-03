package org.app.service;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.app.exception.FilterOrRemoveByFieldException;
import org.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public void saveBook(Book book) {
        if ((Objects.nonNull(book.getAuthor()) && !book.getAuthor().isEmpty())
                || (Objects.nonNull(book.getTitle()) && !book.getTitle().isEmpty())
                || Objects.nonNull(book.getSize())) {
            bookRepo.save(book);
        }
    }

    public void removeBookByField(String bookFieldToRemove, String bookFieldValueToRemove) throws FilterOrRemoveByFieldException {
        try {
            for (Book book : bookRepo.retrieveAll()) {
                Field bookField = book.getClass().getDeclaredField(bookFieldToRemove);
                bookField.setAccessible(true);
                Object bookValue = bookField.get(book);
                if (Objects.nonNull(bookValue)) {
                    if (bookValue.toString().equals(bookFieldValueToRemove)) {
                        bookRepo.removeItemByField(bookFieldToRemove, bookFieldValueToRemove);
                        logger.info("book is removed: " + book);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.info("remove book by field exception: " + ex.getMessage());
            throw new FilterOrRemoveByFieldException("remove book by field exception: " + ex.getMessage());
        }
    }

    public List<Book> filterBooksByField(String bookFieldToFilter, String bookFieldValueToFilter) throws FilterOrRemoveByFieldException {
        List<Book> filteredBooks = new ArrayList<>();
        try {
            for (Book book : bookRepo.retrieveAll()) {
                Field bookField = book.getClass().getDeclaredField(bookFieldToFilter);
                bookField.setAccessible(true);
                Object fieldValue = bookField.get(book);
                if (Objects.nonNull(fieldValue)) {
                    if (fieldValue.toString().contains(bookFieldValueToFilter)) {
                        filteredBooks.add(book);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.info("filter books by field exception: " + ex.getMessage());
            throw new FilterOrRemoveByFieldException("filter books by field exception: " + ex.getMessage());
        }
        return filteredBooks;
    }
}
