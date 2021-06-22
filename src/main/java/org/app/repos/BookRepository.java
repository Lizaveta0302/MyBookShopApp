package org.app.repos;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void save(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public void removeItemByField(String field, String value) {
        try {
            for (Book book : retrieveAll()) {
                Field bookField = book.getClass().getDeclaredField(field);
                bookField.setAccessible(true);
                if (bookField.get(book).toString().equals(value)) {
                    repo.remove(book);
                    logger.info("book is removed: " + book);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.info("remove book by field exception: " + ex.getMessage());
        }
    }

    @Override
    public List<Book> filterItemsByField(String field, String value) {
        List<Book> filteredBooks = new ArrayList<>();
        try {
            for (Book book : retrieveAll()) {
                Field bookField = book.getClass().getDeclaredField(field);
                bookField.setAccessible(true);
                Object fieldValue = bookField.get(book);
                if (fieldValue.toString().contains(value)) {
                    filteredBooks.add(book);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.info("filter books by field exception: " + ex.getMessage());
        }
        return filteredBooks;
    }
}
