package org.app.service;

import org.app.dto.Book;
import org.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

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

    public void removeBookByField(String bookFieldToRemove, String bookFieldValueToRemove) {
        bookRepo.removeItemByField(bookFieldToRemove, bookFieldValueToRemove);
    }

    public List<Book> filterBooksByField(String bookFieldToFilter, String bookFieldValueToFilter) {
        return bookRepo.filterItemsByField(bookFieldToFilter, bookFieldValueToFilter);
    }
}
